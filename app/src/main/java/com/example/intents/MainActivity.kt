package com.example.intents

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var cppArl: ActivityResultLauncher<String>
    private lateinit var parameterArl: ActivityResultLauncher<Intent>
    private lateinit var pickImageArl: ActivityResultLauncher<Intent>

    private val activityMainBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}
