package ray2.integrator;

import java.util.Random;

import egl.math.Colord;
import egl.math.Vector2d;
import egl.math.Vector3d;
import ray2.IntersectionRecord;
import ray2.Ray;
import ray2.RayTracer;
import ray2.Scene;
import ray2.light.Light;
import ray2.light.PointLight;
import ray2.material.BSDF;
import ray2.material.BSDFSamplingRecord;
import ray2.surface.Surface;

/**
 * An Integrator that works by sampling light sources.  It accounts for light that illuminates all surfaces
 * directly from point or area sources, and from the environment.  It also includes recursive reflections
 * for polished surfaces (Glass and Glazed), but not for other surfaces.
 *
 * @author srm
 */
public class BSDFSamplingIntegrator extends Integrator {
	
	/* 
	 * The illumination algorithm is:
	 * 
	 *   0. light source emission:
	 *      if the surface is a light source:
	 *        add the source's radiance
	 *   1. reflected radiance:
	 *      generate a sample from the BSDF
	 *      trace a ray in that direction
	 *      if you hit nothing:
	 *        look up incident radiance from the environment
	 *      if you hit a surface:
	 *        for discrete directions, shade the ray recursively to get incident radiance
	 *        for non-discrete, incident radiance is source radiance if you hit a source (else 0)
	 *      compute the estimate for reflected radiance as incident radiance * brdf * cos theta / pdf
	 *   2. point light source:
	 *      for each point light in the scene:
	 *        compute the light direction and distance
	 *        evaluate the BRDF
	 *        add a contribution to the reflected radiance due to that source
	 * 
	 * For this integrator, step 1 automatically includes light from all sources (area sources and
	 * the environment) but step 2 is needed because point lights can't be hit by rays.
	 * 
	 * In step 1, by making the recursive call only for directions chosen discretely (that is, 
	 * directions belonging to perfectly sharp reflection and refraction components) we are leaving 
	 * out diffuse and glossy interreflections.
	 * 
	 * @see ray2.integrator.Integrator#shade(egl.math.Colord, ray2.Scene, ray2.Ray, ray2.IntersectionRecord, int)
	 */
	
	@Override
	public void shade(Colord outRadiance, Scene scene, Ray ray, IntersectionRecord iRec, int depth) {
      // TODO#A7: Calculate outRadiance at current shading point
      // You need to add contribution from source emission if the current surface has a light source,
      // generate a sample from the BSDF,
      // look up lighting in that direction and get incident radiance.
      // Before you calculate the reflected radiance, you need to check whether the probability value
      // from bsdf sample is 0.
		//---------------------step 0---------------
		if(iRec.surface !=null&&iRec.surface.getLight() != null) {
			iRec.surface.getLight().eval(ray, outRadiance);
		}
		//---------------------step 1------------------------------
		if(iRec.surface == null)
			return;
		BSDF bsdf_m = iRec.surface.getBSDF();
		Vector3d iR_p = iRec.location.clone();
		Vector3d iR_n = iRec.normal.clone().normalize();
		Vector3d v = ray.direction.clone().mul(-1.0f).normalize(); //view direction mul -1
		Colord brdf = new Colord();
		double pdf = 0;
		Random random = new Random();
		Vector2d seed = new Vector2d(random.nextDouble(),random.nextDouble());
		BSDFSamplingRecord bsdfsample = new BSDFSamplingRecord();
		bsdfsample.dir1 = v;
		bsdfsample.normal = iR_n;
		pdf = bsdf_m.sample(bsdfsample, seed, brdf);
		IntersectionRecord record = new IntersectionRecord();
		Ray refR = new Ray(iRec.location,bsdfsample.dir2.clone());
		refR.makeOffsetRay();
		Colord refc = new Colord();
		if(!scene.getFirstIntersection(record, refR) && scene.getEnvironment() != null) {
			scene.getEnvironment().eval(bsdfsample.dir2, refc);
		}else {

			if(bsdfsample.isDiscrete) {
				this.shade(outRadiance, scene, refR, record, depth+1);
			}else {
				if(record.surface != null &&record.surface.getLight() != null) {
					record.surface.getLight().eval(refR, refc);
				}
			}
		}
		double costh = Math.abs(bsdfsample.dir2.dot(iR_n));
		if(pdf != 0)
			outRadiance.add(refc.mul(brdf).mul(costh/pdf));
		
		//------------------step 2-------------------------------
		for(Light li_tmp :scene.getLights()) {
			if(li_tmp.getClass() == PointLight.class) {
				PointLight li_pl = (PointLight)li_tmp;
				Vector3d li_p = li_pl.position.clone();
				Vector3d w = li_p.clone().sub(iR_p);
				double len = w.len();
				w.normalize();
				Ray rayp = new Ray(iR_p,w.clone().normalize());
				rayp.makeOffsetRay();
				rayp.makeOffsetSegment(ray.direction.len());
				if(isShadowed(scene, iR_p, li_p)) {
					continue;
				}
				Colord tmpc = new Colord();
				iRec.surface.getBSDF().eval(w, v, iR_n, tmpc);
				costh = Math.max(0, w.dot(iR_n));
				Vector3d col = li_pl.getIntensity().clone().mul(costh/len/len).mul(tmpc);
				outRadiance.add(col);
			}
		}
	}
	protected boolean isShadowed(Scene scene, Vector3d shadingPoint, Vector3d lightPosition) {	
		
		Ray shadowRay = new Ray();
		
		// Setup the shadow ray to start at surface and end at light
		shadowRay.origin.set(shadingPoint);
		shadowRay.direction.set(lightPosition).sub(shadingPoint);

		// Set the ray to end at the light
		shadowRay.makeOffsetSegment(shadowRay.direction.len());
		shadowRay.direction.normalize();

		return scene.getAnyIntersection(shadowRay);
	}
}
