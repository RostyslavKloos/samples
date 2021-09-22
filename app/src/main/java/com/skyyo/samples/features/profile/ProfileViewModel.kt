package com.skyyo.samples.features.profile

import androidx.lifecycle.ViewModel
import com.skyyo.samples.application.EditProfileGraph
import com.skyyo.samples.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
) : ViewModel() {

    fun onBtnClick() {
        navigationDispatcher.emit { it.navigate(EditProfileGraph.route) }
    }
}
