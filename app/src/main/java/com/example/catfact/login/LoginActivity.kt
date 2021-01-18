package com.example.catfact.login


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.catfact.MainActivity
import com.example.catfact.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {

    //stocking FirebaseAuth in a variable
    //to be initialized when necessary
    private lateinit var auth: FirebaseAuth

    //setting user to be firstTimeUser initially
    private var firstTimeUser = true

    //Holds a reference to the URI we select in the imagePicker
    //initially set to null
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Instantiation of authentication
        auth = FirebaseAuth.getInstance()

        //Function that handles button clicks
        buttonClicks()

    }

    //Reference to all the buttons on the login screen
    private fun buttonClicks() {

        //Clicking on "login" sets the user as NOT a firstTimeUser
        btn_login.setOnClickListener {
            firstTimeUser = false
            createOrLoginUser()
        }

        //Clicking on "register" sets the user as a firstTimeUser
        btn_register.setOnClickListener {
            firstTimeUser = true
            createOrLoginUser()
        }

        //Clicking on the profile image allows the user to select a picture
        iv_profileImage.setOnClickListener() {
            selectImage()
        }

    }


    private fun createOrLoginUser() {
        //creating values of email and password
        //allowing to create the user
        val email = et_emailLogin.text.toString()
        val password = et_passwordLogin.text.toString()

        //Checking if the values are empty
        if (email.isNotEmpty() && password.isNotEmpty()) {
            //Using a coroutine to avoid blocking the UI thread
            //on IO because it's in an In/Out operation (transfer of information)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    //Creating the user if it's their first time
                    if (firstTimeUser) {
                        //await guarantees that async operations finish before returning a value
                        auth.createUserWithEmailAndPassword(email, password).await()
                        auth.currentUser.let {
                            val update = UserProfileChangeRequest.Builder()
                                .setPhotoUri(fileUri)
                                .build()
                            //"it" refers to the Firebase user
                            it?.updateProfile(update)
                            //await() makes sure that the block of code is executed
                            //before going on with some more code
                        }?.await()
                    } else {
                        //if not the user's first time:
                        //logs them in with their email and password
                        auth.signInWithEmailAndPassword(email, password).await()
                    }
                    //withContext moves the execution
                    //of a coroutine to a different thread
                    withContext(Dispatchers.Main) {
                        //displaying a toast to let the user know they are logged in
                        Toast.makeText(
                            this@LoginActivity,
                            "Vous êtes connecté!",
                            Toast.LENGTH_SHORT
                        ).show()
                        //Displaying the profile
//                        supportFragmentManager
//                            .beginTransaction()
//                            .replace(R.id.layout_login, Profile())
//                            .addToBackStack(null)
//                            .commit()

                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(i)
                        finish()


                    }
                    //Lets the user know what went wrong thanks to a toast
                    //displaying the Firebase error message (nb of letters, mistake in email...)
                } catch (e: Exception) {
                    //Switching the coroutine context again
                    //The toast has to be displayed in the UI/Main thread
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkIfUserIsLoggedIn() {
        if (auth.currentUser != null) {
//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.layout_login, Profile()).addToBackStack(null).commit()
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        checkIfUserIsLoggedIn()
    }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    //Allows to receive the image and insert it in the imageView
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            //if we get the picture without problem we assign it as profile pic
            Activity.RESULT_OK -> {
                fileUri = data?.data
                iv_profileImage.setImageURI(fileUri)
            }
            //Problems regarding the profile pic setting are displayed in a toast
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                //if the user  backs out of setting a profile pic
                //a toast informs them that the task was cancelled
                Toast.makeText(this, "Tâche abandonnée", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


