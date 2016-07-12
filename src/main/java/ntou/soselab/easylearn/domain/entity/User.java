package ntou.soselab.easylearn.domain.entity;

public class User {

	private static final long serialVersionUID = 2353528370345499815L;
	private String id;

	public User() {
		super();
	}	

	public User(String id, String token) {
		super();
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + "]";
	}

}
