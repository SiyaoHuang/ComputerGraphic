package ray1.surface;

import ray1.IntersectionRecord;
import ray1.Ray;
import egl.math.Vector3;
import egl.math.Vector3d;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
  
  /** The center of the sphere. */
  protected final Vector3 center = new Vector3();
  public void setCenter(Vector3 center) { this.center.set(center); }
  
  /** The radius of the sphere. */
  protected float radius = 1.0f;
  public void setRadius(float radius) { this.radius = radius; }
  
  protected final double M_2PI = 2 * Math.PI;
  
  public Sphere() { }
  
  /**
   * Tests this surface for intersection with ray. If an intersection is found
   * record is filled out with the information about the intersection and the
   * method returns true. It returns false otherwise and the information in
   * outRecord is not modified.
   *
   * @param outRecord the output IntersectionRecord
   * @param ray the ray to intersect
   * @return true if the surface intersects the ray
   */
  public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
    // TODO#A2: fill in this function.
	double t1 = -1;
	double t2 = -2;
	Vector3d pp = rayIn.origin.clone().sub(this.center.clone());
	double t = Math.pow(pp.clone().dot(rayIn.direction), 2) - pp.clone().dot(pp) + Math.pow(this.radius, 2);
	if ( t < 0)
		return false;
	if(pp.clone().dot(pp) > Math.pow(this.radius, 2)) {
		t1 = -pp.clone().dot(rayIn.direction) - Math.sqrt(t);
	}
	t2 = -pp.clone().dot(rayIn.direction) + Math.sqrt(t);
	if( rayIn.start > t1)
		t = t2;
	else
		t = t1;
	if( t < rayIn.start || t > rayIn.end ) {
		return false;
	}
	outRecord.location.set(rayIn.origin.clone().add(rayIn.direction.clone().mul(t)));
	outRecord.normal.set(outRecord.location.clone().sub(this.center).normalize());
	outRecord.t = t;
	outRecord.surface = this;
	return true;
  }
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "sphere " + center + " " + radius + " " + shader + " end";
  }

}