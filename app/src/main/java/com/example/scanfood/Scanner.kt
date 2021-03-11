package com.example.scanfood

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.ScanMode
import com.example.scanfood.databinding.ActivityScanBinding
import com.google.zxing.BarcodeFormat
import androidx.activity.viewModels
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import android.Manifest
import android.app.Activity
import com.example.scanfood.ScanActivity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.scanfood.databinding.ActivityLoginBinding


object Scanner  : Activity() {
    lateinit var binding: ActivityScanBinding
    lateinit var codeScanner: CodeScanner

    fun startScan(context: Context) {
        codeScanner = CodeScanner(context, binding.scanView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // active la camera de derriere
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE) // scan uniquement les qr codes
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isFlashEnabled = false
        codeScanner.isAutoFocusEnabled = true


        // Permet de renvoyer la valeur du qr code
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                binding.textViewResult.text = it.text
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Erreur : ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //permet de relire un qr code en cliquant à nouveau sur la camera
        binding.scanView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    fun resume() {

        if(::codeScanner.isInitialized) {
            codeScanner?.startPreview()
        }
    }

    fun pause() {
        if(::codeScanner.isInitialized) {
            codeScanner?.releaseResources()
        }
    }
}