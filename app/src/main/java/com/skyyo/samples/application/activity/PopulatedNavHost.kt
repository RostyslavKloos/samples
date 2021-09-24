package com.skyyo.samples.application.activity

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.pager.ExperimentalPagerApi
import com.skyyo.samples.application.Destination
import com.skyyo.samples.application.ProfileGraph
import com.skyyo.samples.features.appBarElevation.AppBarElevation
import com.skyyo.samples.features.autoscroll.AutoScrollScreen
import com.skyyo.samples.features.bottomSheets.BottomSheetScaffoldScreen
import com.skyyo.samples.features.bottomSheets.BottomSheetScreen
import com.skyyo.samples.features.bottomSheets.ModalBottomSheetScreen
import com.skyyo.samples.features.cameraX.CameraXScreen
import com.skyyo.samples.features.customView.CustomViewScreen
import com.skyyo.samples.features.forceTheme.ForceThemeScreen
import com.skyyo.samples.features.googleMap.GoogleMapScreen
import com.skyyo.samples.features.inputValidations.auto.InputValidationAutoScreen
import com.skyyo.samples.features.inputValidations.autoDebounce.InputValidationAutoDebounceScreen
import com.skyyo.samples.features.inputValidations.manual.InputValidationManualScreen
import com.skyyo.samples.features.navigateWithResult.simple.dogContacts.DogContactsScreen
import com.skyyo.samples.features.navigateWithResult.simple.dogDetails.DogDetailsScreen
import com.skyyo.samples.features.navigateWithResult.simple.dogFeed.DogFeedScreen
import com.skyyo.samples.features.navigateWithResult.withObject.catContacts.CatContactsScreen
import com.skyyo.samples.features.navigateWithResult.withObject.catDetails.CatDetailsScreen
import com.skyyo.samples.features.navigateWithResult.withObject.catFeed.CatFeedScreen
import com.skyyo.samples.features.otp.OtpScreen
import com.skyyo.samples.features.pagination.paging.CatsPagingScreen
import com.skyyo.samples.features.pagination.pagingWithDatabase.CatsPagingRoomScreen
import com.skyyo.samples.features.pagination.simple.CatsScreen
import com.skyyo.samples.features.pagination.simpleWithDatabase.CatsRoomScreen
import com.skyyo.samples.features.parallaxEffect.ParallaxEffectScreen
import com.skyyo.samples.features.sampleContainer.SampleContainerScreen
import com.skyyo.samples.features.scanQR.QrScreen
import com.skyyo.samples.features.scrollAnimation1.ScrollAnimation1Screen
import com.skyyo.samples.features.sharedViewModel.ProfileSharedViewModel
import com.skyyo.samples.features.sharedViewModel.confirmProfile.EditProfileConfirmationScreen
import com.skyyo.samples.features.sharedViewModel.editProfile.EditProfileScreen
import com.skyyo.samples.features.sharedViewModel.profile.ProfileScreen
import com.skyyo.samples.features.stickyHeaders.ListsScreen
import com.skyyo.samples.features.table.TableScreen
import com.skyyo.samples.features.viewPager.ViewPagerScreen

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class
)
@Composable
fun PopulatedNavHost(
    startDestination: String,
    innerPadding: PaddingValues,
    navController: NavHostController,
) = AnimatedNavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = Modifier.padding(innerPadding)
) {
    composable(Destination.SampleContainer.route) { SampleContainerScreen() }
    composable(Destination.Cats.route) { CatsScreen() }
    composable(Destination.CatsRoom.route) { CatsRoomScreen() }
    composable(Destination.CatsPaging.route) { CatsPagingScreen() }
    composable(Destination.CatsPagingRoom.route) { CatsPagingRoomScreen() }
    composable(Destination.Map.route) { GoogleMapScreen() }
    composable(Destination.ForceTheme.route) { ForceThemeScreen() }
    composable(Destination.CameraX.route) { CameraXScreen() }
    bottomSheet(Destination.BottomSheet.route) { BottomSheetScreen() }
    composable(Destination.ModalBottomSheet.route) { ModalBottomSheetScreen() }
    composable(Destination.BottomSheetScaffold.route) { BottomSheetScaffoldScreen() }
    composable(Destination.ViewPager.route) { ViewPagerScreen() }
    composable(Destination.StickyHeader.route) { ListsScreen() }
    composable(Destination.DogFeed.route) { DogFeedScreen() }
    composable(Destination.DogDetails.route) { DogDetailsScreen() }
    composable(Destination.DogContacts.route) { DogContactsScreen() }
    composable(Destination.CatFeed.route) { CatFeedScreen() }
    composable(Destination.CatDetails.route) { CatDetailsScreen() }
    composable(Destination.CatContacts.route) { CatContactsScreen() }
    composable(Destination.InputValidationManual.route,
        enterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(400))
        },
        exitTransition = { _, _ ->
            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(400))
        },
        popEnterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(400))
        },
        popExitTransition = { _, _ ->
            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(400))
        }) { InputValidationManualScreen() }
    composable(Destination.InputValidationAuto.route) { InputValidationAutoScreen() }
    composable(Destination.InputValidationDebounce.route) { InputValidationAutoDebounceScreen() }
    composable(Destination.AppBarElevation.route) { AppBarElevation() }
    composable(Destination.Otp.route) { OtpScreen() }
    composable(Destination.AutoScroll.route) { AutoScrollScreen() }
    composable(Destination.Table.route) { TableScreen() }
    composable(Destination.ParallaxEffect.route) { ParallaxEffectScreen() }
    composable(Destination.CustomView.route) { CustomViewScreen() }
    navigation(
        route = ProfileGraph.route,
        startDestination = ProfileGraph.Profile.route
    ) {
        composable(ProfileGraph.Profile.route) { ProfileScreen() }
        composable(ProfileGraph.EditProfile.route) {
            val rootGraphBackStackEntry = remember {
                navController.getBackStackEntry(ProfileGraph.Profile.route)
            }
            val navGraphViewModel: ProfileSharedViewModel = hiltViewModel(rootGraphBackStackEntry)
            EditProfileScreen(navGraphViewModel)
        }
        composable(ProfileGraph.ConfirmProfile.route) {
            val rootGraphBackStackEntry = remember {
                navController.getBackStackEntry(ProfileGraph.Profile.route)
            }
            val navGraphViewModel: ProfileSharedViewModel = hiltViewModel(rootGraphBackStackEntry)
            EditProfileConfirmationScreen(navGraphViewModel)
        }
    }
    composable(Destination.QrCodeScanning.route) { QrScreen() }
    composable(Destination.ScrollAnimation1.route) { ScrollAnimation1Screen() }
}