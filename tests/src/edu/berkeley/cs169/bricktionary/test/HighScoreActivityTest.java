package edu.berkeley.cs169.bricktionary.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.berkeley.cs169.bricktionary.HighScoreActivity;

public class HighScoreActivityTest extends ActivityInstrumentationTestCase2<HighScoreActivity> {
	Activity mActivity;
	LinearLayout hs;
	
	public HighScoreActivityTest() {
		super("edu.berkeley.cs169.bricktionary", HighScoreActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
		//default junit method to set up every test
        super.setUp();
        mActivity = this.getActivity();
        hs = (LinearLayout) mActivity.findViewById(edu.berkeley.cs169.bricktionary.R.id.HighScore);
    }
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mActivity = null;
		hs = null;
	}
	
	public void testPreconditions() {
		//special android method to test for preconditions in the initial application. Only runs once.
	    assertNotNull(hs);
	}
	
	public void testResultsExist() {
		assertTrue(hs.getChildCount()>0);
	}
	
	public void testFirstResultCorrect() {
		assertEquals("\nNick",((TextView)hs.getChildAt(0)).getText());
	}
}
