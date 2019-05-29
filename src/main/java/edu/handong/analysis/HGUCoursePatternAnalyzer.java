package edu.handong.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;

import java.io.File;

import java.lang.Iterable;
import org.apache.commons.cli.Options;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
//import java.io.IOException; final
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HGUCoursePatternAnalyzer {

	private HashMap<String, Student> students;

	String path;
	String optionAnalysis;
	boolean verbose;
	boolean help;
	boolean fullpath;
	String input;
	String output;
	boolean analysis;
	boolean coursecodeExist;
	String coursecode;
	String startyear;
	String endyear;

	/**
	 * This method runs our analysis logic to save the number courses taken by each
	 * student per semester in a result file. Run method must not be changed!!
	 * 
	 * @param args
	 * @throws IOException
	 */
	public void run(String[] args) throws IOException {

		// To sort HashMap entries by key values so that we can save the results by
		// student ids in ascending order.

		// TODO Auto-generated catch block

		// Generate result lines to be saved.
		// Write a file (named like the value of resultPath) with linesTobeSaved.

		Options options = createOptions();

		if (parseOptions(options, args)) {
			final String CSV_FILE_PATH = input;
			boolean removeHeader = true;
			ArrayList<String> lines = new ArrayList<String>();
			String resultPath = output;

			try {
				Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

				for (CSVRecord csvRecord : csvParser) {
					if (removeHeader) {
						removeHeader = false;
						continue;
					} else {
						String year = csvRecord.get(7);
						String line = csvRecord.get(0) + "," + csvRecord.get(1) + "," + csvRecord.get(2) + ","
								+ csvRecord.get(3) + "," + csvRecord.get(4) + "," + csvRecord.get(5) + ","
								+ csvRecord.get(6) + "," + year + "," + csvRecord.get(8);
						if (Integer.parseInt(startyear) <= Integer.parseInt(year.trim())
								&& Integer.parseInt(year.trim()) <= Integer.parseInt(endyear))
							lines.add(line);
					}
				}

			} catch (IOException e) {
				System.out.println("The file path does not exist. Please check your CLI argument!");
				System.exit(0);
			}

			if (analysis) {
				// lines = Utils.getLines(CSV_FILE_PATH, true);
				students = loadStudentCourseRecords(lines);
				Map<String, Student> sortedStudents = new TreeMap<String, Student>(students);
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
				if (optionAnalysis.equals("1")) {
					Utils.writeAFile(linesToBeSaved, resultPath, true);
				} else if (optionAnalysis.equals("2")) {
						if(coursecodeExist) {
					
							ArrayList<String> str = countTheRateOfTakingTheCourse(sortedStudents);
							Utils.writeAFile(str, resultPath, false);
						}
						else {
							printHelp(options);
							return;
						}
					
			//		  for(String s: str) { System.out.println(s); }
					 		
				}

				return;
			}

		}
	}

	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			help = cmd.hasOption("h");
			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			analysis = cmd.hasOption("a");
			optionAnalysis = cmd.getOptionValue("a");
			coursecode = cmd.getOptionValue("c");
			startyear = cmd.getOptionValue("s");
			endyear = cmd.getOptionValue("e");
			analysis = cmd.hasOption("a");
			coursecodeExist = cmd.hasOption("c");

		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}

	// Definition Stage
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder 

		options.addOption(Option.builder("h").longOpt("help").desc("Help").build());
		options.addOption(Option.builder("i").longOpt("input").desc("Set an input file path").hasArg()
				.argName("Input path").required().build());
		options.addOption(Option.builder("o").longOpt("output").desc("Set an output file path").hasArg()
				.argName("Output path").required().build());
		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year").hasArg()
				.argName("Analysis option").required().build());
		options.addOption(Option.builder("c").longOpt("coursecode").desc("Course code for '-a 2' option").hasArg()
				.argName("course code")
				// .required()
				.build());
		options.addOption(
				Option.builder("s").longOpt("startyear").desc("Set a fullPath of a directory or a file to display")
						.hasArg().argName("Start year for analysis").required().build());
		options.addOption(
				Option.builder("e").longOpt("endyear").desc("Set a fullPath of a directory or a file to display")
						.hasArg().argName("End year for analysis").required().build());

		return options;
	}

	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer = "";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}

	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a
	 * student id and the corresponding object is an instance of Student. The
	 * Student instance have all the Course instances taken by the student.
	 * 
	 * @param lines
	 * @return
	 */
	private HashMap<String, Student> loadStudentCourseRecords(ArrayList<String> lines) {
		int position = 0;
		// ArrayList lines
		ArrayList<Course> trimmedLines = new ArrayList<Course>();
		ArrayList<Student> loadedStudentInstance = new ArrayList<Student>();// Integer.parseInt(lines.get(lines.size()-1).trim().split(",
		// ")[0])];
		String previous = "0";
		int positionForStudent = 0;
		while (lines.size() > position) {

			trimmedLines.add(new Course(lines.get(position)));
			if (!trimmedLines.get(position).getStudentid().equals(previous)) {
				loadedStudentInstance.add(new Student(trimmedLines.get(position).getStudentid()));
				if (position != 0)
					positionForStudent++;
			}
			previous = trimmedLines.get(position).getStudentid();
			loadedStudentInstance.get(positionForStudent).addCourse(trimmedLines.get(position));
			position++;

		}

		HashMap<String, Student> loadedStudent = new HashMap<String, Student>();
		int i = 0;
		while (i <= positionForStudent) {
			loadedStudent.put(String.valueOf(i), loadedStudentInstance.get(i));
			i++;
		}

		return loadedStudent;

		// TODO: Implement this method
		// do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each
	 * semester. The result file look like this: StudentID,
	 * TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9 0001,14,2,8 ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In
	 * the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private HashMap<String, Integer> findToTalStudent(Map<String, Student> sortedStudent) {

		HashMap<String, Integer> students = new HashMap<String, Integer>();
		int cnt = 0;
		// int previousYearInCourse=0;
		int countForStudents = 0;
		int countForTotalStudnets = 0;
		int yearInCourse = 0;
		int total = 0;
		ArrayList<Integer> yearsToBeSaved = new ArrayList<>();
		String result = "";

		for (String key : sortedStudent.keySet()) {
			int courseSize = sortedStudent.get(key).getCourse().size();

			for (Course course : sortedStudent.get(key).getCourse()){
				yearInCourse = course.getYearTaken();
				if (!yearsToBeSaved.contains(yearInCourse))
					yearsToBeSaved.add(yearInCourse);
			}
		}
		// countTheRateOfTakingTheCourse(sortedStudent);
		Collections.sort(yearsToBeSaved);
		// System.out.println(yearsToBeSaved.size());
		// int j=-1;
		//boolean first = true;
		for (int j = 0; j < yearsToBeSaved.size(); j++) {
			for (int k = 1; k <= 4; k++) {
				for (String key : sortedStudent.keySet()) {
					// countForTotalStudnets=0;
					int courseSize = sortedStudent.get(key).getCourse().size();
					for (int i = 0; i < courseSize; i++) {
						int yearToCompare = sortedStudent.get(key).getCourse().get(i).getYearTaken();
						int semesterToCompare = sortedStudent.get(key).getCourse().get(i).getSemesterCourseTaken();
						// String courseCodeToCompare =
						// sortedStudent.get(key).getCourse().get(i).getCourseCode();
						if (yearsToBeSaved.get(j).equals(yearToCompare) && k == semesterToCompare) {
							countForTotalStudnets++;
							break;
						}
					}

					// first=false;
				}
				if (j < yearsToBeSaved.size()) {
					students.put(yearsToBeSaved.get(j) + "-" + k, countForTotalStudnets);
				//	 System.out.println(yearsToBeSaved.get(j)+"-"+k +":" + countForTotalStudnets +
				//	 " " );
					total = total + countForTotalStudnets;
					countForTotalStudnets = 0;
					countForStudents = 0;
				}
			}

		}
		// System.out.println(total);
		return students;
	}

	private HashMap<String, Integer> findStudentsTaken(Map<String, Student> sortedStudent) {
		HashMap<String, Integer> students = new HashMap<String, Integer>();
		int cnt = 0;
		// int previousYearInCourse=0;
		int countForStudents = 0;
		int countForTotalStudnets = 0;
		int yearInCourse = 0;
		int total = 0;
		ArrayList<Integer> yearsToBeSaved = new ArrayList<>();
		String result = "";

		for (String key : sortedStudent.keySet()) {
			int courseSize = sortedStudent.get(key).getCourse().size();

			for (int i = 0; i < courseSize; i++) {
				yearInCourse = sortedStudent.get(key).getCourse().get(i).getYearTaken();
				if (!yearsToBeSaved.contains(yearInCourse))
					yearsToBeSaved.add(yearInCourse);
			}
		}
		
		Collections.sort(yearsToBeSaved);
		
		// int j=-1;
	
		for (int year = 0; year < yearsToBeSaved.size(); year++) {
			for (int sem = 1; sem <= 4; sem++) {
				for (String key : sortedStudent.keySet()) {
					// countForTotalStudnets=0;
					//int courseSize = sortedStudent.get(key).getCourse().size();
					for (Course courseInStudent :sortedStudent.get(key).getCourse()) {//(int i = 0; i < courseSize; i++) {
						int yearToCompare = courseInStudent.getYearTaken();
						int semesterToCompare = courseInStudent.getSemesterCourseTaken();
						String courseCodeToCompare = courseInStudent.getCourseCode();
						if (yearsToBeSaved.get(year) ==yearToCompare && sem == semesterToCompare
								&& courseCodeToCompare.equals(coursecode)) {
							countForStudents++;
							// break;
						}
					}

					// first=false;
				}
				if (year< yearsToBeSaved.size()) {
					students.put(yearsToBeSaved.get(year) + "-" + sem, countForStudents);
				//	System.out.println(yearsToBeSaved.get(year)+"-"+sem+":" + countForStudents);
					total = total + countForStudents;
					countForStudents = 0;
				}
			}

		}
		 //System.out.println(total);
		return students;
	}

	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		ArrayList<String> linestobesaved = new ArrayList<String>(); // to return;
		int lastYear;
		int lastSemester;
		int j = 0;

		for (int i = 0; i < sortedStudents.size(); i++) {
			Student lastStudent = sortedStudents.get(String.valueOf(i));
			ArrayList<Course> lastCourse = lastStudent.getCourse();
			String lastElement = String.valueOf(lastCourse.size() - 1);
			lastYear = lastCourse.get(Integer.parseInt(lastElement)).getYearTaken();
			lastSemester = lastCourse.get(Integer.parseInt(lastElement)).getSemesterCourseTaken();
			String keyForHashTag = String.valueOf(lastYear) + "-" + String.valueOf(lastSemester);
			String numOfCourseTaken = String.valueOf(lastStudent.getSemestersByYearAndSemester().get(keyForHashTag));

			for (j = 1; j <= Integer.parseInt(numOfCourseTaken); j++) {
				int coursePerSem = sortedStudents.get(String.valueOf(i)).getNumCourseInNthSementer(j);
				linestobesaved.add(i + 1 + "," + numOfCourseTaken + "," + String.valueOf(j) + "," + coursePerSem);

			}

		}

		//
		// TODO: Implement this method

		return linestobesaved; // do not forget to return a proper variable.
	}

	private ArrayList<String> countTheRateOfTakingTheCourse(Map<String, Student> sortedStudent) {
		Double rate = 0.0;
		HashMap<String, Integer> totalStudent = new HashMap<String, Integer>();
		HashMap<String, Integer> studentTaken = new HashMap<String, Integer>();

		ArrayList<String> result = new ArrayList<String>();
		totalStudent = findToTalStudent(sortedStudent);
		studentTaken = findStudentsTaken(sortedStudent);
		String courseName = "";
		for (String key : sortedStudent.keySet()) {
			int courseSize = sortedStudent.get(key).getCourse().size();
			for (int i = 0; i < courseSize; i++) {
				String names = sortedStudent.get(key).getCourse().get(i).getCourseCode();
				if (coursecode.equals(names)) {
					String name = sortedStudent.get(key).getCourse().get(i).getCourseName();
					courseName = name;
				}

			}
		}
		String rateString = "";

		for (String key : totalStudent.keySet()) {
			String year = key.split("-")[0];
			String semester = key.split("-")[1];
			if (totalStudent.get(key) != 0) {
				rate = (Double.valueOf(studentTaken.get(key))/Double.valueOf(totalStudent.get(key))) * 100;
				rateString = String.format("%.1f", rate);
			} else {
				rateString = "0.0";
			}
			result.add(year + "," + semester + "," + coursecode + "," + courseName + "," + totalStudent.get(key) + ","
					+ studentTaken.get(key) + "," + rateString + "%");

		}
		Collections.sort(result);
		return result;
	}
}
