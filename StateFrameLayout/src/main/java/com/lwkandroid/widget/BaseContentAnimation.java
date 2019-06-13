package com.lwkandroid.widget;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Description:内容视图的动画基类
 *
 * @author LWK
 * @date 2019/6/12
 */
public abstract class BaseContentAnimation
{
    /**
     * 动画加速器：默认为LinearInterpolator
     */
    private Interpolator mInterpolator = new LinearInterpolator();

    /**
     * 动画持续时间：默认为200ms
     */
    private long mAnimDuration = 200;

    /**
     * 设置动画加速器
     */
    public void setInterpolator(Interpolator interpolator)
    {
        this.mInterpolator = interpolator;
    }

    /**
     * 设置动画持续时间
     */
    public void setAnimDuration(long animDuration)
    {
        this.mAnimDuration = animDuration;
    }

    /**
     * 子类实现此方法决定动画类型
     *
     * @param v 执行动画的View
     * @return
     */
    public abstract Animator[] getAnimator(View v);

    /**
     * 开始动画
     */
    public void startAnim(View v)
    {
        Animator[] animators = getAnimator(v);
        for (Animator animator : animators)
        {
            animator.setDuration(mAnimDuration);
            animator.setInterpolator(mInterpolator);
            animator.start();
        }
    }
}
