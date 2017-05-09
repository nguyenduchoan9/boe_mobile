package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.validator.LoginValidator;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.ThreadSchedulerImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

import static org.mockito.Mockito.verify;

/**
 * Created by hoang on 27/03/2017.
 */
public class LoginViewModelTest {
    private LoginViewModel loginViewModel;

    @Mock
    UserRepo userRepo;
    @Mock
    LoginValidator loginValidator;
    @Mock
    PreferencesManager preferencesManager;
    @Mock
    ThreadScheduler threadScheduler;
    @Mock
    Resources resource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        loginViewModel = new LoginViewModel(userRepo, new LoginValidator(resource),
                preferencesManager,
                new ThreadSchedulerImpl(Schedulers.io(), Schedulers.io()), resource);
    }

    @Test
    public void emailError_invalidEmail_showInvalidEmail() {
        loginViewModel.onEmailChange.onChange("invalid mail");
        assertEquals(Constant.INVALID_EMAIL_ERROR, loginViewModel.emailError.get());
    }

    @Test
    public void emailError_invalidToValidEmail_notShowInvalidEmail() {
        loginViewModel.onEmailChange.onChange("invalid email");
        loginViewModel.onEmailChange.onChange("abc@gmail.com");
        assertNull(loginViewModel.emailError.get());
    }

    @Test
    public void emailError_validEmail_notShowInvalidEmail() {
        loginViewModel.onEmailChange.onChange("abc@gmail.com");
        assertNull(loginViewModel.emailError.get());
    }

    @Test
    public void passwordError_invalidPasswordLessSixCharacters_showInvalidPassword() {
        loginViewModel.onPasswordChange.onChange("one");
        assertEquals(Constant.INVALID_PASSWORD_LENGTH_ERROR, loginViewModel.passwordError.get());
    }

    @Test
    public void passwordError_invalidPasswordMoreMaxCharacter_showInvalidPassword() {
        loginViewModel.onPasswordChange.onChange("sevensevensevensevensevensevensevensevensevensevensevenseven");
        assertEquals(Constant.INVALID_PASSWORD_LENGTH_ERROR, loginViewModel.passwordError.get());
    }

    @Test
    public void passwordError_invalidToValidPassword_notShowInvalidPassword() {
        loginViewModel.onPasswordChange.onChange("aaaa");
        loginViewModel.onPasswordChange.onChange("12345678");
        assertNull(loginViewModel.passwordError.get());
    }

    @Test
    public void passwordError_validPassword_notShowInvalidPassword() {
        loginViewModel.onPasswordChange.onChange("12345678");
        assertNull(loginViewModel.passwordError.get());
    }

}