package com.lima.sudoku_vocab;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;


@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class MenuToGameUITest {
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
    public void ClassicEasySixGame() throws UiObjectNotFoundException{
        // Main menu
        device.findObject(new UiSelector()
                .resourceId(VOCABULARY_SUDOKU_PACKAGE+ ":id/" + "classic_mode_btn")
                .className("android.widget.Button")).click();

        // Difficulty menu
        device.findObject(new UiSelector()
                .resourceId(VOCABULARY_SUDOKU_PACKAGE+ ":id/" + "medium_btn")
                .className("android.widget.Button")).click();

        // Board size menu
        device.findObject(new UiSelector()
                .resourceId(VOCABULARY_SUDOKU_PACKAGE+ ":id/" + "six_btn")
                .className("android.widget.Button")).click();

        // Word selection
        UiScrollable wordList = new UiScrollable(new UiSelector()
                .className("android.widget.ListView")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE+ ":id/" + "word_select_list"));

        wordList.getChildByText(new UiSelector().className("android.widget.CheckedTextView"),
                "apple - 'epIl naHmey").click();
        wordList.getChildByText(new UiSelector().className("android.widget.CheckedTextView"),
                "creature - chenmoH").click();
        wordList.getChildByText(new UiSelector().className("android.widget.CheckedTextView"),
                "duck - chech").click();
        wordList.getChildByText(new UiSelector().className("android.widget.CheckedTextView"),
                "egg - Qlm").click();
        wordList.getChildByText(new UiSelector().className("android.widget.CheckedTextView"),
                "fog - tlhimqaH").click();
        wordList.getChildByText(new UiSelector().className("android.widget.CheckedTextView"),
                "love - parmaq").click();

        UiObject statusField = device.findObject(new UiSelector()
                .className("android.widget.TextView")
                .resourceId(VOCABULARY_SUDOKU_PACKAGE+ ":id/" + "selected_word_count"));
        assertEquals(statusField.getText(), "6/6 selected");

        UiObject confirmBtn = device.findObject(new UiSelector()
                .resourceId(VOCABULARY_SUDOKU_PACKAGE+ ":id/" + "confirmBtn")
                .className("android.widget.Button"));
        confirmBtn.click();

        // Main game
        UiObject board = device.findObject(new UiSelector()
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "sudoku_table"));

        UiObject buttons = device.findObject(new UiSelector()
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "word_buttons"));

        // First input mode: cell -> button
        for (int i = 0; i < 18; i++) {
            board.getChild(new UiSelector()
                            .className("android.widget.TableRow")
                            .index(new Random().nextInt(6)))
                    .getChild(new UiSelector()
                            .className("android.widget.TextView")
                            .index(new Random().nextInt(6))).click();
            buttons.getChild(new UiSelector().className("android.widget.TextView").text("'epIl naHmey")).click();
        }

        // Second input mode: button -> many cells
        buttons.getChild(new UiSelector().className("android.widget.TextView").text("parmaq")).click();
        for (int i = 0; i < 18; i++)
            board.getChild(new UiSelector()
                            .className("android.widget.TableRow")
                            .index(new Random().nextInt(6)))
                .getChild(new UiSelector()
                        .className("android.widget.TextView")
                        .index(new Random().nextInt(6))).click();

        // Clear button test
        device.findObject(new UiSelector()
                .resourceId(VOCABULARY_SUDOKU_PACKAGE + ":id/" + "clearButton")).click();
        for (int i = 0; i < 18; i++)
            board.getChild(new UiSelector()
                            .className("android.widget.TableRow")
                            .index(new Random().nextInt(6)))
                    .getChild(new UiSelector()
                            .className("android.widget.TextView")
                            .index(new Random().nextInt(6))).click();
    }
}
