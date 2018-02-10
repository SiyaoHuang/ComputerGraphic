package meshgen;

import math.Vector2;
import math.Vector3;

public class MeshGen {

	//Cylender-----------------------------------------------------------------
	public static OBJMesh Cylinder(int n){
		OBJMesh ans = new OBJMesh();
		//position-----------------------------------------------------
		ans.positions.add(new Vector3(0, -1, 0)); //bottom center 0
		for(int i = 0; i < n; i++) { //bottom anti-clock 1-n position
			float x = (float)Math.cos( -Math.PI / 2-2* Math.PI * i / n);
			float y = -1;
			float z = (float)Math.sin( -Math.PI / 2 -2* Math.PI * i / n);
			ans.positions.add(new Vector3(x, y, z));
		}
		ans.positions.add(new Vector3(0, 1, 0)); //top center n + 1
		for(int i = 0; i < n; i++) { //top anti-clock n+2 - 2n + 1 position
			float x = (float)Math.cos( -Math.PI / 2 -2* Math.PI * i / n);
			float y = 1;
			float z = (float)Math.sin( -Math.PI / 2 -2* Math.PI * i / n);
			ans.positions.add(new Vector3(x, y, z));
		}
		//uv_botton_top----------------------------------------------------------------
		ans.uvs.add(new Vector2((float)0.25, (float)0.75)); //bottom anti-clock 0 uv
		for(int i = 0; i < n; i++) { //bottom anti-clock 1-n uv
			float u = (float)(0.25 * Math.cos( -2* Math.PI * (float)i / n - Math.PI / 2) + 0.25);
			float v = (float)(0.25 * Math.sin( -2* Math.PI * (float)i / n - Math.PI / 2) + 0.75);
			ans.uvs.add(new Vector2(u,v));
		}
		ans.uvs.add(new Vector2((float)0.75, (float)0.75)); //top anti-clock n + 1 uv
		for(int i = 0; i < n; i++) { //top anti-clock n+2 - 2 * n + 1 uv
			float u = (float)(0.25 * Math.cos( 2* Math.PI * (float)i / n + Math.PI / 2) + 0.75);
			float v = (float)(0.25 * Math.sin( 2* Math.PI * (float)i / n + Math.PI / 2) + 0.75);
			ans.uvs.add(new Vector2(u,v));
		}
		//uv_side----------------------------------------------------------------
		for(int i = 0; i < n+1; i++) { //side bottom 2*n + 2 - 3*n + 2 ??? 
			float u = (float)((float)i / n);
			ans.uvs.add(new Vector2(u, 0));
		}
		for(int i = 0; i < n+1; i++) { //side top 3*n + 3 - 4*n + 3 ??? 
			float u = (float)((float)i / n);
			ans.uvs.add(new Vector2(u, (float)0.5));
		}
		
		//normal------------------------------------------------------------
		ans.normals.add(new Vector3(0, -1, 0)); // bottom down 0 normal
		for(int i = 0; i < n; i++) { //bottom side 1-n normal
			float x = (float)Math.cos( -Math.PI / 2-2* Math.PI * i / n);
			float y = 0;
			float z = (float)Math.sin( -Math.PI / 2-2* Math.PI * i / n);
			ans.normals.add(new Vector3(x, y, z));
		}
		ans.normals.add(new Vector3(0, 1, 0)); // top n+1 normal
		//face_botton_top--------------------------------------------------------------
		for(int i = 0; i < n - 1; i++) {//bottom 0- n-2
			OBJFace a = new OBJFace(3, true, true);
			a.setVertex(0, 0, 0, 0);
			a.setVertex(2, i + 1, i + 1, 0);
			a.setVertex(1, i + 2, i + 2, 0);
			ans.faces.add(a);
		}
		OBJFace a_bot = new OBJFace(3, true, true);
		a_bot.setVertex(0, 0, 0, 0);
		a_bot.setVertex(2, n, n, 0);
		a_bot.setVertex(1, 1, 1, 0);
		ans.faces.add(a_bot); //bottom last one n-1
		for(int i = 0; i < n - 1; i++) {//top n-2n-1
			OBJFace a = new OBJFace(3, true, true);
			a.setVertex(0, n+1, n+1, n+1);
			a.setVertex(2, i+n+3, i + n + 3, n+1);
			a.setVertex(1, i+n+2, i + n + 2, n+1);
			ans.faces.add(a);
		}
		OBJFace a_top = new OBJFace(3, true, true);
		a_top.setVertex(0, n+1, n+1, n+1);
		a_top.setVertex(2, n+2, n+2, n+1);
		a_top.setVertex(1, 2*n+1, 2*n+1, n+1);
		ans.faces.add(a_top); //top last one 2*n-1
		//face_suround------------change uv, and change position order-------------------------------------------------
		for(int i = 0; i < n - 1; i++) {
			OBJFace a = new OBJFace(3, true, true);
			a.setVertex(0, i+1, i+2*n + 2, i+1);
			a.setVertex(2, i+n+3, i+3*n + 4, i + 2);//i+n+2, i+3*n + 3, i + 1);
			a.setVertex(1, i+2, i+2*n + 3, i+2);
			ans.faces.add(a);
			OBJFace b = new OBJFace(3, true, true);
			b.setVertex(0, i+n+3, i+3*n + 4, i + 2);
			b.setVertex(1, i+n+2, i+3*n + 3, i + 1);
			b.setVertex(2, i+1, i+2*n + 2, i+1);//i+2, i+2*n + 3, i+2);
			ans.faces.add(b);
		}
		OBJFace a_bots = new OBJFace(3, true, true);
		a_bots.setVertex(0, n, 3*n + 1, n);
		a_bots.setVertex(2, n + 2, 4*n + 3, 1);//2*n+1, 4*n + 2, n);
		a_bots.setVertex(1, 1, 3*n + 2, 1);
		ans.faces.add(a_bots);
		OBJFace a_tops = new OBJFace(3, true, true);
		a_tops.setVertex(0, n + 2, 4*n + 3, 1);
		a_tops.setVertex(1, 2*n + 1, 4*n + 2, n);
		a_tops.setVertex(2, n, 3*n + 1, n);//1, 3*n + 2, 1);
		ans.faces.add(a_tops);
		return ans;
	}
	//Sphere-----------------------------------------------------------------
	public static OBJMesh Sphere(int n, int m){
		OBJMesh ans = new OBJMesh();
		//---------position----normals------------------------------------------------------
		ans.positions.add(new Vector3(0, -1, 0));//bottom 0
		ans.normals.add(new Vector3(0, -1, 0));
		for( int mm = 1; mm < m ; mm++) {
			for( int nn = 0; nn < n; nn++) {
				float x = (float)Math.sin(Math.PI * (float)mm / m) * (float)Math.sin(Math.PI + 2 * Math.PI * (float)nn / n);
				float y = -(float)Math.cos(Math.PI * (float)mm / m);
				float z = (float)Math.sin(Math.PI * (float)mm / m) * (float)Math.cos(Math.PI + 2 * Math.PI * (float)nn / n);
				ans.positions.add(new Vector3(x,y,z));
				ans.normals.add(new Vector3(x,y,z));
			}
		}
		ans.positions.add(new Vector3(0, 1, 0));
		ans.normals.add(new Vector3(0, 1, 0));//top ???
		//----------uvs------------------------------------------------------
		for( int mm = 0; mm < m + 1 ; mm++ ) { 
			for(int nn = 0; nn < n + 1; nn++) {
				if(nn ==0 && mm == 0)
					continue; //bottom 0 ~ n -1
				if(nn == n && mm == m)
					continue; //top ???
				float u = (float)nn / n;
				float v = (float)mm / m;
				ans.uvs.add(new Vector2(u,v)); //side each layer i, position j, i*(n+1) + j -1
			}
		}
		//-------------------faces--------------------------------------------
		for( int i = 0; i < n - 1 ; i++) {//bottom
			OBJFace tmp = new OBJFace(3,true,true);
			tmp.setVertex(0, 0, i, 0);
			tmp.setVertex(2, i + 1, i+n, i + 1);
			tmp.setVertex(1, i + 2, i+n+1, i + 2);
			ans.faces.add(tmp);
		}
		OBJFace tmp_b = new OBJFace(3,true,true);
		tmp_b.setVertex(0, 0, n - 1, 0);
		tmp_b.setVertex(2, n , 2 * n - 1, n );
		tmp_b.setVertex(1, 1 , 2 * n , 1);
		ans.faces.add(tmp_b);
		for( int mm = 1; mm <  m - 1; mm++) {
			for ( int nn = 0; nn < n - 1 ; nn++) {
				OBJFace tmp = new OBJFace(3,true,true);
				OBJFace tmpp = new OBJFace(3,true,true);
				int a = n * (mm - 1) + nn + 1;
				int au = mm * ( n + 1) + nn - 1;
				int b = a + 1;
				int bu = au + 1;
				int c = n * mm + nn + 1;
				int cu = (mm + 1) * ( n + 1) + nn - 1;
				int d = c + 1;
				int du = cu + 1;
				tmp.setVertex(0, a, au, a);
				tmp.setVertex(1, b, bu, b);
				tmp.setVertex(2, c, cu, c);
				ans.faces.add(tmp);
				tmpp.setVertex(0, c, cu, c);
				tmpp.setVertex(1, b, bu, b);
				tmpp.setVertex(2, d, du, d);
				ans.faces.add(tmpp);
				//break;
			}
			OBJFace tmp_l = new OBJFace(3,true,true);
			OBJFace tmpp_l = new OBJFace(3,true,true);
			int a_l = n * (mm - 1) + n;
			int au_l = mm * ( n + 1) + n - 2;
			int b_l = n * (mm - 1) + 1;
			int bu_l = au_l + 1;
			int c_l = n * mm + n;
			int cu_l = (mm + 1) * ( n + 1) + n - 2;
			int d_l = n * mm + 1;
			int du_l = cu_l + 1;
			tmp_l.setVertex(0, a_l, au_l, a_l);
			tmp_l.setVertex(1, b_l, bu_l, b_l);
			tmp_l.setVertex(2, c_l, cu_l, c_l);
			ans.faces.add(tmp_l);
			tmpp_l.setVertex(0, c_l, cu_l, c_l);
			tmpp_l.setVertex(1, b_l, bu_l, b_l);
			tmpp_l.setVertex(2, d_l, du_l, d_l);
			ans.faces.add(tmpp_l);
			//break;
		}
		//System.out.println(ans.uvs.size());
		for( int i = 0; i < n - 1 ; i++) {//top
			int a = (m - 1) * n + 1;
			int au = (m - 1) * ( n + 1) + n + i;
			int b = (m - 2) * n + i + 1;
			int bu = (m - 2) * ( n + 1) + n + i;
			int c = b + 1;
			int cu = bu + 1;
			OBJFace tmp = new OBJFace(3,true,true);
			tmp.setVertex(0, a, au, a);
			tmp.setVertex(1, b, bu, b);
			tmp.setVertex(2, c, cu, c);
			ans.faces.add(tmp);
		}
		OBJFace tmp_t = new OBJFace(3,true,true);
		tmp_t.setVertex(0, (m - 1) * n + 1, (m - 1) * ( n + 1) + 2 * n - 1, (m - 1) * n + 1);
		tmp_t.setVertex(1, (m - 2) * n + n , (m - 2) * ( n + 1) + 2 * n - 1, (m - 2) * n + n );
		tmp_t.setVertex(2, (m - 2) * n + 1 , (m - 2) * ( n + 1) + 2 * n , (m - 2) * n + 1);
		ans.faces.add(tmp_t);
		return ans;	
	}
	
