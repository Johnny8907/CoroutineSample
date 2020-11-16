package com.telenav.vivid.coroutinesample

import android.os.Bundle
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var scope: CoroutineScope
    var testValue: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            //should use lifecycleScope
//            GlobalScope.launch {
//                while (true) {
//                    delay(1000L)
//                    Log.d(TAG, "Still running...")
//                }
//            }
//            GlobalScope.launch {
//                delay(3000L)
//                Intent(this@MainActivity, SecondActivity::class.java).also {
//                    startActivity(it)
//                    finish()
//                }
//            }
            scope.cancel(CancellationException("parent scope cancelled!"))
        }
        startWithAJob()
//        asyncAndAwait()
//        asyncAndAwaitV2()

//        runThread()
    }

    private fun runThread() {
        repeat(2000) {
            Thread {
                Thread.sleep(10_000L)
            }.start()
            Log.d(TAG, it.toString())
        }
    }

    @VisibleForTesting
    fun asyncAndAwait(dispatcher: CoroutineDispatcher) {
        GlobalScope.launch(dispatcher) {
            val time = measureTimeMillis {
                var answer1: String? = null
                var answer2: String? = null
                val job1 = launch { answer1 = networkCall1() }
                val job2 = launch { answer2 = networkCall2() }
                job1.join()
                job2.join()
                Log.d(TAG, "Answer1 is $answer1")
                Log.d(TAG, "Answer2 is $answer2")
            }
            delay(13_000L)
            testValue = 5
            Log.d(TAG, "Requests took $time ms.")
        }
    }

    private fun asyncAndAwaitV2() {
        GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                val answer1 = async { networkCall1() }
                val answer2 = async { networkCall2() }
                Log.d(TAG, "Answer1 is ${answer1.await()}")
                Log.d(TAG, "Answer2 is ${answer2.await()}")
            }
            Log.d(TAG, "Requests took $time ms.")
        }
    }

    private suspend fun networkCall1(): String {
        delay(3000L)
        return "Answer 1"
    }

    private suspend fun networkCall2(): String {
        delay(3000L)
        return "Answer 2"
    }

    private fun startWithAJob() {
        scope = CoroutineScope(Dispatchers.Main + Job())
        scope.launch {
            Log.d(TAG, "coroutine A launched")
            delay(2000L)
            Log.d(TAG, "coroutine A finished")
        }
        scope.launch {
            Log.d(TAG, "coroutine B launched")
            delay(5000L)
            Log.d(TAG, "coroutine B finished")
        }
    }

    suspend fun heavyOperation(): Long {
        return withContext(Dispatchers.Default) {
            delay(30_000)
            return@withContext doHardMaths()
        }
    }

    private fun doHardMaths(): Long {
        var count = 0.0
        for (i in 1..100_000_000) {
            count += sqrt(i.toDouble())

        }
        return count.toLong()
    }
}