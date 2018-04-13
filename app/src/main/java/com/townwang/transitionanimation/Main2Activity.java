package com.townwang.transitionanimation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.townwang.transitionanimation.amin.AbsAnimationListener;
import com.townwang.transitionanimation.amin.ActivityAnimationHelper;

public class Main2Activity extends AppCompatActivity {
    //动画ID
    String id;
    boolean isRunAnim; // 是否已经运行过动画
    public static Intent actionIntent(Context context, String id) {
        Intent intent = new Intent (context, Main2Activity.class);
        intent.putExtra ("id", id);
        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart ();
        if (!isRunAnim) {
            isRunAnim = true;
            ActivityAnimationHelper.animScaleUp (this, getResources ().getColor (R.color.anim_color), getIntent (), null);
        }
    }

    @Override
    public void finish() {
        ActivityAnimationHelper.animScaleDown (this, getResources ().getColor (R.color.anim_color), new AbsAnimationListener () {
            @Override
            public void onAnimationEnd() {
                Main2Activity.super.finish ();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main2);

        id = getIntent ().getStringExtra ("id");
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Snackbar.make (view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction ("Action", null).show ();
            }
        });
    }

}
