package com.sorongos.crawlingapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)

        //extended image
        val url =
            "https://www.pinterest.co.kr/pin/39406565482353666/"

//        val url =
//            "https://www.pinterest.co.kr/search/pins/?rs=ac&len=2&q=%EC%B9%9C%EA%B5%AC%20%EC%82%AC%EC%A7%84%20%ED%8F%AC%EC%A6%88&eq=%EC%B9%9C%EA%B5%AC%20%EC%82%AC%EC%A7%84&etslf=1286"

        GlobalScope.launch(Dispatchers.IO) {
            val imageUrl = getImageUrl(url) // 대상 웹 페이지의 URL을 입력
            Log.d("imgUrl", "$imageUrl")
            imageUrl?.let {
                launch(Dispatchers.Main) {
                    loadImageIntoImageView(it, imageView)

                }
            }
        }
    }

    private fun getImageUrl(url: String): String? {
        return try {
            val document: Document = Jsoup.connect(url).get()
            Log.d("document", "$document")
            val imgElements: Elements = document.select("img")
            Log.d("image","$imgElements")

            if (imgElements.isNotEmpty()) {
                val imageUrl = imgElements[0].absUrl("src")
                if (imageUrl.isNotEmpty()) {
                    imageUrl
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadImageIntoImageView(imageUrl: String, imageView: ImageView) {
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
    }
}