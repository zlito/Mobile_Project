/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.zito.ittcheckbook;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 *
 * <p>To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.example.android.activityinstrumentation.MainActivityTest \
 * quux.tests/android.test.InstrumentationTestRunner
 *
 * <p>Individual tests are defined as any method beginning with 'test'.
 *
 * <p>ActivityInstrumentationTestCase2 allows these tests to run alongside a running
 * copy of the application under inspection. Calling getActivity() will return a
 * handle to this activity (launching it if needed).
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public ApplicationTest() {
        super("com.example.zito.ittcheckbook", MainActivity.class);
    }

    /**
     * Test to make sure that spinner values are persisted across activity restarts.
     *
     * <p>Launches the main activity, sets a spinner value, closes the activity, then relaunches
     * that activity. Checks to make sure that the spinner values match what we set them to.
     */
    // BEGIN_INCLUDE (test_name)
    public void testSpinnerValuePersistedBetweenLaunches() {
        // END_INCLUDE (test_name)
      // final int TEST_SPINNER_POSITION_1 = MainActivity.WEATHER_PARTLY_CLOUDY;

        // BEGIN_INCLUDE (launch_activity)
        // Launch the activity
        Activity activity = getActivity();
        // END_INCLUDE (launch_activity)

        // BEGIN_INCLUDE (write_to_ui)
        // Set spinner to test position 1
        final EditText act = (EditText) activity.findViewById(R.id.acctNumber);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                act.requestFocus();
              //  spinner1.setSelection(TEST_SPINNER_POSITION_1);
            }
        });
        // END_INCLUDE (write_to_ui)

        // BEGIN_INCLUDE (relaunch_activity)
        // Close the activity
        activity.finish();
        setActivity(null);  // Required to force creation of a new activity

    }
}
