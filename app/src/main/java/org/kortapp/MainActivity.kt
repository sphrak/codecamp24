package org.kortapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.fragment.compose.AndroidFragment
import androidx.fragment.compose.rememberFragmentState
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import org.kortapp.card.CardRoute
import org.kortapp.card.boundsTransform
import org.kortapp.carddetail.CardDetailFragment
import org.kortapp.carddetail.CardDetailScreen
import org.kortapp.ui.theme.KortAppTheme

sealed interface NavRoute

@Serializable
data object CardRoute : NavRoute

@Serializable
data class CardDetailRoute(
    val id: String
) : NavRoute

val LocalAnimatedContentScope: ProvidableCompositionLocal<AnimatedContentScope?> =
    compositionLocalOf { null }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope?> =
    compositionLocalOf { null }

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            KortAppTheme {
                SharedTransitionLayout(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val navController = rememberNavController()
                    CompositionLocalProvider(
                        LocalSharedTransitionScope provides this@SharedTransitionLayout
                    ) {
                        Scaffold(contentWindowInsets = WindowInsets.navigationBars) { p ->
                            NavHost(
                                modifier = Modifier.padding(p),
                                navController = navController,
                                startDestination = CardRoute
                            ) {
                                composable<CardRoute> {
                                    CompositionLocalProvider(
                                        LocalAnimatedContentScope provides this,
                                    ) {
                                        CardRoute(
                                            onCardClicked = {
                                                navController.navigate(CardDetailRoute(it))
                                            }
                                        )
                                    }
                                }

                                composable<CardDetailRoute> {
                                    CompositionLocalProvider(
                                        LocalAnimatedContentScope provides this,
                                    ) {

                                        val id = it.toRoute<CardDetailRoute>().id

                                        AndroidFragment<CardDetailFragment>(
                                            modifier = Modifier.sharedBounds(
                                                sharedContentState = rememberSharedContentState(key = id),
                                                animatedVisibilityScope =  LocalAnimatedContentScope.current!!,
                                                boundsTransform = boundsTransform,
                                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                            )
                                        )

                                        // CardDetailScreen()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label) }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object History : BottomNavItem("/transaction", Icons.Default.Home, "History")
    companion object {
        val entries: Set<BottomNavItem> = setOf(History)
    }
}