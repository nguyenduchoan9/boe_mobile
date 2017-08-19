package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.model.Allowance;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.CurrencyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllowancesSuccessActivity extends AppCompatActivity {

    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.btnBackToMenu)
    Button btnBackToMenu;
    @BindView(R.id.tvDate)
    TextView tvDate;
    Allowance allowance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowances_success);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();
        allowance = b.getParcelable(Constant.ALLOWANCE_SUCCESS);
        if (null != allowance) {
//            dishes = object.getDishList();
//            Log.d("pac pac", String.valueOf(info.getDishes().size()));
        } else {
            finish();
        }
        btnBackToMenu.setOnClickListener(v -> naviToMenu());
        tvDate.setText(allowance.getDate());
        tvAmount.setText(CurrencyUtil.formatVNDecimal(allowance.getAmmount()));
        tvDesc.setText(allowance.getDescription());
    }

    private void naviToMenu() {
        Intent i = new Intent(this, CustomerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
