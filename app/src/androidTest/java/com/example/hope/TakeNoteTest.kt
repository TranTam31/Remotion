package com.example.hope

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

private const val BASIC_SAMPLE_PACKAGE = "com.example.hope"
private const val LAUNCH_TIMEOUT = 5000L
class TakeNoteTest {
    private lateinit var  device: UiDevice
    private val appName = "Remotion"
    private lateinit var appIcon : UiObject

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
    fun tearDown() {
//        device.pressHome()
//        //device.executeShellCommand("am force-stop $BASIC_SAMPLE_PACKAGE")
//        device.pressRecentApps()
//        Thread.sleep(1000)
//        // Lấy kích thước màn hình
//        val displayWidth = device.displayWidth
//        val displayHeight = device.displayHeight
//
//        // Tọa độ bắt đầu (giữa màn hình, gần đáy)
//        val startX = displayWidth / 2
//        val startY = (displayHeight /2)
//
//        // Tọa độ kết thúc (giữa màn hình, gần đỉnh)
//        val endX = displayWidth / 2
//        val endY = 0
//
//        // Thực hiện vuốt từ dưới lên trên
//        device.swipe(startX, startY, endX, endY, 20) // 20 là số bước vuốt, tăng nếu cần mượt hơn

        Thread.sleep(1000) // Chờ hiệu ứng hoàn tất
    }

    @Test
    fun testSignIn(){
        val title = device.findObject(UiSelector().textContains("Have a nice day"))
        ViewMatchers.assertThat(title.exists(), `is`(true))
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
        ViewMatchers.assertThat(emotion.exists(), `is`(true))


    }

}