package edu.berkeley.cs169.bricktionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		Button creditsButton = (Button) findViewById(R.id.creditsButton);
		creditsButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Perform action on click
				Intent i = new Intent().setClass(SettingsActivity.this, CreditsActivity.class);
				startActivity(i);
			}
		});

		Button resetgameButton = (Button) findViewById(R.id.resetButton);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		resetgameButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Perform action on click
				builder.setMessage("Are you sure you want to reset the game?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						GlobalVariables.setCurrentLevel(1);
						Intent i = new Intent().setClass(SettingsActivity.this, Bricktionary.class);
						startActivity(i);
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
}
