//Edit comma separated values in a file
import java.io.*;
public class cvteditor
{	public static void main(String arg[])throws IOException
	{	
    	//Writing
    	try {
        BufferedWriter out = new BufferedWriter(new FileWriter("shadow.cvt"));
        out.write("<MPS PALETTE REMAP>"+"\r\n");
	int numlines = 255;
	String lines[] = new String[255];
	for(int i=0;i<255;i++)
		lines[i] = i+" "+131;
        for(int i=0;i<numlines;i++)
        	out.write(lines[i]+"\r\n");
        out.close();
    	} catch (IOException e) {
    	}
    	
    	}
    	
}
