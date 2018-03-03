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
		Vector3 or = viewProjection.mulPos(new Vector3(this.reference.translation.get(0, 3),
														this.reference.translation.get(1, 3),
														this.reference.translation.get(2, 3)));
		Vector2 orr=new Vector2(or.x,or.y);
		
		switch(this.axis) {
		case X:{
			Vector3 b1 = viewProjection.mulPos(new Vector3(0f,1f,0f));
			Vector2 bb1= new Vector2(b1.x,b1.y);
			bb1.sub(orr);
			float bl1 = bb1.len();
			Vector3 b2 = viewProjection.mulPos(new Vector3(0f,0f,1f));
			Vector2 bb2= new Vector2(b2.x,b2.y);
			bb2.sub(orr);
			float bl2 = bb2.len();
			Vector2 l = lastMousePos.sub(orr);
			Vector2 c = curMousePos.sub(orr);
			double lb1 = l.clone().dot(bb1)/(bl1*bl1);
			double lb2 = l.clone().dot(bb2)/(bl2*bl2);
			Vector2 vl = new Vector2((float)lb1,(float)lb2);
			double cb1 = c.clone().dot(bb1)/(bl1*bl1);
			double cb2 = c.clone().dot(bb2)/(bl2*bl2);
			Vector2 vc = new Vector2((float)cb1,(float)cb2);
			double clock = lb1*cb2 - cb2*cb1;
			double dot = vl.normalize().dot(vc.normalize());
			double ang = Math.acos(dot);
			if(clock < 0)
				ang *= -1;
			this.reference.rotationX.mulBefore(Matrix4.createRotationX((float)ang));
			break;
		}
		case Y:{
			Vector3 b = viewProjection.mulPos(new Vector3(0f,1f,0f));
			Vector2 bb= new Vector2(b.x,b.y);
			float bl = bb.len();
			Vector2 l = lastMousePos.sub(orr);
			Vector2 c = curMousePos.sub(orr);
			float dis = (c.sub(l).dot(bb))/(bl*bl);
			this.reference.translation.m[13] += dis;
			break;
		}
		case Z:{
			Vector3 b = viewProjection.mulPos(new Vector3(0f,0f,1f));
			Vector2 bb= new Vector2(b.x,b.y);
			float bl = bb.len();
			Vector2 l = lastMousePos.sub(orr);
			Vector2 c = curMousePos.sub(orr);
			float dis = (c.sub(l).dot(bb))/(bl*bl);
			this.reference.translation.m[14] += dis;
			break;
		}
		}

	}

	@Override
	protected String meshPath () {
		return "data/meshes/Rotate.obj";
	}
}
