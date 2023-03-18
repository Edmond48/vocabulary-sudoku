package com.lima.sudoku_vocab;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class WordListUITest {
    private static final String VOCABULARY_SUDOKU_PACKAGE
            = "com.lima.sudoku_vocab";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice device;

    @Before
    public void startAppFromHomeScreen() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.pressHome();

        final String launcherPackage = device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(VOCABULARY_SUDOKU_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(VOCABULARY_SUDOKU_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void AddThenRemoveTest() throws UiObjectNotFoundException {

        // Go into word list screen
        device.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "word_list_btn")).click();

        // Add mode
        device.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "addBtn")).click();

        // Cancel add
        device.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "cancelAddBtn")).click();

        // Add again
        device.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "addBtn")).click();

        // Input native word
        device.findObject(new UiSelector()
                        .className("android.widget.EditText")
                        .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "editNativeWord"))
                .setText("test_native1");

        // Input foreign word
        device.findObject(new UiSelector()
                        .className("android.widget.EditText")
                        .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "editForeignWord"))
                .setText("test_foreign1");

        // Confirm addition of word pair
        device.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "confirmAddBtn")).click();

        // Remove mode
        device.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "removeBtn")).click();

        // Cancel
        device.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "cancelRemoveBtn")).click();

        // Remove again
        device.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "removeBtn")).click();

        // Choose word added above
        UiScrollable list = new UiScrollable(new UiSelector()
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "word_list_view"));

        list.getChildByText(new UiSelector()
                .className("android.widget.TextView"), "test_native1").click();

        // Confirm removal
        device.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "confirmRemoveBtn")).click();
    }
}
