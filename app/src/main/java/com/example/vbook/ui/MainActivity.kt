package com.example.vbook.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.vbook.R
import com.example.vbook.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import org.jsoup.Jsoup

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

}