package com.lwkandroid.statelayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lwkandroid.stateframelayout.StateFrameLayout;

public class MainActivity extends AppCompatActivity
{
    StateFrameLayout mStateFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateFrameLayout = (StateFrameLayout) findViewById(R.id.stateLayout);
        mStateFrameLayout.setOnRetryListener(new StateFrameLayout.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                Toast.makeText(MainActivity.this, "点击重试", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.btn_loading).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mStateFrameLayout.changeState(StateFrameLayout.LOADING);
            }
        });
        findViewById(R.id.btn_empty).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mStateFrameLayout.changeState(StateFrameLayout.EMPTY);
            }
        });
        findViewById(R.id.btn_net_error).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mStateFrameLayout.changeState(StateFrameLayout.NET_ERROR);
            }
        });
        findViewById(R.id.btn_success).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mStateFrameLayout.changeState(StateFrameLayout.SUCCESS);
            }
        });
    }
}
