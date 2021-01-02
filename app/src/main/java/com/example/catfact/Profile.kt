package com.example.catfact


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.iv_profileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class Profile : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var fileUri : Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setUserInfo()

        btnClicks()

    }

    private fun setUserInfo() {
        et_profileEmail.setText(auth.currentUser?.email)
        et_profileUsername.setText(auth.currentUser?.displayName)
        iv_profileImage.setImageURI(auth.currentUser?.photoUrl)

        fileUri = auth.currentUser?.photoUrl
    }

    private fun btnClicks() {
        tv_profile_signOut.setOnClickListener {
            signOutUser()
        }

        btn_profileSaveInfo.setOnClickListener {
            saveUserInfo()
        }

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
                            "Profile successfully updated !",
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

        Toast.makeText(requireActivity(), "Successfully signed out!", Toast.LENGTH_SHORT ).show()

    }


    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                fileUri = data?.data
                iv_profileImage.setImageURI(fileUri)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireActivity(), "Task cancelled", Toast.LENGTH_SHORT).show()
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