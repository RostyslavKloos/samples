package com.skyyo.samples.features.navigateWithResult

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyo.samples.application.DogDetailsGraph
import com.skyyo.samples.application.models.Dog
import com.skyyo.samples.extensions.navigateWithObject
import com.skyyo.samples.extensions.observeNavigationResult
import com.skyyo.samples.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogFeedViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val handle: SavedStateHandle,
) : ViewModel() {

    //this is the result from DogContacts composable
//    val dogStatus = MutableLiveData<String>()
    val dog = MutableLiveData<Dog>()

    init {
        observeDogStatusResult()
    }

    private fun observeDogStatusResult() {
        navigationDispatcher.emit {
            viewModelScope.launch {
//                it.observeNavigationResult<String>("dogStatus")?.collect {
//                    dogStatus.value = it
//                }
                it.observeNavigationResult<Dog>("dog")?.collect {
                    dog.value = it
                }
            }
        }
    }

    fun goDogAdopt(dogId: String) = navigationDispatcher.emit {
        it.navigate(DogDetailsGraph.DogDetails.createRoute(dogId))
    }

    fun goDogAdoptWithObject(dog: Dog) = navigationDispatcher.emit {
        it.navigateWithObject(
            route = DogDetailsGraph.DogDetails.route,
            arguments = bundleOf("dog" to dog)
        )
    }

}
