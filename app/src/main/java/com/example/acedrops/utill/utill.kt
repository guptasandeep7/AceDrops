package com.example.acedrops.utill

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.acedrops.model.AccessTkn
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.example.acedrops.view.dash.DashboardActivity.Companion.ACC_TOKEN
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
    var accessTkn: String? = null
    ServiceBuilder.buildService().generateToken(refreshToken = refToken)
        .enqueue(object : Callback<AccessTkn?> {
            override fun onResponse(call: Call<AccessTkn?>, response: Response<AccessTkn?>) {
                when {
                    response.isSuccessful -> {
                        accessTkn = response.body()?.access_token.toString()
                        ACC_TOKEN = accessTkn.toString()
                    }
                    response.code() == 402 -> {
                        Toast.makeText(
                            context.applicationContext,
                            "Please login again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        Log.w("generate token", "Response: code is ${response.code()}")
                        Log.w("generate token", "ref token is $refToken")
                        Log.w("generate token", "access token is $accessTkn")
                    }
                }
            }

            override fun onFailure(call: Call<AccessTkn?>, t: Throwable) {
                Toast.makeText(context, " Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    ACC_TOKEN?.let { datastore.saveUserDetails(Datastore.ACCESS_TOKEN_KEY, it) }
}