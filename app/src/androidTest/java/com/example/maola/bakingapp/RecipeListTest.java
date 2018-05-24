package com.example.maola.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeListTest {
    // Tutorial IdlingResource @Link https://medium.com/@wingoku/synchronizing-espresso-with-custom-threads-using-idling-resource-retrofit-70439ad2f07

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void registerIdlingResource() {
        CountingIdlingResource mainActivityIdlingResource = mActivityTestRule.getActivity().getEspressoIdlingResourceForMainActivity();
        // registering MainActivity's idling resource for enabling Espresso sync with MainActivity's background threads
        Espresso.registerIdlingResources(mainActivityIdlingResource);


    }

    @Test
    public void setRecipe(){
        onView(withId(R.id.main_recipe_list_rv)).check(matches(isDisplayed()));
        onView(withId(R.id.main_constraint_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.main_recipe_list_rv)).check(matches(isEnabled()));
        onView(allOf(withId(R.id.main_recipe_list_rv))).perform(actionOnItemAtPosition(0, click()));
    }
}
