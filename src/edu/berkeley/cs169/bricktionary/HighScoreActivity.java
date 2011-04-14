package edu.berkeley.cs169.bricktionary;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class HighScoreActivity extends Activity {
	LinearLayout hs;
	DBManager db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscore);
		hs = (LinearLayout) findViewById(R.id.HighScore);
		db = new DBManager(this);
		dummyUsersToDB();
		displayDBScores();
	}
	
	/**
     * Stores dummy users to database
     * also sets dummy scores
     */
	public void dummyUsersToDB(){
		User nick  = new User(db,"Nick",9);
		User lin  = new User(db,"Lin",10);
		User evan  = new User(db,"Evan",8);
		User karthik  = new User(db,"Karthik",10);
		User bernadette  = new User(db,"Bernadette",8);
		User james  = new User(db,"James",7);
	}
	
	/**
     * Creates TextView and RatingBar to display scores
     * score is from 0-10
     * which gets mapped to 5 stars
     */
	public void displayScore(String username, int score){
		float rating = ((float)score)/2;
		TextView user = new TextView(this);
		user.setText("\n"+username);
		RatingBar rb = new RatingBar(this,null,android.R.attr.ratingBarStyleSmall);
		rb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		rb.setNumStars(5);
		rb.setRating(rating);
		hs.addView(user);
		hs.addView(rb);
	}
	
	/**
     * Retrieves all scores from database and displays it
     */
	public void displayDBScores(){
		Cursor c = db.getScores();
		if(c!=null) {
			c.moveToFirst();
			while(!c.isAfterLast()){
				displayScore(c.getString(0),c.getInt(1));
				c.moveToNext();
			}
			c.close();
		}
	}

}
