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
		Vector3 or = viewProjection.mulPos(new Vector3(this.reference.translation.get(0, 3),
														this.reference.translation.get(1, 3),
														this.reference.translation.get(2, 3)));
		Vector2 orr=new Vector2(or.x,or.y);
		
		switch(this.axis) {
		case X:{
			Vector3 b = viewProjection.mulPos(new Vector3(1f,0f,0f));
			Vector2 bb= new Vector2(b.x,b.y);
			float bl = bb.len();
			Vector2 l = lastMousePos.sub(orr);
			Vector2 c = curMousePos.sub(orr);
			float disl = (l.dot(bb))/(bl*bl);
			float disc = (c.dot(bb))/(bl*bl);
			if(disl != 0)
				this.reference.scale.m[0] *= disc/disl;
			//this.reference.translation.set(0, 3, 3);
			break;
		}
		case Y:{
			Vector3 b = viewProjection.mulPos(new Vector3(0f,1f,0f));
			Vector2 bb= new Vector2(b.x,b.y);
			float bl = bb.len();
			Vector2 l = lastMousePos.sub(orr);
			Vector2 c = curMousePos.sub(orr);
			float disl = (l.dot(bb));
			float disc = (c.dot(bb));
			if(disl != 0)
				this.reference.scale.m[5] *= disc/disl;
			break;
		}
		case Z:{
			Vector3 b = viewProjection.mulPos(new Vector3(0f,0f,1f));
			Vector2 bb= new Vector2(b.x,b.y);
			float bl = bb.len();
			Vector2 l = lastMousePos.sub(orr);
			Vector2 c = curMousePos.sub(orr);
			float disl = (l.dot(bb))/(bl*bl);
			float disc = (c.dot(bb))/(bl*bl);
			if(disl != 0)
				this.reference.scale.m[10] *= disc/disl;
			break;
		}
		}

	}

	@Override
	protected String meshPath () {
		return "data/meshes/Scale.obj";
	}

}
