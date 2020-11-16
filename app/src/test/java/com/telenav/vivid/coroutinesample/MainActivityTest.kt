package com.telenav.vivid.coroutinesample

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    val testDispatcher = TestCoroutineDispatcher()
    val controller  = Robolectric.buildActivity(MainActivity::class.java)

    @Test
    fun `Test button onClick`() = testDispatcher.runBlockingTest {
        val activity = controller.get()
        testDispatcher.pauseDispatcher()
        activity.asyncAndAwait(testDispatcher)
        testDispatcher.resumeDispatcher()
        assert(activity.testValue == 5)
    }

    @Test
    fun useRunBlocking() = runBlockingTest {
        val activity = controller.get()
        val expected = 666666671666
        val result = activity.heavyOperation()
        assertEquals(expected, result)
    }
}