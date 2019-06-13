package com.lwkandroid.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Description:透明渐变动画
 *
 * @author LWK
 * @date 2019/6/12
 */
final class AlphaContentAnimation extends BaseContentAnimation
{
    @Override
    public Animator[] getAnimator(View v)
    {
        return new Animator[]{ObjectAnimator.ofFloat(v, "alpha", 0f, 1f)};
    }
}
