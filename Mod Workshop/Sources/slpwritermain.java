import java.io.*;

class slpwritermain
{	public static void main(String arg[])throws IOException
	{	/*menu
		 *1 - Open frames folder
		 *2 - Create masks
		 *3 - Options
		    - 1. Transparency mask on
		    - 2. Outline1
		    - 3. Outline2
		    - 4. Player colours
		    - 5. Shadow pixels
		 *
		 *4 - Settings
		    - 1. Automatic palette converter
		    - 2. Bluening
		    - 3. Choose game
		 *5 - Write SLP
		 *6 - Exit
		*/
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("==============================================================================");
        System.out.println("----------                SLP Writer by Jatayu                    ------------");
        System.out.println("==============================================================================");
        System.out.println("   1 Open working directory          5 Apply CVT");
        System.out.println("   2 Create masks                    6 Write Slp");
        System.out.println("   3 Options                         7 Check Slp");
        System.out.println("   4 Settings                        8 Quit");
		System.out.println("==============================================================================");
        String str = br.readLine();
		slpwriter slpw = new slpwriter();
		slpreader slpname = new slpreader();
		String inputdir = "../outdir\\";
		
		boolean transmask = true;
		boolean outline1 = true;
		boolean outline2 = false;
		boolean plcolor = true;
		boolean shadowpix = false;
		
		boolean autopalette = true;
		boolean automask = true;
		boolean autobluening = false;
		String sample = "sampleAokTC.bmp";
		
		while(!str.equals("8"))
		{	int select = 0;
			if(!isParsableToInt(str)) select = 0;
			else select = Integer.parseInt(str);
			
			switch(select)
			{	case 1: boolean found = true;
						System.out.println("Enter working directory (either full pathname or relative to outdir) ");
						inputdir = br.readLine();
						if(inputdir.equals("")) inputdir = "../outdir\\Frames01252\\";
						
						if(!directoryexists(inputdir))
						{	if(directoryexists("../outdir\\"+inputdir)) {  inputdir = "../outdir\\"+inputdir; }
							else found = false;
						}
						
						if(found==false) { System.out.println("Not found"); break; }
						
						//inputdir already entered
						System.out.println("Directory found\r\n");
						break;
						
				case 2: //Assuming correct value of inputdir
						File fdir = new File(inputdir);
						File filearray[] = new File[1];
						filearray = fdir.listFiles();
						String fpath = fdir.getPath();
						boolean csvfound = false;
						//
						
						for(int i=0;i<filearray.length;i++)
						{	String filename = filearray[i].getName();
							if(filename.charAt(0)=='G')
							{	String str1 = filename.substring(1);
							
								//Do palette converting
								String fp = filearray[i].getAbsolutePath();
								System.out.println("Frame "+(i)+" "+fp);
								
								if(autopalette) convert(fp,sample,autobluening,automask);
								
								if(transmask) slpw.writemasks(filearray[i].getPath(),fpath+"\\S"+str1,1);
								if(outline1) slpw.writemasks(filearray[i].getPath(),fpath+"\\U"+str1,2);
								if(plcolor) slpw.writemasks(filearray[i].getPath(),fpath+"\\P"+str1,3);
								if(shadowpix) slpw.writemasks(filearray[i].getPath(),fpath+"\\H"+str1,4);
								if(outline2) slpw.writemasks(filearray[i].getPath(),fpath+"\\O"+str1,5);
								
							}
							
							int pos = filename.indexOf(".csv");
							if(pos!=-1)
							{	csvfound = true;
								slpw.csvfile = filearray[i].getPath();
							}
						}
						
						if(csvfound==false) System.out.println("CSV file not found");
						break;
						
				case 3: //Options
						System.out.println("Enter option to change the following");
						System.out.println("1. Create transparency mask "+onoff(transmask));
						System.out.println("2. Create outline1 mask "+onoff(outline1));
						System.out.println("3. Create outline2 mask "+onoff(outline2));
						System.out.println("4. Create player color mask "+onoff(plcolor));
						System.out.println("5. Create shadow pixels mask "+onoff(shadowpix));
						
						int ch = Integer.parseInt(br.readLine());
						switch(ch)
						{	case 1: transmask = !transmask;
									System.out.println("Transparency mask is now "+onoff(transmask));
									break;
							case 2: outline1 = !outline1;
									System.out.println("Outline1 mask is now "+onoff(outline1));
									break;
							case 3: outline2 = !outline2;
									System.out.println("Outline2 mask is now "+onoff(outline2));
									break;
							case 4: plcolor = !plcolor;
									System.out.println("Player colour mask is now "+onoff(plcolor));
									break;
							case 5: shadowpix = !shadowpix;
									System.out.println("Shadow pixels mask is now "+onoff(shadowpix));
									break;
							default: System.out.println("Unknown");
						}
						break;
				case 4: //Settings
						System.out.println("Enter setting to change");
						System.out.println("1. Automatic palette converter "+onoff(autopalette));
						System.out.println("2. Automatic bluening "+onoff(autobluening));
						System.out.println("3. Automatic mask "+onoff(automask));
						System.out.println("4. Choose game");
						
						int ch1 = Integer.parseInt(br.readLine());
						switch(ch1)
						{	case 1: autopalette = !autopalette;
									System.out.println("Automatic palette converter is now "+onoff(autopalette));
									break;
							case 2: autobluening = !autobluening;
									System.out.println("Auto bluening is now "+onoff(autobluening));
									break;
							case 3: automask = !automask;
									System.out.println("Auto mask is now "+onoff(automask));
									break;		
							case 4:	System.out.println("1. AokTC      2. SWGB     3. AoE/RoR");
									int ch2 = Integer.parseInt(br.readLine());
									if(ch2==1) { sample = "sampleAokTC.bmp"; outline2 = false; }
									if(ch2==2) { sample = "sampleAokTC.bmp"; outline2 = true; }
									if(ch2==3) { sample = "sampleAoE.bmp"; outline1 = false; outline2 = false; }
									break;
							default: System.out.println("Unknown");
						}
						break;
				case 5: //apply CVT
						System.out.println("Enter name of cvt file wrt input directory or full path name");
						String cvtname = br.readLine();
						if(cvtname.equals("")) cvtname = "shadow.cvt";
						
						boolean cvtfound = false;
						if(fileexists(cvtname)) cvtfound = true;
						else
						{	cvtname = "../inputdir\\"+cvtname;
							if(fileexists(cvtname)) cvtfound = true;
						}
						
						if(cvtfound) 
						{	System.out.println("Found cvt file");
							slpw.convertcvt(cvtname);
						}
						else System.out.println("Cvt file not found");
						break;
				case 6:	//Write SLP
						System.out.println("Enter a name for new file eg. out.slp");
						String outputname = br.readLine();
						if(outputname.equals("")) outputname = "out.slp";
						
						System.out.println("Getting frame data");
						File fdir2 = new File(inputdir);
						File filearray2[] = new File[1];
						filearray2 = fdir2.listFiles();
						String fpath2 = fdir2.getPath()+"\\";
						int numframes = 0;
						//
						
						for(int i=0;i<filearray2.length;i++)
						{	String filename = filearray2[i].getName();
							if(filename.charAt(0)=='G')
							{	
								numframes++;
								//System.out.println("Frame "+(i+1)+" "+fp);
							}
						}
						
						System.out.println("Number of frames: "+numframes);
						slpw.initframes(numframes);
						int count = 0;
						
						System.out.println("Getting frame data");
						for(int i=0;i<filearray2.length;i++)
						{	String filename = filearray2[i].getName();
							if(filename.charAt(0)=='G')
							{	//Get frame i
								slpw.getframe(count,fpath2,filename,transmask,outline1,outline2,plcolor,shadowpix);
								count++;
							}
						}
						
						System.out.println("Compressing frame data");
						slpw.compress();
						
						System.out.println("Writing SLP out.slp");
						slpw.write("../outdir\\"+outputname);
						
						System.out.println("Done");
						break;
				//check slp	
				//read and print as frames and anchors	
				case 7: System.out.println("Enter name of SLP file to check (blank for out.slp): ");
						String path = br.readLine();
						if(path.equals("")) { path = "../outdir\\out.slp";slpname.name = "-out"; }
						
						boolean found2 = true;
						/*Do some smart checking*/
						int dotpos = path.indexOf(".");
						if(dotpos==-1)
						{	
							if(isParsableToInt(path)) {
								String newpath = nameresource(Integer.parseInt(path),true);
								String newname = nameresource(Integer.parseInt(path),false);
								
								if(fileexists(newpath+".slp")) { slpname.name = newname; path = newpath+".slp"; }	
								else if(fileexists("../outdir\\"+newpath+".slp")) { slpname.name = newname; path = "../outdir\\"+newpath+".slp"; }
								else if(fileexists(path+".slp")) { slpname.name = newname; path = path+".slp"; }
								else if(fileexists("../outdir\\"+path+".slp")) { slpname.name = newname; path = "../outdir\\"+path+".slp"; }
								else found = false;						
							}
							else if(fileexists(path+".slp")) {  slpname.name = path; path = path+".slp"; }
							else if(fileexists("../outdir\\"+path+".slp")) {  slpname.name = path; path = "../outdir\\"+path+".slp"; }
							else found = false;
						}
						else
						{	if(!fileexists(path) && !fileexists("../outdir\\"+path)) found2 = false;
							if(fileexists("../outdir\\"+path)) path = "../outdir\\"+path;
						}
						if(found2==false) { System.out.println("Not found"); break; }
						
						slpname.read(path);
						
						System.out.println("Enter directory path for saving eg. C:\\Games\\ (or blank for outdir)" );
						String outdir = br.readLine();
						if(outdir.equals("")) outdir = "../outdir\\";
						System.out.println("Saving frames");
						slpname.save(outdir);
						System.out.println("Saving CSV");
						slpname.savecsv(outdir);
						break;
						
				case 8:	System.out.println("Exiting"); break;
				default: System.out.println("Unknown"); break;
			}
			
			System.out.println();
			System.out.println("==============================================================================");
        	System.out.println("----------                SLP Writer by Jatayu                    ------------");
        	System.out.println("==============================================================================");
        	System.out.println("   1 Open working directory          5 Apply CVT");
        	System.out.println("   2 Create masks                    6 Write Slp");
        	System.out.println("   3 Options                         7 Check Slp");
        	System.out.println("   4 Settings                        8 Quit");
			System.out.println("==============================================================================");
        	str = br.readLine();	
		
		}
		
		
		
	}
	public static boolean directoryexists(String x)
	{	File f = new File(x);
		return f.exists()&f.isDirectory();
	}
	public static void convert(String filename,String sample,boolean autobluening,boolean automask)
	{	imagehandler img = new imagehandler();
	
		img.loadbitmap(filename,0);
		img.sampleused = sample;
		img.loadbitmap(sample,1);
		
		img.getmapping();
		if(automask) img.getmask();
		if(autobluening) img.setplayercolor();
		
		img.savebitmap(filename,"temp.bmp");
		img.copybitmap("temp.bmp",filename);
	}
	static String onoff(boolean x)
	{	if(x) return "ON"; else return "OFF";
	}
	public static boolean isParsableToInt(String i)
	{
	try
		{
		Integer.parseInt(i);
		return true;
		}
	catch(NumberFormatException nfe)
		{
		return false;
		}
	}
	public static boolean fileexists(String x)
	{	File f = new File(x);
		return f.exists();
	}
	public static String nameresource(int i,boolean gra)
	{	String str;
		if(gra){
			
		if(i>50000) str = "int"+i;
		if(i>15000) str = "ter"+i;
		else if(i>1000) str = "gra0"+i;
		else if(i>100) str = "gra00"+i;
		else if(i>10) str = "gra000"+i;
		else str = "gra0000"+i;
		}
		else
		{
		if(i>50000) str = ""+i;
		if(i>15000) str = ""+i;
		else if(i>1000) str = "0"+i;
		else if(i>100) str = "00"+i;
		else if(i>10) str = "000"+i;
		else str = "0000"+i;
		}
		return str;
	}
	
}