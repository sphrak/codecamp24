package org.kortapp.card

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.kortapp.LocalAnimatedContentScope
import org.kortapp.LocalSharedTransitionScope


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
val boundsTransform = BoundsTransform { initialBounds, targetBounds ->
    keyframes {
        durationMillis = 1000
        initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
        targetBounds at 1000
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CardRoute(
    modifier: Modifier = Modifier,
    onCardClicked: (id: String) -> Unit,
    viewModel: CardViewModel = hiltViewModel()
) {

    val state: State = viewModel.state

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Kortapp")
                }
            )
        }
    ) { innerPadding ->
        when (state) {
            is State.DisplayCards -> Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = state.debitCards,
                        key = { it.id },
                        contentType = { it::class.java.simpleName }
                    ) {debitCard ->
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .animateItem(),
                            contentAlignment = Alignment.Center
                        ) {
                            with(LocalSharedTransitionScope.current!!) {
                                AsyncImage(
                                    modifier = Modifier
                                        .sharedBounds(
                                            sharedContentState = rememberSharedContentState(key = debitCard.id),
                                            animatedVisibilityScope = LocalAnimatedContentScope.current!!,
                                            boundsTransform = boundsTransform
                                        )
                                        .clickable {
                                            onCardClicked(debitCard.id)
                                        }
                                        .fillMaxWidth()
                                    ,
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(debitCard.imageUrl)
                                        .crossfade(true)
                                        .placeholderMemoryCacheKey(debitCard.id) //  same key as shared element key
                                        .memoryCacheKey(debitCard.id) // same key as shared element key
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                            Text(
                                modifier = Modifier.padding(start = 48.dp, bottom = 36.dp).align(Alignment.BottomStart),
                                text = "${debitCard.cardNumber}",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            State.Loading -> Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
