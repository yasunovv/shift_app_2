package com.yasunov.shiftapp2.users

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.request.ImageRequest
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    usersViewModel: UsersViewModel,
    onItemClicked: (Int) -> Unit
) {
    val uiState by usersViewModel.appUiState.collectAsState()
    Crossfade(targetState = uiState, label = "") { it ->
        when (it) {
            is UserUiState.Loading -> LoadingScreen()
            is UserUiState.Error -> ErrorScreen()
            is UserUiState.Success -> LoadedScreen(
                onItemClicked = onItemClicked,
                onUpdateButtonClicked = {
                    usersViewModel.resetUsers()
                },
                userList = it.users,
                modifier = modifier
            )
        }

    }
}

@Composable
private fun ErrorScreen() {
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
private fun Fab(
    modifier: Modifier,
    isVisibleBecauseOfScrolling: Boolean,
    onUpdateButtonClicked: () -> Unit,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisibleBecauseOfScrolling,
        enter = slideInVertically {
            with(density) { 40.dp.roundToPx() }
        } + fadeIn(),
        exit = fadeOut(
            animationSpec = keyframes {
                this.durationMillis = 120
            }
        )
    ) {
        ExtendedFloatingActionButton(
            text = { Text(text = "Обновить пользователей") },
            onClick = { onUpdateButtonClicked() },
            icon = { Icon(Icons.Filled.Refresh, null) }
        )
    }
}

@Composable
private fun LoadedScreen(
    modifier: Modifier = Modifier,
    onItemClicked: (Int) -> Unit,
    userList: List<User>,
    onUpdateButtonClicked: () -> Unit

) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val fabVisibility by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset >= listState.firstVisibleItemIndex
        }
    }
    Box {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
        ) {

            items(
                count = userList.size,
                key = {
                    userList[it].id
                },
                itemContent = { index ->
                    val data = userList[index]
                    ListItem(
                        headlineContent = {
                            Text(
                                data.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingContent = {
                            CoilImage(
                                imageRequest = {
                                    ImageRequest.Builder(context)
                                        .data(data.picture)
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
                                    .clip(RoundedCornerShape(30.dp))
                                    .size(42.dp)
                                    .border(
                                        BorderStroke(2.dp, Color.LightGray),
                                        RoundedCornerShape(30.dp)
                                    )
                            )
                        },
                        overlineContent = {
                            Text(
                                data.location,
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        supportingContent = {
                            Text(
                                data.phone,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        modifier = Modifier.clickable(
                            onClick = {
                                onItemClicked(data.id)
                            }
                        )
                    )
                }
            )

        }

        Fab(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp, top = 40.dp),
            isVisibleBecauseOfScrolling = fabVisibility,
            onUpdateButtonClicked = { onUpdateButtonClicked() }
        )


    }
}

@Composable
private fun LoadingScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy((-6).dp)
    ) {
        repeat(10) {
            UserLoad()
        }
    }
}

@Composable
private fun UserLoad() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val colorAnim by infiniteTransition.animateColor(
        initialValue = Color.LightGray,
        targetValue = Color.Gray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
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
        .clip(MaterialTheme.shapes.extraLarge)

    ListItem(
        headlineContent = {
            Box(
                modifier = settings
                    .height(8.dp)
            )

        },
        leadingContent = {

            Box(
                modifier = settings
                    .size(40.dp)

            ) {}
        },

        overlineContent = {
            Box(
                modifier = settings
                    .fillMaxWidth()
                    .height(8.dp)
            )
        },
        supportingContent = {
            Spacer(
                modifier = Modifier
                    .height(4.dp)
            )
            Box(
                modifier = settings
                    .fillMaxWidth()
                    .height(16.dp)
            )
        },

        )
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen()
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewUsersScreen() {
    UsersScreen(
        usersViewModel = viewModel(),
        onItemClicked = {}
    )
}

