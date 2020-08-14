package com.huawei.codelabs.hiresearch.healthstudy.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

import java.util.Stack;

import kotlin.Suppress;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */

public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    /**
     * 单一实例
     */
    public static ActivityManager getAppManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {

        if (activityStack == null || activityStack.isEmpty())
            return;
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 判断一个Activity 是否存在
     *
     * @param clz
     * @return
     */
    public <T extends Activity> boolean isActivityExist(Class<T> clz) {
        boolean res =false;
        Activity activity = getActivity(clz);
        if (activity == null) {
            res = false;
        } else {
            if (activity.isFinishing() || activity.isDestroyed()) {
                res = false;
            } else {
                res = true;
            }
        }

        return res;
    }

    /**
     * 获得指定activity实例
     *
     * @param clazz Activity 的类对象
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Activity> T getActivity(Class<T> clazz) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(clazz)) {
                return (T) activity;
            }
        }
        return null;
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        finishAllActivity();
        System.exit(0);
    }
}