package edu.handong.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String, Student> students;

	/**
	 * This method runs our analysis logic to save the number courses taken by each
	 * student per semester in a result file. Run method must not be changed!!
	 * 
	 * @param args
	 * @throws IOException
	 */
	public void run(String[] args) throws IOException {

		try {
			// when there are not enough arguments from CLI, it throws the
			// NotEnoughArgmentException which must be defined by you.
			if (args.length < 2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		ArrayList<String> lines = new ArrayList<>();
		lines = Utils.getLines(dataPath, true);

		students = loadStudentCourseRecords(lines);

		// To sort HashMap entries by key values so that we can save the results by
		// student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String, Student>(students);
		// TODO Auto-generated catch block
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		// Generate result lines to be saved.
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
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
		Course[] trimmedLines = new Course[lines.size()];
		Student[] loadedStudentInstance = new Student[17906];// Integer.parseInt(lines.get(lines.size()-1).trim().split(",
																// ")[0])];
		String previous = "0";
		int positionForStudent = 0;
		while (loadedStudentInstance.length > position) {

			trimmedLines[position] = new Course(lines.get(position));
			if (!trimmedLines[position].getStudentid().equals(previous)) {
				loadedStudentInstance[Integer.parseInt(trimmedLines[position].getStudentid())] = new Student(
						trimmedLines[position].getStudentid());
				positionForStudent++;
			}
			previous = trimmedLines[position].getStudentid();
			loadedStudentInstance[positionForStudent].addCourse(trimmedLines[position]);
			position++;

		}

		HashMap<String, Student> loadedStudent = new HashMap<String, Student>();
		int i = 1;
		while (i <= positionForStudent) {
			loadedStudent.put(String.valueOf(i), loadedStudentInstance[i]);
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
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		ArrayList<String> linestobesaved = new ArrayList<String>(); // to return;
		int lastYear;
		int lastSemester;

		for (int i = 1; i <= sortedStudents.size(); i++) {
			Student lastStudent = sortedStudents.get(String.valueOf(i));
			ArrayList<Course> lastCourse = lastStudent.getCourse();
			String lastElement = String.valueOf(lastCourse.size() - 1);
			lastYear = lastCourse.get(Integer.parseInt(lastElement)).getYearTaken();
			lastSemester = lastCourse.get(Integer.parseInt(lastElement)).getSemesterCourseTaken();
			String keyForHashTag = String.valueOf(lastYear) + "-" + String.valueOf(lastSemester);
			String numOfCourseTaken = String.valueOf(lastStudent.getSemestersByYearAndSemester().get(keyForHashTag));

			for (int j = 1; j <= Integer.parseInt(numOfCourseTaken); j++) {
				int coursePerSem = sortedStudents.get(String.valueOf(i)).getNumCourseInNthSementer(j);
				linestobesaved.add(i + "," + numOfCourseTaken + "," + String.valueOf(j) + "," + coursePerSem);
			}
		}
		//
		// TODO: Implement this method

		return linestobesaved; // do not forget to return a proper variable.
	}
}
