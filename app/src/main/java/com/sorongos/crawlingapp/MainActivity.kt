package com.sorongos.crawlingapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils.isEmpty
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sorongos.crawlingapp.databinding.ActivityMainBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var bundle: Bundle
    private val URL: String =
        "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=%EC%95%88%EC%82%B0+%EB%82%A0%EC%94%A8"

    var handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            Log.d("handleMessage","handleMessage")
            val bundle = msg.data    //new Thread에서 작업한 결과물 받기
            Log.d("bundle","${bundle.getString("temperature")}")

            binding.textView.text = bundle.getString("temperature")    //받아온 데이터 textView에 출력
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Thread {
            run {
                try {
                    val doc: Document = Jsoup.connect(URL).get() //URL 웹사이트에 있는 html 코드를 다 끌어오기

                    val temele: Elements =
                        doc.select(".temperature_text") //끌어온 html  에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기

                    var isEmpty = temele.isEmpty() //빼온 값 null체크

                    Log.d("Tag", "isNull? : $isEmpty") //로그캣 출력

                    if (isEmpty === false) { //null값이 아니면 크롤링 실행
                        var tem = temele[0].text()
                            .substring(5) //크롤링 하면 "현재온도30'c" 이런식으로 뽑아와지기 때문에, 현재온도를 잘라내고 30'c만 뽑아내는 코드
                        Log.d("로그", "temperature : $tem") //로그캣 출력
                        val bundle = Bundle()
                        bundle.putString(
                            "temperature",
                            tem
                        ) //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기

                        // 핸들러를 통해 메인 스레드로 데이터 전달
                        val message = handler.obtainMessage(0)
                        message.data = bundle
                        Log.d("로그", "message : $message") //로그캣 출력
                        handler.sendMessage(message)

                    }
                    //크롤링 할 구문
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

}