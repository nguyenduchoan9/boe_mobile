package com.nux.dhoan9.firstmvvm.dependency.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
/**
 * Created by hoang on 13/05/2017.
 */

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ForSecondActivity {
}
