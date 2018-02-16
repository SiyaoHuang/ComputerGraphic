package ray1.camera;

import egl.math.Vector3;
import ray1.Ray;

/**
 * Represents a camera with perspective view. For this camera, the view window
 * corresponds to a rectangle on a plane perpendicular to viewDir but at
 * distance projDistance from viewPoint in the direction of viewDir. A ray with
 * its origin at viewPoint going in the direction of viewDir should intersect
 * the center of the image plane. Given u and v, you should compute a point on
 * the rectangle corresponding to (u,v), and create a ray from viewPoint that
 * passes through the computed point.
 */
public class PerspectiveCamera extends Camera {

    protected float projDistance = 1.0f;
    public float getProjDistance() { return projDistance; }
    public void setprojDistance(float projDistance) {
        this.projDistance = projDistance;
    }


    //TODO#A2: create necessary new variables/objects here, including an orthonormal basis
    //          formed by three basis vectors and any other helper variables 
    //          if needed.
    public Vector3 u = new Vector3();
    public Vector3 v = new Vector3();
    public Vector3 w = new Vector3();
    
    /**
     * Initialize the derived view variables to prepare for using the camera.
     */
    public void init() {
        // TODO#A2: Fill in this function.
        // 1) Set the 3 basis vectors in the orthonormal basis,
        // based on viewDir and viewUp
        // 2) Set up the helper variables if needed
    	w.set(this.getViewDir().mul(-1f).normalize());
    	u.set(this.getViewUp().clone().cross(w).normalize());
    	v.set(w.clone().cross(u).normalize());
    }

    /**
     * Set outRay to be a ray from the camera through a point in the image.
     *
     * @param outRay The output ray (not normalized)
     * @param inU The u coord of the image point (range [0,1])
     * @param inV The v coord of the image point (range [0,1])
     */
    public void getRay(Ray outRay, float inU, float inV) {
        // TODO#A2: Fill in this function.
        // 1) Transform inU so that it lies between [-viewWidth / 2, +viewWidth / 2] 
        //    instead of [0, 1]. Similarly, transform inV so that its range is
        //    [-vieHeight / 2, +viewHeight / 2]
        // 2) Set the origin field of outRay for a perspective camera.
        // 3) Set the direction field of outRay for an perspective camera. This
        //    should depend on your transformed inU and inV and your basis vectors,
        //    as well as the projection distance.
    	float width = this.getViewWidth();
    	float height = this.getViewHeight();
//    	System.out.println(this.getProjDistance());
//    	System.out.println(this.getViewHeight()+" "+this.getViewWidth());
    	inU = inU * width - width /2;
    	inV = inV * height - height /2;
    	outRay.origin.set(this.getViewPoint());
    	outRay.direction.set(this.getViewDir().clone().normalize().mul(this.getProjDistance()).add(this.u.clone().mul(inU)).add(this.v.clone().mul(inV)));
    	outRay.makeOffsetRay();
    	outRay.direction.normalize();
//    	outRay.direction.add(0.2f, 0f, 0f).normalize();
//    	outRay.origin.add(100f, 100f, 100f);
//    	outRay.origin.set(outRay.origin.clone().add(outRay.direction.clone().mul(this.getProjDistance())));
//    	System.out.println(outRay.origin.toString());
//    	System.out.println(outRay.direction.toString());
    }
}