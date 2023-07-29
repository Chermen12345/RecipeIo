package com.example.recipeio.view.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentSignUpBinding
import com.example.recipeio.model.User
import com.example.recipeio.presenter.SignUpPresenterImpl
import com.example.recipeio.presenter.SignUpView
import com.example.recipeio.view.activities.HomeActivity

import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SignUpFragment : Fragment(), SignUpView {
    private lateinit var binding: FragmentSignUpBinding
    var uri: Uri ?= null
    private val presenter = SignUpPresenterImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO launch presenter and signUp
        presenter.attach(this)
        signUp()
        
        //TODO getting the uri from gallery
        getImage()

        //TODO navigation
        goToLogin()
    }

    //TODO fun for getting image

    //here we launch our intent for getting image
    private fun getImage(){
        binding.imgava.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            activityResult.launch(intent)
        }


    }
    //here is our intent which is getting the uri
    val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode==RESULT_OK){
            it.data?.data?.let { gotUri->
                uri = gotUri
                binding.imgava.setImageURI(gotUri)
            }
        }
    }

    //TODO signUp function
    private fun signUp(){
        binding.apply {
            btSignUp.setOnClickListener {
                val username = edUsernameSignUp.text.toString()
                val email = edEmailSignUp.text.toString()
                val pass = edPassSignUp.text.toString()
                val repeatpass = edPassRepeat.text.toString()
                val uri = uri
                val user = User(username,email,pass,repeatpass,uri.toString())
                lifecycleScope.launch{
                    async {
                        if (uri!=null){
                            presenter.signUp(user, uri)
                        }else{
                            message("please, choose the profile image")
                        }
                    }

                }

            }
        }
    }




    //TODO funcs for navigation

    //go to login fragment if user has an account
    private fun goToLogin(){
        binding.apply {
            tvGoToSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
    }

    //go to home activity if user made an account or logged in
    override fun goToHomeActivity() {
        val intent = Intent(context,HomeActivity::class.java)
        startActivity(intent)
    }

    override fun showProgress() {
        binding.prBar.apply {
            visibility = View.VISIBLE
        }
    }

    override fun message(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }


}