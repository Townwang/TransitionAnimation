package com.townwang.transitionanimation.amin;

import android.os.Build;

/**
 * @author Town
 * @created at 2018/4/3 10:37
 * @Last Modified by: Town
 * @Last Modified time: 2018/4/3 10:37
 * @Remarks 动画常量定义
 */

class AnimationConstants {
    public static boolean ENABLE_ANIMATION; //是否开启动画效果

    //Activity动画常量值
    public static final String ACTIVITY_ANIMATION_ENABLE = "activity_animation_enable";
    public static final String ACTIVITY_ANIMATION_PIVOTX = "activity_animation_pivotx";
    public static final String ACTIVITY_ANIMATION_PIVOTY = "activity_animation_pivoty";

    static{
        ENABLE_ANIMATION = getEnableAnimation();
    }

    /**
     * 动画开关是否打开逻辑：
     */
    private static boolean getEnableAnimation(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return false;
        }
        return true;
    }

}
