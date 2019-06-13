package com.lwkandroid.widget;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Description:状态值
 *
 * @author LWK
 * @date 2019/6/11
 */
@IntDef({StateConstants.INIT, StateConstants.LOADING, StateConstants.EMPTY, StateConstants.NET_ERROR, StateConstants.CONTENT})
@Retention(RetentionPolicy.SOURCE)
public @interface LayoutState
{
}
