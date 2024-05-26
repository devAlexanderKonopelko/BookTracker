package com.konopelko.booksgoals.presentation.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.presentation.common.component.navdrawer.NavDrawerItems
import com.konopelko.booksgoals.presentation.common.component.navdrawer.ui.NavDrawerContent
import com.konopelko.booksgoals.presentation.common.theme.BookTrackerTheme
import com.konopelko.booksgoals.presentation.common.theme.backgroundDark
import com.konopelko.booksgoals.presentation.common.theme.backgroundWhite
import com.konopelko.booksgoals.presentation.navigation.MainNavOption.InfoScreen
import com.konopelko.booksgoals.presentation.navigation.NavRoutes.MainRoute
import com.konopelko.booksgoals.presentation.navigation.appStartScreen
import com.konopelko.booksgoals.presentation.navigation.mainGraph
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookTrackerTheme {
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            title = {},
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            scaffoldState.drawerState.open()
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_top_menu),
                                        tint = backgroundWhite,
                                        contentDescription = ""
                                    )
                                }
                            },
                            actions = {},
                            backgroundColor = backgroundDark
                        )
                    },
                    drawerContent = {
                        NavDrawerContent(
                            drawerState = scaffoldState.drawerState,
                            menuItems = NavDrawerItems.drawerButtons,
                            defaultPick = appStartScreen,
                            onNavOptionClicked = { onUserPickedOption ->
                                navController.navigate(onUserPickedOption.name) {
                                    popUpTo(MainRoute.name)
                                }
                            },
                            onInfoPageClicked = {
                                navController.navigate(InfoScreen.name) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    },
                    content = {
                        NavHost(
                            navController = navController,
                            startDestination = MainRoute.name
                        ) {
                            mainGraph(navController = navController)
                        }
                    }
                )

            }
        }
    }
}