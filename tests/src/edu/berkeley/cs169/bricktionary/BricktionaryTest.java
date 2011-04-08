package edu.berkeley.cs169.bricktionary;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class edu.berkeley.cs169.bricktionary.BricktionaryTest \
 * edu.berkeley.cs169.bricktionary.tests/android.test.InstrumentationTestRunner
 */
public class BricktionaryTest extends ActivityInstrumentationTestCase2<Bricktionary> {

    public BricktionaryTest() {
        super("edu.berkeley.cs169.bricktionary", Bricktionary.class);
    }

}