	public static void normal( OBJMesh in) {
		for( int i = 0; i < in.positions.size(); i++) {
			in.normals.add(new Vector3(0, 0, 0));
		}
		for( int i = 0; i < in.faces.size(); i++) {
			help_n(in, i);  
		}
		for( int i = 0; i < in.positions.size(); i++) {
			in.normals.get(i).normalize();
		}
		//System.out.println(in.faces.size());
		for( int i = 0; i < in.faces.size(); i++) {
			in.faces.get(i).normals = new int[3];
			in.faces.get(i).normals[0] = in.faces.get(i).positions[0];
			in.faces.get(i).normals[1] = in.faces.get(i).positions[1];
			in.faces.get(i).normals[2] = in.faces.get(i).positions[2];
		}
	}
	
	//tube
	public static OBJMesh Tube( int n, int m) {
		OBJMesh ans = new OBJMesh();
		int contain[][][] =new int[2][m + 1][n];
		int count = 0;
		for( int i = 0; i < 2; i++) {
			for( int mm = 0; mm < m + 1; mm++) {
				for( int nn = 0; nn < n; nn++) {
					//System.out.println(""+i+" "+mm+" "+nn);
					float x =  (1+i)*(float)Math.sin(2 * Math.PI * (float)nn / n);
					float y = (float)mm / m;
					float z = (1+i)*(float)Math.cos(2 * Math.PI * (float)nn / n);
					ans.positions.add(new Vector3(x,y,z));
					contain[i][mm][nn] = count;
					count++;
				}
			}
		}
		//face---------------------------------------
		for(int i = 0; i < 2;i++) {
			for( int mm = 0; mm < m; mm++) {
				for(int nn = 0; nn < n - 1; nn++) {
					OBJFace a = new OBJFace(3, false, false);
					a.positions[i==0?1:0] = contain[i][mm][nn];
					a.positions[i==0?0:1] = contain[i][mm][nn+1];
					a.positions[2] = contain[i][mm+1][nn];
					ans.faces.add(a);
					OBJFace b = new OBJFace(3, false, false);
					b.positions[i==0?1:0] = contain[i][mm+1][nn];
					b.positions[i==0?0:1] = contain[i][mm][nn+1];
					b.positions[2] = contain[i][mm+1][nn+1];
					ans.faces.add(b);
				}
				OBJFace aa = new OBJFace(3, false, false);
				aa.positions[i==0?1:0] = contain[i][mm][n-1];
				aa.positions[i==0?0:1] = contain[i][mm][0];
				aa.positions[2] = contain[i][mm+1][n-1];
				ans.faces.add(aa);
				OBJFace bb = new OBJFace(3, false, false);
				bb.positions[i==0?1:0] = contain[i][mm+1][n-1];
				bb.positions[i==0?0:1] = contain[i][mm][0];
				bb.positions[2] = contain[i][mm+1][0];
				ans.faces.add(bb);
			}
		}
		for(int j = 0; j < 2; j++) {
			for( int nn = 0; nn < n - 1; nn++) {
				OBJFace a = new OBJFace(3, false, false);
				a.positions[0+j] = contain[0][j*m][nn];
				a.positions[1-j] = contain[0][j*m][nn+1];
				a.positions[2] = contain[1][j*m][nn];
				ans.faces.add(a);
				OBJFace b = new OBJFace(3, false, false);
				b.positions[0+j] = contain[1][j*m][nn];
				b.positions[1-j] = contain[0][j*m][nn+1];
				b.positions[2] = contain[1][j*m][nn+1];
				ans.faces.add(b);
			}
			OBJFace ac = new OBJFace(3, false, false);
			ac.positions[0+j] = contain[0][j*m][n-1];
			ac.positions[1-j] = contain[0][j*m][0];
			ac.positions[2] = contain[1][j*m][n-1];
			ans.faces.add(ac);
			OBJFace bc = new OBJFace(3, false, false);
			bc.positions[0+j] = contain[1][j*m][n-1];
			bc.positions[1-j] = contain[0][j*m][0];
			bc.positions[2] = contain[1][j*m][0];
			ans.faces.add(bc);
		}
		return ans;
		
	}
	
