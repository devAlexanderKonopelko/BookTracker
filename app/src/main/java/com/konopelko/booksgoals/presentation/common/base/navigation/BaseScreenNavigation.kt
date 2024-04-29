package com.konopelko.booksgoals.presentation.common.base.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

abstract class BaseScreenNavigation<NavigationIntent, NavigationArgs>(
    protected val screenName: String,
    protected val defaultOptionalArgs: NavigationArgs,
    protected val optionalArgsKey: String,
    protected val optionalArgsType: NavType<NavigationArgs>
) {

    fun prepareScreenRoute(): String = "$screenName?$optionalArgsKey={$optionalArgsKey}"

    fun navComposable(builder: NavGraphBuilder) = builder.composable(
        route = prepareScreenRoute(),
        arguments = listOf(
            navArgument(optionalArgsKey) {
                type = optionalArgsType
                nullable = optionalArgsType.isNullableAllowed
            },
        )
    ) { backStackEntry ->
        val args = with(backStackEntry.arguments) {
            this?.get(optionalArgsKey) as? NavigationArgs
            ?: this?.getParcelable(optionalArgsKey)
            ?: backStackEntry.savedStateHandle.get<NavigationArgs>(optionalArgsKey)
            ?: defaultOptionalArgs
        }

        Log.e("BaseNavigation", "navigated to ${prepareScreenRoute()}, args: $args")

        ScreenComposable(
            onNavigate = ::onNavigate,
            args = args
        )
    }

    @Composable
    protected abstract fun ScreenComposable(
        onNavigate: (NavigationIntent) -> Unit,
        args: NavigationArgs
    )

    protected abstract fun onNavigate(intent: NavigationIntent)
}
