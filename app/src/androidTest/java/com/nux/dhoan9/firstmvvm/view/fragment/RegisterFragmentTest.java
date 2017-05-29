package com.nux.dhoan9.firstmvvm.view.fragment;

import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.RegisterActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

/**
 * Created by hoang on 03/04/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterFragmentTest {

    public Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    @Rule
    public ActivityTestRule<RegisterActivity> mRegisterFragmentActivityTestRule =
            new ActivityTestRule<>(RegisterActivity.class);


    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mRegisterFragmentActivityTestRule.getActivity().getIdlingResource());
    }

    @Test
    public void usernameFormat_invalidFormatUsername_showErrorMessage() {
        onView(withId(R.id.etEmail)).perform(typeText("invalid email"),
                closeSoftKeyboard());

//        onView(withId(R.id.tilEmail))
//                .check(matches(hasTextInputLayoutErrorText(Constant.INVALID_EMAIL_ERROR)));
    }

    @Test
    public void usernameFormat_validFormatUsername_notShowErrorMessage() {
        onView(withId(R.id.etEmail)).perform(typeText("abc@gmail.com"),
                closeSoftKeyboard());

//        onView(withId(R.id.tilEmail))
//                .check(matches(not(hasTextInputLayoutErrorText(Constant.INVALID_EMAIL_ERROR))));
    }

    @Test
    public void passwordFormat_invalidFormatPasswordLessCharacter_showErrorMessage() {
        onView(withId(R.id.etPassword)).perform(typeText("1234"),
                closeSoftKeyboard());

        onView(withId(R.id.tilPassword))
                .check(matches(hasTextInputLayoutErrorText(Constant.INVALID_PASSWORD_LENGTH_ERROR)));
    }

    @Test
    public void passwordFormat_invalidFormatPasswordMoreCharacter_showErrorMessage() {
        onView(withId(R.id.etPassword)).perform(typeText("12341234123412341234123412341234123412341234123412341234"),
                closeSoftKeyboard());

        onView(withId(R.id.tilPassword))
                .check(matches(hasTextInputLayoutErrorText(Constant.INVALID_PASSWORD_LENGTH_ERROR)));
    }

    @Test
    public void passwordFormat_validFormatPassword_notShowErrorMessage() {
        onView(withId(R.id.etPassword)).perform(typeText("abc@gmail.com"),
                closeSoftKeyboard());

//        onView(withId(R.id.tilPassword))
//                .check(matches(not(hasTextInputLayoutErrorText(Constant.INVALID_EMAIL_ERROR))));
    }


    @Test
    public void passwordConfirmFormat_invalidFormatPasswordConfirmLessCharacter_showErrorMessage() {
        onView(withId(R.id.etPasswordConfirm)).perform(typeText("1234"),
                closeSoftKeyboard());

        onView(withId(R.id.tilPasswordConfirm))
                .check(matches(hasTextInputLayoutErrorText(Constant.INVALID_PASSWORD_LENGTH_ERROR)));
    }

    @Test
    public void passwordConfirmFormat_invalidFormatPasswordConfirmMoreCharacter_showErrorMessage() {
        onView(withId(R.id.etPasswordConfirm)).perform(typeText("12341234123412341234123412341234123412341234123412341234"),
                closeSoftKeyboard());

        onView(withId(R.id.tilPasswordConfirm))
                .check(matches(hasTextInputLayoutErrorText(Constant.INVALID_PASSWORD_LENGTH_ERROR)));
    }

    @Test
    public void passwordConfirmFormat_validFormatConfirmPassword_notShowErrorMessage() {
        onView(withId(R.id.etPassword)).perform(typeText("abc@gmail.com"),
                closeSoftKeyboard());

//        onView(withId(R.id.tilPassword))
//                .check(matches(not(hasTextInputLayoutErrorText(Constant.INVALID_EMAIL_ERROR))));
    }

    @Test
    public void matchingPassword_confirmPasswordNotMatchPassword_showErrorMessage() {
        onView(withId(R.id.etPassword)).perform(typeText("12345678"),
                closeSoftKeyboard());
        onView(withId(R.id.etPasswordConfirm)).perform(typeText("1234567890"),
                closeSoftKeyboard());

        onView(withId(R.id.tilPasswordConfirm))
                .check(matches(hasTextInputLayoutErrorText(Constant.INVALID_PASSWORD_NOT_MATCH_ERROR)));
    }

    @Test
    public void matchingPassword_confirmPasswordMatchPassword_showErrorMessage() {
        onView(withId(R.id.etPassword)).perform(typeText("12345678"),
                closeSoftKeyboard());
        onView(withId(R.id.etPasswordConfirm)).perform(typeText("12345678"),
                closeSoftKeyboard());

        onView(withId(R.id.tilPasswordConfirm))
                .check(matches(not(hasTextInputLayoutErrorText(Constant.INVALID_PASSWORD_NOT_MATCH_ERROR))));
    }

    @Test
    public void registerTask_existEmail_showErrorMessage() {
        onView(withId(R.id.etEmail)).perform(typeText("abcxyz@gmail.com"),
                closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("12345678"),
                closeSoftKeyboard());
        onView(withId(R.id.etPasswordConfirm)).perform(typeText("12345678")
                , closeSoftKeyboard());

        onView(withText("Username-Username has been existed.")).inRoot(withDecorView(not(
                mRegisterFragmentActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void registerTask_validInput_notShowErrorMessage() {
        onView(withId(R.id.etEmail)).perform(typeText("abc@gmail.com"),
                closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("12345678"),
                closeSoftKeyboard());
        onView(withId(R.id.etPasswordConfirm)).perform(typeText("12345678")
                , closeSoftKeyboard());

//        onView(withId(R.id.tvHome)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mRegisterFragmentActivityTestRule.getActivity().getIdlingResource());
    }
}