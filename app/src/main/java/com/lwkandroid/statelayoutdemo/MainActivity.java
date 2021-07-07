package com.lwkandroid.statelayoutdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
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
        mStateFrameLayout.setLoadingView(LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_loading2, null));

        mStateFrameLayout.findViewById(R.id.btn_empty_retry)
                .setOnClickListener(v -> Toast.makeText(MainActivity.this, "点击Empty重试", Toast.LENGTH_SHORT).show());
        mStateFrameLayout.findViewById(R.id.btn_net_error_retry)
                .setOnClickListener(v -> Toast.makeText(MainActivity.this, "点击NetError重试", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btn_loading).setOnClickListener(v -> mStateFrameLayout.switchToLoadingState());
        findViewById(R.id.btn_empty).setOnClickListener(v -> mStateFrameLayout.switchToEmptyState());
        findViewById(R.id.btn_net_error).setOnClickListener(v -> mStateFrameLayout.switchToNetErrorState());
        findViewById(R.id.btn_content).setOnClickListener(v -> mStateFrameLayout.switchToContentState());
    }
}
