package ray1.surface;

import ray1.IntersectionRecord;
import ray1.Ray;
import egl.math.Vector3;
import ray1.shader.Shader;
import ray1.OBJFace;
import egl.math.Matrix3d;
import egl.math.Vector2;

/**
 * Represents a single triangle, part of a triangle mesh
 *
 * @author ags
 */
public class Triangle extends Surface {
  /** The normal vector of this triangle, if vertex normals are not specified */
  Vector3 norm;
  
  /** The mesh that contains this triangle */
  Mesh owner;
  
  /** The face that contains this triangle */
  OBJFace face = null;
  
  double a, b, c, d, e, f;
  public Triangle(Mesh owner, OBJFace face, Shader shader) {
    this.owner = owner;
    this.face = face;

    Vector3 v0 = owner.getMesh().getPosition(face,0);
    Vector3 v1 = owner.getMesh().getPosition(face,1);
    Vector3 v2 = owner.getMesh().getPosition(face,2);
    
    if (!face.hasNormals()) {
      Vector3 e0 = new Vector3(), e1 = new Vector3();
      e0.set(v1).sub(v0);
      e1.set(v2).sub(v0);
      norm = new Vector3();
      norm.set(e0).cross(e1).normalize();
    }

    a = v0.x-v1.x;
    b = v0.y-v1.y;
    c = v0.z-v1.z;
    
    d = v0.x-v2.x;
    e = v0.y-v2.y;
    f = v0.z-v2.z;
    
    this.setShader(shader);
  }

  /**
   * Tests this surface for intersection with ray. If an intersection is found
   * record is filled out with the information about the intersection and the
   * method returns true. It returns false otherwise and the information in
   * outRecord is not modified.
   *
   * @param outRecord the output IntersectionRecord
   * @param rayIn the ray to intersect
   * @return true if the surface intersects the ray
   */
  public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
    // TODO#A2: fill in this function.
	  //System.out.println("????0");
	  Vector3 v0 = owner.getMesh().getPosition(face,0);
	  Vector3 v1 = owner.getMesh().getPosition(face,1);
	  Vector3 v2 = owner.getMesh().getPosition(face,2);
	  
	  Matrix3d ss = new Matrix3d(this.a, this.d, rayIn.direction.x,
				this.b, this.e, rayIn.direction.y,
				this.c, this.f, rayIn.direction.z);
	  Matrix3d sb = new Matrix3d(v0.x - rayIn.origin.x, this.d, rayIn.direction.x,
	               v0.y - rayIn.origin.y, this.e, rayIn.direction.y,
	               v0.z - rayIn.origin.z, this.f, rayIn.direction.z);
	  Matrix3d sr = new Matrix3d(this.a, v0.x - rayIn.origin.x, rayIn.direction.x,
		             this.b, v0.y - rayIn.origin.y, rayIn.direction.y,
		             this.c, v0.z - rayIn.origin.z, rayIn.direction.z);
	  Matrix3d st = new Matrix3d(this.a, this.d, v0.x - rayIn.origin.x,
					this.b, this.e, v0.y - rayIn.origin.y,
					this.c, this.f, v0.z - rayIn.origin.z);
	  double xx = sb.determinant();
	  double yy = sr.determinant();
	  double sss= ss.determinant();
	  double tt = st.determinant();
	  double bb = xx / sss;
	  double rr = yy / sss;
	  //System.out.println("????1");
	  if( sss == 0) {
		  //System.out.println("---");
		  return false;
	  }

	  double t = tt / sss;
	  if( t < rayIn.start || t > rayIn.end) {
		  //System.out.println("+++");
		  return false;
	  }
	  //System.out.println("???2");
	  outRecord.location.set(rayIn.origin.clone().add(rayIn.direction.clone().mul(t)));
	  outRecord.t = t;
	  outRecord.surface = this;
	  
	  Vector3 vp = new Vector3(outRecord.location);
	  //System.out.println("b"+bb+"r"+rr);
	  //System.out.println("???3");
	  if( bb + rr > 1) {
		  //System.out.println("999");
		  return false;
	  }
	  Vector3 v10 = new Vector3((float)a,(float)b,(float)c);
	  Vector3 v20 = new Vector3((float)d,(float)e,(float)f);
	  Vector3 v12 = v10.clone().sub(v20);
	  Vector3 v1p = vp.clone().sub(v1);
	  Vector3 v2p = vp.clone().sub(v2);
	  
	  double A = v10.clone().cross(v20).len() / 2;
	  double A1 = v20.clone().cross(v2p).len() / 2;
	  double A2 = v10.clone().cross(v1p).len() / 2;
	  double A0 = v1p.clone().cross(v12).len() / 2;
	  //System.out.println(A+" "+A0+" "+A1+" "+A2);
	  if(face.hasNormals()) {
		  Vector3 n0 = owner.getMesh().getNormal(face, 0).clone();
		  Vector3 n1 = owner.getMesh().getNormal(face, 1).clone();
		  Vector3 n2 = owner.getMesh().getNormal(face, 2).clone();
		  outRecord.normal.set(n0.clone().mul((float)(A0/A)).add(n1.clone().mul((float)(A1/A))).add(n2.clone().mul((float)(A2/A)))).normalize();
		  //System.out.println(outRecord.normal.x+" "+outRecord.normal.y+" "+outRecord.normal.z);
	  
	  } else {
		  outRecord.normal.set(norm);
	  }
	  //System.out.println("???4");
	  if(face.hasUVs()) {
		  Vector2 t0 = owner.getMesh().getUV(face, 0).clone();
		  Vector2 t1 = owner.getMesh().getUV(face, 1).clone();
		  Vector2 t2 = owner.getMesh().getUV(face, 2).clone();		  
		  outRecord.texCoords.set(t0.clone().mul((float)(A0/A)).add(t1.clone().mul((float)(A1/A)).add(t2.clone().mul((float)(A2/A)))));
	  }
	  //outRecord.normal.set(outRecord.location.clone().sub(this.center).normalize());
	  //outRecord.texCoords.set(u, v);
	  return true;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    return "Triangle ";
  }
}