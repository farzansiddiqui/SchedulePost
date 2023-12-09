package com.siddiqui.schedulepost.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.elevation.SurfaceColors
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.adapter.ViewPagerAdapter
import com.siddiqui.schedulepost.databinding.ActivityListPostBinding
import com.siddiqui.schedulepost.view.MainActivity.Companion.TAG
import java.util.Arrays

class ListPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPostBinding
    private lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth
    private val EMAIL = "email"
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityListPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.navigationBarColor = SurfaceColors.getColorForElevation(this,0f)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<MarginLayoutParams> {
                // Push all content below the top system status bar
                topMargin = insets.top
                bottomMargin = insets.bottom

            }
            WindowInsetsCompat.CONSUMED
        }

// Initialize Firebase Auth
        auth = Firebase.auth

        FacebookSdk.sdkInitialize(applicationContext)


        // using this adapter for add the tab in that layout
        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.post_schedule)
                    tab.icon = AppCompatResources.getDrawable(
                        this,
                        com.facebook.R.drawable.com_facebook_button_icon
                    )
                }

                1 -> {
                    tab.text = getString(R.string.scheduled_post)
                    tab.icon =
                        AppCompatResources.getDrawable(this, R.drawable.baseline_calendar_month_24)
                }
            }
        }.attach()


        callbackManager = CallbackManager.Factory.create()
        binding.facebook.facebookLoginBtn.setReadPermissions(listOf(EMAIL))

        binding.facebook.facebookLoginBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d(TAG, "facebook:onSuccess: ${loginResult ?: ""}")
                if (loginResult != null){
                    handleFacebookAccessToken(loginResult.accessToken)
                }

            }

            override fun onCancel() {
                Log.d(TAG, "facebook : onCancel: ")
                AccessToken.setCurrentAccessToken(null)
                LoginManager.getInstance().logOut()

            }

            override fun onError(error: FacebookException?) {
                Log.d(TAG, "facebook : onError:",error)
            }
        })


    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

}