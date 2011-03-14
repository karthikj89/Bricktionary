package edu.berkeley.cs169.bricktionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OutlineActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outline);
		
		Button outlineButton = (Button) findViewById(R.id.OutlineButton);
        outlineButton.setOnClickListener(new View.OnClickListener() {
            
        	public void onClick(View v) {
                // Perform action on click
        		Intent i = new Intent().setClass(OutlineActivity.this, PlayActivity.class);
        		startActivity(i);
            }
        });
        
        Button noOutlineButton = (Button) findViewById(R.id.NoOutlineButton);
        noOutlineButton.setOnClickListener(new View.OnClickListener() {
            
        	public void onClick(View v) {
                // Perform action on click
        		Intent i = new Intent().setClass(OutlineActivity.this, PlayActivity.class);
        		startActivity(i);
            }
        });
	}

}
