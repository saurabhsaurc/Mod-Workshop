import java.io.*;
import element.*;
import java.awt.*;

class slpreader
{	String name; //Stores resource NUMBER ie like 00002

	String version;
	int numframes;
	String Comment;
	int playernumber;	
	int mask;
	int outline1;
	int outline2;
	int shadow;
	String sample;
	//DrawingWindow d;
	
	frame frames[];
	//command list
	
    //constructor
    slpreader()
    {	version = "2.0N";
    	Comment = "ArtDesk 1.00 SLP Writer";
    	playernumber = 0;
    	mask = 8;
    	outline1=8;
    	outline2=8;
    	shadow = 131;
    	sample = "sampleAokTC.bmp";
    }
    //Read an SLP and make it into 256 color bitmaps
	void read(String s)
	{	try{
		
		FileInputStream fs = new FileInputStream(s);
		
		int bytesread = 0;
		
		int vlen=version.length(); // Version
                
        byte ver[]=new byte[vlen];
        fs.read(ver,0,vlen);
        
        int nlen = 4; //4 byte numframes
        
        byte nfram[]=new byte[nlen];
        fs.read(nfram,0,nlen);
        
        numframes = (((int)nfram[3]&0xff)<<24)
                    | (((int)nfram[2]&0xff)<<16)
                    | (((int)nfram[1]&0xff)<<8)
                    | (int)nfram[0]&0xff;
                    
        System.out.println("Number of frames: "+numframes);
        //initialize frames
        frames = new frame[numframes];
        for(int i=0;i<numframes;i++)
        {	frames[i] = new frame();
        }
        
        int clen=Comment.length(); // Version
                
        byte com[]=new byte[clen];
        fs.read(com,0,clen);
        
        fs.read();//skip a byte
        bytesread = 32;//32 byte SLP header
        
        /*Reading a frame header for every frame
         *size of the frame header is 32 bytes
         */
		for(int i=0;i<numframes;i++)
		{
		//System.out.println("------Frame: "+i+" -------");	        
        int frame_data_offset_len = 4; 
        
        byte bframedo[]=new byte[frame_data_offset_len];
        fs.read(bframedo,0,frame_data_offset_len);
        
        frames[i].frame_data_offsets = (((int)bframedo[3]&0xff)<<24)
                    | (((int)bframedo[2]&0xff)<<16)
                    | (((int)bframedo[1]&0xff)<<8)
                    | (int)bframedo[0]&0xff;
        
        //System.out.println("Frame data offsets: "+frames[i].frame_data_offsets);
        
        int frame_outline_offset_len = 4; 
        
        byte bframeoo[]=new byte[frame_outline_offset_len];
        fs.read(bframeoo,0,frame_outline_offset_len);
        
        frames[i].frame_outline_offset = (((int)bframeoo[3]&0xff)<<24)
                    | (((int)bframeoo[2]&0xff)<<16)
                    | (((int)bframeoo[1]&0xff)<<8)
                    | (int)bframeoo[0]&0xff;
        
        
        //System.out.println("Frame outline offsets: "+frames[i].frame_outline_offset);
        
        int palette_offset_len = 4; 
        
        byte bpal[]=new byte[palette_offset_len];
        fs.read(bpal,0,palette_offset_len);
        
        frames[i].palette_offset = (((int)bpal[3]&0xff)<<24)
                    | (((int)bpal[2]&0xff)<<16)
                    | (((int)bpal[1]&0xff)<<8)
                    | (int)bpal[0]&0xff;
        
        //System.out.println("Palette offsets: "+frames[i].palette_offset);
        
        int properties_len = 4; 
        
        byte bprop[]=new byte[properties_len];
        fs.read(bprop,0,properties_len);
        
        frames[i].properties = (((int)bprop[3]&0xff)<<24)
                    | (((int)bprop[2]&0xff)<<16)
                    | (((int)bprop[1]&0xff)<<8)
                    | (int)bprop[0]&0xff;
        
        //System.out.println("properties: "+frames[i].properties);
        
        int width_len = 4;
        
        byte bwidth[]=new byte[width_len];
        fs.read(bwidth,0,width_len);
        
        frames[i].width = (((int)bwidth[3]&0xff)<<24)
                    | (((int)bwidth[2]&0xff)<<16)
                    | (((int)bwidth[1]&0xff)<<8)
                    | (int)bwidth[0]&0xff;
        
        //System.out.println("Width: "+frames[i].width);
        
        int height_len = 4;
        
        byte bheight[]=new byte[height_len];
        fs.read(bheight,0,height_len);
        
        frames[i].height = (((int)bheight[3]&0xff)<<24)
                    | (((int)bheight[2]&0xff)<<16)
                    | (((int)bheight[1]&0xff)<<8)
                    | (int)bheight[0]&0xff;
        
        //System.out.println("height: "+frames[i].height);
        
        int anchorx_len = 4;
        
        byte banchorx[]=new byte[anchorx_len];
        fs.read(banchorx,0,anchorx_len);
        
        frames[i].anchorx = (((int)banchorx[3]&0xff)<<24)
                    | (((int)banchorx[2]&0xff)<<16)
                    | (((int)banchorx[1]&0xff)<<8)
                    | (int)banchorx[0]&0xff;
        
        //System.out.println("anchorx: "+frames[i].anchorx);
        
        int anchory_len = 4;
        
        byte banchory[]=new byte[anchory_len];
        fs.read(banchory,0,anchory_len);
        
        frames[i].anchory = (((int)banchory[3]&0xff)<<24)
                    | (((int)banchory[2]&0xff)<<16)
                    | (((int)banchory[1]&0xff)<<8)
                    | (int)banchory[0]&0xff;
        
        //System.out.println("anchory: "+frames[i].anchory);
        bytesread += 32;
        
        }
        
        //System.out.println("After reading frame headers: "+bytesread);
        /*Now read data for each frame*/
        for(int i=0;i<numframes;i++)
        //for(int i=0;i<1;i++)//Do for each frame
        {
        
        frames[i].initrowedge(frames[i].height);
        frames[i].initpic(frames[i].width,frames[i].height);
        
        for(int j=0;j<frames[i].height;j++)
        {	byte browedge[] = new byte[2];
        	fs.read(browedge,0,2);
        	bytesread +=2;
        	frames[i].edges[j].left = (((int)(browedge[1]&0xff))<<8) | ((int)(browedge[0]&0xff));
        	if(frames[i].edges[j].left>1000) frames[i].edges[j].left = frames[i].width;
        	fs.read(browedge,0,2);
        	bytesread += 2;
        	frames[i].edges[j].right = (((int)(browedge[1]&0xff))<<8) | ((int)(browedge[0]&0xff));
        	if(frames[i].edges[j].right>1000) frames[i].edges[j].right = frames[i].width;
        	//System.out.println("Rowedge "+j+" left: "+frames[i].edges[j].left+" right: "+frames[i].edges[j].right);
        }
        
        /*read until frame_data_offset */
        //fs.skip(frames[i].frame_data_offsets-bytesread);
        //bytesread = frames[i].frame_data_offsets;
        //System.out.println("Reading command offset data, bytes read: "+bytesread);
        //Command table offsets
        for(int j=0;j<frames[i].height;j++)
        {	byte browedge[] = new byte[4];
        	fs.read(browedge,0,4);
        	bytesread +=4;
        	frames[i].commandoff[j] = (((int)browedge[3]&0xff)<<24)
                    | (((int)browedge[2]&0xff)<<16)
                    | (((int)browedge[1]&0xff)<<8)
                    | (int)browedge[0]&0xff;
        	//System.out.println("Command offset "+frames[i].commandoff[j]);
        }
        /*Command data
         *Can be read sequentially, but check up with the command offsets all the same
         *Saves it in the array picture[][] for each frame
         */
        //System.out.println("At frame "+i+" shape offset: "+bytesread);
        for(int j=0;j<frames[i].height;j++)
        {	
        	//Draw row offset left
        	//System.out.println("Bytes read before starting line "+j+" : "+bytesread);
        	//System.out.println("Width "+frames[i].width+" row edge left "+frames[i].edges[j].left);
        	frames[i].drawmask(j,frames[i].edges[j].left);
        	
        	if(bytesread!=frames[i].commandoff[j])
        	{	System.out.println("--Mismatch in command offset -- read: "+bytesread+ "-should be "+frames[i].commandoff[j]+" -");
        	}
        	//System.out.println("***************** Starting line "+j+" ***************");
        	boolean flag = true;
        	
        	do{
        		
        	byte commandarr[] = new byte[1];
        	fs.read(commandarr,0,1);
        	bytesread +=1;
        	byte commandbyte = commandarr[0]; //command byte
        	
        	int command = commandbyte & 0x0f; //command is the lower nibble
        	
        	/*Commands used for creating the image
        	 *as specified by http://zaynar.demon.co.uk/misc2/slp.txt
        	 */
        	 
        	if(command == 0x00 || command == 0x04 || command == 0x08 || command == 0x0c) 
        	{	//Block copy short
        		int length = ((int)(commandbyte&0xff))>>2;
        		//System.out.println("Block copy(short), length: "+length);
        		
        		byte data[] = new byte[length];
        		fs.read(data,0,length);
        		bytesread += length;
        		
        		frames[i].drawbytearray(j,data);
        	}
        	else if(command == 0x01 || command == 0x05 || command == 0x09 || command == 0x0d) 
        	{	//Skip short
        		int length = ((int)(commandbyte&0xff))>>2;
        		//System.out.println("Skip (short), length: "+length);
        		
        		frames[i].drawmask(j,length);
        	}
        	else if(command == 0x02) 
        	{	//Block copy big
        		byte next[] = new byte[1];
        		fs.read(next,0,1);
        		bytesread +=1;        		
        		byte next_byte = next[0];
        		
        		int length = (((int)(commandbyte & 0xf0)) << 4) + (int)(next_byte&0xff);
        		//System.out.println("Block copy(big), length: "+length);
        		
        		byte data[] = new byte[length];
        		fs.read(data,0,length);
        		bytesread += length;
        		
        		frames[i].drawbytearray(j,data);
        	}
        	else if(command == 0x03) 
        	{	//Skip big
        		byte next[] = new byte[1];
        		fs.read(next,0,1);
        		bytesread +=1;        		
        		byte next_byte = next[0];
        		
        		int length = (((int)(commandbyte & 0xf0)) << 4) + (int)(next_byte&0xff);
        		//System.out.println("Skip (big), length: "+length);
        		frames[i].drawmask(j,length);
        	}
        	else if(command == 0x06) 
        	{	//Copy and transform block
        		int high4bits = commandbyte & 0xf0;
        		int length;
        		if(high4bits == 0)
        		{	length = fs.read();
        			bytesread += 1;
        		}
        		else length = (int)(high4bits&0xff)>>4;
        		
        		//System.out.println("Player colours, length :"+length);
        		
        		byte data[] = new byte[length];
        		fs.read(data,0,length);
        		bytesread += length;
        		
        		frames[i].drawplayercolors(j,data,playernumber);
        	}
        	else if(command == 0x07) 
        	{	//Fill block
        		int high4bits = commandbyte & 0xf0;
        		int length;
        		if(high4bits == 0)
        		{	length = fs.read();
        			bytesread += 1;
        		}
        		else length = (int)(high4bits&0xff)>>4;//length = high4bits>>4;
        		
        		byte next[] = new byte[1];
        		fs.read(next,0,1);
        		bytesread +=1;        		
        		byte next_byte = next[0];
        		
        		int color = next_byte;
        		//System.out.println("Fill color "+color+" to length "+length);
        		
        		frames[i].fill(j,next,length);
        	}
        	else if(command == 0x0a) 
        	{	//Player color fill ( transform block)
        		int high4bits = commandbyte & 0xf0;
        		int length;
        		if(high4bits == 0)
        		{	length = fs.read();
        			bytesread += 1;
        		}
        		else length = (int)(high4bits&0xff)>>4;//length = high4bits>>4;
        		
        		byte next[] = new byte[1];
        		fs.read(next,0,1);
        		bytesread +=1;        		
        		byte next_byte = next[0];
        		
        		int color = next_byte;
        		//System.out.println("Player Fill color "+color+" to length "+length);
        		
        		frames[i].playercolorfill(j,next,length,playernumber);
        	}
        	else if(command == 0x0b) 
        	{	//Shadow pixels
        		int high4bits = commandbyte & 0xf0;
        		int length;
        		if(high4bits == 0)
        		{	length = fs.read();
        			bytesread += 1;
        		}
        		else length = (int)(high4bits&0xff)>>4;
        		
        		frames[i].drawshadowpixels(j,length);
        		//System.out.println("Shadow pixels length: "+length+" frame width "+frames[i].width);
        	}
        	else if(command == 0x0e) 
        	{	//Extended commands
        		//Depends on command byte
        		if(commandbyte == 0x4e || commandbyte == 0x6e)
        		{	//System.out.println("Outline");
        			if(commandbyte == 0x4e)
        				frames[i].drawselectionmask(j,1);
        			else
        				frames[i].drawselectionmask2(j,1);
        		}
        		else if(commandbyte == 0x5e || commandbyte == 0x7e)
        		{	byte next[] = new byte[1];
        			fs.read(next,0,1);
        			bytesread +=1;        		
        			byte next_byte = next[0];
        			
        			int length = next_byte;
        			if(commandbyte == 0x5e)
        				frames[i].drawselectionmask(j,length);
        			else
        				frames[i].drawselectionmask2(j,length);
        			//System.out.println("Outline length: "+length);
        		}
        		else{	System.out.println("Extended command unrecognized");
        		}
        	}
        	else if(command == 0x0f) 
        	{	//End of line
        		flag = false;
        	}
        	
        	
        	}while(flag); //end of line
        	
        	//Draw row offsetright
        	frames[i].drawmask(j,frames[i].edges[j].right);
        	
        	//System.out.println("Reached end of line");
        }
        //System.out.println("Frame: "+i);
        //frames[i].display();
        
        //System.out.println(bytesread);
        
        }
        
        fs.close();
        
        }catch (Exception e)
            {
                System.out.println("Caught exception in read slp! ");
                e.printStackTrace ();
            }
		
	}
	void save(String outdir)
	{	for(int i=0;i<numframes;i++)
		{	String framestring;
        	if((i+1)<10) framestring = "0"+(i+1);
        	else framestring = ""+(i+1);
        	
        	String filepath = outdir+"Frames"+name;
        	File f = new File(filepath);
        	f.mkdir();
        	
        	String filename = outdir+"Frames"+name+"\\"+"G"+name+framestring+".bmp";
        	frames[i].converttobitmap(filename,mask,outline1,outline2,shadow,sample);
		}
	}
	/*Writes anchors in CSV file*/
    void savecsv(String outdir)
    {	String filename = outdir+"Frames"+name+"\\"+name +".csv";
    	String lines[] = new String[numframes];
    	for(int i=0;i<numframes;i++)
    	{	lines[i] = frames[i].anchorx+", "+frames[i].anchory;
    	}
    	try {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        for(int i=0;i<numframes;i++)
        	out.write(lines[i]+"\r\n");
        out.close();
    	} catch (IOException e) {
    	}
    }
    void timedelay(double num)
  	{ long start = System.currentTimeMillis();
    	long wait = (long)(1000*num); long now;
    	do
    	{ now = System.currentTimeMillis();
    	}while(now-start<wait);
  	}
    void view()
    {	DrawingWindow d = new DrawingWindow(800,800,"Drawing "+name);
    	d.moveTo(500,500);
    	/*draws every frame, waits for some time*/
    	int num=0;
    	do{
    		Pt start = new Pt(400-frames[num].anchorx,400-frames[num].anchory);
    		
    		imagehandler img = new imagehandler();
        	img.sampleused = sample;
			img.loadbitmap(sample,1);
    		byte aoktable[][] = img.returnaoktable();
    		
    		for(int i=0;i<frames[num].height;i++)
    		{	for(int j=0;j<frames[num].width;j++)
    			{	int x = frames[num].picture[i][j];
    				if(x==-1) x = mask;
    				if(x==-2) x = outline1;
    				
    				int r = (int)(aoktable[x][2]); if(r<0) r = r+256;
    				int g = (int)(aoktable[x][1]); if(g<0) g = g+256;
    				int b = (int)(aoktable[x][0]); if(b<0) b = b+256;
    				
    				d.setForeground(new Color(r,g,b));
    				d.fillPt(start.x()+j,start.y()+i);
    			}
    		}
    		num = num+1;
    		if(num>=numframes) num=0;
    		
    		timedelay(0.5);
    		d.clearRect(0,0,800,800);
    	}while(!d.mousePressed());
    }
}
	