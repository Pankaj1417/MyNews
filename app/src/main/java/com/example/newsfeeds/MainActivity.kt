package com.example.newsfeeds

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest

import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), NewsItemClicked {
    lateinit var mAdapter: NewsListAdapter
    private lateinit var url : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        url = "https://newsapi.org/v2/top-headlines?country=in" +
                "&apiKey=3ea5fd3c07b34d62b5fef3430f7eb3d0"
        fetchData(url)
         mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData(url :String) {
        val newsJsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                    Log.e("sdsadas","$it")
                    val newsJsonArray = it.getJSONArray("articles")
                    val newsArray = ArrayList<News>()
                    for(i in 0 until  newsJsonArray.length()){
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                                newsJsonObject.getString("author"),
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }
                    mAdapter.update(newsArray)
                },
                Response.ErrorListener { error ->

                }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(newsJsonObjectRequest)
    }

    

    override fun itemClicked(news : News) {
        val builder = CustomTabsIntent.Builder();
       val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(news.url));
    }



   
}