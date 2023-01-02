<<<<<<<< HEAD:app/src/androidTest/java/com/example/switchlanguage/ExampleInstrumentedTest.java
package com.example.switchlanguage;
========
package com.example.rttl_13;
>>>>>>>> df334a9 (合併文件):app/src/androidTest/java/com/example/rttl_13/ExampleInstrumentedTest.java

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
<<<<<<<< HEAD:app/src/androidTest/java/com/example/switchlanguage/ExampleInstrumentedTest.java
        assertEquals("com.example.switchlanguage", appContext.getPackageName());
========
        assertEquals("com.example.rttl_13", appContext.getPackageName());
>>>>>>>> df334a9 (合併文件):app/src/androidTest/java/com/example/rttl_13/ExampleInstrumentedTest.java
    }
}