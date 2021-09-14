package com.example.kirozh.myauthapplication

/**
 * @author Kirill Ozhigin on 25.08.2021
 */
class User( login: String="", password:String="") {

    var mLogin:String=""
    var mPassword: String = ""
    init {
        mLogin = login
        mPassword = password
    }
}
