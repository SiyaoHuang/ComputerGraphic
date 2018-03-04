
import egl.math.*;
import gl.RenderObject;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Vector3 b1 = new Vector3(0f,1f,0f);
		Vector3 b2 = new Vector3(0f,0f,1f);
		Vector3 a  = new Vector3(0f,0f,0f);
		Vector3 l  = new Vector3(1f,-1f,-2f);
		Vector3 b  = new Vector3(4f,4f,4f);
		System.out.println(help(b1,b2,l,a,b));
		System.out.println(b.add(l.mul(-4f)));
	}
	
	private static Vector3 help(Vector3 b1, Vector3 b2, Vector3 l, Vector3 a, Vector3 b) {
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
		System.out.println(sll);
		System.out.println(s11);
		System.out.println(s22);
		System.out.println(srr);
		Vector3 ans = new Vector3((float)(s11/sll),(float)(s22/sll),(float)(srr/sll)); 
		return ans;
	}

}
