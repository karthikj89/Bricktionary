package edu.berkeley.cs169.bricktionary;

import android.app.Activity;
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
	}
}
