package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepoImpl;
import com.nux.dhoan9.firstmvvm.data.validator.RegisterValidator;
import com.nux.dhoan9.firstmvvm.manager.EndpointManager;
import com.nux.dhoan9.firstmvvm.manager.EndpointManagerImpl;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManagerImpl;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.ThreadSchedulerImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import retrofit2.Retrofit;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Created by hoang on 27/03/2017.
 */
public class RegisterViewModelTest {
    private RegisterViewModel registerViewModel;

    @Mock
    UserRepo userRepo;
    @Mock
    Context context;
    @Mock
    Resources resources;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PreferencesManager preferencesManager = new PreferencesManagerImpl(context, new Gson());
        EndpointManager endpointManager = new EndpointManagerImpl(context);
        registerViewModel = new RegisterViewModel(preferencesManager,
                resources, new ThreadSchedulerImpl(Schedulers.io(), Schedulers.io()),
                new RegisterValidator(resources),
                new UserRepoImpl(new RetrofitUtils(preferencesManager, endpointManager).create()));
    }

    @Test
    public void emailError_invalidEmail_showInvalidEmail() {
        registerViewModel.onEmailChange.onChange("invalid email");
        assertEquals("Invalid Email", registerViewModel.usernameError.get());
    }

    @Test
    public void emailError_validEmailFromInvalid_notShowInvalidEmail() {
        registerViewModel.onEmailChange.onChange("cs go");
        registerViewModel.onEmailChange.onChange("abc@gmail.com");
        assertNull(registerViewModel.usernameError.get());
    }

    @Test
    public void passwordError_invalidPasswordLessSixCharacter_showInvalidPassword() {
        registerViewModel.onPasswordChange.onChange("aaaa");
        assertEquals("Password length must be in from 6 to 32 characters", registerViewModel.passwordError.get());
    }

    @Test
    public void passwordError_invalidPasswordMoreSixCharacter_showInvalidPassword() {
        registerViewModel.onPasswordChange.onChange("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertEquals("Password length must be in from 6 to 32 characters", registerViewModel.passwordError.get());
    }

    @Test
    public void passwordError_validPasswordFromInvalid_notShowInvalidPassword() {
        registerViewModel.onPasswordChange.onChange("cs go");
        registerViewModel.onPasswordChange.onChange("12345678");
        assertNull(registerViewModel.passwordError.get());
    }

    @Test
    public void passwordConfirmError_invalidPasswordConfirmLessSixCharacter_showInvalidPassword() {
        registerViewModel.onPasswordConfirmChange.onChange("aaaa");
        assertEquals("Password length must be in from 6 to 32 characters", registerViewModel.passwordConfirmError.get());
    }

    @Test
    public void passwordConfirmError_invalidPasswordConfirmMoreSixCharacter_showInvalidPassword() {
        registerViewModel.onPasswordConfirmChange.onChange("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertEquals("Password length must be in from 6 to 32 characters", registerViewModel.passwordConfirmError.get());
    }

    @Test
    public void passwordConfirmError_validPasswordConfirmFromInvalidNotMatchPassword_notShowInvalidPassword() {
        registerViewModel.onPasswordChange.onChange("123456789");
        registerViewModel.onPasswordConfirmChange.onChange("cs go");
        registerViewModel.onPasswordConfirmChange.onChange("12345678");
        assertEquals("PasswordConfirm not match Password", registerViewModel.passwordConfirmError.get());
    }

    @Test
    public void passwordConfirmError_validPasswordConfirmFromInvalidMatchPassword_notShowInvalidPassword() {
        registerViewModel.onPasswordChange.onChange("12345678");
        registerViewModel.onPasswordConfirmChange.onChange("cs go");
        registerViewModel.onPasswordConfirmChange.onChange("12345678");
        assertNull(registerViewModel.passwordConfirmError.get());
    }
}