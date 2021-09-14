package com.example.kirozh.myauthapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity(),
                    SignInFragment.Callbacks,
                    SignUpFragment.CallBacks,
                    EmailAndPasswordPass{

    private lateinit var mAuth:FirebaseAuth
    private var mEmail:String = ""
    private var mPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signInFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (signInFragment == null){
            val fragment = SignInFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

        mAuth = FirebaseAuth.getInstance()


    }

    /***
     * function from singInFragment callback
     */
    override fun registrationClicked() {
        val fragment = SignUpFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    /***
     * function from singUpFragment callback
     */
    override fun onSignUpClicked() {
        registerUser(mEmail, mPassword)
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    /***
     * function from singInFragment callback
     */
    override fun loginClicked() {
        logInUser(mEmail, mPassword)
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    /***
     * callback passes data from fragment to activity
     */
    override fun onDataPass(bundle: Bundle?) {

        mEmail = bundle?.get("email").toString()
        mPassword = bundle?.get("password").toString()
    }

    /***
     * sign in by verifying with FireBase
     */
    private fun logInUser(login: String, password:String){
        mAuth.signInWithEmailAndPassword(login, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCustomToken:success")

                }
                else{
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCustomToken:FFailure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    /***
     * sign up by adding user to FireBase
     */
    private fun registerUser(login: String, password:String ) {

        mAuth.createUserWithEmailAndPassword(login, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCustomToken:success")
                    val user = User(login, password)
                    FirebaseAuth.getInstance().currentUser?.let {
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(it.uid).setValue(user).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "Successfully signed")
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "signInWithCustomToken:failure", task.exception)
                                    Toast.makeText(
                                        this, "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
                else{
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCustomToken:FFailure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}