package com.example.chris.githubviewer

import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.chris.githubviewer.utils.RecyclerViewMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val rule  = ActivityTestRule(MainActivity::class.java)
    val testString: String = "ha"
    val emptyErrorTestString: String = "huwaqp09o2rh;v2498ug0-2hn2['gf9ihfp[;09wh8je"

    @Test
    fun testInitialScreenDisplaysEmptyListMessage() {
        onView(withText(R.string.empty_results_string)).check(matches(isDisplayed()))
    }

    @Test
    fun testEmptySearchResultsList() {
        performSearch(emptyErrorTestString)
        onView(withId(R.id.empty_results_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchingPopulatesResults() {
        performSearch(testString)
        onView(withId(R.id.github_recycler)).check(matches(hasDescendant(withId(R.id.user_name))))
    }

    @Test
    fun testListItemClickOpensDetailFragment() {
        performSearch(testString)
        onView(RecyclerViewMatcher(R.id.github_recycler).atPositionOnView(0, R.id.user_name)).perform(click())
        onView(withId(R.id.user_repository_link)).check(matches(isDisplayed()))
    }

    private fun performSearch(keyWord: String) {
        onView(withId(R.id.action_search)).perform(click())
        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(typeText(keyWord))
        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(pressImeActionButton())
    }
}
