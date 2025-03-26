package com.example.intents

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_CHOOSER
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_PICK
import android.content.Intent.ACTION_VIEW
import android.content.Intent.EXTRA_INTENT
import android.content.Intent.EXTRA_TITLE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.intents.Extras.PARAMETER_EXTRA
import com.example.intents.Messages.CALL_ERROR
import com.example.intents.Messages.CHOOSE_BROWSER
import com.example.intents.Messages.OPEN_MESSAGE
import com.example.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var cppArl: ActivityResultLauncher<String>
    private lateinit var parameterArl: ActivityResultLauncher<Intent>
    private lateinit var pickImageArl: ActivityResultLauncher<Intent>

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = localClassName

        binding.parameterBt.setOnClickListener {

            Intent(this, ParameterActivity::class.java).let {
                it.putExtra(PARAMETER_EXTRA, binding.parameterTv.text.toString())
                parameterArl.launch(it)
            }
        }

        parameterArl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getStringExtra(PARAMETER_EXTRA)?.let {
                    binding.parameterTv.text = it
                }
            }
        }

        cppArl = registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) callPhone(true)
            else Toast.makeText(this, CALL_ERROR, Toast.LENGTH_SHORT).show()
        }

        pickImageArl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                startActivity(Intent(ACTION_VIEW, result.data?.data))
            }
        }
    }

    private fun callPhone(call: Boolean) {
        val number = "tel:${binding.parameterTv.text}"
        val callIntent = Intent(if (call) ACTION_CALL else ACTION_DIAL).apply {
            data = Uri.parse(number)
        }
        startActivity(callIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_activity_mi -> {
                Toast.makeText(this, OPEN_MESSAGE, Toast.LENGTH_SHORT).show()
                true
            }

            R.id.view_mi -> {
                startActivity(browserIntent())
                true
            }

            R.id.call_mi -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(CALL_PHONE) == PERMISSION_GRANTED) callPhone(true)
                    else cppArl.launch(CALL_PHONE)
                } else callPhone(true)
                true
            }

            R.id.dial_mi -> {
                callPhone(false)
                true
            }

            R.id.pick_mi -> {
                val imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
                val pickImageIntent = Intent(ACTION_PICK).apply {
                    setDataAndType(Uri.parse(imageDir), "image/*")
                }
                pickImageArl.launch(pickImageIntent)
                true
            }

            R.id.chooser_mi -> {
                val chooserIntent = Intent(ACTION_CHOOSER).apply {
                    putExtra(EXTRA_TITLE, CHOOSE_BROWSER)
                    putExtra(EXTRA_INTENT, browserIntent())
                }
                startActivity(chooserIntent)
                true
            }

            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun browserIntent(): Intent {
        val url = Uri.parse(binding.parameterTv.text.toString())
        return Intent(ACTION_VIEW, url)
    }
}
