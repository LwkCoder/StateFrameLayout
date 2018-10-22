package com.lwkandroid.statelayoutdemo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lwkandroid.widget.stateframelayout.StateFrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    StateFrameLayout mStateFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateFrameLayout = (StateFrameLayout) findViewById(R.id.stateLayout);
        View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) findViewById(android.R.id.content), false);
        mStateFrameLayout.setEmptyView(emptyView);
        mStateFrameLayout.setNetErrorViewLayoutId(R.layout.layout_net_error);

        mStateFrameLayout.setOnNetErrorRetryListener(new StateFrameLayout.OnNetErrorRetryListener()
        {
            @Override
            public void onNetErrorRetry()
            {
                Toast.makeText(MainActivity.this, "点击网络错误重试", Toast.LENGTH_LONG).show();
            }
        });
        mStateFrameLayout.setOnEmptyRetryListener(new StateFrameLayout.OnEmptyRetryListener()
        {
            @Override
            public void onEmptyRetry()
            {
                Toast.makeText(MainActivity.this, "点击空数据重试", Toast.LENGTH_LONG).show();
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
