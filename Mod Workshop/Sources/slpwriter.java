import java.io.*;

class slpwriter
{	int shadow;
	int outputmask;
	int outline1,outline2;
	int numframes;
	boolean plcolorused;
	String csvfile; //stores the path name of csv file
	
	frame frames[];
	//Default constructor
	slpwriter()
	{	shadow = 131;
		outline1 = 8;
		outline2 = 124;
		outputmask = 0;
		csvfile = "dunno";
		cvtused = false;
		plcolorused = false;
	}
	void initframes(int n)
	{	numframes = n;
		frames = new frame[n];
	}
	//remember to correct bug in last line of outline!
	void writemasks(String inputfile,String outputfile,int choice)
	{	imagehandler img = new imagehandler();
		img.loadbitmap(inputfile,0);
		byte f[][] = img.returnrawpixels();
		int width = f[0].length;
		int height = f.length;
		
		int newframe[][] = new int[height][width];
		for(int i=0;i<height;i++)
		{	for(int j=0;j<width;j++)
			{	newframe[i][j] = (int)(f[i][j]&0xff);
			}
		}
		//Changed 1st line to last line
		int mask = newframe[height-1][0];
		//mask
		switch(choice)
		{
		case 1: //Mask
				for(int i=0;i<height;i++)
				{	for(int j=0;j<width;j++)
					{	if(newframe[i][j]==mask) newframe[i][j] = outputmask; else newframe[i][j] = 255;
					}
				}
				break;
				
		case 2: //Outline1
				//left-right
				for(int i=0;i<height;i++)
				{	//front
					for(int j=0;j<width-1;j++)
					{	if(newframe[i][j]==mask && newframe[i][j+1]!=mask && j+1!=width-1)
						{	newframe[i][j]= -2;
						}
					}
					//back
					for(int j=width-1;j>0;j--)
					{	if(newframe[i][j]==mask && newframe[i][j-1]!=mask && j+1!=width-1)
						{	newframe[i][j]= -2;
						}
					}
				}
				//top down
				for(int j=0;j<width;j++)
				{	//top
				
					for(int i=0;i<=height-2;i++)
					{	if(newframe[i][j]==mask && newframe[i+1][j]!=mask && newframe[i+1][j]!=-2)
						{	newframe[i][j]= -2;
						}
					}
					//down
					for(int i=height-1;i>0;i--)
					{	if(newframe[i][j]==mask && newframe[i-1][j]!=mask && newframe[i-1][j]!=-2)
						{	newframe[i][j]= -2;
						}
					}
				}
				for(int i=0;i<height;i++)
				{	for(int j=0;j<width;j++)
					{	if(newframe[i][j]!=-2) newframe[i][j] = outputmask; 
					}
				}
				break;
				
		case 3: //Player colors
				for(int i=0;i<height;i++)
				{	for(int j=0;j<width;j++)
					{	if(!(newframe[i][j]>=16 && newframe[i][j]<=23)) newframe[i][j] = outputmask;
					}
				}
				break;
				
		case 4: //Shadow pixels shadow = 131
				//CHANGED!!!
				for(int i=0;i<height;i++)
				{	for(int j=0;j<width;j++)
					{	if(newframe[i][j]==131) newframe[i][j] = 131;
						else newframe[i][j] = outputmask;
					}
				}
				break;
				
		case 5: //Outline2
				//Make an outline 1 and an outline of it.
				//outline1
				//left-right
				for(int i=0;i<height;i++)
				{	//front
					for(int j=0;j<width-1;j++)
					{	if(newframe[i][j]==mask && newframe[i][j+1]!=mask && j+1!=width-1)
						{	newframe[i][j]= -2;
						}
					}
					//back
					for(int j=width-1;j>0;j--)
					{	if(newframe[i][j]==mask && newframe[i][j-1]!=mask && j+1!=width-1)
						{	newframe[i][j]= -2;
						}
					}
				}
				//top down
				for(int j=0;j<width;j++)
				{	//top
				
					for(int i=0;i<=height-2;i++)
					{	if(newframe[i][j]==mask && newframe[i+1][j]!=mask && newframe[i+1][j]!=-2)
						{	newframe[i][j]= -2;
						}
					}
					//down
					for(int i=height-2;i>0;i--)
					{	if(newframe[i][j]==mask && newframe[i-1][j]!=mask && newframe[i-1][j]!=-2)
						{	newframe[i][j]= -2;
						}
					}
				}
				//Now make an outline of it
				for(int i=0;i<height;i++)
				{	//front
					for(int j=0;j<width-1;j++)
					{	if(newframe[i][j]==mask && newframe[i][j+1]!=mask && j+1!=width-1)
						{	newframe[i][j]= -3;
						}
					}
					//back
					for(int j=width-1;j>0;j--)
					{	if(newframe[i][j]==mask && newframe[i][j-1]!=mask && j+1!=width-1)
						{	newframe[i][j]= -3;
						}
					}
				}
				//top down
				for(int j=0;j<width;j++)
				{	//top
				
					for(int i=0;i<=height-2;i++)
					{	if(newframe[i][j]==mask && newframe[i+1][j]!=mask && newframe[i+1][j]!=-3)
						{	newframe[i][j]= -3;
						}
					}
					//down
					for(int i=height-2;i>0;i--)
					{	if(newframe[i][j]==mask && newframe[i-1][j]!=mask && newframe[i-1][j]!=-3)
						{	newframe[i][j]= -3;
						}
					}
				}
				for(int i=0;i<height;i++)
				{	for(int j=0;j<width;j++)
					{	if(newframe[i][j]!=-3) newframe[i][j] = outputmask; 
					}
				}
				break;
		default: System.out.println("Unrecognized command");
		}	
		
		Aokbitmap b = new Aokbitmap(outputmask,outline1,outline2,shadow);
		b.write(outputfile,newframe,f[0].length,f.length);
	}
	/*gets frame data in the picture*/
	void getframe(int num,String path,String filename,boolean msk,boolean o1,boolean o2,boolean pl,boolean sh)
	{	imagehandler img = new imagehandler();
		System.out.println(path+" file:"+filename);
		
		img.loadbitmap(path+filename,0);
		byte f[][] = img.returnrawpixels();
		int width = f[0].length;
		int height = f.length;
		
		int newframe[][] = new int[height][width];
		for(int i=0;i<height;i++)
		{	for(int j=0;j<width;j++)
			{	newframe[i][j] = (int)(f[i][j]&0xff);
			}
		}
		
		if(msk)
		{	String str = path+"S"+filename.substring(1);
			img.loadbitmap(str,0);
			f = img.returnrawpixels();
			
			for(int i=0;i<height;i++)
			{	for(int j=0;j<width;j++)
				{	int pixel = (int)(f[i][j]&0xff);
					if(pixel==0) newframe[i][j] = -1;
				}
			}
		}
		
		if(o1)
		{	String str = path+"U"+filename.substring(1);
			img.loadbitmap(str,0);
			f = img.returnrawpixels();
			
			for(int i=0;i<height;i++)
			{	for(int j=0;j<width;j++)
				{	int pixel = (int)(f[i][j]&0xff);
					if(pixel!=0) newframe[i][j] = -2;
				}
			}
		}
		
		if(o2)
		{	String str = path+"O"+filename.substring(1);
			img.loadbitmap(str,0);
			f = img.returnrawpixels();
			
			for(int i=0;i<height;i++)
			{	for(int j=0;j<width;j++)
				{	int pixel = (int)(f[i][j]&0xff);
					if(pixel!=0) newframe[i][j] = -3;
				}
			}
		}		
		
		if(pl)
		{	String str = path+"P"+filename.substring(1);
			img.loadbitmap(str,0);
			f = img.returnrawpixels();
			
			for(int i=0;i<height;i++)
			{	for(int j=0;j<width;j++)
				{	int pixel = (int)(f[i][j]&0xff);
					if(pixel!=0) newframe[i][j] = pixel;//1000+pixel;
				}
			}
			
			plcolorused = true;
		}
		//shadow overwrites player if conflict
		if(sh)
		{	String str = path+"H"+filename.substring(1);
			img.loadbitmap(str,0);
			f = img.returnrawpixels();
			System.out.println("Entering shadow stuff");
			
			for(int i=0;i<height;i++)
			{	for(int j=0;j<width;j++)
				{	int pixel = (int)(f[i][j]&0xff);
					if(pixel!=0) newframe[i][j] = -4;
				}
			}
		}
		
		frames[num] = new frame();
		frames[num].width = width;
		frames[num].height = height;
		frames[num].picture = newframe;
		
		if(cvtused) frames[num].convertcvt(mapping);
		//frames[num].display();
		
	}
	int mapping[];
	boolean cvtused;
	void convertcvt(String cvtname)
	{	try{
		
		cvtused = true;
		BufferedReader in = new BufferedReader(new FileReader(cvtname));
		mapping = new int[256];
		for(int i=0;i<256;i++)
			mapping[i] = -11;
		
		String str;
		str = in.readLine();
		if(str.equals("<MPS PALETTE REMAP>"))
			System.out.println("Recognized as MPS cvt file");
		else
			System.out.println("Not recognized as MPS cvt file");
			
        while ((str = in.readLine()) != null) {
            int pos = str.indexOf(" ");
            int left = Integer.parseInt(str.substring(0,pos));
            int right = Integer.parseInt(str.substring(pos+1));
            //System.out.println(left+"->"+right);
            
            mapping[left] = right;
        }
        
        
		in.close();
		}
		catch (Exception e)
            {
                System.out.println("Caught exception in applying CVT! ");
                e.printStackTrace ();
            }
	}
	/*compresses frames into a set of commands*/
	void compress()
	{	//rowedges
		//int i=0;
		for(int i=0;i<numframes;i++) {
			//Big for loop starts here - for all frames
			
		frames[i].initrowedge(frames[i].height);
		
		for(int j=0;j<frames[i].height;j++)
		{	int left = 0;
			for(int k=0;k<frames[i].width;k++)
			{	if(frames[i].picture[j][k]!=-1) break;
				left++;
			}
			int right = 0;
			for(int k=frames[i].width-1;k>=0;k--)
			{	if(frames[i].picture[j][k]!=-1) break;
				right++;
			}
			if(left==frames[i].width)
			{	//blank row
				right = 0;
			}
			frames[i].edges[j] = new rowedge(left,right);
			//System.out.println("rowedge: "+left+" "+right);
			
		}
		
		//All commands
		frames[i].initcommands();
		/* implement
		 *color - big and small
		 *skip - big and small
		 *player color
		 *fill and player color fill - not done
		 *outline1
		 *outline2
		 *shadow
		 *
		 */
		for(int j=0;j<frames[i].height;j++) //Do for all lines!
		{	//System.out.println("Line "+j);
			int prev = -5;
		
			int start = frames[i].edges[j].left;
			int end = frames[i].width-frames[i].edges[j].right-1;
			//System.out.println("start: "+start+" end: "+end);
			int pos = start;
			int count = 0;
			String type = "null";
			int colorlength = -1;
			int colorstart = -1;
			/*take the following changes into account
			 *
			 *1. mask/etc. -> mask/etc.
			 *2. mask/etc. -> color/plcolor
			 *3. color/plcolor -> mask/etc.
			 *4. color/plcolor -> different color/plcolor
			 *
			 */
			while(pos<=end)
			{	
				int currentpixel = frames[i].picture[j][pos];
				
				//Change from color/player colour -> mask etc.
				//and mask etc. to different mask etc.
				if(currentpixel!=prev && (currentpixel<0))
				{	if(!type.equals("null") && !type.equals("color"))
					{ 	//System.out.println(type+" "+count);
					
						if(type.equals("skip")) frames[i].cmdskip(count);
						else if(type.equals("outline1")) frames[i].cmdoutline1(count);
						else if(type.equals("outline2")) frames[i].cmdoutline2(count);
						else if(type.equals("shadowpix")) frames[i].cmdshadowpix(count);											
					}
					
					//Forget about fill colors for now
					//colors stored in byte array only
					//System.out.println("Change from: "+prev+" to "+currentpixel);
					
					if(type.equals("color"))
					{	//Change from color to mask
						//System.out.println("color: "+colorlength+" bytes from position "+colorstart);
						
						byte bytearray[] = new byte[colorlength];
						for(int k=0;k<colorlength;k++)
							bytearray[k] = (byte)(frames[i].picture[j][k+colorstart]);
						
						frames[i].cmdcolorblock(bytearray,plcolorused);
						
						colorlength = -1;
						colorstart = -1;
					}
					
					
					//Changes to mask/etc.
					if(currentpixel==-1) type = "skip";
					else if(currentpixel==-2) type = "outline1";
					else if(currentpixel==-3) type = "outline2";
					else if(currentpixel==-4) type = "shadowpix";
					else System.out.println("Unknown pixel");
					
					prev = currentpixel;
					count = 1;
				}
				else if(currentpixel!=prev && (prev<0)) //ie. change from mask/etc. to color/plcolor
				{	
					if(!type.equals("null"))  //Action here						
					{ 	//System.out.println(type+" "+count);
						if(type.equals("skip")) frames[i].cmdskip(count);
						else if(type.equals("outline1")) frames[i].cmdoutline1(count);
						else if(type.equals("outline2")) frames[i].cmdoutline2(count);
						else if(type.equals("shadowpix")) frames[i].cmdshadowpix(count);
					}
					
					type = "color";
					prev = currentpixel;
					count = 1;
					colorstart = pos;
					colorlength = 1;
				}
				else if(type.equals("color"))
				{	
					//Change from one colour/plcolor to another
						//don't change color
							colorlength++;
							prev = currentpixel;
							count = 1;
					
				}
				else count++;
				
				pos++;
			}
			
			//while loop over
			if(type.equals("color"))
			{	//Change from color to mask
						//System.out.println("color: "+colorlength+" bytes from position "+colorstart);
						byte bytearray[] = new byte[colorlength];
						for(int k=0;k<colorlength;k++)
							bytearray[k] = (byte)(frames[i].picture[j][k+colorstart]);
						
							frames[i].cmdcolorblock(bytearray,plcolorused);
							
						colorlength = -1;
						colorstart = -1;
			}
			else 
			{ 	//System.out.println(type+" "+count);
						if(type.equals("skip")) frames[i].cmdskip(count);
						else if(type.equals("outline1")) frames[i].cmdoutline1(count);
						else if(type.equals("outline2")) frames[i].cmdoutline2(count);
						else if(type.equals("shadowpix")) frames[i].cmdshadowpix(count);
			}
			
			//end of the line
			frames[i].cmdeol();
			
		}
		
		//frames[i].displaycommands();
		
		//big for loop ends here
		}
	}
	/*Writes an SLP file with frames as given by frames[] and filename*/
	String version;
	String comment;
	
