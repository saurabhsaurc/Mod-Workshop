import java.io.*;

class pressanykey{
	public static void main(String arg[])throws IOException
	{	System.out.println("Press enter to exit");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		br.readLine();
	}
}