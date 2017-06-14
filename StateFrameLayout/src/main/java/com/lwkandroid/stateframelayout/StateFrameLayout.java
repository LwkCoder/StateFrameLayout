package com.lwkandroid.stateframelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
                if (getChildCount() > 0)
                    mContentView = getChildAt(0);
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
            //切换到初始状态
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
        if (mLoadingView == null)
        {
            if (mVsLoading != null)
                mLoadingView = mVsLoading.inflate();
            else if (mLoadingLayoutId != -1)
                mLoadingView = LayoutInflater.from(getContext()).inflate(mLoadingLayoutId, this, false);
        }
        if (mLoadingView != null)
        {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    /*切换Empty布局*/
    protected void checkEmptyView()
    {
        if (mEmptyView == null)
        {
            if (mVsEmpty != null)
                mEmptyView = mVsEmpty.inflate();
            else if (mEmptyLayoutId != -1)
                mEmptyView = LayoutInflater.from(getContext()).inflate(mEmptyLayoutId, this, false);
        }
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
        if (mNetErrorView == null)
        {
            if (mVsNetError != null)
                mNetErrorView = mVsNetError.inflate();
            else if (mNetErrorLayoutId != -1)
                mNetErrorView = LayoutInflater.from(getContext()).inflate(mNetErrorLayoutId, this, false);
        }
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
     * 获取当前状态
     */
    public int getCurrentState()
    {
        return mCurState;
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

    @Override
    protected Parcelable onSaveInstanceState()
    {
        SavedViewState state = new SavedViewState(super.onSaveInstanceState());
        state.loadingId = mLoadingLayoutId;
        state.emptyId = mEmptyLayoutId;
        state.netErrorId = mNetErrorLayoutId;
        state.lastState = mCurState;
        state.enableAnim = mEnableContentAnim;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (!(state instanceof SavedViewState))
        {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedViewState ss = (SavedViewState) state;
        super.onRestoreInstanceState(ss);

        mLoadingLayoutId = ss.loadingId;
        mEmptyLayoutId = ss.emptyId;
        mNetErrorLayoutId = ss.netErrorId;
        mEnableContentAnim = ss.enableAnim;
        changeState(ss.lastState);
    }

    static class SavedViewState extends BaseSavedState
    {
        int loadingId;
        int emptyId;
        int netErrorId;
        int lastState;
        boolean enableAnim;

        SavedViewState(Parcelable superState)
        {
            super(superState);
        }

        private SavedViewState(Parcel source)
        {
            super(source);
            loadingId = source.readInt();
            emptyId = source.readInt();
            netErrorId = source.readInt();
            lastState = source.readInt();
            enableAnim = source.readByte() == (byte) 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags)
        {
            super.writeToParcel(out, flags);
            out.writeInt(loadingId);
            out.writeInt(emptyId);
            out.writeInt(netErrorId);
            out.writeInt(lastState);
            out.writeByte(enableAnim ? (byte) 1 : (byte) 0);
        }

        public static final Parcelable.Creator<SavedViewState> CREATOR = new Creator<SavedViewState>()
        {
            @Override
            public SavedViewState createFromParcel(Parcel source)
            {
                return new SavedViewState(source);
            }

            @Override
            public SavedViewState[] newArray(int size)
            {
                return new SavedViewState[size];
            }
        };

    }
}
