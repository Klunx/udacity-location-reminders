package com.udacity.project4.authentication.view

import android.app.Application
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.utils.SingleLiveEvent
import com.udacity.project4.utils.asLiveData

class AuthenticationViewModel(
    app: Application,
) : BaseViewModel(app) {

    private val _triggerAuthenticationEvent = SingleLiveEvent<Unit>()
    val triggerAuthenticationEvent = _triggerAuthenticationEvent.asLiveData()

    private val _successLogin = SingleLiveEvent<Unit>()
    val successLogin = _successLogin.asLiveData()

    fun onLoginClicked() {
        _triggerAuthenticationEvent.postValue(Unit)
    }

    fun onLoginSuccess() {
        _successLogin.postValue(Unit)
    }
}