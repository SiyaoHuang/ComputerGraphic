package ray1.camera;

import ray1.Ray;
import egl.math.Vector3;

public class OrthographicCamera extends Camera {

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
        //    based on viewDir and viewUp
    	w.set(this.getViewDir().normalize().mul(-1f));
    	//System.out.println(w.x+" "+w.y+" "+w.z);
    	u.set(this.getViewUp().cross(w).normalize());
    	//System.out.println("u "+u.x+" "+u.y+" "+u.z);
    	v.set(w.clone().cross(u).normalize());
    	//System.out.println("v "+v.x+" "+v.y+" "+v.z);
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
        // 2) Set the origin field of outRay for an orthographic camera. 
        //    In an orthographic camera, the origin should depend on your transformed
        //    inU and inV and your basis vectors u and v.
        // 3) Set the direction field of outRay for an orthographic camera.
    	float width = this.getViewWidth();
    	float height = this.getViewHeight();
    	inU = inU * width - width /2;
    	inV = inV * height - height /2;
    	outRay.origin.set(this.getViewPoint().add(this.u.clone().mul(inU)).add(this.v.clone().mul(inV)));
    	outRay.direction.set(this.w.clone().mul(-1f));
    }

}