	void write(String filename)
	{	//We have data for frames as stored in commands
	try{
		version = "2.0N";
		comment = "ArtDesk 1.00 SLP Writer ";
		//numframes already computed
		//numframes = 1;//for the moment;
		System.out.println("Number of frames "+numframes);
		
		FileOutputStream fout = new FileOutputStream(filename);
		
		//Write SLP header
		byte bv[] = version.getBytes();
		fout.write(bv);
		
		byte bnumfr[] = bytefromint(numframes);
		fout.write(bnumfr);
		
		byte bcomment[] = comment.getBytes();
		fout.write(bcomment);
		
		//Compute the offset stuff
		int pointer = 32;//32 byte slp header
		//There are numframes number of frame headers. Each frame header is 32 bytes long
		pointer += 32*numframes;
		
		for(int i=0;i<numframes;i++)
		{	frames[i].frame_outline_offset = pointer;
			frames[i].computecommandbytes();
			
			//There will be frames[i].height number of rowedge datas (4 bytes each),
			// and the same number of command offset datas after that (4 bytes each)
			
			pointer += frames[i].height*4;
			frames[i].frame_data_offsets = pointer;
			
			pointer += frames[i].height*4;
			for(int j=0;j<frames[i].height;j++)
			{	//computing the command offset data
				
				frames[i].commandoff[j] = pointer;
				pointer += frames[i].linecommandlength(j);//-2;// TEMPORARY FIX!!!
				
				//System.out.println("Command length of line "+j+" is "+frames[i].linecommandlength(j)+" pointer: "+pointer);
			}			
		}
		//compute anchors
		String lines[] = new String[1000];
		int numlines=0;
		BufferedReader in = new BufferedReader(new FileReader(csvfile));
        String str;
        while ((str = in.readLine()) != null) {
            lines[numlines] = str;
            numlines++;
        }
        in.close();
        
        for(int i=0;i<numframes;i++)
        {	int pos = lines[i].indexOf(",");
        	String lx = lines[i].substring(0,pos);
        	String rx = lines[i].substring(pos+1);
        	//System.out.println("x "+lx+" y "+rx);
        	int ancx = Integer.parseInt(lx.trim());
        	int ancy = Integer.parseInt(rx.trim());
        	frames[i].setanchor(ancx,ancy);
        }
        //done setting anchors
		
		for(int i=0;i<numframes;i++)
		{
		
		frames[i].computeframeheader();
		//Write frame headers
		byte bframe_data_offset[] = bytefromint(frames[i].frame_data_offsets);
		fout.write(bframe_data_offset);
		
		byte bframe_outline_offset[] = bytefromint(frames[i].frame_outline_offset);
		fout.write(bframe_outline_offset);
		
		byte bpalette[] = bytefromint(frames[i].palette_offset);
		fout.write(bpalette);
		
		byte bproperties[] = bytefromint(frames[i].properties);
		fout.write(bproperties);
		
		byte bwidth[] = bytefromint(frames[i].width);
		fout.write(bwidth);
		
		byte bheight[] = bytefromint(frames[i].height);
		fout.write(bheight);
		
		byte banchorx[] = bytefromint(frames[i].anchorx);
		fout.write(banchorx);
		
		byte banchory[] = bytefromint(frames[i].anchory);
		fout.write(banchory);		
		
		}
		
		//int i=0;
		//Write frame data for all frames
		for(int i=0;i<numframes;i++)
		{
		
		//write rowedges
		for(int j=0;j<frames[i].height;j++)
		{	//2 byte arrays
			byte bleft[] = bytefromshort(frames[i].edges[j].left);
			byte bright[] = bytefromshort(frames[i].edges[j].right);
			
			fout.write(bleft);
			fout.write(bright);
		}
		
		//write command offsets
		for(int j=0;j<frames[i].height;j++)
		{	//2 byte arrays
			byte bcommandoff[] = bytefromint(frames[i].commandoff[j]);
			
			fout.write(bcommandoff);
		}
		
		//write commands
		for(int j=0;j<frames[i].numcommands;j++)
		{	String type = frames[i].commands[j].type;
			if(type.equals("one"))
			{	byte cmd[] = { frames[i].commands[j].cmdbyte };//1 byte array
				fout.write(cmd);
			}
			else if(type.equals("two length"))
			{	byte cmdlength[] =  { frames[i].commands[j].cmdbyte , frames[i].commands[j].next_byte };//2 byte array
				fout.write(cmdlength);
			}
			else if(type.equals("two data"))
			{	byte cmd[] =  { frames[i].commands[j].cmdbyte };//1 byte array
				fout.write(cmd);
				
				fout.write(frames[i].commands[j].data);
			}
			else if(type.equals("three"))
			{	byte cmdlength[] =  { frames[i].commands[j].cmdbyte , frames[i].commands[j].next_byte };//2 byte array
				fout.write(cmdlength);
				
				fout.write(frames[i].commands[j].data);
			}
			else System.out.println("Whoa, unrecognized type");
		}
		
		}
		
		fout.close();
		
	
	}catch (Exception e)
            {
                System.out.println("Caught exception in write slp! ");
                e.printStackTrace ();
            }
            
	}
	
	
    byte[] bytefromint(int i) {
		return new byte[] { (byte)i , (byte)(i>>8), (byte)(i>>16), (byte)(i>>24) };
	}
	
	byte[] bytefromshort(int i) {
		return new byte[] { (byte)i, (byte)(i>>8) };
	}
}
	