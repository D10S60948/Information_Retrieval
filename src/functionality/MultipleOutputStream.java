package functionality;

import java.io.*;
import java.util.*;

public class MultipleOutputStream extends OutputStream
{
	Vector<OutputStream> vec = new Vector<OutputStream>();
 
	MultipleOutputStream(OutputStream output)
	{
		vec.addElement(output);
	}
 
	public synchronized void addOutputStream(OutputStream output)
	{
		vec.addElement(output);
	}
	
	public synchronized void write(int b) throws IOException 
	{
		for(Enumeration<OutputStream> enumeration = vec.elements(); enumeration.hasMoreElements();)
		{
			OutputStream out = (OutputStream)(enumeration.nextElement());
			out.write(b);
		}
	}
	
	public synchronized void write(byte[] data, int offset, int length) throws IOException
	{
		for(Enumeration<OutputStream> enumeration = vec.elements(); enumeration.hasMoreElements();)
		{
			OutputStream out = (OutputStream)(enumeration.nextElement());
			out.write(data, offset, length);;
		}
	}
	
	public synchronized void flush() throws IOException
	{
		for(Enumeration<OutputStream> enumeration = vec.elements(); enumeration.hasMoreElements();)
		{
			OutputStream out = (OutputStream)(enumeration.nextElement());
			out.flush();
		}
	}
	
}