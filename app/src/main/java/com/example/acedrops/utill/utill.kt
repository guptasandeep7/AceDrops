package com.example.acedrops.utill

import android.content.Context
import com.example.acedrops.model.AccessTkn
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun validPass(password: String): String? {
    return when {
        password.length < 8 -> {
            "Password must contains 8 Characters"
        }
        !password.matches(".*[A-Z].*".toRegex()) && (!password.matches(".*[\$#%@&*/+_=?^!].*".toRegex())) -> {
            "Must contain 1 Special character and 1 upper case character (\$#%@&*/+_=?^!)"
        }
        !password.matches(".*[a-z].*".toRegex()) -> {
            "Must contain 1 Lower case character"
        }
        !password.matches(".*[\$#%@&*/+_=?^!].*".toRegex()) -> {
            "Must contain 1 Special character (\$#%@&*/+_=?^!)"
        }
        !password.matches(".*[A-Z].*".toRegex()) -> {
            "Must contain 1 upper case character"
        }
        else -> null
    }
}

suspend fun generateToken(context: Context) {
    val datastore = Datastore(context)
    val refToken = datastore.getUserDetails(Datastore.REF_TOKEN_KEY)!!
    val prevToken = datastore.getUserDetails(Datastore.ACCESS_TOKEN_KEY)!!
    var accessTkn:String? = null
        ServiceBuilder.buildService().generateToken(refreshToken = refToken)
            .enqueue(object : Callback<AccessTkn?> {
                override fun onResponse(call: Call<AccessTkn?>, response: Response<AccessTkn?>) {
                    if (response.isSuccessful) {
                        accessTkn = response.body()?.access_token.toString()
                    } else {
                        TODO("not yet decided")
                    }
                }

                override fun onFailure(call: Call<AccessTkn?>, t: Throwable) {
                    TODO("Not yet decided")
                }
            })
        datastore.saveUserDetails(Datastore.ACCESS_TOKEN_KEY, accessTkn?:prevToken)
}