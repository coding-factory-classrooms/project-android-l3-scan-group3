package com.example.scanfood

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.scanfood.databinding.ActivityLoginBinding
import com.example.scanfood.databinding.ActivityScanBinding
import com.google.zxing.BarcodeFormat
import java.util.*


class ScanActivity : AppCompatActivity() {

    private var scan : Scanner = Scanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scan.binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(scan.binding.root)
        //permet de demander l'autorisation
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {
            scan.startScan(this)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Autorisation accordée", Toast.LENGTH_SHORT).show()
                scan.startScan(this)
            } else {
                Toast.makeText(this, "Autorisation refusée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        scan.resume()
    }

    override fun onPause() {
        scan.pause()
        super.onPause()
    }
}
