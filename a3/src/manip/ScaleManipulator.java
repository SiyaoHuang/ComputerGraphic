package manip;

import egl.math.*;
import gl.RenderObject;

public class ScaleManipulator extends Manipulator {

	public ScaleManipulator (ManipulatorAxis axis) {
		super();
		this.axis = axis;
	}

	public ScaleManipulator (RenderObject reference, ManipulatorAxis axis) {
		super(reference);
		this.axis = axis;
	}

	@Override
	protected Matrix4 getReferencedTransform () {
		if (this.reference == null) {
			throw new RuntimeException ("Manipulator has no controlled object!");
		}
		return new Matrix4().set(reference.scale)
				.mulAfter(reference.rotationZ)
				.mulAfter(reference.rotationY)
				.mulAfter(reference.rotationX)
				.mulAfter(reference.translation);
	}

	@Override
	public void applyTransformation(Vector2 lastMousePos, Vector2 curMousePos, Matrix4 viewProjection) {
		// TODO#A3: Modify this.reference.scale given the mouse input.
		// Use this.axis to determine the axis of the transformation.
		// Note that the mouse positions are given in coordinates that are normalized to the range [-1, 1]
		//   for both X and Y. That is, the origin is the center of the screen, (-1,-1) is the bottom left
		//   corner of the screen, and (1, 1) is the top right corner of the screen.
//		System.out.println("---------------------");
		Matrix4 viewinv = viewProjection.clone().invert();
		Vector3 l = viewinv.mulPos(new Vector3(lastMousePos.x,lastMousePos.y,1f));
		Vector3 l0 = viewinv.mulPos(new Vector3(lastMousePos.x,lastMousePos.y,-1f));
		l.sub(l0);
		Vector3 c = viewinv.mulPos(new Vector3(curMousePos.x,curMousePos.y,1f));
		Vector3 c0 = viewinv.mulPos(new Vector3(curMousePos.x,curMousePos.y,-1f));
		c.sub(c0);
		Vector3 camd = viewinv.mulPos(new Vector3(0f,0f,1f));
		Vector3 cam = viewinv.mulPos(new Vector3(0f,0f,-1f));
		camd.sub(cam).normalize();
		Vector3 or = this.reference.translation.mulPos(new Vector3(0f,0f,0f));
		Matrix4 ro = this.getReferencedTransform().clone();
		Vector3 bx = ro.mulPos(new Vector3(1f,0f,0f));
		bx.sub(or).normalize();
		Vector3 by = ro.mulPos(new Vector3(0f,1f,0f));
		by.sub(or).normalize();
		Vector3 bz = ro.mulPos(new Vector3(0f,0f,1f));
		bz.sub(or).normalize();
		switch(this.axis) {
		case X:{
			Vector3 b1 = bx;
			Vector3 b2 = b1.clone().cross(camd);

			if(b2.len()==0)
				return;
			b2.normalize();
			Vector3 tl = help(b1,b2,l,or,l0);
			Vector3 tc = help(b1,b2,c,or,c0);
			float dis = tc.x / tl.x;
			this.reference.scale.m[0] *= dis;
			break;
		}
		case Y:{
			Vector3 b1 = by;
			Vector3 b2 = b1.clone().cross(camd);
			if(b2.len()==0)
				return;
			b2.normalize();
			Vector3 tl  = help(b1,b2,l,or,l0);
			Vector3 tc = help(b1,b2,c,or,c0);
			float dis = tc.x / tl.x;
			this.reference.scale.m[5] *= dis;
			break;
		}
		case Z:{
			Vector3 b1 = bz;
			Vector3 b2 = b1.clone().cross(camd);
			if(b2.len()==0)
				return;
			b2.normalize();
			Vector3 tl  = help(b1,b2,l,or,l0);
			Vector3 tc = help(b1,b2,c,or,c0);
			float dis = tc.x / tl.x;
			this.reference.scale.m[10] *= dis;
			break;
		}
		}
	}
	
	private Vector3 help(Vector3 b1, Vector3 b2, Vector3 l, Vector3 a, Vector3 b) {
		Matrix3d sl = new Matrix3d(b1.x, b2.x, -l.x,
				b1.y, b2.y, -l.y,
				b1.z, b2.z, -l.z);
		Matrix3d s1 = new Matrix3d(b.x-a.x, b2.x, -l.x,
				b.y-a.y, b2.y, -l.y,
				b.z-a.z, b2.z, -l.z);
		Matrix3d s2 = new Matrix3d(b.x, b.x-a.x, -l.x,
				b1.y, b.y-a.y, -l.y,
				b1.z, b.z-a.z, -l.z);
		Matrix3d sr = new Matrix3d(b1.x, b2.x, b.x-a.x,
				b1.y, b2.y, b.y-a.y,
				b1.z, b2.z, b.z-a.z);
		double sll = sl.determinant();
		double s11 = s1.determinant();
		double s22 = s2.determinant();
		double srr = sr.determinant();
		Vector3 ans = new Vector3((float)(s11/sll),(float)(s22/sll),(float)(srr/sll)); 
		return ans;
	}

	@Override
	protected String meshPath () {
		return "data/meshes/Scale.obj";
	}

}
