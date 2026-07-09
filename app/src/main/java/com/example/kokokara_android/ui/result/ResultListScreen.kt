package com.example.kokokara_android.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kokokara_android.data.model.Shop

private val OrangeColor = Color(0xFFE8621A)
private const val API_KEY = "12e98313d33e043d" // ← ここにAPIキーを入力

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultListScreen(
    lat: Double,
    lng: Double,
    radius: Int,
    genreCodes: List<String>,
    onBackClick: () -> Unit,
    onMapClick: () -> Unit,
    onShopClick: (String) -> Unit,
    viewModel: ResultListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 初回検索
    LaunchedEffect(Unit) {
        viewModel.search(
            apiKey = API_KEY,
            lat = lat,
            lng = lng,
            radius = radius,
            genreCodes = genreCodes
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("検索結果リスト", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                        }
                    }
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { viewModel.onPrevPage(API_KEY) },
                        enabled = uiState.currentPage > 1
                    ) {
                        Text(
                            "前へ",
                            color = if (uiState.currentPage > 1) OrangeColor else Color.LightGray,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = "${uiState.currentPage} / ${uiState.totalPages}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    TextButton(
                        onClick = { viewModel.onNextPage(API_KEY) },
                        enabled = uiState.currentPage < uiState.totalPages
                    ) {
                        Text(
                            "次へ",
                            color = if (uiState.currentPage < uiState.totalPages) OrangeColor else Color.LightGray,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when {
                    // ローディング
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = OrangeColor
                        )
                    }
                    // エラー
                    uiState.error != null -> {
                        Text(
                            text = uiState.error!!,
                            color = Color.Red,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    // 結果なし
                    uiState.shops.isEmpty() -> {
                        Text(
                            text = "該当する店舗が見つかりませんでした",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                    // 結果あり
                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "検索結果：${uiState.totalCount}件",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            uiState.shops.forEach { shop ->
                                ShopCard(
                                    shop = shop,
                                    onClick = { onShopClick(shop.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // マップ切り替えボタン
        FloatingActionButton(
            onClick = onMapClick,
            containerColor = Color.White,
            contentColor = OrangeColor,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 80.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "マップで見る",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun ShopCard(
    shop: Shop,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = shop.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            if (shop.catchCopy.isNotEmpty()) {
                Text(
                    text = shop.catchCopy,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // 店舗画像
                AsyncImage(
                    model = shop.photo.pc.medium,
                    contentDescription = shop.name,
                    modifier = Modifier
                        .size(width = 110.dp, height = 90.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ShopInfoRow(icon = "🍴", text = shop.genre.name)
                    ShopInfoRow(icon = "¥", text = shop.budget.average)
                    ShopInfoRow(icon = "🕐", text = shop.open, maxLines = 2)
                }
            }
            Text(
                text = shop.access,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ShopInfoRow(
    icon: String,
    text: String,
    maxLines: Int = 1
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = icon, fontSize = 13.sp, color = OrangeColor)
        Text(
            text = text,
            fontSize = 13.sp,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}