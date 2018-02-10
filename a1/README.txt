Name: Siyao Huang NetID: sh2435

partner: I am still searching for partner.

1.under folder src/
2.compile the java
	javac meshgen/MeshGen.java
3.run the programe
	(1) java meshgen.MeshGen -g <sphere|cylinder> [-n <divisionsU>] [-m <divisionsV>] -o <outfile.obj>
	(2) java meshgen.MeshGen -i <infile.obj> -o <outfile.obj>
	If you write something else it should print out some recommand like the syntax or "read README.txt"
	
	for example:
		java meshgen.MeshGen -g cylinder -o test.obj
		java meshgen.MeshGen -i bunny-nonorms.obj -o test.obj

PS: you are not allowed to sperate the "-? ???" in the command line. But you can make use command in different order. And you can miss some part
	not allowed: java meshgen.MeshGen -g -m sperate 22 -n 33 -o test.obj
	allowed: java meshgen.MeshGen -g sphere -m 22 -n 33 -o test.obj
	allowed: java meshgen.MeshGen -g test.obj

extension: 
I add -g <tube> it will generate a tube the width of inside-circle and outside-circle are 1 and 2. You can specify the m and n division. It did not generate normal and UVs, since I do not have picture for texture.

1.under folder src/
2.compile the java
	javac meshgen/MeshGen.java
3.run the programe
	(1) java meshgen.MeshGen -g <tube|sphere|cylinder> [-n <divisionsU>] [-m <divisionsV>] -o <outfile.obj>
	(2) java meshgen.MeshGen -i <infile.obj> -o <outfile.obj>
	If you write something else it should print out some recommand like the syntax or "read README.txt"
	
	for example:
		java meshgen.MeshGen -g cylinder -o test.obj
		java meshgen.MeshGen -i bunny-nonorms.obj -o test.obj

Something else: l use cinema 4D and houdini to do some modeling and animation. I think they are pretty interesting. If I want to join these companies, what should I foucus on? 
