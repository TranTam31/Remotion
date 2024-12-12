package com.example.hope.noteScreen

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.example.hope.signInScreen.FirstTest
import com.example.hope.signInScreen.FirstTest.Companion
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters


private const val BASIC_SAMPLE_PACKAGE = "com.example.hope"
private const val LAUNCH_TIMEOUT = 5000L

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NoteTest {
    private lateinit var device: UiDevice

    @Before
    fun setUp() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE)?.apply {
            // Clear out any previous instances
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(
            Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT
        )
        Thread.sleep(3000)
        device.findObject(UiSelector().text("Sign in with Google"))
            ?.takeIf { it.exists() }
            ?.click()
        Thread.sleep(5000)

        if (device.findObject(UiSelector().textContains("Choose an account")).exists()) {
            val chooseAccount = device.findObject(UiSelector().textContains("Continue"))
            chooseAccount.click()
        }
    }

    @After
    fun tearDown(){
        Thread.sleep(1000)
    }

    @Test

    fun addNoteTest(){
        val date = device.findObject(UiSelector().text("7"))
        date.click()
        Thread.sleep(500)

        var emotion = device.findObject(UiSelector().descriptionContains("Emotion 5"))
        emotion.click()
        Thread.sleep(500)

        val textField = device.findObject(UiSelector().textContains("Nhập nội dung"))
        textField.click()
        Thread.sleep(500)
        device.pressKeyCode(KeyEvent.KEYCODE_1)
        Thread.sleep(500)

        val saveButton = device.findObject(UiSelector().text("Lưu"))
        saveButton.click()
        Thread.sleep(500)

        emotion= device.findObject(UiSelector().descriptionContains("Emotion 5"))
        emotion.click()
        Thread.sleep(500)
        val note = device.findObject(UiSelector().text("1"))
        assertThat(note.exists(), `is`(true))
    }
    @Test
    fun deleteNoteTest(){
        val date = device.findObject(UiSelector().descriptionContains("Emotion 5"))
        date.click()
        Thread.sleep(500)

        val delButton = device.findObject(UiSelector().text("Xóa"))
        delButton.click()
        Thread.sleep(500)

        val check = device.findObject(UiSelector().text("7"))
        assertThat(check.exists(), `is`(true))
    }

    @Test
    fun changeNoteTest(){
        val date = device.findObject(UiSelector().descriptionContains("Emotion 1"))
        date.click()
        Thread.sleep(500)

        val editButton = device.findObject(UiSelector().text("Sửa"))
        editButton.click()
        Thread.sleep(500)

        val note = device.findObject(UiSelector().textContains("Nhập nội dung"))
        note.click()
        Thread.sleep(500)

        note.clearTextField()
        for (i in 1..10) {
            FirstTest.device.pressKeyCode(KeyEvent.KEYCODE_DEL)
        }
        device.pressKeyCode(KeyEvent.KEYCODE_5)
        Thread.sleep(500)

        val saveButton = device.findObject(UiSelector().text("Lưu"))
        saveButton.click()
        Thread.sleep(500)

        val check = device.findObject(UiSelector().text("5"))
        assertThat(check.exists(), `is`(true))
    }
}