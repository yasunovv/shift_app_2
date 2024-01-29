package com.yasunov.shiftapp2.person

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.request.ImageRequest
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun PersonScreen(
    personViewModel: PersonViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by personViewModel.appUiState.collectAsState()
    Crossfade(targetState = uiState, label = "") { it ->
        when (it) {
            is PersonUiState.Success -> PersonLoaded(
                modifier = modifier,
                userData = it.person
            )
            is PersonUiState.Loading -> PersonLoading()
            is PersonUiState.Error -> PersonError()
        }
    }
}

@Composable
private fun PersonError() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(Icons.Filled.Refresh, null)
        Text(text = "Ошибка в загрузке")

    }
}
@Composable
private fun PersonLoaded(
    modifier: Modifier,
    userData: PersonProfile
) {
    val context = LocalContext.current
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            CoilImage(
                imageRequest = {
                    ImageRequest.Builder(context)
                        .data(userData.picture)
                        .crossfade(true)
                        .build()
                },
                component = rememberImageComponent {
                    // shows a shimmering effect when loading an image.
                    +ShimmerPlugin(
                        baseColor = Color.LightGray,
                        highlightColor = Color.White
                    )
                },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .size(120.dp)
                    .border(
                        BorderStroke(2.dp, Color.LightGray),
                        RoundedCornerShape(20.dp)
                    )
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                userData.name,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Birthday: ${userData.birthday}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                userData.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier.clickable(
                    onClick = {
                        context.sendEmail(userData.email)
                    }
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Location: ${userData.location}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .clickable(
                        onClick = {
                            context.openLocationOnMap(userData.location)
                        }
                    )
                    .widthIn(0.dp, 300.dp),
                textAlign = TextAlign.Center,
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Geo: ${userData.geo}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier.clickable(
                    onClick = {
                        context.openGeoOnMap(userData.geo)
                    }
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Phone: ${userData.phone}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier.clickable(
                    onClick = {
                        context.callPhone(userData.phone)
                    }
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Login: ${userData.login}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Password: ${userData.password}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun PersonLoading() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val colorAnim by infiniteTransition.animateColor(
        initialValue = Color.LightGray,
        targetValue = Color.Gray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val settings = Modifier
        .background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    colorAnim,
                    colorAnim,
                    MaterialTheme.colorScheme.background,
                )
            )
        )
        .graphicsLayer {
            clip = true // don't forget this
            shape = CircleShape
        }
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = settings
                    .size(120.dp)

            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Box(
                modifier = settings
                    .height(22.dp)
                    .width(180.dp)
            )

        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Box(
                modifier = settings
                    .height(22.dp)
                    .width(160.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Box(
                modifier = settings
                    .height(22.dp)
                    .width(180.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
private fun Context.callPhone(tel: String) {
    val callIntent: Intent = Uri.fromParts("tel",tel.filter { it.isDigit() }, null).let { number ->
        Intent(Intent.ACTION_DIAL, number)
    }
    try {
        startActivity(callIntent)
    } catch (_: ActivityNotFoundException) {
        Log.e("Shift app", "Ошибка при открытие телефона")
    }
}
private fun Context.sendEmail(email: String){
    val callIntent: Intent = Uri.fromParts("mailto",email, null).let { i ->
        Intent(Intent.ACTION_SENDTO, i)
    }
    try {
        startActivity(callIntent)
    } catch (_: ActivityNotFoundException) {
        Log.e("Shift app", "Ошибка при открытие почты")
    }
}
private fun Context.openGeoOnMap(geo: String){
    val callIntent: Intent = Uri.parse("geo:${geo}").let { i ->
        Intent(Intent.ACTION_VIEW, i)
    }
    try {
        startActivity(callIntent)
    } catch (_: ActivityNotFoundException) {
        Log.e("Shift app", "Ошибка при открытие почты")
    }
}
private fun Context.openLocationOnMap(location: String){
    val callIntent: Intent = Uri.parse("geo:0,0?q=${location}").let { i ->
        Intent(Intent.ACTION_VIEW, i)
    }
    try {
        startActivity(callIntent)
    } catch (_: ActivityNotFoundException) {
        Log.e("Shift app", "Ошибка при открытие почты")
    }
}
@Preview(showSystemUi = true)
@Composable
private fun PersonScreenPreview() {
    PersonScreen(
        personViewModel = viewModel(),
        modifier = Modifier)
}

@Preview(showSystemUi = true)
@Composable
private fun PersonScreenLoadingPreview() {
    PersonLoading()
}
