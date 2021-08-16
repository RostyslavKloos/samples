package com.skyyo.igdbbrowser.features.launches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyo.igdbbrowser.application.repositories.games.GamesRepository
import com.skyyo.igdbbrowser.application.repositories.games.PastLaunchesResult
import com.skyyo.igdbbrowser.utils.eventDispatchers.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val PAGE_LIMIT = 20

@HiltViewModel
class LaunchesListViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val handle: SavedStateHandle,
    private val gamesRepository: GamesRepository
) : ViewModel() {

//    val latestLaunch = launchesRepository.observeLatestLaunch(LocalDateTime.now(), viewModelScope)
    val games = gamesRepository.observePastLaunches(viewModelScope)
    val events = Channel<PastLaunchesListEvent>(Channel.UNLIMITED)

    var isFetchingAllowed = true
    private var itemOffset = 0

    init {
       // getLatestLaunch()
        getGames()
    }

//    private fun getLatestLaunch() {
//        viewModelScope.launch(Dispatchers.IO) {
//            if (launchesRepository.getLatestLaunch() == LatestLaunchResult.NetworkError) {
//                events.send(PastLaunchesListEvent.NetworkError)
//            }
//        }
//    }

    fun getGames(isFirstPage: Boolean = false) {
        isFetchingAllowed = false
        if (isFirstPage) itemOffset = 0
        viewModelScope.launch(Dispatchers.IO) {
            when (gamesRepository.getPastLaunches(PAGE_LIMIT, itemOffset)) {
                PastLaunchesResult.NetworkError -> {
                    itemOffset = 0
                    isFetchingAllowed = true
                    events.send(PastLaunchesListEvent.NetworkError)
                }
                PastLaunchesResult.Success -> {
                    itemOffset += PAGE_LIMIT
                    isFetchingAllowed = true
                }
                PastLaunchesResult.LastPageReached -> {
                    isFetchingAllowed = false
                }
            }
        }
    }

    fun onSwipeToRefresh() {
//        getLatestLaunch()
        getGames(true)
    }
}
