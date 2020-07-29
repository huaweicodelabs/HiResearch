package com.huawei.codelabs.hiresearch.healthstudy;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.huawei.codelabs.hiresearch.lgtest.Class1;
import com.huawei.codelabs.hiresearch.lgtest.Manager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        Class1 class1 = new Class1("name1", "project1");
        Class1 class2 = new Class1("name2", "project2");
        Class1 class3 = new Class1("name3", "project3");

        Class1 class11 = Manager.getInstance().getClass1("project1", "name1");
        String name = class11.getName();
        // Context of the app under test.
        // Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //assertEquals("com.huawei.codelabs.hiresearch.healthstudy", appContext.getPackageName());
    }
}