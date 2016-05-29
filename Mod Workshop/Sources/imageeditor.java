import java.awt.*;
import java.awt.image.*;
import java.awt.MediaTracker.*;
import java.io.*;
import java.util.*;
import java.util.Vector.*;

public class imageeditor
{	/*Will display a screen
	1. - renaming
	2. - load a sample image and get it's color table
	3. - Compare with given image and get color table mapping using the distance formula
	4. - settings
	     1 - convert to- AokTC, Aok, AoE
	     2 - change mask index
	     3 - manipulate player colors - for blue color
	     4 - Change no. of frames
	5. - generate output 256 color images in a particular directory. Use new color table and indices based on mapping
	6. - exit
	*/
	static String workdir; //Working directory
	static String filename;
	static String imagename;//Sample image
	
	public static void main(String arg[])throws IOException
	{	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("What operation do you want to perform? ");
        System.out.println("   1 Open working directory            4 Options");
        System.out.println("   2 Load sample image                 5 Generate new images");
        System.out.println("   3 Generate color table mapping      6 quit");
		String str = br.readLine();
		File fdir;
		File filearray[] = new File[1];
		String lastchar;
		String sample = "sampleAokTC.bmp";
		boolean playercolorch = false;
		boolean setnomask = false;
		
		imagehandler img = new imagehandler();
		try{
		
		while(!str.equals("6"))
		{	int select = Integer.parseInt(str);
			switch(select)
			{	case 1:	System.out.println("Enter working directory ");
						workdir = br.readLine();
						if(workdir.equals("")) workdir = "../inputdir\\";
						lastchar = workdir.substring(workdir.length()-1);
						if(!lastchar.equals("\\"))
						{	workdir += "\\"; System.out.println("\\Added automatically");
						}
						fdir = new File(workdir);
						filearray = fdir.listFiles();
						for(int i=0;i<filearray.length;i++)
						{	filename = filearray[i].getName();
							System.out.println(filename);
						}
						System.out.println("First file: "+filearray[0].getName());
						System.out.println("Rename to resource: ");
						String res = br.readLine();
						if(res.equals("")) System.out.println("Original filenames - no renaming");
						else
						{	for(int i=0;i<filearray.length;i++)
							{	filename = filearray[i].getName();
								int pos = filename.indexOf(".bmp");
								if(pos==-1) pos = filename.indexOf(".BMP");
								if(pos!=-1)
								{	int num = i+1;
									String out = workdir+"G"+res;
									if(num<10) out = out+"0"+num+".bmp";
									else out = out +num+".bmp";
									
									System.out.println("Renamed to "+out);
									filearray[i].renameTo(new File(out));
								}
							}
						}
						fdir = new File(workdir);
						filearray = fdir.listFiles();
						
						break;
				
				case 2: System.out.println("Enter sample image name ");
						imagename = br.readLine();
						if(imagename.equals(""))
						{	System.out.println("Default: "+filearray[0].getName());
							imagename = filearray[0].getName();
						}
						img.loadbitmap((workdir+imagename),0);
						img.sampleused = sample;
						img.loadbitmap(sample,1);
						break;
				
				case 3: System.out.println("Getting mapping.. ");
						img.getmapping();
						if(setnomask==false) img.getmask();
						break;
				
				case 4: System.out.println("Settings");
						System.out.println("1. Choose game 2. Change mask index 3. Player color 4. Set No mask ");
						int choice = Integer.parseInt(br.readLine());
						switch(choice)
						{	case 1: System.out.println("1. AokTC (default) 2. Aok 3. AoE-RoR 4. Custom");
									int resp = Integer.parseInt(br.readLine());
									if(resp==1) { sample = "sampleAokTC.bmp"	; img.mask = 255;	}
									if(resp==2) { sample = "sampleAok.bmp"	; img.mask = 255;	}
									if(resp==3) { sample = "sampleAoE.bmp"	; img.mask = 0;	}
									if(resp==4) { System.out.println("Enter custom image name");sample = br.readLine();}
									break;
							case 2: System.out.println("Change mask index to:? ");
									int changedmask = Integer.parseInt(br.readLine());
									img.setmask(changedmask);
									break;
							case 3: System.out.println("Player colours adjusted");
									playercolorch = true;
									img.setplayercolor();
									break;
							case 4: System.out.println("No mask index set.");
									setnomask = true;
									break;
							default: break;
						}
						break;
				case 5: System.out.println("Enter target directory");
						String outdir = br.readLine();
						if(outdir.equals("")) outdir = "../outputdir\\";
						lastchar = outdir.substring(outdir.length()-1);
						if(!lastchar.equals("\\"))
						{	outdir += "\\"; System.out.println("\\Added automatically");
						}
						System.out.println("Generating new images");
						//System.out.println(workdir);
						fdir = new File(workdir);
						filearray = fdir.listFiles();
						//String fileschanged[] = new String[filearray.length][2]; 
						if(playercolorch) img.setplayercolor();
						// Stores input filenames and output filenames
						for(int i=0;i<filearray.length;i++)
						{	filename = filearray[i].getName();
							System.out.println("File no: "+(i));
							String infilename = workdir+filename;
							String outfilename = outdir+filename;
							if( filename.indexOf(".bmp")==-1 && filename.indexOf(".BMP")==-1) continue;
							img.savebitmap(infilename,outfilename);
						}						
						break;
				case 6: break;
				default: System.out.println("Command not recognized");
			}
			System.out.println("What operation do you want to perform? ");
        	System.out.println("   1 Open working directory            4 Options");
        	System.out.println("   2 Load sample image                 5 Generate new images");
        	System.out.println("   3 Generate color table mapping      6 quit");
			str = br.readLine();
		}
		
		}catch(IOException e)
		{	System.out.println(e);
			System.exit(0);
		}
	}
}