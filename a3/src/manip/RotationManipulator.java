package manip;

import egl.math.*;
import gl.RenderObject;

public class RotationManipulator extends Manipulator {

	protected String meshPath = "Rotate.obj";

	public RotationManipulator(ManipulatorAxis axis) {
		super();
		this.axis = axis;
	}

	public RotationManipulator(RenderObject reference, ManipulatorAxis axis) {
		super(reference);
		this.axis = axis;
	}

	//assume X, Y, Z on stack in that order
	@Override
	protected Matrix4 getReferencedTransform() {
		Matrix4 m = new Matrix4();
		switch (this.axis) {
		case X:
			m.set(reference.rotationX).mulAfter(reference.translation);
			break;
		case Y:
			m.set(reference.rotationY)
				.mulAfter(reference.rotationX)
				.mulAfter(reference.translation);
			break;
		case Z:
			m.set(reference.rotationZ)
			.mulAfter(reference.rotationY)
			.mulAfter(reference.rotationX)
			.mulAfter(reference.translation);
			break;
		}
		return m;
	}

	@Override
	public void applyTransformation(Vector2 lastMousePos, Vector2 curMousePos, Matrix4 viewProjection) {
		// TODO#A3: Modify this.reference.rotationX, this.reference.rotationY, or this.reference.rotationZ
		//   given the mouse input.
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
		Vector3 or = this.reference.translation.mulPos(new Vector3(0f,0f,0f));
		Vector3 bx = new Vector3(1f,0f,0f);
		Vector3 by = new Vector3(0f,1f,0f);
		Vector3 bz = new Vector3(0f,0f,1f);
		switch(this.axis) {
		case X:{
			
			Vector3 tl = help(by,bz,l,or,l0);
			Vector3 tc = help(by,bz,c,or,c0);
			Vector3 rl = l0.clone().add(l.mul(tl.z));//by.clone().mul(tl.x).add(bz.clone().mul(tl.y)).add(or);
			Vector3 rc = c0.clone().add(c.mul(tc.z));//by.clone().mul(tc.x).add(bz.clone().mul(tc.y));
			rl.sub(or);
			rc.sub(or);
			double ang = rl.clone().cross(rc).dot(bx)/rl.len()/rc.len();
			ang = Math.asin(ang); 
			if(Double.isNaN(ang))
				return;
			this.reference.rotationX.mulBefore(Matrix4.createRotationX((float)ang));
			break;
		}
		case Y:{
			Matrix4 tr = this.reference.rotationX.clone();
			bx = tr.mulPos(bx);
			by = tr.mulPos(by);
			bz = tr.mulPos(bz);
			Vector3 tl = help(bx,bz,l,or,l0);
			Vector3 tc = help(bx,bz,c,or,c0);

			Vector3 rl = l0.clone().add(l.mul(tl.z));
			Vector3 rc = c0.clone().add(c.mul(tc.z));
			rl.sub(or);
			rc.sub(or);
			double ang = rl.clone().cross(rc).dot(by)/rl.len()/rc.len();
			ang = Math.asin(ang); 
			if(Double.isNaN(ang))
				return;
			
			this.reference.rotationY.mulBefore(Matrix4.createRotationY((float)ang));

			break;
		}
		case Z:{
			Matrix4 tr = this.reference.rotationX.clone().mulBefore(this.reference.rotationY);
			bx = tr.mulPos(bx);
			by = tr.mulPos(by);
			bz = tr.mulPos(bz);
			Vector3 tl = help(bx,by,l,or,l0);
			Vector3 tc = help(bx,by,c,or,c0);

			Vector3 rl = l0.clone().add(l.mul(tl.z));
			Vector3 rc = c0.clone().add(c.mul(tc.z));
			rl.sub(or);
			rc.sub(or);
			double ang = rl.clone().cross(rc).dot(bz)/rl.len()/rc.len();
			ang = Math.asin(ang); 
			if(Double.isNaN(ang))
				return;
			
			this.reference.rotationZ.mulBefore(Matrix4.createRotationZ((float)ang));
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
		
		return new Vector3(ans);
	}
	
	@Override
	protected String meshPath () {
		return "data/meshes/Rotate.obj";
	}
}
