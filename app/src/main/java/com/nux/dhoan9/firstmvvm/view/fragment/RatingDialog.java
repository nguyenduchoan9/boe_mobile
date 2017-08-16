package com.nux.dhoan9.firstmvvm.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.view.adapter.RatingPagerAdapter;

/**
 * Created by hoang on 20/07/2017.
 */

public class RatingDialog extends DialogFragment {
    private Context mContext;
    ViewPager viewPager;
    RatingPagerAdapter adapter;
//    InkPageIndicator indicator;
    TextView tvNext;

    public RatingDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static RatingDialog newInstance() {
        RatingDialog dialog = new RatingDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_dialog_rating, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        viewPager = (ViewPager) v.findViewById(R.id.vpRating);
        adapter = new RatingPagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(adapter);
//        CircleIndicator pageIndicatorView = (CircleIndicator) v.findViewById(R.id.indicator );
//        pageIndicatorView.setViewPager(viewPager);
        tvNext = (TextView) v.findViewById(R.id.tvNext);
        tvNext.setOnClickListener(e -> {
            RatingFragment ratingFragment = (RatingFragment) adapter.getFragment(0);
            FeedbackFragment feedbackFragment = (FeedbackFragment) adapter.getFragment(1);
            Log.d("Hoang", "onViewCreated: start " + ratingFragment.getRating() + " feed " + feedbackFragment.getFeedback());
            if(null != listener){
                listener.onSubmit(ratingFragment.getRating(), feedbackFragment.getFeedback());
            }
            dismiss();
        });

    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public interface RatingListener{
        void onSubmit(float rate, String feedback);
    }

    private RatingListener listener;

    public void setListener(RatingListener listener) {
        this.listener = listener;
    }
}
