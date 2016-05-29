import java.io.*;
import element.*;
import java.awt.*;

class frame
{	int  frame_data_offsets;
    int  frame_outline_offset;
    int  palette_offset;
    int  properties;
    int     width;
    int     height;
    int     anchorx;
    int     anchory;
    
    rowedge edges[];
    int commandoff[];
    
    commandclass commands[];//For the writing business
    int numcommands;
    int numcommandbytes; //no. of bytes used for commands
    
    int picture[][];
    int pointer[];
    
    void initrowedge(int n)
    {	edges = new rowedge[n];
    	for(int i=0;i<n;i++)
    		edges[i] = new rowedge();
    	
    	commandoff = new int[n];
    	pointer = new int[n];
    }
    void initpic(int w,int h)
    {	picture = new int[h][w];
    }
    void initcommands()
    {	commands = new commandclass[500000];
    	numcommands = 0;
    }
    
    //commands which change pic[][]
    void drawmask(int row, int n)
    {	//draws n mask pixels to the array
    	//mask pixels represented as -1
    	//System.out.println("Drawing "+n+" mask pixels to line "+row);
    	
    	for(int i=0;i<n;i++)
    	{	picture[row][pointer[row]] = -1;
    		pointer[row]++;
    	}    	
    }
    void drawselectionmask(int row, int n)
    {	//draws n selection mask pixels to the array
    	//mask pixels represented as -2
    	//System.out.println("Drawing "+n+" outline mask pixels to line "+row);
    	
    	for(int i=0;i<n;i++)
    	{	picture[row][pointer[row]] = -2;
    		pointer[row]++;
    	}
    }
    void drawselectionmask2(int row, int n)
    {	//draws n selection mask pixels to the array
    	//mask pixels represented as -2
    	//System.out.println("Drawing "+n+" outline mask pixels to line "+row);
    	
    	for(int i=0;i<n;i++)
    	{	picture[row][pointer[row]] = -3;
    		pointer[row]++;
    	}
    }
    void drawshadowpixels(int row, int n)
    {	//draws n selection mask pixels to the array
    	//mask pixels represented as -2
    	//System.out.println("Drawing "+n+" outline mask pixels to line "+row);
    	
    	for(int i=0;i<n;i++)
    	{	picture[row][pointer[row]] = -4;
    		pointer[row]++;
    	}
    }
    void drawbytearray(int row,byte data[])
    {	//draws color elements as specified by the byte array data[]
    	//index range from 0 to 255
    	int len = data.length;
    	
    	//System.out.println("Drawing "+len+" color pixels to line "+row);
    	
    	for(int i=0;i<len;i++)
    	{	int val = data[i];
    		if(val<0) val += 256;
    		picture[row][pointer[row]] = val;
    		pointer[row]++;
    	}
    }
    void drawplayercolors(int row,byte data[],int player)
    {	//draws player color elements as specified by the byte array data[]
    	//blue color - 16 to 23
    	int len = data.length;
    	
    	//System.out.println("Drawing "+len+" player color pixels to line "+row);
    	
    	for(int i=0;i<len;i++)
    	{	int val = data[i];
    		if(val<0) val += 256;
    		val = player*16+16+val;
    		picture[row][pointer[row]] = val;
    		pointer[row]++;
    	}
    }
    void fill(int row,byte color[],int n)
    {	//fills n bytes with color
    	//System.out.println("Filling "+n+" color pixels to line "+row);
    	
    	for(int i=0;i<n;i++)
    	{	int val = color[0];
    		if(val<0) val += 256;
    		picture[row][pointer[row]] = val;
    		pointer[row]++;
    	}
    	
    }
    void playercolorfill(int row,byte color[],int n,int player)
    {	//fills n bytes with color according to player color formula
    	//System.out.println("Filling "+n+" player color pixels to line "+row);
    	
    	for(int i=0;i<n;i++)
    	{	int val = color[0];
    		if(val<0) val += 256;
    		val = player*16+16+val;
    		picture[row][pointer[row]] = val;
    		pointer[row]++;
    	}
    	
    }
    //Displays the frame
    void display()
    {	for(int i=0;i<height;i++)
    	{	System.out.println("line: "+i);
    		for(int j=0;j<width;j++)
    		{	System.out.print(picture[i][j]+" ");
    		}
    		System.out.println();
    	}
    }
    
