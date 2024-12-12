package com.example.hope.signInScreen

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass


private const val BASIC_SAMPLE_PACKAGE = "com.example.hope"
private const val LAUNCH_TIMEOUT = 5000L

class SignInTest {
    private lateinit var device: UiDevice

    @Before
    fun setUp() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        MatcherAssert.assertThat(launcherPackage, CoreMatchers.notNullValue())
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
    @Test
    fun testSignIn() {
        val title = device.findObject(UiSelector().textContains("Have a nice day"))
        MatcherAssert.assertThat(title.exists(), `is`(true))
    }
}


class FirstTest {


//    @Before
//    fun signIn() {
//        device =  UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
//        appIcon = device.findObject(UiSelector().text(appName))
//        appIcon.click()
//
//        Thread.sleep(2000)
//
//        device.findObject(UiSelector().text("Sign in with Google"))
//            ?.takeIf { it.exists() }
//            ?.click()
//
//        Thread.sleep(5000)
//
//        if (device.findObject(UiSelector().textContains("Choose an account")).exists()) {
//            val chooseAccount = device.findObject(UiSelector().textContains("Continue"))
//            chooseAccount.click()
//        }
//        Thread.sleep(2000)
//    }
//    @After
//    fun tearDown() {
//        device.pressHome()
//        Thread.sleep(2000)
//    }

    companion object {
        lateinit var device: UiDevice
        val appName = "Remotion"
        lateinit var appIcon: UiObject

        @JvmStatic
        @BeforeClass
        fun open() {

            device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            appIcon = device.findObject(UiSelector().text(appName))
            appIcon.click()

            Thread.sleep(2000)

            device.findObject(UiSelector().text("Sign in with Google"))
                ?.takeIf { it.exists() }
                ?.click()

            Thread.sleep(5000)

            if (device.findObject(UiSelector().textContains("Choose an account")).exists()) {
                val chooseAccount = device.findObject(UiSelector().textContains("Continue"))
                chooseAccount.click()
            }
            Thread.sleep(2000)
        }
    }

    @Test
    fun testSignIn() {
        val title = device.findObject(UiSelector().textContains("Have a nice day"))
        assertThat(title.exists(), `is`(true))
    }

    @Test
    fun testChangeEmotion() {


        val firstDat = device.findObject(UiSelector().descriptionContains("Emotion"))
        firstDat.click()
        Thread.sleep(500)
        val editButton = device.findObject(UiSelector().text("Sửa"))
        editButton.click()
        Thread.sleep(500)

        val emotion = device.findObject(UiSelector().descriptionContains("Emotion 2"))
        emotion.click()
        Thread.sleep(500)
        val saveButton = device.findObject(UiSelector().text("Lưu"))
        saveButton.click()
        Thread.sleep(500)

        emotion.click()
        Thread.sleep(500)
        assertThat(emotion.exists(), `is`(true))


    }

    @Test
    fun testChangeNote() {
        val date = device.findObject(UiSelector().descriptionContains("Emotion"))
        date.click()
        Thread.sleep(500)

        val editButton = device.findObject(UiSelector().text("Sửa"))
        editButton.click()
        Thread.sleep(500)

        val note = device.findObject(UiSelector().textContains("Nhập"))
        note.click()
        Thread.sleep(500)

        note.clearTextField()

        for (i in 1..50) {
            device.pressKeyCode(KeyEvent.KEYCODE_DEL)
        }
        Thread.sleep(500)

        device.pressKeyCode(KeyEvent.KEYCODE_1)
        Thread.sleep(500)

        val saveButton = device.findObject(UiSelector().text("Lưu"))
        saveButton.click()
        Thread.sleep(500)

        date.click()
        Thread.sleep(500)
        val noteChange = device.findObject(UiSelector().text("1"))
        assertThat(noteChange.exists(), `is`(true))

    }

}

class NoteTest {
    private lateinit var device: UiDevice
    private val appName = "Remotion"
    private lateinit var appIcon: UiObject


    @Before
    fun signIn() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        appIcon = device.findObject(UiSelector().text(appName))
        appIcon.click()

        Thread.sleep(2000)

        device.findObject(UiSelector().text("Sign in with Google"))
            ?.takeIf { it.exists() }
            ?.click()

        Thread.sleep(5000)

        if (device.findObject(UiSelector().textContains("Choose an account")).exists()) {
            val chooseAccount = device.findObject(UiSelector().textContains("Continue"))
            chooseAccount.click()
        }
        Thread.sleep(2000)
    }

    @After
    fun tearDown() {
        device.pressHome()
        Thread.sleep(2000)
    }

    @Test
    fun testAddNote() {


    }
}