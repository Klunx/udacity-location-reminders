package com.udacity.project4.locationreminders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.project4.utils.asLiveData

class ReminderViewModel() : ViewModel() {

    private var _locationPermission = MutableLiveData<Boolean>()
    val locationPermission = _locationPermission.asLiveData()

    val checkPermission = MutableLiveData<Boolean>()

    fun onLocationPermissionRequest() {
        checkPermission.value = true
    }

    fun locationPermissionIsGranted(permission: Boolean) {
        _locationPermission.value = permission
    }
}