    /*Converts the bitmap in memory into a 256 color bitmap*/
    void converttobitmap(String filename, int mask, int outline1, int outline2, int shadow ,String samp)
    {	Aokbitmap b = new Aokbitmap(mask,outline1,outline2,shadow);
    	b.sample = samp;
    	b.write(filename,picture,width,height);
    }
    
    //Writing commands
    //deals with the entire block including player colors
    void cmdcolorblock(byte array[],boolean playercolorused)
    {	
    	int start = 0;
    	int length = 0;
    	int playerstart = 0;
    	int playerlength = 0;
    	boolean isprevplayer = false;
    	
    	int pos = 0;
    	isprevplayer = ( array[pos]>=16 && array[pos]<=23 )&&playercolorused;
    	if(isprevplayer)
    	{	playerstart = 0;
    		playerlength = 1;
    	}
    	else
    	{	start = 0;
    		length = 1;
    	}
    	
    	pos = 1;
    	
    	while(pos<array.length)
    	{	boolean isplayer = ( array[pos]>=16 && array[pos]<=23 )&&playercolorused;
    		if(isplayer && !isprevplayer)
    		{	//write colors
    			byte bytearray[] = new byte[length];
    			for(int i=0;i<length;i++)
    				bytearray[i] = array[start+i];
    			cmdcolor(bytearray);
    			
    			playerstart = pos;
    			playerlength = 1;
    			
    			isprevplayer = true;
    		}
    		else if(!isplayer && isprevplayer)
    		{	//write player colors
    			byte bytearray[] = new byte[playerlength];
    			for(int i=0;i<playerlength;i++)
    				bytearray[i] = array[playerstart+i];
    			cmdplayercolor(bytearray);
    			
    			start = pos;
    			length = 1;
    			
    			isprevplayer = false;
    		}
    		else if(isplayer){ playerlength++;
    		}
    		else { length++;
    		}
    		
    		pos++;
    	}
    	
    	//last block
    	if(isprevplayer)
    	{	byte bytearray[] = new byte[playerlength];
    			for(int i=0;i<playerlength;i++)
    				bytearray[i] = array[playerstart+i];
    			cmdplayercolor(bytearray);
    	}
    	else
    	{	byte bytearray[] = new byte[length];
    			for(int i=0;i<length;i++)
    				bytearray[i] = array[start+i];
    			cmdcolor(bytearray);
    	}
    	//
    }
    void cmdplayercolor(byte array[])//writes player colors
    {	
    	byte command = 0x06;
    	int length = array.length;
    	byte newarray[] = new byte[length];
    	
    	if(length>=256)
    	{	int left,right;
    		
    		if(length%2==0) { left = (length)/2; right = (length)/2; }
    		else { left = (length-1)/2; right = (length+1)/2; }
    		
    		byte leftarray[] = new byte[left];
    		byte rightarray[] = new byte[right];
    		
    		for(int k=0;k<left;k++)
    			leftarray[k] = array[k];
    			
    		for(int k=0;k<right;k++)
    			rightarray[k] = array[left+k-1];
    			
    		cmdplayercolor(leftarray);
    		cmdplayercolor(rightarray);
    		
    		return;
    	}
    	
    	byte next_byte = (byte)(length);
    	for(int i=0;i<array.length;i++)
    	{	newarray[i] = (byte)( (int)(array[i]&0xff) -16  );
    	}
    	
    	commands[numcommands] = new commandclass(command,next_byte,newarray);
    	numcommands++;
    }
    void cmdcolor(byte array[])
    {	//Writes array of colours
    	//System.out.println("Writing byte array");
    	
    	for(int i=0;i<array.length;i++)
    	{	//System.out.print((int)(array[i]&0xff)+" ");
    	}
    	//System.out.println();
    	int length = array.length;
    	
    	if(length>=64)
    	{	//cmdbigcolor(array);
    		int left,right;
    		
    		if(length%2==0) { left = (length)/2; right = (length)/2; }
    		else { left = (length-1)/2; right = (length+1)/2; }
    		
    		byte leftarray[] = new byte[left];
    		byte rightarray[] = new byte[right];
    		
    		for(int k=0;k<left;k++)
    			leftarray[k] = array[k];
    			
    		for(int k=0;k<right;k++)
    			rightarray[k] = array[left+k-1];
    			
    		cmdcolor(leftarray);
    		cmdcolor(rightarray);
    		
    		return;
    	}
    	//System.out.println("length "+length);
    	
    	byte command = (byte)(length<<2);
    	
    	int commandnibbleleft = (int)( command & 0x0c ) ; //ie. ....11..
    	/*
    	if(commandnibbleleft==0) command = command | 0x00;
    	else if(commandnibbleleft==4) command = command | 0x00;
    	else if(commandnibbleleft==8) command = command | 0x00;
    	else if(commandnibbleleft==12) command = command | 0x00;
    	else System.out.println("uh oh");
    	*/
    	//System.out.println(bytestring(command)+" is the command byte\r\n");    	
    	
    	commands[numcommands] = new commandclass(command,array);
    	numcommands++;
    }
    //Does not work!! - is implemented recursively
    /*
    void cmdbigcolor(byte array[])
    {	//System.out.println("big color!");
    	//Writes big array of colours
    	System.out.println("Writing big byte array");
    	
    	for(int i=0;i<array.length;i++)
    	{	System.out.print((int)(array[i]&0xff)+" ");
    	}
    	System.out.println();
    	
    	int length = array.length;
    	byte command = (byte)( ( (length>>4)|0x0f )&0xf2 );
    	
    	System.out.println("length "+length);
    	byte nextbyte = (byte)length;
    	
    	commands[numcommands] = new commandclass(command,nextbyte,array);
    	numcommands++;
    }*/
    void cmdskip(int n)
    {	//System.out.println("Skip "+n);
    	
    	int length = n;
    	//System.out.println("length "+length);
    	if(length>=64)
    	{	//cmdbigskip(n);
    		//recursive business
    		int left,right;
    		if(n%2==0) { left = n/2; right = n/2;}
    		else { left = (n-1)/2; right = (n+1)/2; }
    		
    		cmdskip(left);
    		cmdskip(right);
    		return;
    	}
    	
    	byte command = (byte)(length<<2);
    	
    	int commandnibbleleft = (int)( command & 0x0c ) ; //ie. ....11..
    	
    	if(commandnibbleleft==0) command = (byte)((int)(command&0xff) +1);
    	else if(commandnibbleleft==4) command =(byte)((int)(command&0xff) +1);
    	else if(commandnibbleleft==8) command = (byte)((int)(command&0xff) +1);
    	else if(commandnibbleleft==12) command = (byte)((int)(command&0xff) +1);
    	else System.out.println("uh oh");
    	
    	//System.out.println(bytestring(command)+" is the command byte\r\n");    	
    	
    	commands[numcommands] = new commandclass(command);
    	numcommands++;
    }
    //Does not work!!
    /*
    void cmdbigskip(int n)
    {	byte command = (byte)( ( (n>>4)|0x0f )&0xf2 );
    	
    	byte length = (byte)n;
    	
    	commands[numcommands] = new commandclass(command,length);
    	numcommands++;
    }*/
    void cmdoutline1(int n)
    {	//Writes n pixels of outline
    	//System.out.println("Outline "+n);
    	
    	int length = n;
    	//System.out.println("length "+length);
    	byte command;
    	
    	if(length==1)
    	{	command = 0x4e;
    		commands[numcommands] = new commandclass(command);
    		numcommands++;    		
    	}
    	else
    	{	command = 0x5e;
    		byte next_byte = (byte)length;
    		commands[numcommands] = new commandclass(command,next_byte);
    		numcommands++;
    	}
    	//System.out.println(bytestring(command)+" is the command byte\r\n");    	    	
    	
    }
    void cmdoutline2(int n)
    {	//Writes n pixels of outline2
    	//System.out.println("Outline "+n);
    	
    	int length = n;
    	//System.out.println("length "+length);
    	byte command;
    	
    	if(length==1)
    	{	command = 0x6e;
    		commands[numcommands] = new commandclass(command);
    		numcommands++;    		
    	}
    	else
    	{	command = 0x7e;
    		byte next_byte = (byte)length;
    		commands[numcommands] = new commandclass(command,next_byte);
    		numcommands++;
    	}
    	//System.out.println(bytestring(command)+" is the command byte\r\n");    	    	
    	
    }
    void cmdshadowpix(int n)
    {	byte command = 0x0b;
    	int length = n;
    	//reucursively for big ones
    	if(length>=256)
    	{	int left,right;
    		if(n%2==0) { left = n/2; right = n/2;}
    		else { left = (n-1)/2; right = (n+1)/2; }
    		
    		cmdshadowpix(left);
    		cmdshadowpix(right);
    		return;
    	}
    	
    	byte next_byte = (byte)(length);
    	
    	commands[numcommands] = new commandclass(command,next_byte);
    	numcommands++;
    }
    
