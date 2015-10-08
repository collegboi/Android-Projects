package myPack;

public class Student extends Person implements PublishDetails{
	
	private String studentID;
	private String studentCode;
	
	public Student() {
	}
	
	public Student(String name, String gender, String studentID, String studentCode) {
		super(name, gender);
		this.studentCode = studentCode;
		this.studentID = studentID;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	
	public String toString() {
		
		String returnString;
		returnString = super.toString()+ ", StudentID: "+this.studentID+", CourseCode: "+this.studentCode;
		
		return returnString;
	}
	
	public void confirmDetails() {
		String studentDetails;
		studentDetails = "\n--Student Details--\n"+super.toString()+ "\nStudentID: "+this.studentID+"\nCourseCode: "+this.studentCode;
		System.out.println(studentDetails);
	}
	
	public void getCourseCode() {	
		String courseCode;
		courseCode = "\nCourse Code: "+this.studentCode;
		System.out.println(courseCode);
	}
	
}
