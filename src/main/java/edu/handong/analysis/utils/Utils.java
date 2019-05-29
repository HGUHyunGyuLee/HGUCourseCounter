package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Utils {

	/*public static ArrayList<String> getLines(String file, boolean removeHeader) throws IOException {
		ArrayList<String> getLines = new ArrayList<String>();
		
		 try (
		            Reader reader = Files.newBufferedReader(Paths.get(file));
		            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
		        ) {
		            for (CSVRecord csvRecord : csvParser) {
		            	if(removeHeader) {
		            		removeHeader = false;
		            		continue;
		            	}
		            	else
		            	{
		            		String line = csvRecord.get(0) +","+ csvRecord.get(1) + ","+csvRecord.get(2) +","+
		            				csvRecord.get(3) +","+ csvRecord.get(4) +","+ csvRecord.get(5) +","+ csvRecord.get(6) +","+ csvRecord.get(7) +","+csvRecord.get(8);
		            		getLines.add(line);
		            	}
		            	
		            		
		            	}
		            }
		            	/*ArrayList<String> getLines = new ArrayList<String>();

		try {
			Scanner inputStream = new Scanner(new File(file));
			if (removeHeader == true)
				inputStream.nextLine();

			while (inputStream.hasNextLine()) {
				String line = inputStream.nextLine();
				getLines.add(line);

			}
			inputStream.close();
		} catch (IOException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		}
		return getLines;
	}*/

	public static void writeAFile(ArrayList<String> lines, String targetFileName, boolean option1) {
		
		String finalFileName=null;
		String temp = targetFileName;
		
		for(String targetDirectory:targetFileName.split("/")) 
			finalFileName = targetDirectory;
		
		int index = temp.indexOf(finalFileName);
		String dirName = temp.substring(0, index);
			
		File theDir = new File(dirName);
		if (!theDir.exists()) theDir.mkdirs();
		
		String fileName = targetFileName;
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		}

		if(option1)outputStream.println("StudentID,TotalNumberOfSemestersRegistered,Semester,NumCoursesTakenInTheSemester");
		else if(!option1)outputStream.println("Year,Semester,CouseCode, CourseName,TotalStudents,StudentsTaken,Rate");
		for (String line : lines) {
			outputStream.println(line);
		}
		outputStream.close();
	}
}
