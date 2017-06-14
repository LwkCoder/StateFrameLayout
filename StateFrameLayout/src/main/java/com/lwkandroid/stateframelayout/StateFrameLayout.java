package com.lwkandroid.stateframelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 配合各状态切换的FrameLayout
 */

public class StateFrameLayout extends FrameLayout
{
    /*初始状态，不显示任何View*/
    public static final int INIT = 0X1;
    /*请求数据*/
    public static final int LOADING = 0X2;
    /*空数据*/
    public static final int EMPTY = 0X3;
    /*网络异常*/
    public static final int NET_ERROR = 0X4;
    /*正常显示*/
    public static final int SUCCESS = 0X5;

    @IntDef({INIT, LOADING, EMPTY, NET_ERROR, SUCCESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State
    {
    }

    /*Loading视图布局id*/
    protected int mLoadingLayoutId = -1;
    /*Empty视图布局id*/
    protected int mEmptyLayoutId = -1;
    /*NetError视图布局id*/
    protected int mNetErrorLayoutId = -1;
    /*Loading视图*/
    protected ViewStub mVsLoading;
    protected View mLoadingView;
    /*Empty视图*/
    protected ViewStub mVsEmpty;
    protected View mEmptyView;
    /*NetError视图*/
    protected ViewStub mVsNetError;
    protected View mNetErrorView;
    /*内容视图*/
    protected View mContentView;
    /*内容视图显示动画*/
    protected boolean mEnableContentAnim = true;
    /*当前状态*/
    @State
    protected int mCurState;
    /*重试监听*/
    protected OnRetryListener mRetryListener;
    protected boolean mHasInit = false;

    public StateFrameLayout(Context context)
    {
        super(context);
        init(context, null);
    }

    public StateFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StateFrameLayout);
        if (ta != null)
        {
            int count = ta.getIndexCount();
            for (int i = 0; i < count; i++)
            {
                int index = ta.getIndex(i);
                if (index == R.styleable.StateFrameLayout_loadingLayoutResId)
                    mLoadingLayoutId = ta.getResourceId(index, -1);
                else if (index == R.styleable.StateFrameLayout_emptyLayoutResId)
                    mEmptyLayoutId = ta.getResourceId(index, -1);
                else if (index == R.styleable.StateFrameLayout_netErrorLayoutResId)
                    mNetErrorLayoutId = ta.getResourceId(index, -1);
                else if (index == R.styleable.StateFrameLayout_enableContentAnim)
                    mEnableContentAnim = ta.getBoolean(index, true);
            }
            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if (!mHasInit)
        {
            //初始化
            if (mContentView == null)
            {
                mContentView = findViewById(R.id.id_sfl_content_view);
            }
            if (mLoadingLayoutId != -1)
            {
                mVsLoading = new ViewStub(getContext());
                mVsLoading.setLayoutResource(mLoadingLayoutId);
                addView(mVsLoading);
            }
            if (mEmptyLayoutId != -1)
            {
                mVsEmpty = new ViewStub(getContext());
                mVsEmpty.setLayoutResource(mEmptyLayoutId);
                addView(mVsEmpty);
            }
            if (mNetErrorLayoutId != -1)
            {
                mVsNetError = new ViewStub(getContext());
                mVsNetError.setLayoutResource(mNetErrorLayoutId);
                addView(mVsNetError);
            }
            changeState(INIT);
            mHasInit = true;
        }
    }

    /**
     * 切换状态
     *
     * @param state 状态标识
     */
    public void changeState(@State int state)
    {
        if (mCurState == state)
            return;

        hideAllViews();
        if (state == LOADING)
        {
            checkLoadingView();
        } else if (state == EMPTY)
        {
            checkEmptyView();
        } else if (state == NET_ERROR)
        {
            checkNetErrorView();
        } else if (state == SUCCESS)
        {
            checkContentView();
        }
        mCurState = state;
    }

    /*隐藏所有布局*/
    protected void hideAllViews()
    {
        if (mLoadingView != null)
            mLoadingView.setVisibility(View.GONE);
        if (mEmptyView != null)
            mEmptyView.setVisibility(View.GONE);
        if (mNetErrorView != null)
            mNetErrorView.setVisibility(View.GONE);
        if (mContentView != null)
            mContentView.setVisibility(View.GONE);
    }

    /*切换Loading布局*/
    protected void checkLoadingView()
    {
        if (mLoadingView == null && mVsLoading != null)
            mLoadingView = mVsLoading.inflate();
        if (mLoadingView != null)
            mLoadingView.setVisibility(View.VISIBLE);
    }

    /*切换Empty布局*/
    protected void checkEmptyView()
    {
        if (mEmptyView == null && mVsEmpty != null)
            mEmptyView = mVsEmpty.inflate();
        if (mEmptyView != null)
        {
            View v = mEmptyView.findViewById(R.id.id_sfl_empty_retry);
            if (v != null)
                v.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (mRetryListener != null)
                            mRetryListener.onRetry();
                    }
                });
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    /*切换NetError布局*/
    protected void checkNetErrorView()
    {
        if (mNetErrorView == null && mVsNetError != null)
            mNetErrorView = mVsNetError.inflate();
        if (mNetErrorView != null)
        {
            View v = mNetErrorView.findViewById(R.id.id_sfl_net_error_retry);
            if (v != null)
                v.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (mRetryListener != null)
                            mRetryListener.onRetry();
                    }
                });
            mNetErrorView.setVisibility(View.VISIBLE);
        }
    }

    /*切换内容布局*/
    protected void checkContentView()
    {
        if (mContentView == null)
            mContentView = findViewById(R.id.id_sfl_content_view);
        if (mContentView != null)
        {
            if (mEnableContentAnim)
            {
                AlphaAnimation animation = new AlphaAnimation(0f, 1.0f);
                animation.setDuration(200);
                mContentView.startAnimation(animation);
            }
            mContentView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 是否开启内容布局显示动画
     */
    public void enableContentAnim(boolean enable)
    {
        this.mEnableContentAnim = enable;
    }

    /**
     * 设置重试监听
     *
     * @param listener 重试监听
     */
    public void setOnRetryListener(OnRetryListener listener)
    {
        this.mRetryListener = listener;
    }

    public interface OnRetryListener
    {
        void onRetry();
    }

    /****************************************************** 状态恢复 ***********************************************************************/

    private final String KEY_LOADING_ID = "l";
    private final String KEY_EMPTY_ID = "e";
    private final String KEY_NET_ERROR_ID = "n";
    private final String KEY_ENABLE_ANIM = "a";

    @Override
    protected Parcelable onSaveInstanceState()
    {
        super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LOADING_ID, mLoadingLayoutId);
        bundle.putInt(KEY_EMPTY_ID, mEmptyLayoutId);
        bundle.putInt(KEY_NET_ERROR_ID, mNetErrorLayoutId);
        bundle.putBoolean(KEY_ENABLE_ANIM, mEnableContentAnim);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        super.onRestoreInstanceState(state);
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            this.mLoadingLayoutId = bundle.getInt(KEY_LOADING_ID, -1);
            this.mEmptyLayoutId = bundle.getInt(KEY_EMPTY_ID, -1);
            this.mNetErrorLayoutId = bundle.getInt(KEY_NET_ERROR_ID, -1);
            this.mEnableContentAnim = bundle.getBoolean(KEY_ENABLE_ANIM, true);
        }
    }
}
