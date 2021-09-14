package com.example.kirozh.myauthapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

/**
 * @author Kirill Ozhigin on 24.08.2021
 *
 * Fragment sign up
 *
 */
class SignUpFragment: Fragment() {

    interface CallBacks{
        fun onSignUpClicked()
    }

    private var emailAndPasswordPass: EmailAndPasswordPass? = null
    private lateinit var mAuth: FirebaseAuth
    private var callBacks:CallBacks? = null
    private lateinit var mLoginEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var mPasswordRepeatEditText: EditText
    private lateinit var mSignUpButton: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBacks = context as CallBacks?
        emailAndPasswordPass = context as EmailAndPasswordPass
    }

    override fun onCreate(savedInstaceState: Bundle?){
        super.onCreate(savedInstaceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        mLoginEditText = view.findViewById(R.id.signUpEditTextEmailAddress)
        mPasswordEditText = view.findViewById(R.id.signUpEditTextTextPassword)
        mPasswordRepeatEditText = view.findViewById(R.id.signUpEditTextTextPasswordRepeat)
        mSignUpButton = view.findViewById(R.id.signUpButton)

        return view
    }

    override fun onStart() {
        super.onStart()

        mSignUpButton.setOnClickListener {view->
            if (isInputValid(mLoginEditText.text.toString(),
                    mPasswordEditText.text.toString(),
                    mPasswordRepeatEditText.text.toString())){

                val bundle = Bundle()
                bundle.putString("email", mLoginEditText.text.toString() )

                bundle.putString("password", mPasswordEditText.text.toString() )

                emailAndPasswordPass?.onDataPass(bundle)

                callBacks?.onSignUpClicked()
            }
            else{
                Snackbar.make(view, "Could not sign up. Try again", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
        emailAndPasswordPass = null
    }

    private fun isEmailValid(charSequence: String): Boolean{
        return if (charSequence.isEmpty()) {

            false
        }
        else{
            android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()
        }

    }

    private fun isPasswordValid(charSequence: String):Boolean{
        return (charSequence.isNotEmpty())
    }

    private fun isInputValid(charSequence1: String,
                             charSequence2: String,
                             charSequence3: String): Boolean{

        return (isEmailValid(charSequence1)
                && isPasswordValid(charSequence2)
                && charSequence2 == charSequence3)
    }
}