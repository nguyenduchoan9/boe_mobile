package com.nux.dhoan9.firstmvvm.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nux.dhoan9.firstmvvm.R;

public class ColorChangeActivity extends AppCompatActivity {

    ImageView ivImage;
    ImageView ivPng;
    ImageView ivJpg;
    TextView tvRed;
    TextView tvGreen;
    TextView tvYellow;
    TextView tvBlue;
    TextView tvReset;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_change);

        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivPng = (ImageView) findViewById(R.id.ivPng);
        ivJpg = (ImageView) findViewById(R.id.ivPng);
        ivImage.setImageDrawable(getDrawable(R.drawable.ic_android_black_24dp));
        tvRed = (TextView) findViewById(R.id.tvRed);
        tvGreen = (TextView) findViewById(R.id.tvGreen);
        tvBlue = (TextView) findViewById(R.id.tvBlue);
        tvYellow = (TextView) findViewById(R.id.tvYellow);
        tvReset = (TextView) findViewById(R.id.tvReset);

        tvRed.setOnClickListener(v -> {
            Drawable edit = changeDrawableColor(ColorChangeActivity.this, R.drawable.ic_android_black_24dp, Color.RED);
            ivImage.setImageDrawable(edit);
            Drawable edit1 = changeDrawableColor(ColorChangeActivity.this, R.drawable.png_image, Color.RED);
            ivPng.setImageDrawable(edit1);
            Drawable edit2 = changeDrawableColor(ColorChangeActivity.this, R.drawable.popup_shadow_transparent, Color.RED);
            ivJpg.setImageDrawable(edit2);
        });

        tvYellow.setOnClickListener(v -> {
            Drawable edit = changeDrawableColor(ColorChangeActivity.this, R.drawable.ic_android_black_24dp, Color.YELLOW);
            ivImage.setImageDrawable(edit);
            Drawable edit1 = changeDrawableColor(ColorChangeActivity.this, R.drawable.png_image, Color.YELLOW);
            ivPng.setImageDrawable(edit1);
            Drawable edit2 = changeDrawableColor(ColorChangeActivity.this, R.drawable.popup_shadow_transparent, Color.YELLOW);
            ivJpg.setImageDrawable(edit2);
        });

        tvBlue.setOnClickListener(v -> {
            Drawable edit = changeDrawableColor(ColorChangeActivity.this, R.drawable.ic_android_black_24dp, Color.BLUE);
            ivImage.setImageDrawable(edit);
            Drawable edit1 = changeDrawableColor(ColorChangeActivity.this, R.drawable.png_image, Color.BLUE);
            ivPng.setImageDrawable(edit1);
            Drawable edit2 = changeDrawableColor(ColorChangeActivity.this, R.drawable.popup_shadow_transparent, Color.BLUE);
            ivJpg.setImageDrawable(edit2);
        });

        tvGreen.setOnClickListener(v -> {
            Drawable edit = changeDrawableColor(ColorChangeActivity.this, R.drawable.ic_android_black_24dp, Color.GREEN);
            ivImage.setImageDrawable(edit);
            Drawable edit1 = changeDrawableColor(ColorChangeActivity.this, R.drawable.png_image, Color.GREEN);
            ivPng.setImageDrawable(edit1);
            Drawable edit2 = changeDrawableColor(ColorChangeActivity.this, R.drawable.popup_shadow_transparent, Color.GREEN);
            ivJpg.setImageDrawable(edit2);
        });

        tvReset.setOnClickListener(v -> {
            ivImage.setImageDrawable(getDrawable(R.drawable.ic_android_black_24dp));
            ivPng.setImageDrawable(getDrawable(R.drawable.png_image));
            ivJpg.setImageDrawable(getDrawable(R.drawable.popup_shadow_transparent));
        });
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, ColorChangeActivity.class);
        return i;
    }

    public Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }
}
