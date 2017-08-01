package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.response.AfterRefundNotification;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.ParcelDishList;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.adapter.DishNotServeAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RefundInfoActivity extends AppCompatActivity {
    List<Dish> dishes = new ArrayList<>();

    @BindView(R.id.rvListNotServe)
    RecyclerView rvListNotServe;
    @BindView(R.id.btnBackToMenu)
    Button btnBackToMenu;
    DishNotServeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_info);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        AfterRefundNotification object = b.getParcelable(Constant.LIST_DISH_NOT_SERVE);

        if (null != object) {
//            dishes = object.getDishList();
            Log.d("pac pac", String.valueOf(object.getDishes().size()));
        }
        btnBackToMenu.setOnClickListener(v -> naviToMenu());

//        configRecyclerView();
    }

    private void configRecyclerView() {
        adapter = new DishNotServeAdapter(this);
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvListNotServe.setLayoutManager(manager);
        rvListNotServe.addItemDecoration(decoration);
        rvListNotServe.setAdapter(adapter);
        adapter.setData(dishes);
    }

    private void naviToMenu() {
        Intent i = new Intent(this, CustomerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
