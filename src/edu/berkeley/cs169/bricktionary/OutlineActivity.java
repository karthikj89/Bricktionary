package edu.berkeley.cs169.bricktionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        noOutlineButton.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				// Perform action on click
				builder.setMessage("You have 20 seconds to memorize puzzle")
				.setCancelable(false)
				.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						GlobalVariables.setOutlineStatus(false);
		        		Intent i = new Intent().setClass(OutlineActivity.this, PlayNoOutlineActivity.class);
		        		startActivity(i);
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

}
