package com.lwkandroid.statelayoutdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lwkandroid.widget.StateFrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    StateFrameLayout mStateFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateFrameLayout = findViewById(R.id.stateLayout);
        mStateFrameLayout.setEmptyLayoutId(R.layout.layout_empty2);
        mStateFrameLayout.setLoadingLayoutId(R.layout.layout_loading);
        mStateFrameLayout.setNetErrorLayoutId(R.layout.layout_net_error);

        mStateFrameLayout.findViewById(R.id.btn_empty_retry).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, "点击Empty重试", Toast.LENGTH_SHORT).show();
            }
        });
        mStateFrameLayout.findViewById(R.id.btn_net_error_retry).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, "点击NetError重试", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_loading).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mStateFrameLayout.switchToLoadingState();
            }
        });
        findViewById(R.id.btn_empty).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mStateFrameLayout.switchToEmptyState();
            }
        });
        findViewById(R.id.btn_net_error).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mStateFrameLayout.switchToNetErrorState();
            }
        });
        findViewById(R.id.btn_success).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mStateFrameLayout.switchToContentState();
            }
        });
    }
}
