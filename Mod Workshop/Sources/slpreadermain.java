import java.io.*;
import element.*;
import java.awt.*;
//long - 8 byte
//int - 4 byte
//short - 2 byte
class slpreadermain{
	public static void main(String arg[])throws IOException
	{	
		/*menu
		 *1 - Open SLP file path name
		 *2 - Save frames and anchors
		 *3 - Options
		    - 1. Player colors
		    - 2. Shadow mask
		    - 3. Outline
		*/
		try{
			
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("==============================================================================");
        System.out.println("----------                SLP Reader by Jatayu                    ------------");
        System.out.println("==============================================================================");
		System.out.println("   1 Load SLP file                  3 Options          5 View SLP");
        System.out.println("   2 Save as frames and anchors     4 Search           6 Quit");
		System.out.println("==============================================================================");
        String str = br.readLine();
		slpreader slpname = new slpreader();
		String path = "gra00002.slp";
		
		while(!str.equals("6"))
		{	
			int select;
			if(!isParsableToInt(str)) str = "10";
			select = Integer.parseInt(str);
			
			switch(select)
			{	case 1: System.out.println("Enter name of SLP file: ");
						path = br.readLine();
						if(path.equals("")) path = "../inputdir\\gra00002.slp";
						
						boolean found = true;
						/*Do some smart checking*/
						int dotpos = path.indexOf(".");
						if(dotpos==-1)
						{	
							if(isParsableToInt(path)) {
								String newpath = nameresource(Integer.parseInt(path),true);
								String newname = nameresource(Integer.parseInt(path),false);
								
								if(fileexists(newpath+".slp")) { slpname.name = newname; path = newpath+".slp"; }	
								else if(fileexists("../inputdir\\"+newpath+".slp")) { slpname.name = newname; path = "../inputdir\\"+newpath+".slp"; }
								else if(fileexists(path+".slp")) { slpname.name = newname; path = path+".slp"; }
								else if(fileexists("../inputdir\\"+path+".slp")) { slpname.name = newname; path = "../inputdir\\"+path+".slp"; }
								else found = false;						
							}
							else if(fileexists(path+".slp")) {  slpname.name = path; path = path+".slp"; }
							else if(fileexists("../inputdir\\"+path+".slp")) {  slpname.name = path; path = "../inputdir\\"+path+".slp"; }
							else found = false;
						}
						else
						{	if(!fileexists(path) && !fileexists("../inputdir\\"+path)) found = false;
							if(fileexists("../inputdir\\"+path)) path = "../inputdir\\"+path;
						}
						if(found==false) { System.out.println("Not found"); break; }
						
						slpname.read(path);
						break;
				
				case 2: System.out.println("Enter directory path eg. C:\\Games\\ (or blank for outdir)" );
						String outdir = br.readLine();
						if(outdir.equals("")) outdir = "../outdir\\";
						System.out.println("Saving frames");
						slpname.save(outdir);
						System.out.println("Saving CSV");
						slpname.savecsv(outdir);
						break;
						
				case 3: System.out.println("Choose options");
						System.out.println("1. Player Color     2. Mask      3. Outline1      4. Outline2        5. Choose palette");
						int choice = Integer.parseInt(br.readLine());
						switch(choice)
						{	case 1: System.out.println("Enter player number ( default 1-blue )");
									slpname.playernumber = Integer.parseInt(br.readLine())-1;
									slpname.read(path);
									break;
							case 2: System.out.println("Enter index of shadow mask ( default 8) ");
									slpname.mask = Integer.parseInt(br.readLine());
									break;
							case 3: System.out.println("Enter index of outline mask1 ( default 255) ");
									slpname.outline1 = Integer.parseInt(br.readLine());
									break;
							case 4: System.out.println("Enter index of outline mask2 ( default 255) ");
									slpname.outline2 = Integer.parseInt(br.readLine());
									break;
							case 5: System.out.println("Enter 1 for AokTC, 2 for Aok , 3 for AoE/RoR");
									int choice2 = Integer.parseInt(br.readLine());
									if(choice2==1) slpname.sample = "sampleAokTC.bmp";
									else if(choice2==2) slpname.sample = "sampleAok.bmp";
									else if(choice2==3) slpname.sample = "sampleAoE.bmp";
									else System.out.println("Wrong choice");
									break;
							default: System.out.println("Incorrect choice");
						}
						break;
				
				case 4: //Searching
						
						//reading
						try {
        						BufferedReader in = new BufferedReader(new FileReader("list.txt"));
        						System.out.println("Enter the keyword ( case insensitive )");
        						System.out.println("Eg. teutonic knight,knight,or teutonic. All possible matches will be shown");
        						String match = br.readLine();
        						System.out.println();
        						
        						match = match.toLowerCase();
        						boolean foundmatch = false;
        						
        						String s;
        						while ((s = in.readLine()) != null) {
        							String s2 = s;
        							s = s.toLowerCase();
        							
        						    int posm = s.indexOf(match);
        						    if(posm!=-1)
        						    {	System.out.println(s2);
        						    	foundmatch = true;
        						    }
        						}
        						if(!foundmatch) System.out.println("No match found");
        						System.out.println();
        						
        						in.close();
        				}
        				catch (IOException e) {
        					System.out.println("IO Exception in reading list");
    					}
    					break;
					
				case 5: slpname.view();
						break;
				
				default: System.out.println("Unknown choice");
			}
			System.out.println("==============================================================================");
        	System.out.println("----------                SLP Reader by Jatayu                    ------------");
        	System.out.println("==============================================================================");
        	System.out.println("   1 Load SLP file                  3 Options          5 View SLP");
        	System.out.println("   2 Save as frames and anchors     4 Search           6 Quit");
			System.out.println("==============================================================================");
        	str = br.readLine();
			if(str.equals("")) str = "10";
			
			
		}
		System.exit(0);
		
		}catch(NumberFormatException nfe)
			{
				System.out.println("String entered is not a number!");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Press enter to exit");
				br.readLine();
			}
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