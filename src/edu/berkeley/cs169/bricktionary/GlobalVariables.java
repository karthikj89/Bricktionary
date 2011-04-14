package edu.berkeley.cs169.bricktionary;
import android.app.Application;


public class GlobalVariables extends Application {
	private static int currentLevel = 1;
	private int highScore = 0;
	private boolean soundOn;
	private boolean soundEffects;
	public static boolean outlineOn=true;
	
	/*the setter for current level*/
	public static void setCurrentLevel(int level){
		currentLevel = level;
	}
	
	/*the setter for high score*/
	public void setHighScore(int score){
		highScore = score;
	}
	
	/*the getter for current level*/
	public static int getCurrentLevel(){
		return currentLevel;
	}
	
	/*the getter for the highScore*/
	public int getHighScore(){
		return highScore;
	}
	
	public static void setOutlineStatus(boolean bool){
		outlineOn = bool;
	}
	
	public boolean getOutlineStatus(){
		return outlineOn;
	}
}
