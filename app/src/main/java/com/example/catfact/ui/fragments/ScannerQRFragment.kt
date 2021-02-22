package com.example.catfact.ui.fragments


import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import kotlinx.android.synthetic.main.fragment_scanner_q_r.*
import com.example.catfact.R


private const val CAMERA_REQUEST_CODE = 101

class ScannerQR : Fragment() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner_q_r, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Checks camera permissions
        setUpPermissions()
        //If permissions are granted allows user to use the scanner
        //Method that sets the scanner
        codeScanner()
    }


    private fun codeScanner() {
        //Stocking the reference to the scanner layout
        val scannerViewLO = requireView().findViewById<CodeScannerView>(R.id.scanner_view)

        //Passing the context and layout as parameters to the method setting up the scanner
        codeScanner = CodeScanner(requireContext(), scannerViewLO)

        //Setting of the scanner
        codeScanner.apply {
            //Using the back camera to see the code in front of us
            camera = CodeScanner.CAMERA_BACK
            //Supporting all formats
            formats = CodeScanner.ALL_FORMATS

            //Auto focusing on regular intervals, not continuously
            autoFocusMode = AutoFocusMode.SAFE
            //Avoids that scanner stops now and then
            scanMode = ScanMode.CONTINUOUS
            //Auto focus on at initilization
            isAutoFocusEnabled = true
            //Flash not on at initialization, avoids being dazzled by the light
            isFlashEnabled = false

            //Decoding the scanned QR code or bar code
            decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    tv_textView.text = it.text
                }
            }
            //Handling errors related to the camera
            errorCallback = ErrorCallback {
                requireActivity().runOnUiThread {
                    Log.e("ScannerQR", "Erreur d'initialisation de la caméra : ${it.message}")
                }
            }
        }

    }


    //When leaving and coming back to the app
    //tries to fetch QRcode again
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    //When leaving the app
    //releases the resources used by the camera
    //Avoids memory leaks
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    //Sets up camera permission for users of API > 23
    private fun setUpPermissions() {
        val permission = ContextCompat.checkSelfPermission(requireActivity(),
            android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    //Requests camera permission
    private fun makeRequest() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    //Handles the camera permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(activity, "Il faut autoriser l'utilisation de la caméra pour cette fonctionnalité!",
                        Toast.LENGTH_SHORT).show()
                }else{
                    //successful
                }
            }
        }
    }

}