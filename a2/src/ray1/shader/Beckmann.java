package ray1.shader;

import ray1.shader.BRDF;
import egl.math.Vector3;

/**
 * Evaluate microfacet BRDF function with Beckmann distribution
 * @author zechen
 */
public class Beckmann  extends BRDF
{

	public String toString() {    
		return "Beckmann microfacet " + super.toString();
	}
	
	/**
	 * Evaluate the BRDF function value in microfacet model with Beckmann distribution
	 *
	 * @param IncomingVec Direction vector of the incoming ray.
	 * @param OutgoingVec Direction vector of the outgoing ray.
	 * @param SurfaceNormal Normal vector of the surface at the shaded point.
	 * @return evaluated BRDF function value
	 */

	public float EvalBRDF(Vector3 IncomingVec, Vector3 OutgoingVec, Vector3 SurfaceNormal)
	{
		// TODO#A2: Evaluate the BRDF function of microfacet-based model with Beckmann distribution
		// Walter, Bruce, et al. 
		// "Microfacet models for refraction through rough surfaces." 
		// Proceedings of the 18th Eurographics conference on Rendering Techniques. Eurographics Association, 2007.
		Vector3 wi = IncomingVec.clone();
		Vector3 wo = OutgoingVec.clone();
		Vector3 n = SurfaceNormal.clone();
		Vector3 h = wi.clone().add(wo).normalize();
		double c = wi.clone().dot(h);
		double ni = 1.0;
		double tmp =Math.pow(nt, 2)/Math.pow(ni, 2) - 1 + Math.pow(c, 2);
		double F=0,g=0,th = 0,xx = 0;
		if ( tmp < 0)
			F = 1;
		else {
			g = Math.sqrt(tmp);
			
			xx = help_x(h.clone().dot(n));
			F = 0.5 * Math.pow(g - c , 2) / Math.pow(g + c, 2) * (1 + Math.pow(c * (g + c) - 1, 2) / Math.pow(c * (g-c)+1, 2));
		}
		th = h.angle(n);
		double DU = xx * Math.exp(-Math.pow(Math.tan(th), 2) / Math.pow(alpha, 2));
		double DD = Math.PI * Math.pow(alpha, 2) * Math.pow(Math.cos(th), 4);
		double D = DU / DD;

		double Gi = help_g( wi, h, n);
		double Go = help_g( wo, h, n);
		double G = Gi * Go;
//		System.out.println(Gi);
//		System.out.println(F);
//		System.out.println(D);
		return (float)(F * G * D / (4 * wi.clone().dot(n) * wo.clone().dot(n)));
	}
	
	public double help_x( double a ) {
		if ( a > 0) {
			return 1.0;
		}
		return 0;
	}
	
	public double help_g( Vector3 v, Vector3 h, Vector3 n) {
		double angle = v.angle(n);
		double a = 1 /(alpha*Math.tan(angle));
//		System.out.println(a);
		if (a < 1.6) {
			return help_x(v.clone().dot(h) / v.clone().dot(n))*(3.535 * a + 2.181 * Math.pow(a, 2))/(1+2.276*a+2.577* Math.pow(a, 2));
		}
		return help_x(v.clone().dot(h) / v.clone().dot(n));
	}
}