    void cmdeol()
    {	byte command = 0x0f;
    	
    	commands[numcommands] = new commandclass(command);
    	numcommands++;
    }
    /*display commands*/
    void displaycommands()
    {	int linenum = 0;
    	int count = 0;
    	System.out.println("----------------------Line 0");
    	while(count<numcommands)
    	{	commands[count].print();
    		if(commands[count].cmdbyte==0x0f)
    		{	linenum++;
    			System.out.println("----------------------Line"+linenum);
    		}
    		count++;
    	}
    }
    void convertcvt(int array[])
    {	for(int i=0;i<picture.length;i++)
    	{	for(int j=0;j<picture[0].length;j++)
    		{	for(int k=0;k<256;k++)
    			{	
    				if(picture[i][j]==k && array[k]>=0)
    				{	picture[i][j]= array[k];
    					if(array[k]==131) picture[i][j] = -4;
    				}
    			}
    		}
    	}
    }
        
    String bytestring(byte b)
    {	int num = b;
    	String str = "";
    	for(int i=1;i<=8;i++)
    	{	int r = num%2;
    		str = r+str;
    		num = num/2;
    	}
    	return str;
    }
    
    void computeframeheader()
    {	//computes everything but the shape data offsets etc.
    	//frame_data_offsets = 1840;
    	//frame_outline_offset = 1632;
    	palette_offset = 0; //Default 50500 palette
    	properties = 24;
    	//anchorx = 10;
    	//anchory = 10;
    }
    void setanchor(int a, int b)
    {	anchorx = a; anchory = b;
    }
    //returns the number of bytes taken up by the commands
    void computecommandbytes()
    {	int total = 0;
    	for(int i=0;i<numcommands;i++)
    	{	String str = commands[i].type;
    		if(str.equals("one"))
    		{	total += 1;	
    		}
    		else if(str.equals("two length"))
    		{	total += 2;	
    		}
    		else if(str.equals("two data"))
    		{	total += 1+commands[i].data.length;	
    		}
    		else if(str.equals("three"))
    		{	total += 2+commands[i].data.length;	
    		}
    		else System.out.println("Whoa, weird command.type in computecommandbytes");
    	}
    	numcommandbytes = total;
    }
    //length of the command bytes in a line
    int linecommandlength(int n)
    {	int currentline = 0;
    	int currentlength = 0;
    	int returnvalue = 0;
    	
    	for(int i=0;i<numcommands;i++)
    	{	if(commands[i].type.equals("one"))
    			currentlength +=1;
    		else if(commands[i].type.equals("two length"))
    			currentlength +=2;
    		else if(commands[i].type.equals("two data"))
    			currentlength += 1+commands[i].data.length;
    		else if(commands[i].type.equals("three"))
    			currentlength += 2+commands[i].data.length;
    		else System.out.println("Whoa, weird command.type in linecommandlength");
    	
    		if(commands[i].cmdbyte==0x0f)
    		{	if(currentline==n)
    				returnvalue = currentlength;
    				
    			currentline++;
    			currentlength = 0;
    			
    		}
    		
    	}
    	return returnvalue;
    }
}
	