package edu.berkeley.cs169.bricktionary;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ResultsActivity extends Activity{
	LinearLayout layout;
	DBManager db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		layout = (LinearLayout) findViewById(R.id.RBResults);
		displayScore(5);

	}
	
	/**
     * Creates TextView and RatingBar to display scores
     * score is from 0-10
     * which gets mapped to 5 stars
     */
	public void displayScore(int score){
		float rating = ((float)score)/2;
		TextView numericScore = new TextView(this);
		numericScore.setText("\n"+score);
		RatingBar rb = new RatingBar(this,null,android.R.attr.ratingBarStyleSmall);
		rb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		rb.setNumStars(5);
		rb.setRating(rating);
		layout.addView(rb);
		layout.addView(numericScore);
	}

}
