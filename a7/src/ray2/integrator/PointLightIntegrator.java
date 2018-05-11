package ray2.integrator;

import egl.math.Color;
import egl.math.Colord;
import egl.math.Vector3d;
import ray2.IntersectionRecord;
import ray2.Ray;
import ray2.Scene;
import ray2.light.Light;
import ray2.light.PointLight;
import ray2.material.BSDF;
import ray2.surface.Surface;

/**
 * An Integrator that computes a result like the one in Ray 1, which deterministically accounts for
 * only point lights.
 * 
 * @author srm
 */
public class PointLightIntegrator extends Integrator {

	/* 
	 * The illumination algorithm is:
	 *   for each point light in the scene:
	 *     compute the light direction and distance
	 *     evaluate the BRDF
	 *     add a contribution to the reflected radiance due to that source
	 *      
	 * @see ray2.integrator.Integrator#shade(egl.math.Colord, ray2.Scene, ray2.Ray, ray2.IntersectionRecord, int)
	 */
	@Override
	public void shade(Colord outRadiance, Scene scene, Ray ray, IntersectionRecord iRec, int depth) {
      // TODO#A7: Calculate outRaidance at current shading point.
		for(Light li_tmp :scene.getLights()) {
			if(li_tmp.getClass() == PointLight.class) {
				PointLight li_pl = (PointLight)li_tmp;
				Vector3d v = ray.direction.clone().mul(-1.0f).normalize();
				Vector3d li_p = li_pl.position.clone();
				Vector3d iR_p = iRec.location.clone();
				Vector3d iR_n = iRec.normal.clone().normalize();
				Vector3d w = li_p.clone().sub(iR_p);
				double len = w.len();
				w.normalize();
				
				if(isShadowed(scene, iR_p, li_p)) {
					continue;
				}
				Colord tmpc = new Colord();
				iRec.surface.getBSDF().eval(w, v, iR_n, tmpc);
				double costh = Math.max(0, w.dot(iR_n));
				Vector3d col = li_pl.getIntensity().clone().mul(costh/len/len).mul(tmpc);
				outRadiance.add(col);
			}
		}
	}

	/**
	 * A utility method to check if there is any surface between the given intersection
	 * point and the given light.
	 * 
	 * @param scene The scene in which the surface exists.
	 * @param light A light in the scene.
	 * @param iRec The intersection point on a surface.
	 * @param shadowRay A ray that is set to point from the intersection point towards
	 * the given light.
	 * @return true if there is any surface between the intersection point and the light;
	 * false otherwise.
	 */
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
