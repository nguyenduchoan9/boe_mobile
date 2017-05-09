package com.nux.dhoan9.firstmvvm.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.R;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

//    }
//        void onFragmentInteraction(Uri uri);
//        // TODO: Update argument type and name
//    public interface OnFragmentInteractionListener {
//
//
//    }
//        mListener = null;
//        super.onDetach();
//    public void onDetach() {
//    @Override
//
//    }
//        }
//                    + " must implement OnFragmentInteractionListener");
//            throw new RuntimeException(context.toString()
//        } else {
//            mListener = (OnFragmentInteractionListener) context;
//        if (context instanceof OnFragmentInteractionListener) {
//        super.onAttach(context);
//    public void onAttach(Context context) {
//    @Override
//
//private OnFragmentInteractionListener mListener;
}
