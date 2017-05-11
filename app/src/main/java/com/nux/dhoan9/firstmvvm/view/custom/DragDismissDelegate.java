package com.nux.dhoan9.firstmvvm.view.custom;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityDishDetailBinding;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by hoang on 10/05/2017.
 */

public class DragDismissDelegate extends BaseDelegate {
    @BindView(R.id.dishDetailActivity)
    ElasticDragDismissFrameLayout draggableFrame;
    Activity activity;

    public DragDismissDelegate(Activity activity) {
        this.activity = activity;
    }

    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle bundle) {
        ButterKnife.bind(this, activity);
        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(activity);
        draggableFrame.addListener(
                new ElasticDragDismissFrameLayout.SystemChromeFader(activity) {
                    @Override
                    public void onDragDismissed() {
                        ((FragmentActivity) activity).supportFinishAfterTransition();
                    }
                });
    }

    @Override
    public void onResume() {
        draggableFrame.addListener(chromeFader);

    }

    @Override
    public void onPause() {
        draggableFrame.removeListener(chromeFader);
    }
}
