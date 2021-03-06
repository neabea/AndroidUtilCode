package com.blankj.utilcode.pkg.feature.bar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.lib.base.BaseDrawerActivity;
import com.blankj.utilcode.pkg.R;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.Utils;

import java.util.Random;


/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/05/27
 *     desc  : demo about BarUtils
 * </pre>
 */
public class BarStatusDrawerActivity extends BaseDrawerActivity {

    private Random mRandom;
    private int    mColor;
    private int    mAlpha;

    private View     fakeStatusBar;
    private CheckBox cbAlpha;
    private CheckBox cbFront;
    private TextView tvStatusAlpha;
    private SeekBar  sbChangeAlpha;
    private Button   btnRandomColor;

    public static void start(Context context) {
        Intent starter = new Intent(context, BarStatusDrawerActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        mRandom = new Random();
        mColor = ContextCompat.getColor(Utils.getApp(), R.color.colorPrimary);
        mAlpha = 112;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_bar_status_drawer;
    }

    @Override
    public void initView(Bundle savedInstanceState, @NonNull View contentView) {
        fakeStatusBar = findViewById(R.id.fake_status_bar);
        cbAlpha = findViewById(R.id.cb_alpha);
        cbFront = findViewById(R.id.cb_front);
        btnRandomColor = findViewById(R.id.btn_random_color);
        tvStatusAlpha = findViewById(R.id.tv_status_alpha);
        sbChangeAlpha = findViewById(R.id.sb_change_alpha);

        cbAlpha.setOnCheckedChangeListener(mAlphaCheckedChangeListener);
        cbFront.setOnCheckedChangeListener(mFrontCheckedChangeListener);
        btnRandomColor.setOnClickListener(this);
        findViewById(R.id.btn_set_transparent).setOnClickListener(this);
        sbChangeAlpha.setOnSeekBarChangeListener(mColorListener);

        tvStatusAlpha.setText(String.valueOf(mAlpha));

        updateStatusBar();
    }


    @Override
    public void doBusiness() {

    }

    @Override
    public void onWidgetClick(@NonNull View view) {
        int i = view.getId();
        if (i == R.id.btn_random_color) {
            mColor = 0xff000000 | mRandom.nextInt(0xffffff);
            updateStatusBar();

        } else if (i == R.id.btn_set_transparent) {
            sbChangeAlpha.setProgress(0);

        }
    }

    private SeekBar.OnSeekBarChangeListener mColorListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mAlpha = progress;
            tvStatusAlpha.setText(String.valueOf(mAlpha));
            updateStatusBar();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    CompoundButton.OnCheckedChangeListener mAlphaCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                btnRandomColor.setVisibility(View.GONE);
                mDrawerContainerView.setBackgroundResource(R.drawable.bar_status_alpha_bg);
            } else {
                btnRandomColor.setVisibility(View.VISIBLE);
                mDrawerContainerView.setBackgroundColor(Color.WHITE);
            }
            updateStatusBar();
        }
    };

    CompoundButton.OnCheckedChangeListener mFrontCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            updateStatusBar();
        }
    };

    private void updateStatusBar() {
        if (cbAlpha.isChecked()) {
            BarUtils.setStatusBarAlpha4Drawer(BarStatusDrawerActivity.this, mDrawerRootLayout, fakeStatusBar, mAlpha, cbFront.isChecked());
        } else {
            BarUtils.setStatusBarColor4Drawer(BarStatusDrawerActivity.this, mDrawerRootLayout, fakeStatusBar, mColor, mAlpha, cbFront.isChecked());
        }
        BarUtils.addMarginTopEqualStatusBarHeight(cbAlpha);// 其实这个只需要调用一次即可
    }
}