	public static void help_n( OBJMesh in, int i) {
		/**
		Vector3 an = in.positions.get(in.faces.get(i).positions[1]).clone().sub(in.faces.get(i).positions[0]);
		Vector3 bn = in.positions.get(in.faces.get(i).positions[2]).clone().sub(in.faces.get(i).positions[0]);
		Vector3 nor = an.clone();
		nor.cross(bn);
		nor.normalize();
		in.normals.get(in.faces.get(i).positions[0]).add(nor);
		in.normals.get(in.faces.get(i).positions[1]).add(nor);
		in.normals.get(in.faces.get(i).positions[2]).add(nor);
		**/
		float a[] = new float[3];
		float b[] = new float[3];
		float c[] = new float[3];
		a[0] = in.positions.get(in.faces.get(i).positions[1]).x-in.positions.get(in.faces.get(i).positions[0]).x;
		a[1] = in.positions.get(in.faces.get(i).positions[1]).y-in.positions.get(in.faces.get(i).positions[0]).y;
		a[2] = in.positions.get(in.faces.get(i).positions[1]).z-in.positions.get(in.faces.get(i).positions[0]).z;
		b[0] = in.positions.get(in.faces.get(i).positions[2]).x-in.positions.get(in.faces.get(i).positions[0]).x;
		b[1] = in.positions.get(in.faces.get(i).positions[2]).y-in.positions.get(in.faces.get(i).positions[0]).y;
		b[2] = in.positions.get(in.faces.get(i).positions[2]).z-in.positions.get(in.faces.get(i).positions[0]).z;
		c[0] = a[1] * b[2] - a[2] * b[1];
		c[1] = a[2] * b[0] - a[0] * b[2];
		c[2] = a[0] * b[1] - a[1] * b[0];
		//System.out.println(c[0]+" "+c[1]+" "+ c[2]);
		Vector3 no = new Vector3(c[0], c[1], c[2]);
		no.normalize();
		in.normals.get(in.faces.get(i).positions[0]).add(no);
		in.normals.get(in.faces.get(i).positions[1]).add(no);
		in.normals.get(in.faces.get(i).positions[2]).add(no);
	}
	
