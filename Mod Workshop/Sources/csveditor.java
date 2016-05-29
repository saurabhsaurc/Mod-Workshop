//Edit comma separated values in a file
import java.io.*;
public class csveditor
{	public static void main(String arg[])throws IOException
	{	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("This program changes all the csv files in a particular directory");
		System.out.println("It assumes the first pair of anchors in each of the 5 angles to be correct and copies it to all the frames in the same angle");
		System.out.println("Enter working directory (full path name)");
		String resp = br.readLine();
		if(resp.equals("")) resp = "C:\\Games\\Mod\\qaz\\temp\\";
		
		File f1 = new File(resp);
		File[] filearray = f1.listFiles ( );
		for(int k=0;k<filearray.length;k++)
		{	String lines[] = new String[1000];
			int numlines=0;
			String filename = filearray[k].getName();
			int pos = filename.indexOf(".csv");
			String path = resp+filename;
			if(pos!=-1)
		{
		//reading
		try {
        BufferedReader in = new BufferedReader(new FileReader(path));
        String str;
        while ((str = in.readLine()) != null) {
            lines[numlines] = str;
            numlines++;
        }
        in.close();
    	} catch (IOException e) {
    	}
    	//Numlines is total no. of lines. There are 5 angles
    	int numframes = numlines/5;
    	int j=0;
    	for(int i=1;i<=5;i++)
    	{	String current = lines[j];
    		for(int l=1;l<=numframes;l++)
    		{	lines[j] = current;
    			j++;
    		}
    	}
    	
    	//Writing
    	try {
        BufferedWriter out = new BufferedWriter(new FileWriter(path));
        for(int i=0;i<numlines;i++)
        	out.write(lines[i]+"\r\n");
        out.close();
    	} catch (IOException e) {
    	}
    	
    	}
    	}
	}
}