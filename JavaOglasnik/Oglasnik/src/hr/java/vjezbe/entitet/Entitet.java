package hr.java.vjezbe.entitet;

public abstract class Entitet {
	private Long id;
	//Constructor
	public Entitet(Long id) {
		super();
		this.id = id;
	}
	
	public Entitet() {
		
	}
	
		//Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
