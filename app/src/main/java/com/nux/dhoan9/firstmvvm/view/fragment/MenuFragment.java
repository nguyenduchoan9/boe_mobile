package com.nux.dhoan9.firstmvvm.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentMenuBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.view.adapter.MenuCategoryListAdapter;
import com.nux.dhoan9.firstmvvm.view.custom.NavigationBottom;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCateListViewModel;

import javax.inject.Inject;

public class MenuFragment extends BaseFragment {
    FragmentMenuBinding binding;
    private int fragmentPos;
    @Inject
    MenuCategoryListAdapter adapter;
    @Inject
    MenuCateListViewModel viewModel;
    CutleryFragment cutleryFragment = new CutleryFragment();
    DrinkingFragment drinkingFragment = new DrinkingFragment();
    OrderFragment orderFragment = new OrderFragment();
    HistoryFragment historyFragment = new HistoryFragment();

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getActivity().getApplication()).getComponent()
                .plus(new ActivityModule(getActivity()))
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializer();
    }

    private void initializer() {
    }

    private void initView() {
        initContent();
        binding.bottomNavigation.setPress(0);
        binding.bottomNavigation.setListener(new NavigationBottom.NavigationListener() {
            @Override
            public void onCutleryClick() {
                showFragmentPosition(0);
            }

            @Override
            public void onDrinkingClick() {
                showFragmentPosition(1);
            }

            @Override
            public void onOrderClick() {
                showFragmentPosition(2);
            }

            @Override
            public void onHistoryClick() {
                showFragmentPosition(3);
            }
        });
    }

    private void initContent() {
        replaceContent(cutleryFragment, false, "CutleryFragment");
        replaceContent(drinkingFragment, false, "DrinkingFragment");
        replaceContent(orderFragment, false, "OrderFragment");
        replaceContent(historyFragment, false, "HistoryFragment");

        showFragmentPosition(0);
    }

    private void replaceContent(Fragment fragment, boolean exist, String tag) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (exist) {
            ft.show(fragment);
        } else {
            ft.add(R.id.frContent, fragment, tag);
        }
        ft.commit();
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    private FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    private void showFragment(Fragment fragment) {
        if (fragment instanceof CutleryFragment) {
            getActionBar().setTitle(R.string.cutlery_fragment);
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        } else if (fragment instanceof DrinkingFragment) {
            getActionBar().setTitle(R.string.drinking_fragment);
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        } else if (fragment instanceof OrderFragment) {
            getActionBar().setTitle(R.string.order_fragment);
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        } else if (fragment instanceof HistoryFragment) {
            getActionBar().setTitle(R.string.history_fragment);
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        }
    }

    private void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    private void showFragmentPosition(int pos) {
        fragmentPos = pos;
        if (0 == pos) {
            showFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (1 == pos) {
            showFragment(drinkingFragment);
            hideFragment(cutleryFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (2 == pos) {
            showFragment(orderFragment);
            hideFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(historyFragment);
        } else if (3 == pos) {
            showFragment(historyFragment);
            hideFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
        }
    }
}
