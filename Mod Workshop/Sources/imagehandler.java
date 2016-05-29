import java.awt.*;
import java.awt.image.*;
import java.awt.MediaTracker.*;
import java.io.*;
import java.util.*;
import java.util.Vector.*;

class imagehandler
{	Image img;
	byte colortable[][]; // byte array representing the color table of the sample image
	byte aoktable[][]; //represents color table of working image
	int mapping[]; //represents mapping from current color table to aok color table
	int mask; //mask index
	byte rawpixels[]; //the raw pixels indicating the data;
	byte outputpixels[];
	byte aokpalette[];
	int aokmask;
	String sampleused;
	boolean aoeused;
	int imgheight,imgwidth;
	imagehandler()
	{	colortable = new byte[256][4];
		aoktable = new byte[256][4];
		mapping = new int[256];
		aokmask = 255;
		sampleused = "sampleAokTC.bmp";
		imgheight=imgwidth=0;
	}	
	void loadbitmap(String filename,int whichfile)
	{	try
            {	if(whichfile==1) filename = sampleused;
            	
            	FileInputStream fs;
				fs=new FileInputStream(filename);
		
				int bflen=14; // 14 byte BITMAPFILEHEADER
                
                byte bf[]=new byte[bflen];
                fs.read(bf,0,bflen);
                int bilen=40; // 40-byte BITMAPINFOHEADER
                byte bi[]=new byte[bilen];
                fs.read(bi,0,bilen);

                // Interperet data.
                int nsize = (((int)bf[5]&0xff)<<24)
                    | (((int)bf[4]&0xff)<<16)
                    | (((int)bf[3]&0xff)<<8)
                    | (int)bf[2]&0xff;
                //System.out.println("File type is :"+(char)bf[0]+(char)bf[1]);
                //System.out.println("Size of file is :"+nsize);

                int nbisize = (((int)bi[3]&0xff)<<24)
                    | (((int)bi[2]&0xff)<<16)
                    | (((int)bi[1]&0xff)<<8)
                    | (int)bi[0]&0xff;
                //System.out.println("Size of bitmapinfoheader is :"+nbisize);

                int nwidth = (((int)bi[7]&0xff)<<24)
                    | (((int)bi[6]&0xff)<<16)
                    | (((int)bi[5]&0xff)<<8)
                    | (int)bi[4]&0xff;
                //System.out.println("Width is :"+nwidth);

                int nheight = (((int)bi[11]&0xff)<<24)
                    | (((int)bi[10]&0xff)<<16)
                    | (((int)bi[9]&0xff)<<8)
                    | (int)bi[8]&0xff;
                //System.out.println("Height is :"+nheight);


                int nplanes = (((int)bi[13]&0xff)<<8) | (int)bi[12]&0xff;
                //System.out.println("Planes is :"+nplanes);

                int nbitcount = (((int)bi[15]&0xff)<<8) | (int)bi[14]&0xff;
                //System.out.println("BitCount is :"+nbitcount);

                // Look for non-zero values to indicate compression
                int ncompression = (((int)bi[19])<<24)
                    | (((int)bi[18])<<16)
                    | (((int)bi[17])<<8)
                    | (int)bi[16];
                //System.out.println("Compression is :"+ncompression);

                int nsizeimage = (((int)bi[23]&0xff)<<24)
                    | (((int)bi[22]&0xff)<<16)
                    | (((int)bi[21]&0xff)<<8)
                    | (int)bi[20]&0xff;
                //System.out.println("SizeImage is :"+nsizeimage);

                int nxpm = (((int)bi[27]&0xff)<<24)
                    | (((int)bi[26]&0xff)<<16)
                    | (((int)bi[25]&0xff)<<8)
                    | (int)bi[24]&0xff;
                //System.out.println("X-Pixels per meter is :"+nxpm);

                int nypm = (((int)bi[31]&0xff)<<24)
                    | (((int)bi[30]&0xff)<<16)
                    | (((int)bi[29]&0xff)<<8)
                    | (int)bi[28]&0xff;
                //System.out.println("Y-Pixels per meter is :"+nypm);

                int nclrused = (((int)bi[35]&0xff)<<24)
                    | (((int)bi[34]&0xff)<<16)
                    | (((int)bi[33]&0xff)<<8)
                    | (int)bi[32]&0xff;
                //System.out.println("Colors used are :"+nclrused);

                int nclrimp = (((int)bi[39]&0xff)<<24)
                    | (((int)bi[38]&0xff)<<16)
                    | (((int)bi[37]&0xff)<<8)
                    | (int)bi[36]&0xff;
                //System.out.println("Colors important are :"+nclrimp);

                if (nbitcount!=8) System.out.println("Not a 256 color bitmap!");
                else 
                    {
                        // Have to determine the number of colors, the clrsused
                        // parameter is dominant if it is greater than zero. If
                        // zero, calculate colors based on bitsperpixel.
                        int nNumColors = 0;
                        if (nclrused > 0)
                            {
                                nNumColors = nclrused;
                            }
                        else
                            {
                                nNumColors = (1&0xff)<<nbitcount;
                            }
                        //System.out.println("The number of Colors is"+nNumColors);

                        // Some bitmaps do not have the sizeimage field calculated
                        // Ferret out these cases and fix 'em.
                        if (nsizeimage == 0)
                            {
                                nsizeimage = ((((nwidth*nbitcount)+31) & ~31 ) >> 3);
                                nsizeimage *= nheight;
                                //System.out.println("nsizeimage (backup) is"+nsizeimage);
                            }

                        // Read the palatte colors.
                        byte bpalette[] = new byte [nNumColors*4];
                        fs.read (bpalette, 0, nNumColors*4);
                        if(whichfile==1)
                        {	aokpalette = new byte [nNumColors*4];
                        	aokpalette = bpalette;
                        }
                        int nindex8 = 0;
                        for (int n = 0; n < nNumColors; n++)
                            {
                                
                                //modified
                                for(int j = 0;j<=3;j++)
                                {	
                                	if(whichfile==1) aoktable[n][j] = bpalette[nindex8+j];
                                	else colortable[n][j] = bpalette[nindex8+j];
                                	
                                }
                                nindex8 += 4;
                            }
                        // Read the image data (actually indices into the palette)
                        // Scan lines are still padded out to even 4-byte
                        // boundaries.
                        int npad8 = (nsizeimage / nheight) - nwidth;
                        if(whichfile==0)
                        {
                        
                        rawpixels = new byte[(nwidth+npad8)*nheight];
                        fs.read (rawpixels, 0, (nwidth+npad8)*nheight);
                        imgheight = nheight;
                        imgwidth = nwidth;
                        }
                        
                    fs.close();
                    }
                    
                    
            }catch (Exception e)
            {
                System.out.println("Caught exception in loadbitmap!");
            }
	}
	void getmapping()
	{	//Assume that color table and aok table are already initialized
		for(int i=0;i<256;i++)
		{	int min = 200000;
			int index = 0;
			for(int j = 0;j<256;j++)
			{	int cr = colortable[i][0]; if(cr<0) cr = cr+256;
				//System.out.print(cr+" ");
				int cg = colortable[i][1]; if(cg<0) cg = cg+256;
				int cb = colortable[i][2]; if(cb<0) cb = cb+256;
				//System.out.print("Aok table ");
				int aokr = aoktable[j][0]; if(aokr<0) aokr = aokr+256;
				//System.out.print(aokr+" ");
				int aokg = aoktable[j][1]; if(aokg<0) aokg = aokg+256;
				int aokb = aoktable[j][2]; if(aokb<0) aokb = aokb+256;
				long dist = (cr-aokr)*(cr-aokr) + (cg-aokg)*(cg-aokg) + (cb-aokb)*(cb-aokb);
				if(dist<min) 
				{	min = (int)dist;
					index = j;
				}
				//System.out.println();
			}
			mapping[i] = index;
			//System.out.println(i+" --> "+mapping[i]);
		}
		byte first = rawpixels[0];
		mask = first;
		if(mask<0) mask = mask+256;
	}
	int maskoriginal;
	void getmask()
	{	//System.out.println("Default mask: "+mask);
		maskoriginal = mapping[mask];
		mapping[mask] = aokmask;
	}
	void setmask(int x)
	{	mapping[mask] = maskoriginal;
		mapping[x] = aokmask;
	}
	void setplayercolor()
	{	//Sets all blue AoKTC blue colours to 1-7 blues so as to make them recognized as player colours
		//"blue" colours recognized as b>=97 && r+g<b
		int bluemapping[] = new int[256];
		for(int i=0;i<256;i++)
		{	int min = 200000;
			int index = 0;
			for(int j = 17;j<=23;j++)
			{	int cb = aoktable[i][0]; if(cb<0) cb = cb+256;
				int cg = aoktable[i][1]; if(cg<0) cg = cg+256;
				int cr = aoktable[i][2]; if(cr<0) cr = cr+256;
				int aokb = aoktable[j][0]; if(aokb<0) aokb = aokb+256;
				int aokg = aoktable[j][1]; if(aokg<0) aokg = aokg+256;
				int aokr = aoktable[j][2]; if(aokr<0) aokr = aokr+256;
				long dist = (cr-aokr)*(cr-aokr) + (cg-aokg)*(cg-aokg) + (cb-aokb)*(cb-aokb);
				if(dist<min && cb>=97 && (cb>cg+cr) && (cr<120 && cg<120)) 
				{	min = (int)dist;
					//System.out.println(cr+" "+cg+" "+cb+" index: "+i);
					index = j;
				}
				//System.out.println();
			}
			if(index!=0)
			{	for(int j=0;j<256;j++)
				{	if(mapping[j]==i) mapping[j] = index;
				}
				//System.out.println(i+" --> "+index);
			}
			
		}
		//System.out.println("Mapping: ");
		/*
		for(int i=0;i<256;i++)
		{	System.out.println(i+" --> "+mapping[i]);
		}*/
		getmask();
		//done
	}
	void savebitmap(String inputfile,String outputfile)
	{	try
            {	
            	FileInputStream fs = new FileInputStream(inputfile);
            	//System.out.println("Creating file");
                
				FileOutputStream fout = new FileOutputStream(outputfile);
            		
            	
				int bflen=14; // 14 byte BITMAPFILEHEADER
                //System.out.println("Writing bitmapfileheader");
                
                byte bf[]=new byte[bflen];
                fs.read(bf,0,bflen);
                fout.write(bf,0,bflen);
                
                //System.out.println("Writing bitmapinfoheader");
                int bilen=40; // 40-byte BITMAPINFOHEADER
                byte bi[]=new byte[bilen];
                fs.read(bi,0,bilen);
                fout.write(bi,0,bilen);

                // Interperet data.
                int nsize = (((int)bf[5]&0xff)<<24)
                    | (((int)bf[4]&0xff)<<16)
                    | (((int)bf[3]&0xff)<<8)
                    | (int)bf[2]&0xff;
                //System.out.println("File type is :"+(char)bf[0]+(char)bf[1]);
                //System.out.println("Size of file is :"+nsize);

                int nbisize = (((int)bi[3]&0xff)<<24)
                    | (((int)bi[2]&0xff)<<16)
                    | (((int)bi[1]&0xff)<<8)
                    | (int)bi[0]&0xff;
                //System.out.println("Size of bitmapinfoheader is :"+nbisize);

                int nwidth = (((int)bi[7]&0xff)<<24)
                    | (((int)bi[6]&0xff)<<16)
                    | (((int)bi[5]&0xff)<<8)
                    | (int)bi[4]&0xff;
                //System.out.println("Width is :"+nwidth);

                int nheight = (((int)bi[11]&0xff)<<24)
                    | (((int)bi[10]&0xff)<<16)
                    | (((int)bi[9]&0xff)<<8)
                    | (int)bi[8]&0xff;
                //System.out.println("Height is :"+nheight);


                int nplanes = (((int)bi[13]&0xff)<<8) | (int)bi[12]&0xff;
                //System.out.println("Planes is :"+nplanes);

                int nbitcount = (((int)bi[15]&0xff)<<8) | (int)bi[14]&0xff;
                //System.out.println("BitCount is :"+nbitcount);

                // Look for non-zero values to indicate compression
                int ncompression = (((int)bi[19])<<24)
                    | (((int)bi[18])<<16)
                    | (((int)bi[17])<<8)
                    | (int)bi[16];
                //System.out.println("Compression is :"+ncompression);

                int nsizeimage = (((int)bi[23]&0xff)<<24)
                    | (((int)bi[22]&0xff)<<16)
                    | (((int)bi[21]&0xff)<<8)
                    | (int)bi[20]&0xff;
                //System.out.println("SizeImage is :"+nsizeimage);

                int nxpm = (((int)bi[27]&0xff)<<24)
                    | (((int)bi[26]&0xff)<<16)
                    | (((int)bi[25]&0xff)<<8)
                    | (int)bi[24]&0xff;
                //System.out.println("X-Pixels per meter is :"+nxpm);

                int nypm = (((int)bi[31]&0xff)<<24)
                    | (((int)bi[30]&0xff)<<16)
                    | (((int)bi[29]&0xff)<<8)
                    | (int)bi[28]&0xff;
                //System.out.println("Y-Pixels per meter is :"+nypm);

                int nclrused = (((int)bi[35]&0xff)<<24)
                    | (((int)bi[34]&0xff)<<16)
                    | (((int)bi[33]&0xff)<<8)
                    | (int)bi[32]&0xff;
                //System.out.println("Colors used are :"+nclrused);

                int nclrimp = (((int)bi[39]&0xff)<<24)
                    | (((int)bi[38]&0xff)<<16)
                    | (((int)bi[37]&0xff)<<8)
                    | (int)bi[36]&0xff;
                //System.out.println("Colors important are :"+nclrimp);

                if (nbitcount!=8) System.out.println("Not a 256 color bitmap!");
                else 
                    {
                        // Have to determine the number of colors, the clrsused
                        // parameter is dominant if it is greater than zero. If
                        // zero, calculate colors based on bitsperpixel.
                        int nNumColors = 0;
                        if (nclrused > 0)
                            {
                                nNumColors = nclrused;
                            }
                        else
                            {
                                nNumColors = (1&0xff)<<nbitcount;
                            }
                        //System.out.println("The number of Colors is"+nNumColors);

                        // Some bitmaps do not have the sizeimage field calculated
                        // Ferret out these cases and fix 'em.
                        if (nsizeimage == 0)
                            {
                                nsizeimage = ((((nwidth*nbitcount)+31) & ~31 ) >> 3);
                                nsizeimage *= nheight;
                                //System.out.println("nsizeimage (backup) is"+nsizeimage);
                            }

                        // Read the palatte colors.
                        byte bpalette[] = new byte [nNumColors*4];
                        fs.read(bpalette, 0, nNumColors*4);
                        //System.out.println("Writing pallete");
                
                        fout.write(aokpalette,0,nNumColors*4);
                        
                        // Read the image data (actually indices into the palette)
                        // Scan lines are still padded out to even 4-byte
                        // boundaries.
                        int npad8 = (nsizeimage / nheight) - nwidth;
                        
                        rawpixels = new byte[(nwidth+npad8)*nheight];
                        fs.read(rawpixels, 0, (nwidth+npad8)*nheight);
                        //Generating outputpixels
                        //System.out.println("Writing outputpixels");
                		outputpixels = new byte[(nwidth+npad8)*nheight];
                		for(int i=0;i<outputpixels.length;i++)
                		{	byte b = rawpixels[i];
                			int index = b;
                			if(index<0) index = index+256;
                			outputpixels[i] = (byte)(mapping[index]);
                		}
                        fout.write(outputpixels, 0, (nwidth+npad8)*nheight);
                        
                    
                    fs.close();
                    //System.out.println("Closing..");
                
                    fout.close();
                    }
                    
                  
            }catch (Exception e)
            {
                System.out.println("Caught exception in savebitmap!"+e);
            }
	}
	
	byte[] returnaokpalette()
	{	return aokpalette;
	}
	byte[][] returnaoktable()
	{	return aoktable;
	}
	byte[][] returnrawpixels()
	{	
		int w = rawpixels.length/imgheight;
		byte frame[][] = new byte[imgheight][imgwidth];
		//System.out.println("Width: "+w+" height: "+imgheight);
		for(int i=0;i<imgheight;i++)
		{	for(int j=0;j<imgwidth;j++)
				frame[imgheight-i-1][j] = rawpixels[i*w+j];
		}
		return frame;
	}
	void copybitmap(String infilename,String outfilename)
	{	try{
		FileInputStream fin = new FileInputStream(infilename);
		FileOutputStream fout = new FileOutputStream(outfilename);
		
		int n = fin.available();
		byte b[] = new byte[n];
		fin.read(b,0,n);
		fout.write(b,0,n);
		
		fin.close();
		fout.close();
		
		}
		catch (Exception e)
            {
                System.out.println("Caught exception in copybitmap!"+e);
            }
	}
}
	