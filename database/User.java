package database;

/**
 *Map player information in the database, including user name, password, maximum score and last update time
 */
public class User {
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public int getBestScore() {
		return bestScore;
	}
	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}
 
	public String getLastUpdatetime() {
		return lastUpdatetime;
	}
	public void setLastUpdatetime(String lastUpdatetime) {
		this.lastUpdatetime = lastUpdatetime;
	}

	private String  userName;
	private String  passWord;
	private int bestScore;
	private String lastUpdatetime;
	public User(String userName, String passWord, int bestScore, String lastUpdatetime) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.bestScore = bestScore;
		this.lastUpdatetime = lastUpdatetime;
	}
	
	 

}
