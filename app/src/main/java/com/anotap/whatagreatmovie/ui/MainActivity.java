package com.anotap.whatagreatmovie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.anotap.whatagreatmovie.MovieApp;
import com.anotap.whatagreatmovie.R;
import com.anotap.whatagreatmovie.Toolbarable;
import com.anotap.whatagreatmovie.util.FragmentUtil;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity will be used as Main to handle all fragments replace by the class {@link FragmentUtil}
 */

public class MainActivity extends AppCompatActivity implements Toolbarable {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        FragmentUtil.replaceFragment(getSupportFragmentManager(),
                TextUtils.isEmpty(MovieApp.getInstance().getUsername()) ? new LoginFragment() : new MainFragment(),
                false);
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setShowBackArrow(boolean show) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        getSupportActionBar().setDisplayShowHomeEnabled(show);
    }

}