	public static void main(String [] Args) {
		int n = 32;
		int m = 16;
		boolean a = true;
		String c_s = "";
		int input = 0;
		int output = Args.length - 1;
		//(1) java MeshGen -g <sphere|cylinder> [-n <divisionsU>] [-m <divisionsV>] -o <outfile.obj>
		//(2) java MeshGen -i <infile.obj> -o <outfile.obj>
		if(Args.length == 0)
			return;
		for( int i = 0; i < Args.length - 1; i += 2) {
			switch(Args[i]) {
			case "-g" :
				//System.out.println("-g ------"+Args[i+1]);
				switch(Args[i+1]) {
				case "sphere":
					c_s = "s";
					break;
				case "cylinder":
					c_s ="c";
					break;
				case "tube":
					c_s ="t";
					break;
				default :
					System.out.println("Please input sphere or cylinder");
					return;
				}
				break;
			case "-n":
				//System.out.println("-n");
				n = Integer.parseInt(Args[i+1]);
				break;
			case "-m":
				//System.out.println("-m");
				m = Integer.parseInt(Args[i+1]);
				break;
			case "-o":
				//System.out.println("-n");
				output = i + 1;
				break;
			case "-i":
				//System.out.println("-i");
				a = false;
				input = i + 1;
				break;
			default :
				System.out.println("wrong input formate!\n(1) java MeshGen -g <sphere|cylinder> [-n <divisionsU>] [-m <divisionsV>] -o <outfile.obj>\n"
						+ "(2) java MeshGen -i <infile.obj> -o <outfile.obj>");
				return;
			}
		}
		if( a ) {
			//System.out.println("modle");
			

			switch(c_s) {
			case "c":
				//System.out.println("cylinder");
				OBJMesh cy = Cylinder(n);
				try {
					cy.writeOBJ(Args[output]);
				}
				catch(Exception e){
				}
				break;
			case "s":
				//System.out.println("sphere");
				OBJMesh sy = Sphere(n,m);
				try {
					sy.writeOBJ(Args[output]);
				}
				catch(Exception e){
				}
				break;
			case "t":
				System.out.println("tube");
				OBJMesh ty = Tube(n,m);
				try {
					ty.writeOBJ(Args[output]);
					System.out.println("---");
				}
				catch(Exception e){
				}
				break;
			default:
				System.out.println("Please read README.txt");
			}
		}else {
			//System.out.println("Normal");
			OBJMesh nn = new OBJMesh();
			try {
				nn.parseOBJ(Args[input]);
				normal(nn);
				nn.writeOBJ(Args[output]);
			}
			catch(Exception e) {
			}
		}
	}
}
