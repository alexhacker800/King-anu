package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.models.Post
import com.example.data.models.Story

@Composable
fun HomeScreen(
    posts: List<Post>,
    stories: List<Story>,
    onLikeClick: (Long) -> Unit,
    onDeletePost: (Long) -> Unit,
    onNavigateToStories: () -> Unit,
    onNavigateToUpload: () -> Unit,
    onShareToChat: (Post) -> Unit
) {
    var reportingPostId by remember { mutableStateOf<Long?>(null) }
    var selectedCommentPost by remember { mutableStateOf<Post?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stories Row Header
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Stories",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = onNavigateToStories) {
                        Text("View All")
                    }
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        // Add Story item
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { onNavigateToUpload() }
                                .testTag("add_story_item")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Story",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your Story",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                    items(stories) { story ->
                        StoryRingItem(story = story, onClick = onNavigateToStories)
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Couples Feed",
                    style = MaterialTheme.typography.titleLarge
                )
                AssistChip(
                    onClick = onNavigateToUpload,
                    label = { Text("New Post") },
                    leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) }
                )
            }
        }

        // Posts List
        if (posts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No posts yet",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Share romantic photos, videos, and love notes with your partner and friends!",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(posts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    onLikeClick = { onLikeClick(post.id) },
                    onCommentClick = { selectedCommentPost = post },
                    onShareClick = { onShareToChat(post) },
                    onDeleteClick = { onDeletePost(post.id) },
                    onReportClick = { reportingPostId = post.id }
                )
            }
        }
    }

    // Report Dialog
    reportingPostId?.let { postId ->
        AlertDialog(
            onDismissRequest = { reportingPostId = null },
            title = { Text("Report Post") },
            text = { Text("Are you sure you want to report this post for inappropriate content? Our moderation system will review it within 24 hours.") },
            confirmButton = {
                Button(onClick = { reportingPostId = null }) {
                    Text("Report")
                }
            },
            dismissButton = {
                TextButton(onClick = { reportingPostId = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Comments Modal
    selectedCommentPost?.let { post ->
        AlertDialog(
            onDismissRequest = { selectedCommentPost = null },
            title = { Text("Comments (${post.commentsCount})") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("❤️ Aarav: Beautiful memories together!", style = MaterialTheme.typography.bodyMedium)
                    Text("💖 Priya: Looking so happy you two!", style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Add a love comment...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { selectedCommentPost = null }) {
                    Text("Post Comment")
                }
            }
        )
    }
}

@Composable
fun StoryRingItem(story: Story, onClick: () -> Unit) {
    val ringGradient = Brush.sweepGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.primary
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag("story_item_${story.id}")
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .border(3.dp, ringGradient, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
        ) {
            AsyncImage(
                model = story.authorAvatar,
                contentDescription = story.authorName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = story.authorName,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun PostCard(
    post: Post,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("post_card_${post.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Post Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = post.authorAvatar,
                        contentDescription = post.authorName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = post.authorName,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.extraSmall
                            ) {
                                Text(
                                    text = post.privacy,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                            if (post.isVideo) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "• Reel (${post.videoDuration})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Options Menu
                Row {
                    if (post.isUserPost) {
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete Post",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    } else {
                        IconButton(onClick = onReportClick) {
                            Icon(
                                imageVector = Icons.Outlined.Report,
                                contentDescription = "Report Post",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Media Preview
            if (post.mediaUrl.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(Color.Black)
                ) {
                    AsyncImage(
                        model = post.mediaUrl,
                        contentDescription = "Post Media",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (post.isVideo) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = CircleShape,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Video",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }

            // Action Buttons Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onLikeClick() }
                    ) {
                        Icon(
                            imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLikedByMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.likesCount}",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onCommentClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ModeComment,
                            contentDescription = "Comment"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.commentsCount}",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    IconButton(onClick = onShareClick) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = "Share to Partner Chat"
                        )
                    }
                }

                Text(
                    text = "2 hours ago",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Caption & Hashtags
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                Text(
                    text = post.caption,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (post.hashtags.isNotBlank()) {
                    Text(
                        text = post.hashtags,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
