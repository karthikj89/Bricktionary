package edu.berkeley.cs169.bricktionary;
import android.app.Application;


public class GlobalVariables extends Application {
	private int currentLevel = 0;
	private int highScore = 0;
	private boolean soundOn;
	private boolean soundEffects;
	
	/*the setter for current level*/
	public void setCurrentLevel(int level){
		currentLevel = level;
	}
	
	/*the setter for high score*/
	public void setHighScore(int score){
		highScore = score;
	}
	
	/*the getter for current level*/
	public int getCurrentLevel(){
		return currentLevel;
	}
	
	/*the getter for the highScore*/
	public int getHighScore(){
		return highScore;
	}
}
