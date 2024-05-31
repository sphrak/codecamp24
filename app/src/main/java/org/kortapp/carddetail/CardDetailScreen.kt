package org.kortapp.carddetail

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.kortapp.LocalAnimatedContentScope
import org.kortapp.LocalSharedTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CardDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: CardDetailViewModel = hiltViewModel()
) {

    val state = viewModel.state

    val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(1400) }

    when (state) {
        is State.DisplayDetails -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            with(LocalSharedTransitionScope.current!!) {

                AsyncImage(
                    modifier = Modifier/*.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = state.debitCard.id),
                        animatedVisibilityScope =  LocalAnimatedContentScope.current!!,
                        boundsTransform = boundsTransform,
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                    )*/.fillMaxWidth(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.debitCard.imageUrl)
                        .crossfade(true)
                        .placeholderMemoryCacheKey(state.debitCard.id) //  same key as shared element key
                        .memoryCacheKey(state.debitCard.id) // same key as shared element key
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        State.Loading -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}