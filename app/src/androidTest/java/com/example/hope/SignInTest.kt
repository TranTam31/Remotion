package com.example.hope

import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.Test
import org.hamcrest.Matchers.*

class SignInTest {

    @Test
    fun testSignIn() {

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val appName = "Remotion"
        val appIcon = device.findObject(UiSelector().text(appName))

        appIcon.click()

        Thread.sleep(2000)

//        val signIn = device.findObject(UiSelector().text("Sign in with Google"))
//        signIn.click()
//
//        Thread.sleep(5000)


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
}