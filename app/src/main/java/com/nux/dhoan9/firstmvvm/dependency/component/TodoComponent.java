package com.nux.dhoan9.firstmvvm.dependency.component;

import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.dependency.module.TodoModule;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.view.fragment.NoteFragment;

import dagger.Subcomponent;

/**
 * Created by hoang on 12/04/2017.
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class, TodoModule.class})
public interface TodoComponent {
//    void inject(NoteFragment noteFragment);
}
