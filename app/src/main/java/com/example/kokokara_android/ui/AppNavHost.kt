package com.example.kokokara_android.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kokokara_android.ui.search.SearchScreen
import com.example.kokokara_android.ui.result.ResultListScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kokokara_android.ui.result.ResultListViewModel
import com.example.kokokara_android.ui.detail.ShopDetailScreen


// ルート定数
object Routes {
    const val SEARCH = "search"
    const val RESULT_LIST = "result_list/{radius}/{genres}"
    const val RESULT_MAP = "result_map/{radius}/{genres}"
    const val SHOP_DETAIL = "shop_detail/{shopId}"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    // ViewModelをNavHost全体で共有
    val resultListViewModel: ResultListViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH
    ) {
        composable(Routes.SEARCH) {
            SearchScreen(
                onSearchClick = { radius, genres ->
                    val genresStr = genres.joinToString(",").ifEmpty { "none" }
                    navController.navigate("result_list/$radius/$genresStr")
                }
            )
        }

        composable(
            route = Routes.RESULT_LIST,
            arguments = listOf(
                navArgument("radius") { type = NavType.IntType },
                navArgument("genres") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val radius = backStackEntry.arguments?.getInt("radius") ?: 1000
            val genresStr = backStackEntry.arguments?.getString("genres") ?: ""
            val genres = if (genresStr == "none") emptyList() else genresStr.split(",")
            ResultListScreen(
                lat = 34.7024,
                lng = 135.4959,
                radius = radius,
                genreCodes = genres,
                onBackClick = { navController.popBackStack() },
                onMapClick = {
                    navController.navigate("result_map/$radius/$genresStr")
                },
                onShopClick = { shopId ->
                    navController.navigate("shop_detail/$shopId")
                },
                viewModel = resultListViewModel
            )
        }

        composable(
            route = Routes.RESULT_MAP,
            arguments = listOf(
                navArgument("radius") { type = NavType.IntType },
                navArgument("genres") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val radius = backStackEntry.arguments?.getInt("radius") ?: 1000
            val genres = backStackEntry.arguments?.getString("genres") ?: ""
            ResultMapScreenDummy(
                radius = radius,
                genres = genres,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.SHOP_DETAIL,
            arguments = listOf(
                navArgument("shopId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val shopId = backStackEntry.arguments?.getString("shopId") ?: ""
            val shop = resultListViewModel.getShopById(shopId)
            if (shop != null) {
                ShopDetailScreen(
                    shop = shop,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}