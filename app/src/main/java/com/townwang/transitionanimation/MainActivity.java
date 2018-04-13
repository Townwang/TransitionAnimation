package com.townwang.transitionanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.townwang.transitionanimation.amin.ActivityAnimationHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ButterKnife.bind (this);
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        // ::::: 第一参数可activity可fragment   最后参数为扩散起始点
        ActivityAnimationHelper.startActivity (this, Main2Activity.actionIntent (this, "100"), btn);
    }
}
