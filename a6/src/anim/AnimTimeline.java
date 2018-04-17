package anim;

import java.util.TreeSet;

import common.SceneObject;
import egl.math.Matrix3;
import egl.math.Matrix4;
import egl.math.Quat;
import egl.math.Vector3;
import egl.math.Vector4;

/**
 * A timeline for a particular object in the scene.  The timeline holds
 * a sequence of keyframes and a reference to the object that they
 * pertain to.  Via linear interpolation between keyframes, the timeline
 * can compute the object's transformation at any point in time.
 * 
 * @author Cristian
 */
public class AnimTimeline {
	
	/**
	 * A sorted set of keyframes.  Invariant: there is at least one keyframe.
	 */
	public final TreeSet<AnimKeyframe> frames = new TreeSet<>(AnimKeyframe.COMPARATOR);
	
	/**
	 * The object that this timeline animates
	 */
	public final SceneObject object;

	/**
	 * Create a new timeline for an object.  The new timeline initially has the object
	 * stationary, with the same transformation it currently has at all times.  This is
	 * achieve by createing a timeline with a single keyframe at time zero.
	 * @param o Object
	 */
	public AnimTimeline(SceneObject o) {
		object = o;
		
		// Create A Default Keyframe
		AnimKeyframe f = new AnimKeyframe(0);
		f.transformation.set(o.transformation);
		frames.add(f);
	}
	
	/**
	 * Add A keyframe to the timeline.
	 * @param frame Frame number
	 * @param t Transformation
	 */
	public void addKeyFrame(int frame, Matrix4 t) {
		// TODO#A6: Add an AnimKeyframe to frames and set its transformation
		AnimKeyframe tmp = new AnimKeyframe(frame);
		tmp.transformation.set(t);
		frames.add(tmp);
	}
	/**
	 * Remove a keyframe from the timeline.  If the timeline is empty,
	 * maintain the invariant by adding a single keyframe with the given
	 * transformation.
	 * @param frame Frame number
	 * @param t Transformation
	 */
	public void removeKeyFrame(int frame, Matrix4 t) {
		// TODO#A6: Delete a frame, you might want to use Treeset.remove
		// If there is no frame after deletion, add back this frame.
		if(frames.size() != 0) {
			AnimKeyframe tmp = new AnimKeyframe(frame);
			tmp.transformation.set(t);
			frames.remove(tmp);
		}
		if(frames.size() == 0) {
			AnimKeyframe tmp = new AnimKeyframe(frame);
			tmp.transformation.set(t);
			frames.add(tmp);
		}
			
	}

	
	/**
	 * Takes a rotation matrix and decomposes into Euler angles. 
	 * Returns a Vector3 containing the X, Y, and Z degrees in radians.
	 * Formulas from http://nghiaho.com/?page_id=846
	 */
	public static Vector3 eulerDecomp(Matrix3 mat) {
		double theta_x = Math.atan2(mat.get(2, 1), mat.get(2, 2));
		double theta_y = Math.atan2(-mat.get(2, 0), Math.sqrt(Math.pow(mat.get(2, 1), 2) + Math.pow(mat.get(2, 2), 2)));
		double theta_z = Math.atan2(mat.get(1, 0), mat.get(0, 0));
		
		return new Vector3((float)theta_x, (float)theta_y, (float)theta_z);
	}
	
	
	/**
	 * Update the transformation for the object connected to this timeline to the current frame
	 * @curFrame Current frame number
	 * @rotation Rotation interpolation mode: 
	 * 0 - Euler angles, 
	 * 1 - Linear interpolation of quaternions,
	 * 2 - Spherical linear interpolation of quaternions.
	 */
	public void updateTransformation(int curFrame, int rotation) {
		//TODO#A6: You need to get pair of surrounding frames,
		// calculate interpolation ratio,
		// calculate Translation, Scale and Rotation Interpolation,
		// and combine them.
		// Argument curFrame is current frame number
		// Argument rotation is rotation interpolation mode
		// 0 - Euler angles, 
		// 1 - Linear interpolation of quaternions,
		// 2 - Spherical linear interpolation of quaternions.
		
		
//		frames.
		AnimKeyframe cur = new AnimKeyframe(curFrame);
		AnimKeyframe pp = frames.floor(cur);
		AnimKeyframe aa = frames.ceiling(cur);
		if( pp == null && aa == null)
			return;
		if( pp == null) {
			object.transformation.set(aa.transformation);
			return;
		}
		if( aa == null || pp.frame == curFrame) {
			object.transformation.set(pp.transformation);
			return;
		}
		int pre = pp.frame;
		Matrix4 prev = pp.transformation;
		int aft = aa.frame;
		Matrix4 aftv = aa.transformation;
		
//		for(AnimKeyframe tmp : frames) {
//			int tt = tmp.frame;
//			if(tt > pre && tt <= curFrame) {
//				pre = tt;
//				prev.set(tmp.transformation);
//			}
//			if(tt < aft && tt >= curFrame) {
//				aft = tt;
//				aftv.set(tmp.transformation);
//			}
//			if(tt == curFrame) {
//				aft = curFrame;
//				object.transformation.set(tmp.transformation);
//				return;
//			}
//		}
		
		//set to keyframe
//		if( pre == Integer.MIN_VALUE ) {
//			object.transformation.set(aftv);
//			return;
//		}
//		if( aft == Integer.MAX_VALUE ) {
//			object.transformation.set(aftv);
//			return;
//		}
		
		float t = (float) ((curFrame - pre) * 1.0 /(aft - pre));
		
		
		 
		//------------------translate -----------------------
		Vector3 tprev = prev.getTrans();
		Vector3 taftv = aftv.getTrans();
		Vector3 trans = tprev.clone().mul(1f - t).add(taftv.clone().mul(t));
		Matrix4 tran4 = Matrix4.createTranslation(trans);
		
		//------------------scale -----------------------
		Matrix3 p3 = new Matrix3(prev);
		Matrix3 a3 = new Matrix3(aftv);
		Matrix3 Rp3 = new Matrix3();
		Matrix3 Sp3 = new Matrix3();
		Matrix3 Ra3 = new Matrix3();
		Matrix3 Sa3 = new Matrix3();
		
		p3.polar_decomp(Rp3, Sp3);
		a3.polar_decomp(Ra3, Sa3);
		Matrix3 tt = Sp3.clone().interpolate(Sp3, Sa3, t);

		Matrix4 scal4 = new Matrix4(tt);
		
		Quat q1 = new Quat(Rp3);
		Quat q2 = new Quat(Ra3);
		//------------------rotate -----------------------
		Matrix4 Rota4 = new Matrix4();
		switch(rotation) {
		case 0:{
			Vector3 eup = eulerDecomp(p3);
			Vector3 eua = eulerDecomp(a3);
			Vector3 euu = eup.clone().mul(1f -t).add(eua.mul(t));
			Rota4 = Matrix4.createRotationX(euu.x).mulBefore(Matrix4.createRotationY(euu.y).mulBefore(Matrix4.createRotationZ(euu.z)));
			break;
		}
		case 1:{
			Quat q = q1.clone().scale(1 - t).add(q2.scale(t));
			q.toRotationMatrix(Rota4);
			break;
		}
		case 2:{
			Quat q = Quat.slerp(q1, q2, t);
			q.toRotationMatrix(Rota4);
			break;
		}
		}
		
		object.transformation.set(tran4.mulBefore(Rota4.mulBefore(scal4)));
	}
}
