package com.townwang.transitionanimation.amin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.townwang.transitionanimation.utils.Log;

/**
 * @author Town
 * @created at 2018/4/3 10:37
 * @Last Modified by: Town
 * @Last Modified time: 2018/4/3 10:37
 * @Remarks 动画
 */

public class ActivityAnimationHelper {
    private static final int ANIM_STATE_PREPARE = 1;
    private static final int ANIM_STATE_START = 2;
    private static final int ANIM_STATE_END = 3;

    /**
     * Activity 跳转到 Activity
     *
     * @param activity activity
     * @param intent   intent
     * @param view     view
     */
    public static void startActivity(Activity activity, Intent intent, View view) {
        if (!AnimationConstants.ENABLE_ANIMATION) {
            activity.startActivity (intent);
            return;
        }
        activity.startActivity (intentWrapper (activity, intent, view));
        activity.overridePendingTransition (0, 0);
    }

    /**
     * Fragment跳转到Activity
     *
     * @param fragment fragment
     * @param intent   intent
     * @param view     view
     */
    public static void startActivity(Fragment fragment, Intent intent, View view) {
        if (!AnimationConstants.ENABLE_ANIMATION) {
            fragment.startActivity (intent);
            return;
        }
        fragment.startActivity (intentWrapper (fragment.getActivity (), intent, view));
        fragment.getActivity ().overridePendingTransition (0, 0);
    }

    /**
     * 添加动画常量
     *
     * @param activity activity
     * @param intent   intent
     * @param view     view
     * @return intent
     */
    private static Intent intentWrapper(Activity activity, Intent intent, View view) {
        int[] location = new int[2];
        calculatePivotXY (view, location, activity);
        intent.putExtra (AnimationConstants.ACTIVITY_ANIMATION_ENABLE, true);
        intent.putExtra (AnimationConstants.ACTIVITY_ANIMATION_PIVOTX, location[0]);
        intent.putExtra (AnimationConstants.ACTIVITY_ANIMATION_PIVOTY, location[1]);
        return intent;
    }

    /**
     * 位置设置
     *
     * @param view     点击视图
     * @param location 位置
     * @param context  上下文
     */
    private static void calculatePivotXY(View view, int[] location, Context context) {
        int screenCenterX = DeviceUtil.getScreenW (context) / 2;
        int screenCenterY = DeviceUtil.getScreenH (context) / 2;
        if (view != null) {
            view.getLocationOnScreen (location);
            location[0] = location[0] + view.getWidth () / 2;
            location[1] = location[1] + view.getHeight () / 2;
        } else {
            location[0] = screenCenterX;
            location[1] = screenCenterY;
        }
    }

    public static void animScaleUp(Activity mainActivity, int color, Intent intent, final OnAnimationListener listener) {
        //普通页面默认在style里将activity设置成透明的，所以动画开始前不需要再手动设置，手动设置比较耗时
        animScaleUp (mainActivity, intent, listener, color);
    }

    /**
     * @param activity 视图
     * @param intent   意图
     * @param listener 监听
     */
    public static void animScaleUp(final Activity activity, Intent intent,
                                   final OnAnimationListener listener, int color) {
        if (!canAnimation (activity)) {
            notifyAnimState (listener, ANIM_STATE_END);
            return;
        }

        final View view = activity.findViewById (activity.getWindow ().ID_ANDROID_CONTENT);

        if (view == null) {
            notifyAnimState (listener, ANIM_STATE_END);
            return;
        }

        notifyAnimState (listener, ANIM_STATE_PREPARE);
        final ViewGroup viewGroup = (ViewGroup) view;
        viewGroup.getChildAt (0).setVisibility (View.GONE);
        int screenCenterX = DeviceUtil.getScreenW (activity) / 2;
        int screenCenterY = DeviceUtil.getScreenH (activity) / 2;
        final int x = intent.getIntExtra (AnimationConstants.ACTIVITY_ANIMATION_PIVOTX, screenCenterX);
        final int y = intent.getIntExtra (AnimationConstants.ACTIVITY_ANIMATION_PIVOTY, screenCenterY);

        final WaveRevealView waveRevealView = new WaveRevealView (activity);
        //添加波控件
        viewGroup.addView (waveRevealView);
        //获取动画开始位置
        final int[] location = new int[2];
        location[0] = x;
        location[1] = y;
        //启动动画
        waveRevealView.getViewTreeObserver ().addOnPreDrawListener (new ViewTreeObserver.OnPreDrawListener () {
            @Override
            public boolean onPreDraw() {
                Log.d ("转场动画开始");
                waveRevealView.getViewTreeObserver ().removeOnPreDrawListener (this);
                waveRevealView.setFILL_TIME (400);
                waveRevealView.setFillPaintColor (color);
                waveRevealView.startFromLocation (location, true);
//                hasAnimationStarted = true;
                return true;

            }
        });

        waveRevealView.setOnStateChangeListener (state -> {
            if (state == WaveRevealView.STATE_FINSHED) {
                //动画执行结束后，移除圆形动画
                viewGroup.removeView (waveRevealView);
                viewGroup.getChildAt (0).setVisibility (View.VISIBLE);
                waveRevealView.post (() -> notifyAnimState (listener, ANIM_STATE_END));
                Log.d ("转场动画结束");
            }
        });
    }

