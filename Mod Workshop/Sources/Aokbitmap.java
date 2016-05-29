import java.io.*;

class Aokbitmap
{	//--- Private constants
    private final static int BITMAPFILEHEADER_SIZE = 14;
    private final static int BITMAPINFOHEADER_SIZE = 40;

    //--- Private variable declaration

    //--- Bitmap file header
    private byte bitmapFileHeader [] = new byte [14];
    private byte bfType [] = {(byte)'B', (byte)'M'};
    private int bfSize = 0;
    private int bfReserved1 = 0;
    private int bfReserved2 = 0;
    private int bfOffset = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE;

    //--- Bitmap info header
    private byte bitmapInfoHeader [] = new byte [40];
    private int biSize = BITMAPINFOHEADER_SIZE;
    private int biWidth = 0;
    private int biHeight = 0;
    private int biPlanes = 1;
    private int biBitCount = 8;
    private int biCompression = 0;
    private int biSizeImage = 0x030000;
    private int biXPelsPerMeter = 0x0;
    private int biYPelsPerMeter = 0x0;
    private int biClrUsed = 0;
    private int biClrImportant = 0;
    
    //--- Color table
    private byte colortable[];

    //--- Bitmap raw data
    private byte bitmap [];	

    //--- File section
    private FileOutputStream fo;
    
    //Masks, outlines, etc.
    int mask,outline1,outline2,shadow;
    String sample;
    
    //Constructor
    Aokbitmap()
    {	mask = 244; outline1=outline2=251; shadow =13; sample = "sampleAokTC.bmp";
    }
    Aokbitmap(int m,int o1,int o2,int sh)
    {	mask = m; outline1 = o1; outline2 = o2; shadow = sh; sample = "sampleAokTC.bmp";
    }
    
	//Write bitmap
	void write(String outputfile,int picture[][],int width,int height)
	{	try{
		
				fo = new FileOutputStream(outputfile);
				
				convertimage (picture, width, height);
            	writeBitmapFileHeader ();
            	writeBitmapInfoHeader ();
            	writecolortable();
            	writeBitmap ();
            	
	   			fo.close();
		
		}catch (Exception e)
            {
                System.out.println("Caught exception in saving bitmap!"+e);
            }
	}
	void convertimage(int picture[][],int width, int height)
	{	//Bitmap file header
		bfOffset = 14+40+256*4;
		
		//bitmap data: scan lines are padded into a 4 byte boundary
		//Bitmap info header
		int pad = (4 - ((width) % 4)) * height;

        if (4 - ((width) % 4) == 4) pad = 0 ;

        biSizeImage = ((width * height)) + pad;
        bfSize = biSizeImage + BITMAPFILEHEADER_SIZE +
            BITMAPINFOHEADER_SIZE + 1024;
        biWidth = width;
        biHeight = height;
        
        //color table
        imagehandler img = new imagehandler();
        
        img.sampleused = sample;
		img.loadbitmap(sample,1);
		colortable = img.returnaokpalette();		
		
		//bitmap raw data
		
		int pad_line = 4 - (width%4);
		if(pad_line==4) pad_line = 0;
		
		bitmap = new byte[(width+pad_line)*height];
		int count = 0;
		
		for(int i=height-1;i>=0;i--)
		{	for(int j=0;j<width;j++)
			{	int x = picture[i][j];
				if(x==-1) x = mask;
				if(x==-2) x = outline1;
				if(x==-3) x = outline2;
				if(x==-4) x = shadow;
				
				bitmap[count] = (byte)(x);
				count++;
			}
			
			for(int j=0;j<pad_line;j++)
			{	bitmap[count] = 0;
				count++;
			}
		}
		//Bitmap data is stored in byte array bitmap[]
	}
	/*
     * writeBitmapFileHeader writes the bitmap file header to the file.
     *
     */
    private void writeBitmapFileHeader () {

        try {
            fo.write (bfType);
            fo.write (intToDWord (bfSize));
            fo.write (intToWord (bfReserved1));
            fo.write (intToWord (bfReserved2));
            fo.write (intToDWord (bfOffset));

        }
        catch (Exception wbfh) {
            wbfh.printStackTrace ();
        }

    }

    /*
     *
     * writeBitmapInfoHeader writes the bitmap information header
     * to the file.
     *
     */

    private void writeBitmapInfoHeader () {

        try {
            fo.write (intToDWord (biSize));
            fo.write (intToDWord (biWidth));
            fo.write (intToDWord (biHeight));
            fo.write (intToWord (biPlanes));
            fo.write (intToWord (biBitCount));
            fo.write (intToDWord (biCompression));
            fo.write (intToDWord (biSizeImage));
            fo.write (intToDWord (biXPelsPerMeter));
            fo.write (intToDWord (biYPelsPerMeter));
            fo.write (intToDWord (biClrUsed));
            fo.write (intToDWord (biClrImportant));
        }
        catch (Exception wbih) {
            wbih.printStackTrace ();
        }

    }
    
    private void writecolortable(){
    	try {
            fo.write(colortable);
        }
        catch (Exception wbih) {
            wbih.printStackTrace ();
        }
    }
    
    private void writeBitmap(){
    	try {
            fo.write(bitmap);
        }
        catch (Exception wbih) {
            wbih.printStackTrace ();
        }
    }

    /*
     *
     * intToWord converts an int to a word, where the return
     * value is stored in a 2-byte array.
     *
     */
    private byte [] intToWord (int parValue) {

        byte retValue [] = new byte [2];

        retValue [0] = (byte) (parValue & 0x00FF);
        retValue [1] = (byte) ((parValue >> 8) & 0x00FF);

        return (retValue);

    }

    /*
     *
     * intToDWord converts an int to a double word, where the return
     * value is stored in a 4-byte array.
     *
     */
    private byte [] intToDWord (int parValue) {

        byte retValue [] = new byte [4];
        retValue [0] = (byte) (parValue & 0x00FF);
        retValue [1] = (byte) ((parValue >> 8) & 0x000000FF);
        retValue [2] = (byte) ((parValue >> 16) & 0x000000FF);
        retValue [3] = (byte) ((parValue >> 24) & 0x000000FF);

        return (retValue);

    }
}