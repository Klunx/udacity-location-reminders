package com.udacity.project4.authentication.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentAuthenticationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationFragment : BaseFragment() {

    companion object {
        const val TAG = "AuthenticationFragment"
        const val SIGN_IN_REQUEST_CODE = 1001
    }

    override val _viewModel: AuthenticationViewModel by viewModel()
    private lateinit var binding: FragmentAuthenticationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_authentication, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        binding.login.setOnClickListener {
            _viewModel.onLoginClicked()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
                _viewModel.onLoginSuccess()
            } else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun setupObservers() {
        _viewModel.triggerAuthenticationEvent.observeForever {
            launchSignInFlow()
        }

        _viewModel.successLogin.observeForever {
            navigateToReminderList()
        }

        _viewModel.authenticationState.observe(viewLifecycleOwner) { authenticationState ->
            when (authenticationState) {
                BaseViewModel.AuthenticationState.AUTHENTICATED -> {
                    navigateToReminderList()
                }
                else -> {}
            }
        }
    }

    private fun navigateToReminderList() {
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(AuthenticationFragmentDirections.navActionToReminderList())
        )
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val authenticationLayout = AuthMethodPickerLayout.Builder(R.layout.authorization_layout)
            .setGoogleButtonId(R.id.google_login_button)
            .setEmailButtonId(R.id.email_login_button)
            .build()

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme)
                .setAuthMethodPickerLayout(authenticationLayout)
                .build(),
            SIGN_IN_REQUEST_CODE
        )
    }
}