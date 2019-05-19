package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.*;
public class Utils {
	
	
	public static ArrayList<String> getLines(String file, boolean removeHeader) throws IOException {
		
		ArrayList<String> getLines = new ArrayList<String>();
		/* BufferedInputStream inputStream= new BufferedInputStream(new FileInputStream(new File(file)));
	        byte[] buffer= new byte[2048]; // 2KB
	        while (inputStream.read(buffer)!=-1)
	        {
	        	String string = new String(buffer);
	        	getLines.add(string);
	        }
		inputStream.close();*/
	
		
		try {
		Scanner inputStream = new Scanner(new File(file));
		if(removeHeader == true)inputStream.nextLine ();
		
		 		
		while (inputStream.hasNextLine () ) {
			
			String line = inputStream.nextLine ();
			
			getLines.add(line);
			
		}
		inputStream.close ();
		}
		catch (IOException e) {
			System.out.println("Problem reading the file ");
		}
		return getLines;
	}

	static void writeAFile(ArrayList<String> lines, String targetFileName) {
		
	}
}
