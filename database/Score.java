package database;

import java.sql.Date;
/**
 *Map score information in the database, including user name, password, score and  update time
 */
public class Score {
	
	private String playerName;
	private int score;
	private Date time;
 
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Score(String playerName, int score, Date time) {
		super();
		this.playerName = playerName;
		this.score = score;
		this.time = time;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	

}
