package com.example.hope

import com.example.hope.auth.ui.sign_in.SignInResult
import com.example.hope.auth.ui.sign_in.SignInViewModel
import com.example.hope.auth.ui.sign_in.UserData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest {

//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SignInViewModel
//    private val testDispatcher = TestCoroutineDispatcher()
//    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        viewModel = SignInViewModel()
    }

    @Test
    fun `onSignInResult with success updates state correctly`() = runTest {
        val signInResult = SignInResult(
            data = UserData("userId", "userName", "profilePictureUrl"),
            errorMessage = null
        )

        viewModel.onSignInResult(signInResult)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isSignInSuccessfull)
        assertNull(state.signInError)
    }

    @Test
    fun `onSignInResult with error updates state correctly`() = runTest {
        val signInResult = SignInResult(
            data = null,
            errorMessage = "Sign in error"
        )

        viewModel.onSignInResult(signInResult)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isSignInSuccessfull)
        assertEquals("Sign in error", state.signInError)
    }

    @Test
    fun `resetState resets state to initial values`() = runTest {
        viewModel.resetState()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isSignInSuccessfull)
        assertNull(state.signInError)
    }

    @Test
    fun handleError() = runTest {
        val signInResult = SignInResult(
            data = null,
            errorMessage = "Sign in error"
        )

        viewModel.onSignInResult(signInResult)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isSignInSuccessfull)
        assertEquals("Sign in error", state.signInError)
    }
}