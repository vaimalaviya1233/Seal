package com.junkfood.seal.ui.page.playground

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.junkfood.seal.R
import com.junkfood.seal.ui.theme.SealTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class Loader(private val colorState: MutableState<Color>) {
    var color by colorState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SkeletonLoadersPlayground() {
    SealTheme {

        val containerColor = MaterialTheme.colorScheme.surface
        val loaderList = buildList {
            repeat(9) {
                add(Loader(mutableStateOf(containerColor)))
            }
        }
        val containerColorElevated = MaterialTheme.colorScheme.surfaceContainer

        val scope = rememberCoroutineScope()
        val interval = 75L
        val delay = 500L
        LaunchedEffect(Unit) {
            while (true) {
                loaderList.forEach {
                    launch {
                        it.color = containerColorElevated
                        delay(delay)
                        it.color = containerColor
                    }
                    delay(interval)
                }
                delay(loaderList.size * interval + delay)
            }
        }


        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Column(modifier = Modifier.padding(it)) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .padding(horizontal = 12.dp),
                    query = "",
                    placeholder = { Text(text = "Search music") },
                    onQueryChange = {},
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.seal),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(6.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimary)
                                .size(30.dp),
                        )
                    }
                ) {}
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 24.dp)
                        .height(12.dp)
                        .width(80.dp)
                        .clip(MaterialTheme.shapes.large)
                        .animatedBackground(loaderList[0].color),
                ) {}
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 8.dp)
                        .height(180.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .animatedBackground(loaderList[1].color),
                ) {}
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 12.dp)
                        .padding(top = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .align(Alignment.Top)
                            .height(12.dp)
                            .width(80.dp)
                            .clip(shape = MaterialTheme.shapes.large)
                            .animatedBackground(loaderList[2].color)

                    ) {}
                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(50.dp)
                            .clip(CircleShape)
                            .animatedBackground(loaderList[3].color),
                    ) {}
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .width(125.dp)
                            .clip(MaterialTheme.shapes.extraLarge)
                            .animatedBackground(loaderList[4].color),
                    ) {}
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 24.dp)
                        .height(12.dp)
                        .width(80.dp)
                        .clip(MaterialTheme.shapes.large)
                        .animatedBackground(loaderList[3].color),
                ) {}
                LazyRow(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) {
                        item {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(MaterialTheme.shapes.large)
                                        .animatedBackground(loaderList[3 + it].color),
                                ) {}
                                Box(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .height(12.dp)
                                        .width(80.dp)
                                        .clip(shape = MaterialTheme.shapes.large)
                                        .animatedBackground(loaderList[3 + it].color)

                                ) {}
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 24.dp)
                        .height(12.dp)
                        .width(80.dp)
                        .clip(MaterialTheme.shapes.large)
                        .animatedBackground(loaderList[5].color),
                ) {}
                LazyRow(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) {
                        item {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(MaterialTheme.shapes.large)
                                        .animatedBackground(loaderList[5 + it].color),
                                ) {}
                                Box(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .height(12.dp)
                                        .width(80.dp)
                                        .clip(shape = MaterialTheme.shapes.large)
                                        .animatedBackground(loaderList[5 + it].color)

                                ) {}
                            }
                        }
                    }
                }
            }
        }
    }
}

val easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)

@Composable
fun Modifier.animatedBackground(targetColor: Color): Modifier {
    val color by animateColorAsState(
        targetValue = targetColor,
        label = "",
        animationSpec = tween(400)
    )
    return this.background(color)
}

@Preview
@Composable
private fun Block() {
    SealTheme {

        val color1 = MaterialTheme.colorScheme.surfaceContainerLowest
        val color2 = MaterialTheme.colorScheme.surfaceContainerHighest

        var currentColor by remember {
            mutableStateOf(color1)
        }

        LaunchedEffect(Unit) {
            while (true) {
                currentColor = color2
                delay(500)
                currentColor = color1
                delay(500)
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(200.dp)
                .clip(shape = MaterialTheme.shapes.large)
                .animatedBackground(currentColor)

        ) {}
    }
}
