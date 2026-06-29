package com.example.kokokara_android.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kokokara_android.ui.search.SearchScreen

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

    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH
    ) {
        // 検索条件入力画面
        composable(Routes.SEARCH) {
            SearchScreen(
                onSearchClick = { radius, genres ->
                    val genresStr = genres.joinToString(",").ifEmpty { "none" }
                    navController.navigate("result_list/$radius/$genresStr")
                }
            )
        }

        // 検索結果リスト画面（ダミー）
        composable(
            route = Routes.RESULT_LIST,
            arguments = listOf(
                navArgument("radius") { type = NavType.IntType },
                navArgument("genres") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val radius = backStackEntry.arguments?.getInt("radius") ?: 1000
            val genres = backStackEntry.arguments?.getString("genres") ?: ""
            ResultListScreenDummy(
                radius = radius,
                genres = genres,
                onMapClick = {
                    navController.navigate("result_map/$radius/$genres")
                },
                onShopClick = { shopId ->
                    navController.navigate("shop_detail/$shopId")
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 検索結果マップ画面（ダミー）
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

        // 店舗詳細画面（ダミー）
        composable(
            route = Routes.SHOP_DETAIL,
            arguments = listOf(
                navArgument("shopId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val shopId = backStackEntry.arguments?.getString("shopId") ?: ""
            ShopDetailScreenDummy(
                shopId = shopId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}