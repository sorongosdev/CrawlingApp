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
        val url =
            "https://search.naver.com/search.naver?where=image&sm=tab_jum&query=%EC%95%88%EC%82%B0+%EB%82%A0%EC%94%A8"

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
            val imgElements: Elements = document.select("div .thumb img")

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