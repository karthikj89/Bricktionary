package edu.berkeley.cs169.bricktionary;

public class User {
	private long userID;
	private String username;
	private int score;
	private DBManager db;
	
	public User(DBManager db, String username){
		this.db = db;
		this.username = username;
		userID = db.addUser(username,score); //add user info into database
	}
	
	public User(DBManager db, String username, int score){
		this(db,username);
		setScore(score);
	}
	
	public String getUserName(){
		return username;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setScore(int score){
		this.score = score;
		db.updateScore(userID, score);
	}
}
