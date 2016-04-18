package com.knoxpo.popularmoviesstage1.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.knoxpo.popularmoviesstage1.R;

/**
 * Created by asus on 4/17/2016.
 */
public abstract class SingleFragmentActivity
        extends AppCompatActivity {

    public abstract Fragment getContentFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fm.beginTransaction().add(R.id.fragment_container, getContentFragment()).commit();
        }
    }

    protected int getContentId(){
        return R.layout.activity_single;
    }
}