    public static void animScaleDown(final Activity activity, int color, final OnAnimationListener listener) {
        if (!canAnimation (activity)) {
            notifyAnimState (listener, ANIM_STATE_END);
            return;
        }

        final View view = activity.findViewById (activity.getWindow ().ID_ANDROID_CONTENT);
        if (view == null) {
            notifyAnimState (listener, ANIM_STATE_END);
            return;
        }
        notifyAnimState (listener, ANIM_STATE_PREPARE);
        final ViewGroup viewGroup = (ViewGroup) view;
        viewGroup.getChildAt (0).setVisibility (View.GONE);

        int screenCenterX = DeviceUtil.getScreenW (activity) / 2;
        int screenCenterY = DeviceUtil.getScreenH (activity) / 2;
        final int x = activity.getIntent ().getIntExtra (AnimationConstants.ACTIVITY_ANIMATION_PIVOTX, screenCenterX);
        final int y = activity.getIntent ().getIntExtra (AnimationConstants.ACTIVITY_ANIMATION_PIVOTY, screenCenterY);


        final WaveRevealView waveRevealView = new WaveRevealView (activity);
        //添加波控件
        viewGroup.addView (waveRevealView);
        //获取动画开始位置
        final int[] location = new int[2];
        location[0] = x;
        location[1] = y;


        //启动动画
        waveRevealView.getViewTreeObserver ().addOnPreDrawListener (new ViewTreeObserver.OnPreDrawListener () {
            @Override
            public boolean onPreDraw() {
                Log.d ("转场动画开始");
                waveRevealView.getViewTreeObserver ().removeOnPreDrawListener (this);
                waveRevealView.setFillPaintColor (color);
                waveRevealView.startFromLocation (location, false);
                return true;

            }
        });
        waveRevealView.setOnStateChangeListener (state -> {
            if (state == WaveRevealView.STATE_FINSHED) {
                try {
                    notifyAnimState (listener, ANIM_STATE_END);
                    viewGroup.removeViewAt (1);
                    viewGroup.removeViewAt (0);
                    activity.overridePendingTransition (0, 0);
                    Log.d ("转场动画结束");
                } catch (Exception e) {
                    Log.e ("转场动画结束时出错");
                }
            }
        });
    }

    private static boolean canAnimation(Activity activity) {
        if (!AnimationConstants.ENABLE_ANIMATION) {//动画总开关
            return false;
        }
        if (activity.getIntent () == null ||
                !activity.getIntent ().getBooleanExtra (AnimationConstants.ACTIVITY_ANIMATION_ENABLE, false)) {//每个Activity自己的动画开关
            return false;
        }
        return true;
    }

    private static void notifyAnimState(OnAnimationListener listener, int animState) {
        if (listener != null) {
            switch (animState) {
                case ANIM_STATE_PREPARE:
                    listener.onAnimationPrepare ();
                    break;
                case ANIM_STATE_START:
                    listener.onAnimationStart ();
                    break;
                case ANIM_STATE_END:
                    listener.onAnimationEnd ();
                    break;
                default:
                    break;
            }
        }
    }
}
