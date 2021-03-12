package com.example.scanfood.presentation.scan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.scanfood.databinding.ActivityScanBinding


class ScanActivity : AppCompatActivity() {

    private var scan : Scanner =
        Scanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Scanner.binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(Scanner.binding.root)
        //permet de demander l'autorisation
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {
            Scanner.startScan(
                this,
                object : ScannerCallBack {
                    override fun onScanIDCallBack(value: String) {
                        val intent = Intent()
                        intent.putExtra("id", value)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Autorisation accordée", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Autorisation refusée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Scanner.resume()
    }

    override fun onPause() {
        Scanner.pause()
        super.onPause()
    }
}
