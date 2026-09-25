package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.models.Post

@Composable
fun ReelsScreen(
    posts: List<Post>,
    onLikeClick: (Long) -> Unit,
    onShareClick: (Post) -> Unit
) {
    val videoPosts = remember(posts) { posts.filter { it.isVideo } }

    if (videoPosts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.OndemandVideo,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "LoveNest Short Video Reels",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "No short video reels uploaded yet. Create a video post with 'Video Reel' option to see full-screen video feed!",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    } else {
        val pagerState = rememberPagerState(pageCount = { videoPosts.size })

        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val post = videoPosts[page]
            ReelVideoItem(
                post = post,
                onLikeClick = { onLikeClick(post.id) },
                onShareClick = { onShareClick(post) }
            )
        }
    }
}

@Composable
fun ReelVideoItem(
    post: Post,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("reel_item_${post.id}")
    ) {
        // Video Preview Background Image
        AsyncImage(
            model = post.mediaUrl,
            contentDescription = "Video Reel",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable { isPlaying = !isPlaying }
        )

        // Compression Badge
        Surface(
            color = Color.Black.copy(alpha = 0.6f),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Compress,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Fast Stream • 720p Compressed",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }

        // Play Pause Overlay
        if (!isPlaying) {
            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(72.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Video",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                )
            }
        }

        // Overlay Content (Bottom)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = post.authorAvatar,
                    contentDescription = post.authorName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = post.authorName,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = "🎶 Original Audio • LoveNest Sounds",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.caption,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = post.hashtags,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Action Buttons Column (Right Side)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Like
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like Reel",
                        tint = if (post.isLikedByMe) MaterialTheme.colorScheme.primary else Color.White
                    )
                }
                Text(
                    text = "${post.likesCount}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
            }

            // Comment
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ModeComment,
                        contentDescription = "Comments",
                        tint = Color.White
                    )
                }
                Text(
                    text = "${post.commentsCount}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
            }

            // Share to Partner
            IconButton(
                onClick = onShareClick,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Share Reel",
                    tint = Color.White
                )
            }
        }
    }
}
