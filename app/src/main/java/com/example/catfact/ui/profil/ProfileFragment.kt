package com.example.catfact.ui.profil


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.catfact.R
import com.example.catfact.ui.login.LoginActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.iv_profileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class ProfileFragment : Fragment() {

    //stocking FirebaseAuth in a variable
    //to be initialized when necessary
    private lateinit var auth: FirebaseAuth
    //Holds a reference to the URI we select in the imagePicker
    //initially set to null
    private var fileUri : Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Instantiation of authentication
        auth = FirebaseAuth.getInstance()


        setUserInfo()

        //Function that handles button clicks
        btnClicks()
    }

    private fun setUserInfo() {
        et_profileEmail.setText(auth.currentUser?.email)
        et_profileUsername.setText(auth.currentUser?.displayName)
        iv_profileImage.setImageURI(auth.currentUser?.photoUrl)

        fileUri = auth.currentUser?.photoUrl
    }

    //Reference to all the buttons on the profile screen
    private fun btnClicks() {
        //signing the user our
        tv_profile_signOut.setOnClickListener {
            signOutUser()
        }

        //saving user info
        btn_profileSaveInfo.setOnClickListener {
            saveUserInfo()
        }

        //Setting or changing the profile pic
        iv_profileImage.setOnClickListener {
            selectImage()
        }
    }

    private fun saveUserInfo() {
        auth.currentUser?.let {
            val userName = et_profileUsername.text.toString()
            val userProfilePicture = fileUri
            val userEmail = et_profileEmail.text.toString()

            val update = UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .setPhotoUri(userProfilePicture)
                .build()

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    it.updateProfile(update).await()
                    it.updateEmail(userEmail)

                    withContext(Dispatchers.IO){
                        setUserInfo()

                        Toast.makeText(
                            requireActivity(),
                            "Profil mis à jour!",
                            Toast.LENGTH_SHORT
                            ).show()
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun signOutUser() {
        auth.signOut()

        val i = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(i)

        Toast.makeText(requireActivity(), "Vous êtes déconnecté", Toast.LENGTH_SHORT ).show()
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
                Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                //if the user  backs out of setting a profile pic
                //a toast informs them that the task was cancelled
                Toast.makeText(requireActivity(), "Tâche abandonnée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_profile, container, false)

        return itemView
    }

}