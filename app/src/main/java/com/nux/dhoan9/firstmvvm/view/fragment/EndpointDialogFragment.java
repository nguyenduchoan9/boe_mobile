package com.nux.dhoan9.firstmvvm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.manager.EndpointManager;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hoang on 27/05/2017.
 */

public class EndpointDialogFragment extends DialogFragment {
    @BindView(R.id.edtEndpoint)
    EditText edtEndpoint;

    @Inject
    EndpointManager endpointManager;

    public static EndpointDialogFragment newInstance() {
        return new EndpointDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BoeApplication) getActivity().getApplication()).getComponent()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_endpoint, container);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        edtEndpoint.setText(endpointManager.getEndpoint());
        getDialog().setTitle("API Endpoint");
        edtEndpoint.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @OnClick(R.id.btnSubmit)
    public void onSubmit() {
        endpointManager.setEndpoint(edtEndpoint.getText().toString());
        if (null != listener) {
            listener.onFinish();
        }
        dismiss();
    }

    public interface ProcessListener {
        void onFinish();
    }

    private ProcessListener listener;

    public void setListener(ProcessListener listener) {
        this.listener = listener;
    }
}
