package com.nux.dhoan9.firstmvvm.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentNoteBinding;
import com.nux.dhoan9.firstmvvm.view.adapter.TodoListAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.TodoListViewModel;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;


public class NoteFragment extends BaseFragment {
    Context mContext;
    FragmentNoteBinding binding;
    private RecyclerView rvTodos;

    @Inject
    TodoListAdapter todoListAdapter;
    @Inject
    TodoListViewModel todoListViewModel;

//    private OnFragmentInteractionListener mListener;

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((BoeApplication)getActivity().getApplication()).getComponent()
//                .plus(new ActivityModule(getActivity()))
//                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initialize();
    }

    private void initialize() {
        binding.setViewModel(todoListViewModel);
        todoListViewModel.initialize();
        todoListViewModel.scrollTo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pos -> rvTodos.smoothScrollToPosition(pos));
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mContext, layoutManager.getOrientation());
        rvTodos = binding.rvTodos;
        rvTodos.setAdapter(todoListAdapter);
        rvTodos.setLayoutManager(layoutManager);
        rvTodos.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
