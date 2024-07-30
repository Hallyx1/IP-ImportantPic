package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

import java.io.File
import java.lang.Exception
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()
        ){isGranted: Boolean ->
            if (isGranted){
                Log.i("permission","granted")
                startCamera()
            }else{
                Log.i("permission","Denied")
            }
        }
    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var outputDirectory: File
    private lateinit var imageCapture: ImageCapture

    val storage = Firebase.storage
    var storageRef = storage.reference
    //var imagesRef: StorageReference? = storageRef.child("images")
    val storageDirectoryPath = "images/"
    val storageDirectoryRef = storageRef.child(storageDirectoryPath)






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        cameraExecutor = Executors.newSingleThreadExecutor()
        requestPermission()
        outputDirectory = getOutputDirectory()

        imageCapture = ImageCapture.Builder().build()



        val btnPhoto = binding.btnPhoto

        btnPhoto.setOnClickListener{
            takePhoto()
        }



    }

    private fun requestPermission(){

        when{
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED ->{
                //PERM GRANTED
                startCamera()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
              android.Manifest.permission.CAMERA
            ) -> {
                //Toast.makeText(this, "you Need to accept permission", Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }


    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                // Camera initialization code
                val processCameraProvider = ProcessCameraProvider.getInstance(this)
                processCameraProvider.addListener({
                    try {
                        val cameraProvider = processCameraProvider.get()
                        val previewUseCase = buildPreviewUseCase()

                        // Ensure that the cameraSelector is correctly set
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        // Unbind and rebind to apply changes
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase, imageCapture)
                    } catch (e: Exception) {
                        Log.d("error", e.message.toString())
                        // Handle initialization error
                    }
                }, ContextCompat.getMainExecutor(this))
            } catch (e: Exception) {
                Log.e("CameraX", "Error initializing camera: ${e.message}", e)
                // Handle initialization error
            }
        } else {
            // Handle the case where permissions are not granted
            Log.e("CameraX", "Camera permission not granted.")
        }
    }
    /*private fun startCamera() {

        val processCameraProvider = ProcessCameraProvider.getInstance(this)
        processCameraProvider.addListener({
            try {
                val cameraProvider = processCameraProvider.get()
                val previewIseCase = buildPreviewUseCase()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA,previewIseCase)
            }catch (e: Exception){
                Log.d("error" , e.message.toString())
                //Toast.makeText(this, "oops" + e, Toast.LENGTH_SHORT).show()
            }

        },ContextCompat.getMainExecutor(this))
    }*/


    fun buildPreviewUseCase(): Preview {
        return Preview.Builder().build().also { it.setSurfaceProvider(binding.previewView.surfaceProvider) }
     }

    private fun takePhoto(){
        Log.d("Debug", "takePhoto() called")
        var camView = binding.previewView

        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("CameraX", "Error capturing image: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)

                    uploadImageToFirebase(savedUri)
                    Log.d("Debug", "takePhoto() completed")

                    //afficher la photo prise en trouvant comment changer le camview




                }
            }
        )

}

    private fun uploadImageToFirebase(uri: Uri) {
        val file = File(uri.path ?: "")
        val imageRef = storageDirectoryRef.child(file.name)

        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                // Image uploaded successfully
                Log.i("Firebase", "Image uploaded successfully")
                // Additional actions if needed
            } else {
                // Handle the error
                Log.e("Firebase", "Error uploading image: ${task.exception?.message}", task.exception)
            }

    }
}
    private fun getOutputDirectory(): File {
        // Get the external media directory, if available
        val mediaDir = externalMediaDirs.firstOrNull()

        // Create a directory within the external media directory or use the app's internal files directory
        return if (mediaDir != null && mediaDir.exists()) {
            File(mediaDir, resources.getString(R.string.app_name)).apply { mkdirs() }
        } else {
            filesDir
        }
    }

    fun viewPicPage(view: View){
        val intent = Intent(this,viewPic::class.java)
        startActivity(intent)
    }

}