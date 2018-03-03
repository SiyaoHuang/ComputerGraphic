package manip;

import egl.math.*;
import gl.RenderObject;

public class TranslationManipulator extends Manipulator {

	public TranslationManipulator (ManipulatorAxis axis) {
		super();
		this.axis = axis;
	}

	public TranslationManipulator (RenderObject reference, ManipulatorAxis axis) {
		super(reference);
		this.axis = axis;
	}

	@Override
	protected Matrix4 getReferencedTransform () {
		if (this.reference == null) {
			throw new RuntimeException ("Manipulator has no controlled object!");
		}
		return new Matrix4().set(reference.translation);
	}

	@Override
	public void applyTransformation(Vector2 lastMousePos, Vector2 curMousePos, Matrix4 viewProjection) {
		// TODO#A3: Modify this.reference.translation given the mouse input.
		// Use this.axis to determine the axis of the transformation.
		// Note that the mouse positions are given in coordinates that are normalized to the range [-1, 1]
		//   for both X and Y. That is, the origin is the center of the screen, (-1,-1) is the bottom left
		//   corner of the screen, and (1, 1) is the top right corner of the screen.
		System.out.println("---------------------");
		Matrix4 viewinv = viewProjection.clone().invert();
		Vector3 l = viewinv.mulPos(new Vector3(lastMousePos.x,lastMousePos.y,1f)).normalize();
		Vector3 l0 = viewinv.mulPos(new Vector3(lastMousePos.x,lastMousePos.y,-1f)).normalize();
		l0.sub(l);
		Vector3 c = viewinv.mulPos(new Vector3(curMousePos.x,curMousePos.y,1f)).normalize();
		Vector3 c0 = viewinv.mulPos(new Vector3(curMousePos.x,curMousePos.y,-1f)).normalize();
		c0.sub(c);
		
		Vector3 camd = viewinv.mulPos(new Vector3(0f,0f,1f));
		Vector3 cam = viewinv.mulPos(new Vector3(0f,0f,-1f));
		camd.sub(cam).normalize();
		Vector3 or = this.reference.translation.mulPos(new Vector3(0f,0f,0f));
		switch(this.axis) {
		case X:{
			Vector3 b1 = new Vector3(1f,0f,0f);
			Vector3 b2 = b1.clone().cross(camd);

			if(b2.len()==0)
				return;
			b2.normalize();
//			System.out.println(b1);
//			System.out.println(b2);
			Vector3 tl = help(b1,b2,l,or,l0);
			Vector3 tc = help(b1,b2,c,or,c0);
			System.out.println(b1.clone().mul(tl.x).add(b2.mul(tl.y)).add(or));
			System.out.println(l0.clone().add(l.mul(tl.z)));
			float dis = tc.x - tl.x;
			this.reference.translation.m[12] += dis;
			break;
		}
		case Y:{
			Vector3 b1 = new Vector3(0f,1f,0f);
			Vector3 b2 = b1.clone().cross(camd);
			if(b2.len()==0)
				return;
			b2.normalize();
			Vector3 tl  = help(b1,b2,l0,or,l);
			Vector3 tc = help(b1,b2,c0,or,c);
			System.out.println("b2 "+b2);
			System.out.println(tl);
			float dis = tc.x - tl.x;
			this.reference.translation.m[13] += dis;
			break;
		}
		case Z:{
			Vector3 b = viewProjection.mulPos(new Vector3(0f,0f,1f));
			Vector2 bb= new Vector2(b.x,b.y);
			float bl = bb.len();
			float dis = (curMousePos.sub(lastMousePos).dot(bb))/(bl*bl);
			this.reference.translation.m[14] += dis;
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
		
		return new Vector3((float)(s11/sll),(float)(s22/sll),(float)(srr/sll));
	}

	@Override
	protected String meshPath () {
		return "data/meshes/Translate.obj";
	}

}
