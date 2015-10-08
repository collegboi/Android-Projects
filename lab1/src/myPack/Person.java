package myPack;

public class Person {
	
	private String name;
	private String gender;
	
	public Person() {
		
	}
	public Person(String name, String gender) {
		this.name = name;
		this.gender = gender;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getName() {
		return this.name;
	}
	public String getGender() {
		return this.gender;
	}
	
	public String toString() {
		return "Name: "+this.name+", Gender: "+this.gender;
	}
	
}
