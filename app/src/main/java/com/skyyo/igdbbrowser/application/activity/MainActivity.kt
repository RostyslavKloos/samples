package com.skyyo.igdbbrowser.application.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skyyo.igdbbrowser.application.Screens
import com.skyyo.igdbbrowser.application.activity.cores.bottomBar.BottomBarCore
import com.skyyo.igdbbrowser.application.persistance.DataStoreManager
import com.skyyo.igdbbrowser.application.persistance.room.AppDatabase
import com.skyyo.igdbbrowser.extensions.log
import com.skyyo.igdbbrowser.theme.IgdbBrowserTheme
import com.skyyo.igdbbrowser.utils.eventDispatchers.NavigationDispatcher
import com.skyyo.igdbbrowser.utils.eventDispatchers.UnauthorizedEventDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher

    @Inject
    lateinit var unauthorizedEventDispatcher: UnauthorizedEventDispatcher

    @Inject
    lateinit var appDatabase: AppDatabase

    @ExperimentalPagerApi
    @ExperimentalMaterialApi
    @ExperimentalMaterialNavigationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEdgeToEdge()
        //these boys won't be hoisted in the template
        val drawerOrBottomBarScreens = listOf(
            Screens.DogFeed,
            Screens.Profile,
            Screens.Games,
        )
        val startDestination = when {
            //TODO measure async + splash delegation profit
            runBlocking { dataStoreManager.getAccessToken() } == null -> Screens.SignIn.route
            else -> Screens.DogFeed.route
        }
        val savedTheme = runBlocking { dataStoreManager.getAppTheme() } //TODO can be optimized

        setContent {
            IgdbBrowserTheme(savedTheme) {
                val systemUiController = rememberSystemUiController()
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    Surface(color = MaterialTheme.colors.background) {
                        val bottomSheetNavigator = rememberBottomSheetNavigator()
                        val navController = rememberNavController()
                        navController.navigatorProvider += bottomSheetNavigator

                        val lifecycleOwner = LocalLifecycleOwner.current
                        val navigationEvents = remember(navigationDispatcher.emitter, lifecycleOwner) {
                                navigationDispatcher.emitter.flowWithLifecycle(
                                    lifecycleOwner.lifecycle,
                                    Lifecycle.State.STARTED
                                )
                            }
                        val unauthorizedEvents = remember(unauthorizedEventDispatcher.emitter, lifecycleOwner) {
                                unauthorizedEventDispatcher.emitter.flowWithLifecycle(
                                    lifecycleOwner.lifecycle,
                                    Lifecycle.State.STARTED
                                )
                            }
                        LaunchedEffect(Unit) {
                            launch {
                                navigationEvents.collect { event -> event(navController) }
                            }
                            launch {
                                unauthorizedEvents.collect { onUnauthorizedEventReceived() }
                            }
                        }
                        // used only for the bottom sheet destinations
                        ModalBottomSheetLayout(bottomSheetNavigator) {
//                        SimpleCore(
//                            startDestination,
//                            navController
//                        )
                            BottomBarCore(
                                drawerOrBottomBarScreens,
                                startDestination,
                                navController,
                                systemUiController
                            )
//                    DrawerCore(
//                        drawerOrBottomBarScreens,
//                        startDestination,
//                        navController
//                    )
                        }
                    }
                }
            }
        }
    }

    @Suppress("GlobalCoroutineUsage")
    @OptIn(DelicateCoroutinesApi::class)
    private fun onUnauthorizedEventReceived() {
        if (isFinishing) return
        GlobalScope.launch(Dispatchers.IO) {
            appDatabase.clearAllTables()
            dataStoreManager.clearData()
        }
        finish()
        startActivity(intent)
    }

    private fun applyEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}

