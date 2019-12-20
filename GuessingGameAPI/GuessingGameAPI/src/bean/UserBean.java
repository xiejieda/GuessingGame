package bean;

public class UserBean {
	private int id;
	private String username;
	private String password;
	private int game_status;
	private int game_grade;
	
	public int getGame_grade() {
		return game_grade;
	}
	public void setGame_grade(int game_grade) {
		this.game_grade = game_grade;
	}
	public int getGame_status() {
		return game_status;
	}
	public void setGame_status(int game_status) {
		this.game_status = game_status;
	}
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username=username;
	}
	
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password=password;
	}

}
