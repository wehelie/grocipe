package com.example.wehelie.grocipe;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private GrocipeDao grocipeDao;
    private AppDatabase mDb;
    Grocipe grocipe;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("All Meals"),
                        childAtPosition(
                                allOf(withId(R.id.menu),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("All Meals")));

        ViewInteraction relativeLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.card),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0)),
                        0),
                        isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.floatingButtonMain),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));

    }

    @Test
    public void checkSpinnerMainPage() {
        onView(allOf(withId(R.id.menu), not(withText("All Meals"))));
    }

    @Test
    public void clickMainAddButton() {
        onView(withId(R.id.floatingButtonMain)).perform(click());
    }


    @Test
    public void checkDishNameFieldExists() {
        onView(withId(R.id.floatingButtonMain)).perform(click());
        onView(withId(R.id.dName)).check(matches(isDisplayed()));
    }

    @Test
    public void checkRecipeFieldExists() {
        onView(withId(R.id.floatingButtonMain)).perform(click());
        onView(withId(R.id.grecipe)).check(matches(isDisplayed()));
    }

    @Test
    public void CheckMenuSpinnerExists() {
        onView(withId(R.id.floatingButtonMain)).perform(click());
        onView(withId(R.id.menu)).perform(click());
    }

    @Test
    public void openSpinnerSelection() {
        onView(withId(R.id.floatingButtonMain)).perform(click());
        onView(withId(R.id.closeAdd)).check(matches(isDisplayed()));
    }

    @Test
    public void selectSpinnerMenuItem() {
        onView(withId(R.id.floatingButtonMain)).perform(click());
        onView(withId(R.id.menu)).perform(click());
        //onView(withId(R.id.menu)).check(matches(withText(containsString("Lunch"))));
        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());
    }




    @Test
    public void closeAddButtonExists() {
        onView(withId(R.id.floatingButtonMain)).perform(click());
        onView(withId(R.id.closeAdd)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnCloseAddButton() {
        clickMainAddButton();
        onView(withId(R.id.closeAdd)).perform(click());
    }

    @Test
    public void populateGrocipeDishNameField() {
        clickMainAddButton();
        String dishName = "Pizza";
        onView(withId(R.id.dName)).perform(typeText(dishName));
    }

    @Test
    public void populateGrocipeRecipeField() {
        clickMainAddButton();
        String recipe = "Cheese, Sauce, Dough";
        onView(withId(R.id.grecipe)).perform(typeText(recipe));
    }

    @Test
    public void makeANewGrocipeItem() {
        clickMainAddButton();
        String dishName = "Pie";
        onView(withId(R.id.dName)).perform(typeText(dishName));
        String recipe = "Dough, AppleSauce, Sugar";
        onView(withId(R.id.grecipe)).perform(typeText(recipe));
        onView(withId(R.id.menu)).perform(click());
        //onView(withId(R.id.menu)).check(matches(withText(containsString("Lunch"))));
        onData(allOf(is(instanceOf(String.class)), is("Snack"))).perform(click());
        onView(withId(R.id.closeAdd)).perform(click());
    }

    @Test
    public void checkEdtitableHintDishName() {
        clickMainAddButton();
        onView(withId(R.id.dName)).check(matches(withHint("Name of Dish")));
    }

    @Test
    public void checkEdtitableHintRecipe() {
        clickMainAddButton();
        onView(withId(R.id.grecipe)).check(matches(withHint("Recipe")));
    }

    @Test
    public void scrollToPosition() {
        onView(ViewMatchers.withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.longClick()));
    }

    @Test
    public void deleteGrocipeItem() {
        scrollToPosition();
        onView(withId(R.id.delete)).perform(click());
    }


    @Test
    public void updateCardView() {
        scrollToPosition();
        String dishName = "Pie";
        onView(withId(R.id.dName)).perform(typeText(dishName));
        String recipe = "Dough, AppleSauce, Sugar";
        onView(withId(R.id.grecipe)).perform(typeText(recipe));
        onView(withId(R.id.menu)).perform(click());
        //onView(withId(R.id.menu)).check(matches(withText(containsString("Lunch"))));
        onData(allOf(is(instanceOf(String.class)), is("Snack"))).perform(click());
        onView(withId(R.id.closeAdd)).perform(click());
    }


    @Test
    public void verifyInfoFromDatabase() {
        scrollToPosition();
        onView(withId(R.id.dName)).check(matches(withText("Pizza")));
        //onView(withId(R.id.displayName)).check(matches(withText("Thisis afakeuser")));
        //onView(withId(R.id.photoUrl)).check(matches(withText("https://i.imgur.com/ZYVZT1d.jpg")));
    }



    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

