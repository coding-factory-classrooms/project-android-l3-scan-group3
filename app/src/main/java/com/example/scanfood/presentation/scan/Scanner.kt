package com.example.scanfood.presentation.scan

import android.content.Context
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode
import com.example.scanfood.databinding.ActivityScanBinding
import com.google.zxing.BarcodeFormat
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import android.app.Activity
import android.util.Log


object Scanner  : Activity() {
    lateinit var binding: ActivityScanBinding
    lateinit var codeScanner: CodeScanner

    fun startScan(context: Context, callBack: ScannerCallBack){
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
                callBack.onScanIDCallBack(it.text)
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

        binding.fabBack.setOnClickListener {
            Log.i("ScanActivity", "closing the scanner...")
            codeScanner.stopPreview()
            finish()
        }

    }

    fun resume() {
        if(Scanner::codeScanner.isInitialized) {
            codeScanner?.startPreview()
        }
    }

    fun pause() {
        if(Scanner::codeScanner.isInitialized) {
            codeScanner?.releaseResources()
        }
    }
}

interface ScannerCallBack{
    /**
     * Callback based on scan ID
     *
     * @param  value String
     * @return
     * @see
     */
    fun onScanIDCallBack(value: String)
}