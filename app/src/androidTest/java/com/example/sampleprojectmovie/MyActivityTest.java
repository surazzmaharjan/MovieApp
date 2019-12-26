package com.example.sampleprojectmovie;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MyActivityTest {

    @Rule
    public ActivityTestRule<TestActivity> rule = new ActivityTestRule<>(TestActivity.class);
    String mytext = "hello";

    @Test
    public void TestActivityUI() throws Exception{
        onView(withId(R.id.test)).perform(typeText(mytext));
        closeSoftKeyboard();
        onView(withId(R.id.bttest)).perform(click());
        onView(withId(R.id.display)).check(matches(withText(mytext)));
    }
}
