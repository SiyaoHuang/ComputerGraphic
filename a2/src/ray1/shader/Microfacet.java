package ray1.shader;

import ray1.IntersectionRecord;
import ray1.Ray;
import ray1.Scene;
import ray1.shader.BRDF;
import egl.math.Color;
import egl.math.Colorf;
import egl.math.Vector2;
import egl.math.Vector3;
import egl.math.Vector3d;

/**
 * Microfacet-based shader
 *
 * @author zechen
 */
public class Microfacet extends Shader {
	
	protected BRDF brdf = null;
	public void setBrdf(BRDF t) { brdf = t; }
	public BRDF getBrdf() { return brdf; }

	/** The color of the microfacet reflection. */
	protected final Colorf microfacetColor = new Colorf(Color.Black);
	public void setMicrofacetColor(Colorf microfacetColor) { this.microfacetColor.set(microfacetColor); }
	public Colorf getMicrofacetColor() {return new Colorf(microfacetColor);}
	
	/** The color of the diffuse reflection. */
	protected final Colorf diffuseColor = new Colorf(Color.Black);
	public void setDiffuseColor(Colorf diffuseColor) { this.diffuseColor.set(diffuseColor); }
	public Colorf getDiffuseColor() {return new Colorf(diffuseColor);}
	
	public Microfacet() { }

	/**
	 * @see Object#toString()
	 */
	public String toString() {    
		return "Microfacet model, microfacet color " + microfacetColor + " diffuseColor " + diffuseColor + brdf.toString();
	}

	/**
	 * Evaluate the intensity for a given intersection using the Microfacet shading model.
	 *
	 * @param outIntensity The color returned towards the source of the incoming ray.
	 * @param scene The scene in which the surface exists.
	 * @param ray The ray which intersected the surface.
	 * @param record The intersection record of where the ray intersected the surface.
	 */
	@Override
	public void shade(Colorf outIntensity, Scene scene, Ray ray, IntersectionRecord record) {
		// TODO#A2: Fill in this function.
		// 1) Loop through each light in the scene.
		// 2) If the intersection point is shadowed, skip the calculation for the light.
		//	  See Shader.java for a useful shadowing function.
		// 3) Compute the incoming direction by subtracting
		//    the intersection point from the light's position.
		// 4) Compute the color of the point using the microfacet shading model. 
		//	  EvalBRDF method of brdf object should be called to evaluate BRDF value at the shaded surface point.
		// 5) Add the computed color value to the output.
		outIntensity.set(Color.Black);
		Vector3d n = record.normal.clone();

		for(ray1.Light Li : scene.getLights()) {

			Ray ra = new Ray(ray);
			IntersectionRecord ro = new IntersectionRecord();
			ro.set(record);
			if(!this.isShadowed(scene, Li, ro, ra)) {

				Vector3d wi = record.location.clone().sub(Li.position).clone().mul(-1f).normalize();
				Vector3d wo = ray.origin.clone().sub(record.location.clone()).normalize();
				double irra = n.clone().dot(wi.clone());
				if( irra > 0) {
					Vector3d r = record.location.clone().sub(Li.position).clone().mul(-1f);
					double cc = 1.0 / (Math.pow(r.len(), 2)) * irra;
					double dd = brdf.EvalBRDF(new Vector3(wi),new Vector3(wo),new Vector3(n));
					try {
						Vector2 uv = new Vector2(record.texCoords);
						diffuseColor.set(record.surface.getShader().getTexture().getTexColor(uv));

					}catch(NullPointerException e) {
						
					}
					double red = cc * Li.intensity.r() * (this.diffuseColor.r()/Math.PI + this.microfacetColor.r()*dd);
					double gre = cc * Li.intensity.g() * (this.diffuseColor.g()/Math.PI + this.microfacetColor.g()*dd);
					double blu = cc * Li.intensity.b() * (this.diffuseColor.b()/Math.PI + this.microfacetColor.b()*dd);
					outIntensity.add((float)red,(float)gre,(float)blu);
					/*
					Vector3d col =new Vector3d(Li.intensity.mul((float)(1.0/Math.pow(r.len(), 2))));
					col.mul(irra);
					col.mul(this.diffuseColor.clone().mul((float)(1.0/Math.PI)).clone().add(this.microfacetColor.mul(brdf.EvalBRDF(new Vector3(wi),new Vector3(wo),new Vector3(n)))));
					System.out.println(brdf.EvalBRDF(new Vector3(wi),new Vector3(wo),new Vector3(n)));
					outIntensity.add((float)col.x,(float)col.y,(float)col.z);
					*/
				}
			}
		}
	}
	

}
