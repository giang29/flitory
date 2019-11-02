package leo.me.la.flitory

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.not

fun Int.checkInvisible() {
    onView(withId(this)).check(matches(not(isDisplayed())))
}

fun Int.checkVisible() {
    onView(withId(this)).check(matches(isDisplayed()))
}
