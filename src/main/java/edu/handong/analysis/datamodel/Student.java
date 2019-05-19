package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student {
	private String studentid;
	private ArrayList<Course> coursesTaken = new ArrayList<Course>();
	private HashMap<String, Integer> semestersByYearAndSemester;

	public Student(String studentId) {
		this.studentid = studentId;
	}

	public void addCourse(Course newRecord) {
		this.coursesTaken.add(newRecord);

	}

	public HashMap<String, Integer> getSemestersByYearAndSemester() {
		semestersByYearAndSemester = new HashMap<String, Integer>();
		int previousYearTaken = -1;
		int previousSemesterTaken = -1;
		int count = 1;
		boolean first = true;
	
		for (Course courseTaken : coursesTaken) {
			int yearTaken = courseTaken.getYearTaken();
			int semesterTaken = courseTaken.getSemesterCourseTaken();
			if (first) {
				semestersByYearAndSemester.put(String.valueOf(coursesTaken.get(0).getYearTaken()) + "-"
						+ coursesTaken.get(0).getSemesterCourseTaken(), count++);
				first = false;
			} else {
				if (yearTaken == previousYearTaken && semesterTaken != previousSemesterTaken) {
					semestersByYearAndSemester.put(String.valueOf(yearTaken) + "-" + semesterTaken, count++);

				} else if (yearTaken != previousYearTaken) {
					semestersByYearAndSemester.put(String.valueOf(yearTaken) + "-" + semesterTaken, count++);
				}
			}
				previousYearTaken = yearTaken;
				previousSemesterTaken = semesterTaken;
		}

		return semestersByYearAndSemester;
	}

	public int getNumCourseInNthSementer(int semester) {
		HashMap<String, Integer> semestersByYears = this.getSemestersByYearAndSemester();
		int i = 0;
		int count = 0;
		for (int key : semestersByYears.values()) {
			if (key == semester) {
				// System.out.println(this.getKey(semestersByYears, key));
				String targetKey = this.getKey(semestersByYears, key);
				for (i = 0; i < coursesTaken.size(); i++) {
					int yearTaken = coursesTaken.get(i).getYearTaken();
					int semesterTaken = coursesTaken.get(i).getSemesterCourseTaken();
					String target = String.valueOf(yearTaken) + "-" + String.valueOf(semesterTaken);
					if (target.equals(targetKey))
						count++;
				}
			}
		}

		return count;
	}

	public ArrayList<Course> getCourse() {
		return coursesTaken;
	}

	public void printCourse() {
		System.out.print(coursesTaken.get(1).getStudentid());
	}

	public String getStudent() {

		return studentid;
	}

	public <K, V> K getKey(Map<K, V> map, V value) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

}
