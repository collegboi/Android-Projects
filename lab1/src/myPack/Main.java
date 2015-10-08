package myPack;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Person timothy = new Person("Timothy", "M");
		//Person paul = new Person("Paul", "F");
		
		//System.out.println(paul.getGender());
		
		Student timothy = new Student("Timothy","M","C13720705","DT228");
		
		System.out.println(timothy);
		
		timothy.confirmDetails();
		timothy.getCourseCode();
	}

}
