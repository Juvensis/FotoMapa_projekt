package pbs.edu.fotomapaprojekt.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
// Za chwilę stworzymy te ekrany, więc teraz mogą być na czerwono:
import pbs.edu.fotomapaprojekt.screens.home.HomeScreen
import pbs.edu.fotomapaprojekt.screens.details.DetailsScreen

@Composable
fun PhotoNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = PhotoScreens.HomeScreen.name
    ) {
        composable(PhotoScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }

        composable(
            route = PhotoScreens.DetailsScreen.name + "/{photoId}",
            arguments = listOf(navArgument(name = "photoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId")
            DetailsScreen(navController = navController, photoId = photoId)
        }
    }
}