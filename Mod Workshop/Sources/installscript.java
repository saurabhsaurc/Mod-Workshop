import java.io.*;

/* For installing mods and dat files only*/
class installscript{
	public static void main(String arg[])throws IOException
	{	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean found = true;
		System.out.println("Enter Install directory");
		String inputdir = br.readLine();
		if(inputdir.equals("")) inputdir = "C:\\Games\\1modworkshop\\examples\\Maul";
						
		if(!directoryexists(inputdir))
		{	if(directoryexists("../outdir\\"+inputdir)) {  inputdir = "../outdir\\"+inputdir; }
							else found = false;
		}
						
		if(found==false) { System.out.println("Not found"); System.exit(0); }
		
		System.out.println("Directory found\r\n");
		
		//do renaming
		//rename .slp's to gra0xxxx.slp , ter15xxx.slp etc.
		String slplist[] = new String[10000];
		int slpnum = 0;
		String stringdata = "";
		
		File dir = new File(inputdir); 
		File filearray[] = dir.listFiles();
		boolean eulafound = false;
		boolean drsbuildfound = false;
		boolean datafilefound = false;
		boolean graphicsfound = false;
		boolean interfacfound = false;
		boolean terrainfound = false;
		
		for(int i=0;i<filearray.length;i++)
		{	String filename = filearray[i].getName();
			filename = filename.toUpperCase();
			
			if(filename.equals("EULA.TXT")) eulafound = true;
			if(filename.equals("DRSBUILD.EXE")) drsbuildfound = true;
			if(filename.equals("DATA"))
			{	File datadir = filearray[i];
				File dataarray[] = datadir.listFiles();
				stringdata = filearray[i].getName();
				
				for(int j=0;j<dataarray.length;j++)
				{	String datafilename = dataarray[j].getName();
					datafilename = datafilename.toUpperCase();
					//System.out.println(datafilename);
					
					if(datafilename.equals("GRAPHICS"))
					{	//SLP's here should be renamed and added
						System.out.println("Graphics slps found");
						graphicsfound = true;
						
						File gdir = dataarray[j];
						File graphicsdir[] = gdir.listFiles();
						
						System.out.println("Number of graphics SLP's: "+graphicsdir.length);
						
						for(int k=0;k<graphicsdir.length;k++)
						{	String slpname = graphicsdir[k].getName();
							String path = graphicsdir[k].getParent();
							String newstr = "";
							String first3 = slpname.substring(0,3);
							if(first3.equals("gra")) newstr = slpname;
							else{
							
							int pos = slpname.indexOf(".slp");
							
							int num = Integer.parseInt(slpname.substring(0,pos));
							
							if(num<10) newstr = "gra0000"+num+".slp";
							else if(num<100) newstr = "gra000"+num+".slp";
							else if(num<1000) newstr = "gra00"+num+".slp";
							else if(num<10000) newstr = "gra0"+num+".slp";
							}
							
							slplist[slpnum] = newstr;
							slpnum++;
							
							newstr = path+"\\"+newstr;
							System.out.println(newstr);
							
							graphicsdir[k].renameTo(new File(newstr));
						}
						
						
					}
					
					if(datafilename.equals("TERRAIN"))
					{	//SLP's here should be renamed and added
						System.out.println("Terrain slps found");
						terrainfound = true;
						
						File tdir = dataarray[j];
						File terraindir[] = tdir.listFiles();
						
						System.out.println("Number of terrain SLP's: "+terraindir.length);
						
						for(int k=0;k<terraindir.length;k++)
						{	String slpname = terraindir[k].getName();
							String path = terraindir[k].getParent();
							String newstr = "";
							String first3 = slpname.substring(0,3);
							if(first3.equals("ter")) newstr = slpname;
							else{
							
							int pos = slpname.indexOf(".slp");
							
							int num = Integer.parseInt(slpname.substring(0,pos));
							
							newstr = "ter"+num+".slp";
							
							}
							
							slplist[slpnum] = newstr;
							slpnum++;
							
							newstr = path+"\\"+newstr;
							System.out.println(newstr);
							
							terraindir[k].renameTo(new File(newstr));
						}
						
						
					}
					
					if(datafilename.equals("INTERFAC"))
					{	//SLP's here should be renamed and added
						
						System.out.println("Interfac slps found");
						interfacfound = true;
						
						File idir = dataarray[j];
						File intdir[] = idir.listFiles();
						
						System.out.println("Number of interfac SLP's: "+intdir.length);
						
						for(int k=0;k<intdir.length;k++)
						{	String slpname = intdir[k].getName();
							String path = intdir[k].getParent();
							String newstr = "";
							String first3 = slpname.substring(0,3);
							if(first3.equals("int")) newstr = slpname;
							else{
							
							int pos = slpname.indexOf(".slp");
							
							int num = Integer.parseInt(slpname.substring(0,pos));
							
							newstr = "int"+num+".slp";
							
							}
							
							slplist[slpnum] = newstr;
							slpnum++;
							
							newstr = path+"\\"+newstr;
							System.out.println(newstr);
							
							intdir[k].renameTo(new File(newstr));
						}
						
						
					}
					
					if(datafilename.equals("EMPIRES2_X1_P1.DAT"))
					{	System.out.println("Data file found");
						datafilefound = true;
					}
					
				}
				
				
			}
		}
		System.out.println("Creating batch files");
		System.out.println("Enter a name for the batch files, no spaces (eg. something like install_maul)");
		String installname = br.readLine();
		String uninstallname = "un"+installname;
		String backupdir = "backup"+installname;
		//Create myInstall and myUninstall
		try{
			BufferedWriter finstall = new BufferedWriter(new FileWriter(inputdir+"\\"+stringdata+"\\"+installname+".bat"));
			
			for(int i=0;i<slpnum;i++)
			{	
				if(slplist[i].indexOf("gra")!=-1) finstall.write("drsbuild /e graphics.drs "+slplist[i]+" /o "+backupdir+"\\graphics"+"\r\n");
				else if(slplist[i].indexOf("int")!=-1) finstall.write("drsbuild /e interfac.drs "+slplist[i]+" /o "+backupdir+"\\interfac"+"\r\n");
				else if(slplist[i].indexOf("ter")!=-1) finstall.write("drsbuild /e terrain.drs "+slplist[i]+" /o "+backupdir+"\\terrain"+"\r\n");
				
			}
			
			finstall.write("drsbuild /r graphics.drs graphics\\*.slp"+"\r\n");
			finstall.write("drsbuild /r interfac.drs interfac\\*.slp"+"\r\n");
			finstall.write("drsbuild /r terrain.drs terrain\\*.slp"+"\r\n");
			
			finstall.close();
			
			BufferedWriter funinstall = new BufferedWriter(new FileWriter(inputdir+"\\"+stringdata+"\\"+uninstallname+".bat"));
			
			funinstall.write("drsbuild /r graphics.drs "+backupdir+"\\graphics\\*.slp"+"\r\n");
			funinstall.write("drsbuild /r interfac.drs "+backupdir+"\\interfac\\*.slp"+"\r\n");
			funinstall.write("drsbuild /r terrain.drs "+backupdir+"\\terrain\\*.slp"+"\r\n");
			
			funinstall.close();
		}catch (Exception e)
            {
                System.out.println("Caught exception in writing install/uninstall batch files! ");
                e.printStackTrace ();
            }
		
		if(eulafound==false)
		{	System.out.println("Eula not found. Creating EULA.txt automatically");
			
			try{
				BufferedReader eulain = new BufferedReader(new FileReader("EULA.txt"));
				BufferedWriter eulaout = new BufferedWriter(new FileWriter(inputdir+"\\EULA.txt"));
				
				int cfile;
				while ((cfile = eulain.read()) != -1)
      				eulaout.write(cfile);
      			
      			eulain.close();
      			eulaout.close();
				
			}catch (Exception e)
            {
                System.out.println("Caught exception in writing EULA! ");
                e.printStackTrace ();
            }
		}
		
		if(drsbuildfound==false)
		{	try{
				FileInputStream fin = new FileInputStream("../resources\\drsbuild.exe");
				FileOutputStream fout = new FileOutputStream(inputdir+"\\Data\\drsbuild.exe");
		
				int n = fin.available();
				byte b[] = new byte[n];
				fin.read(b,0,n);
				fout.write(b,0,n);
		
				fin.close();
				fout.close();
		
				}
				catch (Exception e)
            	{
                System.out.println("Caught exception in copying drsbuild!"+e);
            	}
		}
		long spaceneeded = size(new File(inputdir))/1024;
		System.out.println("Total size: "+spaceneeded+" kb");
		//input constants
		System.out.println("Enter name of your mod");
        String name = br.readLine();
        name = name.trim();
        System.out.println("Enter your own name");
        String myname = br.readLine();
        System.out.println("Enter the name of your install script. eg myscript.nsi");
        String scriptfilename = "../examples\\"+br.readLine();
		
		try{
			
			BufferedWriter fout = new BufferedWriter(new FileWriter(scriptfilename));
        
        	fout.write("!define CAMPAIGN_NAME \""+name+"\"\r\n");
			fout.write("!define RELEASE_VERSION 1.0\r\n");
			fout.write("!define MY_SOURCE_DIRECTORY \""+inputdir+"\"\r\n");
			fout.write("!define MY_NAME \""+myname+"\"\r\n");
			fout.write("!define INSTALLER_NAME "+"\"Install_"+name+"\"\r\n");
			fout.write("!define UNINSTALLER_NAME "+"\"Uninstall_"+name+"\"\r\n");
			fout.write("!define EULA_NAME \"EULA\""+"\r\n");
			fout.write("\r\n");
			fout.write("!define PRODUCT_UNINST_KEY \"Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\${CAMPAIGN_NAME}\\\""+"\r\n");
			fout.write("!define PRODUCT_UNINST_ROOT_KEY \"HKLM\""+"\r\n");
			fout.write("!include \"MUI.nsh\""+"\r\n");
			fout.write("\r\n");
			fout.write("!define MUI_ABORTWARNING"+"\r\n");
			fout.write("!define MUI_ABORTWARNING_TEXT \"Do you wish to cancel installation of ${CAMPAIGN_NAME}?\""+"\r\n");
			fout.write("\r\n");
			fout.write("!define MUI_WELCOMEPAGE_TITLE \"Thankyou for downloading ${CAMPAIGN_NAME}\" "+"\r\n");
			fout.write("!define MUI_WELCOMEPAGE_TEXT 'Please make sure that you close all windows, \\ \r\n");
			fout.write("especially Age of Kings: The Conquerors, before attempting installation.'"+"\r\n");
			fout.write("!insertmacro MUI_PAGE_WELCOME"+"\r\n");
			fout.write("\r\n");
			fout.write("!define MUI_LICENSEPAGE_TEXT_TOP \"Important Notice:\""+"\r\n");
			fout.write("!define MUI_LICENSEPAGE_TEXT_BOTTOM \"Please make sure you have thoroughly read the agreement before continuing.\""+"\r\n");
			fout.write("!define MUI_LICENSEPAGE_BUTTON \"I agree\""+"\r\n");
			fout.write("!insertmacro MUI_PAGE_LICENSE \"${MY_SOURCE_DIRECTORY}\\${EULA_NAME}.txt\""+"\r\n");
			fout.write("\r\n");
			fout.write("!define MUI_DIRECTORYPAGE_TEXT_TOP \"${CAMPAIGN_NAME} must be installed in your default AoK directory\\"+"\r\n");
			fout.write(".$\\r$\\rIf you have previously installed an earlier version of ${CAMPAIGN_NAME}, uninstall it before \\"+"\r\n");
			fout.write("attempting to install this version..$\\r$\\rYou must have C Patch installed.\""+"\r\n");
			fout.write("\r\n");			
			fout.write("!define MUI_DIRECTORYPAGE_TEXT_DESTINATION \"Select your default AoK directory:\""+"\r\n");
			fout.write("!insertmacro MUI_PAGE_DIRECTORY"+"\r\n");
			fout.write("!insertmacro MUI_PAGE_INSTFILES"+"\r\n");
			fout.write("\r\n");
			fout.write("!define MUI_FINISHPAGE_NOAUTOCLOSE"+"\r\n");
			fout.write("!define MUI_FINISHPAGE_TEXT \"Congratulations! ${CAMPAIGN_NAME} is now installed.\""+"\r\n");
			fout.write("!insertmacro MUI_PAGE_FINISH"+"\r\n");
			fout.write("!insertmacro MUI_UNPAGE_INSTFILES"+"\r\n");
			fout.write("!insertmacro MUI_LANGUAGE \"English\""+"\r\n");
			fout.write("\r\n");
			fout.write("Name \"${CAMPAIGN_NAME} ${RELEASE_VERSION}\""+"\r\n");
			fout.write("OutFile \"${INSTALLER_NAME}.exe\""+"\r\n");
			fout.write("InstallDir \"$PROGRAMFILES\\Microsoft Games\\Age of Empires II\\\""+"\r\n");
			fout.write("\r\n");
			fout.write("BrandingText \" \""+"\r\n");
			fout.write("ShowInstDetails show"+"\r\n");
			fout.write("ShowUnInstDetails show"+"\r\n");
			fout.write("\r\n");
			fout.write("Section \"Main\" SEC01"+"\r\n");
			fout.write("Addsize "+spaceneeded+"\r\n");
			fout.write("SetOverwrite on"+"\r\n");
			fout.write("\r\n");
			if(datafilefound)
			{
			fout.write("CreateDirectory \"$INSTDIR\\DATA\\"+backupdir+"\""+"\r\n");
			fout.write("Copyfiles /silent \"$INSTDIR\\DATA\\empires2_x1_p1.dat\" \"$INSTDIR\\DATA\\"+backupdir+"\""+"\r\n");
			fout.write("\r\n");
			
			fout.write("SetOutPath \"$INSTDIR\\DATA\""+"\r\n");
			fout.write("File \"${MY_SOURCE_DIRECTORY}\\DATA\\empires2_x1_p1.dat\""+"\r\n");
			fout.write("\r\n");
			}
			if(graphicsfound)
			{
			fout.write("CreateDirectory \"$INSTDIR\\DATA\\"+backupdir+"\\graphics\""+"\r\n");
			fout.write("\r\n");
			
			fout.write("CreateDirectory \"$INSTDIR\\DATA\\graphics\""+"\r\n");
			fout.write("SetOutPath \"$INSTDIR\\DATA\\graphics\""+"\r\n");
			fout.write("File \"${MY_SOURCE_DIRECTORY}\\DATA\\graphics\\*.slp\""+"\r\n");
			fout.write("\r\n");
			}
			if(terrainfound)
			{
			fout.write("CreateDirectory \"$INSTDIR\\DATA\\"+backupdir+"\\terrain\""+"\r\n");
			fout.write("\r\n");
			
			fout.write("CreateDirectory \"$INSTDIR\\DATA\\terrain\""+"\r\n");
			fout.write("SetOutPath \"$INSTDIR\\DATA\\terrain\""+"\r\n");
			fout.write("File \"${MY_SOURCE_DIRECTORY}\\DATA\\terrain\\*.slp\""+"\r\n");
			fout.write("\r\n");
			}
			if(interfacfound)
			{
			fout.write("CreateDirectory \"$INSTDIR\\DATA\\"+backupdir+"\\interfac\""+"\r\n");
			fout.write("\r\n");
			
			fout.write("CreateDirectory \"$INSTDIR\\DATA\\interfac\""+"\r\n");
			fout.write("SetOutPath \"$INSTDIR\\DATA\\interfac\""+"\r\n");
			fout.write("File \"${MY_SOURCE_DIRECTORY}\\DATA\\interfac\\*.slp\""+"\r\n");
			fout.write("\r\n");
			}
			fout.write("Setoutpath \"$INSTDIR\\DATA\""+"\r\n");
			fout.write("File \"${MY_SOURCE_DIRECTORY}\\DATA\\"+installname+".bat\" "+"\r\n");
			fout.write("File \"${MY_SOURCE_DIRECTORY}\\DATA\\"+uninstallname+".bat\""+"\r\n");
			fout.write("File \"${MY_SOURCE_DIRECTORY}\\DATA\\drsbuild.exe\""+"\r\n");
			fout.write("\r\n");
			fout.write("nsExec::exec \'\"$INSTDIR\\DATA\\"+installname+".bat\"\'"+"\r\n");
			fout.write("\r\n");
			if(graphicsfound)
			{
			fout.write("Delete \"$INSTDIR\\DATA\\graphics\\*.*\""+"\r\n");
			fout.write("RMDir \"$INSTDIR\\DATA\\graphics\""+"\r\n");
			}
			if(terrainfound)
			{
			fout.write("Delete \"$INSTDIR\\DATA\\terrain\\*.*\""+"\r\n");
			fout.write("RMDir \"$INSTDIR\\DATA\\terrain\""+"\r\n");
			}
			if(interfacfound)
			{
			fout.write("Delete \"$INSTDIR\\DATA\\interfac\\*.*\""+"\r\n");
			fout.write("RMDir \"$INSTDIR\\DATA\\interfac\""+"\r\n");
			}
			fout.write("Delete \"$INSTDIR\\DATA\\"+installname+".bat\""+"\r\n");
			
			fout.write("\r\n");
			fout.write("SectionEnd"+"\r\n");
			fout.write("\r\n");
			fout.write("Section -Post"+"\r\n");
  			fout.write("WriteUninstaller \"$INSTDIR\\${UNINSTALLER_NAME}.exe\""+"\r\n");
			fout.write("\r\n");
  			fout.write("WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\" \"DisplayName\" \"$(^Name)\""+"\r\n");
  			fout.write("WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\" \"UninstallString\" \"$INSTDIR\\${UNINSTALLER_NAME}.exe\""+"\r\n");
  			fout.write("WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\" \"DisplayVersion\" \"${RELEASE_VERSION}\""+"\r\n");
			fout.write("SectionEnd"+"\r\n");
			fout.write("\r\n");
			fout.write("Function un.onUninstSuccess"+"\r\n");
  			fout.write("  HideWindow"+"\r\n");
  			fout.write("  MessageBox MB_ICONINFORMATION|MB_OK \"${CAMPAIGN_NAME} was successfully uninstalled.\" "+"\r\n");
			fout.write("FunctionEnd"+"\r\n");
			fout.write("\r\n");
			fout.write("Function un.onInit"+"\r\n");
  			fout.write("  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 \"Are you sure you want to uninstall ${CAMPAIGN_NAME}?\" \\"+"\r\n");
  			fout.write("  IDYES +2"+"\r\n");
  			fout.write("  Abort"+"\r\n");
			fout.write("FunctionEnd"+"\r\n");
			fout.write("\r\n");
			fout.write("Section Uninstall"+"\r\n");
			fout.write("SetOutPath \"$INSTDIR\\DATA\""+"\r\n");
			fout.write("\r\n");
			fout.write("nsExec::exec '\"$INSTDIR\\DATA\\"+uninstallname+".bat\"'"+"\r\n");
			fout.write("\r\n");
			if(graphicsfound)
			{
  			fout.write("Delete \"$INSTDIR\\DATA\\"+backupdir+"\\graphics\\*.*\""+"\r\n");
  			fout.write("RMDir \"$INSTDIR\\DATA\\"+backupdir+"\\graphics\""+"\r\n");
			fout.write("\r\n");
			}
			if(terrainfound)
			{
  			fout.write("Delete \"$INSTDIR\\DATA\\"+backupdir+"\\terrain\\*.*\""+"\r\n");
  			fout.write("RMDir \"$INSTDIR\\DATA\\"+backupdir+"\\terrain\""+"\r\n");
			fout.write("\r\n");
			}
			if(interfacfound)
			{
  			fout.write("Delete \"$INSTDIR\\DATA\\"+backupdir+"\\interfac\\*.*\""+"\r\n");
  			fout.write("RMDir \"$INSTDIR\\DATA\\"+backupdir+"\\interfac\""+"\r\n");
			fout.write("\r\n");
			}
			if(datafilefound)
			{
    		fout.write("Delete \"$INSTDIR\\DATA\\empires2_x1_p1.dat\""+"\r\n");
    		fout.write("Copyfiles /silent \"$INSTDIR\\DATA\\"+backupdir+"\\empires2_x1_p1.dat\" \"$INSTDIR\\DATA\""+"\r\n");
			fout.write("\r\n");
        	fout.write("Delete \"$INSTDIR\\DATA\\"+backupdir+"\\empires2_x1_p1.dat\""+"\r\n");
        	}
        	fout.write("RMDir \"$INSTDIR\\DATA\\"+backupdir+"\""+"\r\n");
			fout.write("\r\n");
    		fout.write("Delete \"$INSTDIR\\DATA\\drsbuild.exe\""+"\r\n");
    		fout.write("Delete \"$INSTDIR\\DATA\\"+uninstallname+".bat\""+"\r\n"); 
			fout.write("\r\n");
  			fout.write("Delete \"$INSTDIR\\${UNINSTALLER_NAME}.exe\""+"\r\n");
			fout.write("\r\n");
  			fout.write("DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\""+"\r\n");
  			fout.write("SetAutoClose true"+"\r\n");
			fout.write("SectionEnd"+"\r\n");
			
			fout.close();
		}
		catch (Exception e)
            {
                System.out.println("Caught exception in writing install script! ");
                e.printStackTrace ();
            }
	}
	
	public static long size(File file) {
  		if (file.isFile())
   		 return file.length();
  		File[] files = file.listFiles();
  		long size = 0;
  		if (files != null) {
  		  for (int i = 0; i < files.length; i++)
 		     size += size(files[i]);
 		 }
 		 return size;
	}
	public static boolean directoryexists(String x)
	{	File f = new File(x);
		return f.exists()&f.isDirectory();
	}


}