package splines;

import java.util.ArrayList;

import egl.math.Matrix4;
import egl.math.Vector2;
/*
 * Cubic Bezier class for the splines assignment
 */

public class CubicBezier {
	
	//This Bezier's control points
	public Vector2 p0, p1, p2, p3;
	
	//Control parameter for curve smoothness
	float epsilon;
	
	//The points on the curve represented by this Bezier
	private ArrayList<Vector2> curvePoints;
	
	//The normals associated with curvePoints
	private ArrayList<Vector2> curveNormals;
	
	//The tangent vectors of this bezier
	private ArrayList<Vector2> curveTangents;
	
	
	/**
	 * 
	 * Cubic Bezier Constructor
	 * 
	 * Given 2-D BSpline Control Points correctly set self.{p0, p1, p2, p3},
	 * self.uVals, self.curvePoints, and self.curveNormals
	 * 
	 * @param bs0 First Bezier Spline Control Point
	 * @param bs1 Second Bezier Spline Control Point
	 * @param bs2 Third Bezier Spline Control Point
	 * @param bs3 Fourth Bezier Spline Control Point
	 * @param eps Maximum angle between line segments
	 */
	public CubicBezier(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, float eps) {
		curvePoints = new ArrayList<Vector2>();
		curveTangents = new ArrayList<Vector2>();
		curveNormals = new ArrayList<Vector2>();
		epsilon = eps;
		
		this.p0 = new Vector2(p0);
		this.p1 = new Vector2(p1);
		this.p2 = new Vector2(p2);
		this.p3 = new Vector2(p3);
		
		tessellate();
	}

    /**
     * Approximate a Bezier segment with a number of vertices, according to an appropriate
     * smoothness criterion for how many are needed.  The points on the curve are written into the
     * array self.curvePoints, the tangents into self.curveTangents, and the normals into self.curveNormals.
     * The final point, p3, is not included, because cubic Beziers will be "strung together".
     */
    private void tessellate() {
    	 // TODO A5
		this.curvePoints.add(p0);
		Vector2 cN = p1.clone().sub(p0).normalize();
		this.curveTangents.add(cN);
		this.curveNormals.add(new Vector2(cN.y, -cN.x).normalize());
		tesshelp(p0, p1, p2, p3, 0);
	}

	public void tesshelp(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, int count) {
		Vector2 p01 = p1.clone().sub(p0).normalize();
		Vector2 p12 = p2.clone().sub(p1).normalize();
		Vector2 p23 = p3.clone().sub(p2).normalize();
		double ang1 = Math.acos(p01.dot(p12));
		double ang2 = Math.acos(p12.dot(p23));

		count++;
		if (count > 10)
			return;
		if (ang1 < this.epsilon && ang2 < this.epsilon) {
			return;
		}

		Vector2 p1_0 = p0.clone().mul(0.5f).add(p1.clone().mul(0.5f));
		Vector2 p1_1 = p1.clone().mul(0.5f).add(p2.clone().mul(0.5f));
		Vector2 p1_2 = p2.clone().mul(0.5f).add(p3.clone().mul(0.5f));
		Vector2 p2_0 = p1_0.clone().mul(0.5f).add(p1_1.clone().mul(0.5f));
		Vector2 p2_1 = p1_1.clone().mul(0.5f).add(p1_2.clone().mul(0.5f));
		Vector2 p3_0 = p2_0.clone().mul(0.5f).add(p2_1.clone().mul(0.5f));

		tesshelp(p0, p1_0, p2_0, p3_0, count);

		this.curvePoints.add(p3_0);
		Vector2 cN = p2_1.clone().sub(p3_0).normalize();
		this.curveTangents.add(cN);
		this.curveNormals.add(new Vector2(cN.y, -cN.x).normalize());

		tesshelp(p3_0, p2_1, p1_2, p3, count);
		return;
	}
	
    
    /**
     * @return The points on this cubic bezier
     */
    public ArrayList<Vector2> getPoints() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curvePoints) returnList.add(p.clone());
    	return returnList;
    }
    
    /**
     * @return The tangents on this cubic bezier
     */
    public ArrayList<Vector2> getTangents() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curveTangents) returnList.add(p.clone());
    	return returnList;
    }
    
    /**
     * @return The normals on this cubic bezier
     */
    public ArrayList<Vector2> getNormals() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curveNormals) returnList.add(p.clone());
    	return returnList;
    }
    
    
    /**
     * @return The references to points on this cubic bezier
     */
    public ArrayList<Vector2> getPointReferences() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curvePoints) returnList.add(p);
    	return returnList;
    }
    
    /**
     * @return The references to tangents on this cubic bezier
     */
    public ArrayList<Vector2> getTangentReferences() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curveTangents) returnList.add(p);
    	return returnList;
    }
    
    /**
     * @return The references to normals on this cubic bezier
     */
    public ArrayList<Vector2> getNormalReferences() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curveNormals) returnList.add(p);
    	return returnList;
    }
}
