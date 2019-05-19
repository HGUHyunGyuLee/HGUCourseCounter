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
			
			if(j==0) {semestersByYearAndSemester.put(String.valueOf(yearTaken) + "-" + semesterTaken, count++);}
			else if(yearTaken == previousYearTaken && semesterTaken != previousSemesterTaken) {
					semestersByYearAndSemester.put(String.valueOf(yearTaken) + "-" + semesterTaken, count++);
				
				}
			else if(yearTaken != previousYearTaken) {
					semestersByYearAndSemester.put(String.valueOf(yearTaken) + "-" + semesterTaken, count++);
				}			
			previousYearTaken = yearTaken;
			previousSemesterTaken = semesterTaken;
			
				
		}

		return semestersByYearAndSemester;
	}
	public int getNumCourseInNthSementer(int semester) {
		
		int[] countSemester = new int[253];
		int position=1;
		
		//System.out.println(this.getCourse().size());
	for(int i=0;i<this.getCourse().size();) {
		
		int currentSemester =1;
		int previousSemester =1;
		int temp = 1;
		
		while(temp == currentSemester){		
			countSemester[position]++;
			currentSemester = this.getCourse().get(i++).getSemesterCourseTaken();
		//	System.out.println(currentSemester);
			temp = previousSemester;
			previousSemester = currentSemester;			
			i++;
			if(i==this.getCourse().size()) break;
		}
		position++;
			
	}
	
		return countSemester[semester];
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
