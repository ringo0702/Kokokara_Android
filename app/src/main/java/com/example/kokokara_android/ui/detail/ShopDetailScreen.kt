package com.example.kokokara_android.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kokokara_android.data.model.Shop

private val OrangeColor = Color(0xFFE8621A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailScreen(
    shop: Shop,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = shop.name,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ヘッダー画像
            AsyncImage(
                model = shop.photo.pc.large,
                contentDescription = shop.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 店舗名
                Text(
                    text = shop.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                // ジャンル
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🍴", fontSize = 16.sp, color = OrangeColor)
                    Text(
                        text = shop.genre.name,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // 予算
                DetailRow(icon = "¥", text = buildString {
                    append(shop.budget.average)
                    if (shop.budgetMemo.isNotEmpty()) append("　${shop.budgetMemo}")
                })

                // 営業時間
                DetailRow(icon = "🕐", text = shop.open)

                // アクセス
                DetailRow(icon = "📍", text = shop.access)

                Spacer(modifier = Modifier.height(8.dp))

                // 詳細情報セクション
                Text(
                    text = "詳細情報",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider()

                Spacer(modifier = Modifier.height(4.dp))

                // 店舗住所
                if (shop.address.isNotEmpty()) {
                    DetailInfoRow(label = "店舗住所：", value = shop.address)
                }

                // 料金備考
                if (shop.budgetMemo.isNotEmpty()) {
                    DetailInfoRow(label = "料金備考：", value = shop.budgetMemo)
                }

                // カード決済
                if (shop.card.isNotEmpty()) {
                    DetailInfoRow(label = "カード決済：", value = shop.card)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(icon: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = icon, fontSize = 16.sp, color = OrangeColor)
        Text(
            text = text,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
            lineHeight = 20.sp
        )
    }
}