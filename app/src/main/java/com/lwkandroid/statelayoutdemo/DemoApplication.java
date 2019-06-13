package com.lwkandroid.statelayoutdemo;

import android.animation.Animator;
import android.app.Application;
import android.view.View;

import com.lwkandroid.widget.BaseContentAnimation;
import com.lwkandroid.widget.StateFrameLayoutManager;

/**
 * Description:
 *
 * @author LWK
 * @date 2019/6/12
 */
public class DemoApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        //全局配置
        StateFrameLayoutManager.getGlobalOptions()
                .setEmptyLayoutId(R.layout.layout_empty)
                .setLoadingLayoutId(R.layout.layout_loading)
                .setNetErrorLayoutId(R.layout.layout_net_error)
                .setEnableContentAnim(true)
                .setContentAnim(new BaseContentAnimation()
                {
                    @Override
                    public Animator[] getAnimator(View v)
                    {
                        return new Animator[0];
                    }
                });
    }
}
