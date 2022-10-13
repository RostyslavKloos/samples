package com.skyyo.samples.features.navigationCores.tab1

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.skyyo.samples.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Tab1ViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val handle: SavedStateHandle,
) : ViewModel()
