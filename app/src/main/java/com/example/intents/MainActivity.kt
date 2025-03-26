package com.example.intents

import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.intents.Extras.PARAMETER_EXTRA
import com.example.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var cppArl: ActivityResultLauncher<String>
    private lateinit var parameterArl: ActivityResultLauncher<Intent>

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = localClassName

        binding.parameterBt.setOnClickListener {

            Intent("OPEN_PARAMETER_ACTIVITY_ACTION").let {
                it.putExtra(PARAMETER_EXTRA, binding.parameterTv.text.toString())
                parameterArl.launch(it)
            }
        }

        parameterArl =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {

                    result.data?.getStringExtra(PARAMETER_EXTRA).let {
                        binding.parameterTv.text = it
                    }
                }
            }
        cppArl =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
                if (permissionGranted) {
                    // Chamar o número
                    callPhone(true)
                } else {
                    Toast.makeText(
                        this,
                        "Permission required to call a number!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun callPhone (call:Boolean){
        val number = "tel: ${binding.parameterTv.text}"
        val callIntent = Intent(if (call) ACTION_CALL else ACTION_DIAL)

        callIntent.data = Uri.parse(number)
        startActivity(callIntent)
    }
}
