package kush.example.kotlincoroutinesexapmle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private val RESULT1 = "result1"
    private val RESULT2 = "result2"
    private val NETWORK_TIMEOUT = 500L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnClickMe.setOnClickListener(View.OnClickListener {
            CoroutineScope(IO).launch {
                val result = getResult1FromApi()
                setTextOnMainThread(result)
            }
        })
    }

    suspend fun setTextOnMainThread(result: String) {

        withContext(Main) {
            tv.text = result

            val job = withTimeoutOrNull(NETWORK_TIMEOUT) {
                val result2 = getResult2FromApi()
                withContext(Main) {
                    tv2.text = result2
                }
            }
            if (job == null) {
                tv2.text = "Process took too long to execute !!"
            }
        }
    }

//    private suspend fun fakeApiRequest() {
//        val result = getResult1FromApi()
//        setTextOnMainThread(result)
//    }

    suspend fun getResult1FromApi(): String {
        delay(1000)
        return RESULT1
    }

    suspend fun getResult2FromApi(): String {
        delay(1000)
        return RESULT2
    }
}