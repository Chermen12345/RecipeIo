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
import com.example.recipeio.view.activities.HomeActivity
import com.example.recipeio.view.interfaces.SignUpView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SignUpFragment : Fragment(),SignUpView {
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

        presenter.attach(this)
        signUp()
        getImage()

        goToLogin()
    }

    private fun getImage(){
        binding.imgava.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            activityResult.launch(intent)
        }


    }
    val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode==RESULT_OK){
            it.data?.data?.let { gotUri->
                uri = gotUri
                binding.imgava.setImageURI(gotUri)
            }
        }
    }

    private fun signUp(){
        binding.apply {
            btSignUp.setOnClickListener {
                val email = edEmailSignUp.text.toString()
                val pass = edPassSignUp.text.toString()
                val repeatpass = edPassRepeat.text.toString()
                val uri = uri
                val user = User(email,pass,repeatpass,uri.toString())
                lifecycleScope.launch{
                    async {
                        presenter.signUp(user,uri)
                    }

                }

            }
        }
    }




    private fun goToLogin(){
        binding.apply {
            tvGoToSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
    }

    override fun goToHomeActivity() {
        val intent = Intent(context,HomeActivity::class.java)
        startActivity(intent)
    }

    override fun message(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }


}