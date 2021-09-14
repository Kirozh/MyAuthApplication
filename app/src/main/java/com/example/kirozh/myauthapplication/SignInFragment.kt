package com.example.kirozh.myauthapplication

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class SignInFragment : Fragment() {

    interface Callbacks{
        fun registrationClicked()
        fun loginClicked()
    }

    private lateinit var mAuth: FirebaseAuth
    private var emailAndPasswordPass: EmailAndPasswordPass? = null
    private var callbacks: Callbacks? = null
    lateinit var mLoginEditText: EditText
    lateinit var mPasswordEditText: EditText
    private lateinit var mRememberMeCheckBox: CheckBox
    private lateinit var mSignInButton: Button
    private lateinit var mSignUpButton: Button
    private lateinit var login: String
    private lateinit var password: String
    private var isRemember: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
        emailAndPasswordPass = context as EmailAndPasswordPass?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        mLoginEditText = view.findViewById(R.id.editTextTextEmailAddress)
        mPasswordEditText = view.findViewById(R.id.editTextTextPassword)
        mRememberMeCheckBox = view.findViewById(R.id.rememberMeCheckBox)
        mSignInButton = view.findViewById(R.id.logInButton)
        mSignUpButton = view.findViewById(R.id.registrationButton)


        mSignUpButton.setOnClickListener{
            callbacks?.registrationClicked()
        }
        return view
    }

    override fun onStart() {
        super.onStart()

        mLoginEditText.addTextChangedListener(MyTextWatcher())
        mPasswordEditText.addTextChangedListener(MyTextWatcher())

        mRememberMeCheckBox.apply{
            setOnCheckedChangeListener { _, isChecked -> isRemember = isChecked  }
        }

        mSignInButton.setOnClickListener {view->
            login = mLoginEditText.text.toString()
            password = mPasswordEditText.text.toString()
            if (isInputValid(login, password)){

                val bundle = Bundle()
                bundle.putString("email", login )
                bundle.putString("password", password )
                emailAndPasswordPass?.onDataPass(bundle)

                callbacks?.loginClicked()

            }
            else{
                Snackbar.make(view, "Could not sign in. Try again", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    override fun onDetach() {
        super.onDetach()
        callbacks = null
        emailAndPasswordPass = null
    }

    /***
     * Class that inspects input EditText
     */
    inner class MyTextWatcher: TextWatcher{

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val userEmail:String = mLoginEditText.text.toString()
            val userPassword:String = mPasswordEditText.text.toString()

            mSignInButton.isEnabled = (userEmail.isNotEmpty() && userPassword.isNotEmpty())
        }

        override fun afterTextChanged(s: Editable?) {
        }

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

    /***
     * function inspects validation of email and password
     */
    private fun isInputValid(charSequence1: String, charSequence2: String): Boolean{

        return (isEmailValid(charSequence1) && isPasswordValid(charSequence2))
    }

}