import java.io.*;
import element.*;
import java.awt.*;

class commandclass
{	byte cmdbyte;
	byte next_byte;
	byte data[];
	String type; // Stores whether it has 1,2, or 3 arguments
	
	commandclass(byte b)
	{	cmdbyte = b; type = "one";
	}
	commandclass(byte b, byte n)
	{	cmdbyte = b; next_byte = n; type = "two length";
	} 
	commandclass(byte b,byte d[])
	{	cmdbyte = b;
		data = d;
		type = "two data";
	}
	commandclass(byte b,byte nb,byte d[])
	{	cmdbyte = b;
		next_byte = nb;
		data = d;
		type = "three";
	}
	void print()
	{	if(type.equals("one")) System.out.println("command: "+byteToHex(cmdbyte));
		else if(type.equals("two length")) System.out.println("command: "+byteToHex(cmdbyte)+" next byte "+byteToHex(next_byte));
		else if(type.equals("two data"))
		{	System.out.print("command: "+byteToHex(cmdbyte)+" data ");
			for(int i=0;i<data.length;i++)
				System.out.print((int)(data[i]&0xff)+" ");
			System.out.println();
		}
		else if(type.equals("three"))
		{	System.out.println("command: "+byteToHex(cmdbyte)+" next byte "+byteToHex(next_byte)+" data "+" length "+data.length);
			for(int i=0;i<data.length;i++)
				System.out.print((int)(data[i]&0xff)+" ");
			System.out.println();
		}
		
	}
	int commandlength()
	{	if(type.equals("one"))
    		return 1;
    	else if(type.equals("two length"))
    		return 2;
    	else if(type.equals("two data"))
    		return  1+data.length;
    	else if(type.equals("three"))
    		return 2+data.length;	
    	else System.out.println("Whoa, weird type of command");
    	
    	return 0;
	}
	/**
	* Convenience method to convert a byte to a hex string.
	*
	* @param data the byte to convert
	* @return String the converted byte
	*/
	public String byteToHex(byte d)
	{
		return Integer.toHexString(d&0xff);
	}
}
	