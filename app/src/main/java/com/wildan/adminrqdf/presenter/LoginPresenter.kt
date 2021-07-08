package com.wildan.adminrqdf.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.utils.Validation.Companion.validateEmail
import com.wildan.adminrqdf.utils.Validation.Companion.validateFields
import com.wildan.adminrqdf.view.LoginView

class LoginPresenter(
    private val context: Context,
    private val view: LoginView.View
) : LoginView.Presenter {

    override fun requestLogin(email: String, password: String) {
        if (validateFields(email) || validateFields(password)) {
            view.handleResponse(context.getString(R.string.email_password_null))
        } else if (validateEmail(email)) {
            view.handleResponse(context.getString(R.string.email_not_valid))
        } else {
            if (email == "adminrqc@gmail.com") {
                view.showProgressBar()
                login(email, password)
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_user_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    
                    Log.i("LoginActivity", "Login Success")

                } else {
                    view.hideProgressBar()
                    view.handleResponse((task.exception as FirebaseAuthException).errorCode)
                }
            }
    }
}