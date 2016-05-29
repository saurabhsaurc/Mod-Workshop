import java.io.*;

public class renaming
{	public static void main(String arg[])throws IOException
	{	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Welcome to Renaming Program");
		System.out.println("Enter working directory");
		String workdir = br.readLine();
		if(workdir.equals("")) workdir = "C:\\Games\\current";
		System.out.println("1. Rename xxxx.slp to gra/ter etc. type");
		System.out.println("2. Convert bitmaps in last modified order to Gxxxxxxx.bmp's");
		System.out.println("3. Convert bitmaps in specific formats to Gxxxxxxx.bmp's and adjust frames");
		
		int choice = Integer.parseInt(br.readLine());
		
		File f1 = new File(workdir);
		File[] filearray = f1.listFiles ( );
		int numframes = (filearray.length-1)/5;
		
		String resp="";
		int newframes = 0;
		
		switch(choice)
		{	case 1: for(int i=0;i<filearray.length;i++)
					{	String name = filearray[i].getName();
						String parent = filearray[i].getParent();
						int pos = name.indexOf(".slp");
						pos = name.indexOf(".slp");
						if(pos!=-1)
						{	int num = Integer.parseInt(name.substring(0,pos));
							String newstr = "";
							if(num<10) newstr = "gra0000"+num;
							else if(num<100) newstr = "gra000"+num;
							else if(num<1000) newstr = "gra00"+num;
							else if(num<10000) newstr = "gra0"+num;
							else if(num<16000) newstr = "ter"+num;
							else newstr = "int"+num;
				
							newstr = parent+"\\"+newstr+".slp";
							System.out.println(newstr);
							System.out.println(filearray[i].renameTo(new File(newstr)));
						}
					}
					break;
					
			case 2: 					
					//bubble sort them in order of last modified
					for(int i =0;i<filearray.length;i++)
					{	for(int j=0;j<filearray.length-1;j++)
						{	long n1 = filearray[j].lastModified();
							long n2 = filearray[j+1].lastModified();
							if(n2<n1)
							{	System.out.println("swap");
								File swap = filearray[j];
								filearray[j] = filearray[j+1];
								filearray[j+1] = swap;
							}
					}
					}
					System.out.println("Enter resource number eg 00002: ");
					resp = br.readLine();
					if(resp.equals("")) resp = "00000";
					
					for(int i=0;i<filearray.length;i++)
					{	String newstr = workdir+"\\"+"G"+resp+i+".bmp";
						System.out.println(newstr);
						System.out.println(filearray[i].renameTo(new File(newstr)));
					}
					
					break;
			
			case 3: System.out.println("Number of frames: "+numframes);
					System.out.println("Enter number of frames ( must be >= number of frames present");
					resp = br.readLine();
					if(resp.equals("")) newframes = numframes;
					else newframes = Integer.parseInt(resp);
		
					System.out.println("Options: 1. Hexadecimal ( AOE type ) 2. qaz123's units format");
					System.out.println("         3. Normal format            4. Civ3 type");
					
					resp = br.readLine();
					int type = 1;
					if(resp.equals("2")) type = 2;
					if(resp.equals("3")) type = 3;
					if(resp.equals("4")) type = 4;
					
					System.out.println("Enter resource number eg 00002: ");
					resp = br.readLine();
					if(resp.equals("")) resp = "00000";
		
					System.out.println("Enter target directory");
					String outdir = br.readLine();
					if(outdir.equals("")) outdir = "C:\\Games\\current\\";
		
					//bubble sort them in order of last modified
					for(int i =0;i<filearray.length;i++)
					{	for(int j=0;j<filearray.length-1;j++)
						{	long n1 = filearray[j].lastModified();
							long n2 = filearray[j+1].lastModified();
							if(n2<n1)
							{	System.out.println("swap");
								File swap = filearray[j];
								filearray[j] = filearray[j+1];
								filearray[j+1] = swap;
							}
						}
					}
					
					for(int i=0;i<filearray.length-1;i++)
					{	String name = filearray[i].getName();
						System.out.println(name);
						int pos = name.indexOf(".BMP");
						if(pos==-1) pos = name.indexOf(".bmp");
						if(pos!=-1)
						{
						int newnum;
						int n=0;
						if(type==1)	
						{	String num = name.substring(pos-2,pos);
				
							String before = name.substring(0,pos-2);
							String lastdigit = num.substring(1);
							int last = 0;
							if(lastdigit.equals("A")) last = 10;
							else if(lastdigit.equals("B")) last = 11;
							else if(lastdigit.equals("C")) last = 12;
							else if(lastdigit.equals("D")) last = 13;
							else if(lastdigit.equals("E")) last = 14;
							else if(lastdigit.equals("F")) last = 15;
							else last = Integer.parseInt(lastdigit);
				
							newnum = Integer.parseInt(num.substring(0,1))*16+last;
						}
						else if(type==2)
						{	String num = name.substring(pos-3,pos);
							char c = num.charAt(1);
							char c2 = num.charAt(0);
							n=i+1;
				
							if(c<48 || c>57) n = Integer.parseInt(num.substring(2));
							else if(c2<48 || c2>57) n = Integer.parseInt(num.substring(1));
							else n = Integer.parseInt(num);
							System.out.println(n);
				
							//Smart renaming system for qaz123's units
				
							newnum = 0;
							if(n<=numframes*1)
							{	newnum = newframes*1+ (n-numframes*0);
							}
							else if(n<=numframes*2)
							{	newnum = newframes*3+(n-numframes*1);
							}
							else if(n<=numframes*5)
							{	newnum = newframes*0+(n-numframes*4);
							}
							else if(n<=numframes*6)
							{	newnum = newframes*2+(n-numframes*5);
							}
							else if(n<=numframes*7)
							{	newnum = newframes*4+(n-numframes*6);
							}
						}
						else if(type==4)
						{	String num = name.substring(pos-3,pos);
							char c = num.charAt(1);
							char c2 = num.charAt(0);
							n=i+1;
				
							if(c<48 || c>57) n = Integer.parseInt(num.substring(2));
							else if(c2<48 || c2>57) n = Integer.parseInt(num.substring(1));
							else n = Integer.parseInt(num);
							System.out.println(n);
				
							//Smart renaming system for qaz123's units
				
							newnum = 0;
							if(n<=numframes*1)
							{	newnum = newframes*1+ (n-numframes*0);
							}
							else if(n<=numframes*2)
							{	newnum = newframes*0+(n-numframes*1);
							}
							else if(n<=numframes*6)
							{	newnum = newframes*4+(n-numframes*5);
							}
							else if(n<=numframes*7)
							{	newnum = newframes*3+(n-numframes*6);
							}
							else if(n<=numframes*8)
							{	newnum = newframes*2+(n-numframes*7);
							}
						}
						else
						{	String num = name.substring(pos-2,pos);
							char c = num.charAt(1);
							char c2 = num.charAt(0);
							n=i+1;
				
							if(c<48 || c>57) n = Integer.parseInt(num.substring(2));
							else if(c2<48 || c2>57) n = Integer.parseInt(num.substring(1));
							else n = Integer.parseInt(num);
							System.out.println(n);
				
							//Smart renaming system for normal format units
				
							newnum = 0;
							if(n<=numframes*1)
							{	newnum = newframes*0+ (n-numframes*0);
							}
							else if(n<=numframes*2)
							{	newnum = newframes*1+(n-numframes*1);
							}
							else if(n<=numframes*3)
							{	newnum = newframes*2+(n-numframes*2);
							}
							else if(n<=numframes*4)
							{	newnum = newframes*3+(n-numframes*3);
							}
							else if(n<=numframes*5)
							{	newnum = newframes*4+(n-numframes*4);
							}
						}
						String nstr = "";
						if(newnum<10) nstr = "0"+newnum;
						else nstr = ""+newnum;
						//System.out.println("newnum= "+newnum);
						String newstr = outdir +"G"+resp+nstr+".bmp";
				
						System.out.println(newstr);
						System.out.println(filearray[i].renameTo(new File(newstr)));
						int cfile;
						if(type==2)
						{
						if(n==numframes*1 || n==numframes*2 || n==numframes*5 || n==numframes*6 || n==numframes*7)
						{	int diff = newframes-numframes;
							for(int j=1;j<=diff;j++)
							{	int x = newnum+j;
								//copying file contents
								nstr = "";
								if(x<10) nstr = "0"+x;
								else nstr = ""+x;
								String instr = newstr;
								String outstr = outdir+"G0000"+nstr+".bmp";
								File inputFile = new File(instr);
    							File outputFile = new File(outstr);
    							
    							try{
									FileInputStream fin = new FileInputStream(inputFile);
									FileOutputStream fout = new FileOutputStream(outputFile);
		
									int nn = fin.available();
									byte b[] = new byte[nn];
									fin.read(b,0,nn);
									fout.write(b,0,nn);
		
									fin.close();
									fout.close();
		
									}
									catch (Exception e)
            						{
                					System.out.println("Caught exception in copying drsbuild!"+e);
            						}
    					
							}
						}
						
						}
						else if(type==3)
						{
							
							if(n==numframes*1 || n==numframes*2 || n==numframes*3 || n==numframes*4 || n==numframes*5)
							{	int diff = newframes-numframes;
							for(int j=1;j<=diff;j++)
							{	int x = newnum+j;
								//copying file contents
								nstr = "";
								if(x<10) nstr = "0"+x;
								else nstr = ""+x;
								String instr = newstr;
								String outstr = outdir+"G"+resp+nstr+".bmp";
								File inputFile = new File(instr);
    							File outputFile = new File(outstr);

    							FileReader in = new FileReader(inputFile);
    							FileWriter out = new FileWriter(outputFile);
    					
    							while ((cfile = in.read()) != -1)
      								out.write(cfile);

    							in.close();
    							out.close();
							}
							}
						}
						
						
						}
			
					}
					break;
			default:System.out.println("Unknown choice");				
					
		}
		
		
		//System.exit(0);
		
	}
}