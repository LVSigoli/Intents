package com.example.intents

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.intents.Extras.PARAMETER_EXTRA
import com.example.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var cppArl: ActivityResultLauncher<String>
    private lateinit var parameterArl: ActivityResultLauncher<Intent>
    private lateinit var pickImageArl: ActivityResultLauncher<Intent>

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent("OPEN_PARAMETER_ACTIVITY_OPTION").let {


            it.putExtra(PARAMETER_EXTRA, binding.parameterTv.text.toString())

            parameterArl.launch(it)
        }

        parameterArl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode === RESULT_OK){
                result.data?.getStringExtra(PARAMETER_EXTRA).let {
                    binding.parameterTv.text = it
                }
            }
        }


    }
}




