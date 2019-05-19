package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
	private String studentid;
	private ArrayList<Course> coursesTaken = new ArrayList<Course>();
	private HashMap<String,Integer> semestersByYearAndSemester;

	
	public Student(String studentId){	
		
		this.studentid =  studentId;
	}
	public void addCourse(Course newRecord) {
		this.coursesTaken.add(newRecord);
		
	}
	public HashMap<String,Integer> getSemestersByYearAndSemester()
	{
		HashMap<String,Integer> semestersByYearAndSemester = new HashMap<String,Integer>();
			int previousYearTaken=-1;
			int previousSemesterTaken=-1;
			int count=1;
			int j=0;
		for(j=0;j<this.getCourse().size();j++) {
			int yearTaken = coursesTaken.get(j).getYearTaken();
			
			int semesterTaken = coursesTaken.get(j).getSemesterCourseTaken();
			System.out.println(yearTaken);// + semesterTaken);
			System.out.print(coursesTaken.get(j).getSemesterCourseTaken());
			if(j==0) {semestersByYearAndSemester.put(String.valueOf(yearTaken) + "-" + semesterTaken, count++);}
			else if(yearTaken == previousYearTaken && semesterTaken != previousSemesterTaken) {
					semestersByYearAndSemester.put(String.valueOf(yearTaken) + "-" + previousSemesterTaken, count);
					count++;
				}
			else if(yearTaken != previousYearTaken) {
					semestersByYearAndSemester.put(String.valueOf(yearTaken) + "-" + previousSemesterTaken, count);
					count++;
				}			
			previousYearTaken = yearTaken;
			previousSemesterTaken = semesterTaken;
			
				
		}
		System.out.print(j);
		return semestersByYearAndSemester;
	}
	public int getNumCourseInNthSementer(int semester) {
		
	
		return 0;
	}
	public ArrayList<Course> getCourse() {
		return coursesTaken;
	}
	public void printCourse() {
		System.out.print(coursesTaken.get(1).getStudentid());
	}
	public String getStudent(){
		
		return studentid;
	}
}
