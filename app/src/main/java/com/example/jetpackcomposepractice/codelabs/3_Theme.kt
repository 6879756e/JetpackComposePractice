/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetpackcomposepractice.codelabs

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposepractice.R
import com.example.jetpackcomposepractice.data.Post
import com.example.jetpackcomposepractice.data.PostRepo
import com.example.jetpackcomposepractice.ui.theme.JetnewsTheme
import java.util.Locale


class ThemeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                Home()
            }
        }
    }
}


@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Home - Dark")
@Preview(name = "Home")
fun Home(isNewTheme: Boolean = true) {
    val featured = remember { PostRepo.getFeaturedPost() }
    val posts = remember { PostRepo.getPosts() }

    if (isNewTheme) {
        JetnewsTheme {
            Scaffold(
                topBar = { AppBar() }
            ) { innerPadding ->
                LazyColumn(contentPadding = innerPadding) {
                    item {
                        Header(stringResource(R.string.top))
                    }
                    item {
                        FeaturedPost(
                            post = featured,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        Header(stringResource(R.string.popular))
                    }
                    items(posts) { post ->
                        PostItem(post = post)
                        Divider(startIndent = 72.dp)
                    }
                }
            }
        }
    } else {
        MaterialTheme {
            Scaffold(
                topBar = { AppBar() }
            ) { innerPadding ->
                LazyColumn(contentPadding = innerPadding) {
                    item {
                        Header(stringResource(R.string.top))
                    }
                    item {
                        FeaturedPost(
                            post = featured,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        Header(stringResource(R.string.popular))
                    }
                    items(posts) { post ->
                        PostItem(post = post)
                        Divider(startIndent = 72.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun AppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Rounded.Palette,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        },
        title = {
            Text(text = stringResource(R.string.app_title))
        },
    )

}

@Composable
fun Header(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
        contentColor = MaterialTheme.colors.primary,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true, name = "Night mode Header")
@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    JetnewsTheme {
        Header(text = "Popular")
    }
}

@Composable
fun FeaturedPost(
    post: Post,
    modifier: Modifier = Modifier
) {
//  elevation = (10.0.pow(2222) - 1).dp
    Card(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* onClick */ }
        ) {
            Image(
                painter = painterResource(post.imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .heightIn(min = 180.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            val padding = Modifier.padding(horizontal = 16.dp)
            Text(
                text = post.title,
                modifier = padding
            )
            Text(
                text = post.metadata.author.name,
                modifier = padding
            )
            PostMetadata(post, padding)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PostMetadata(
    post: Post,
    modifier: Modifier = Modifier
) {
    val divider = "  •  "
    val tagDivider = "  "
    val tagStyle = MaterialTheme.typography.overline.toSpanStyle().copy(
        background = MaterialTheme.colors.primarySurface.copy(alpha = 0.1f)
    )
    val text = buildAnnotatedString {
        append(post.metadata.date)
        append(divider)
        append(stringResource(R.string.read_time, post.metadata.readTimeMinutes))
        append(divider)
        post.tags.forEachIndexed { index, tag ->
            if (index != 0) {
                append(tagDivider)
            }
            withStyle(tagStyle) {
                append(" ${tag.uppercase(Locale.getDefault())} ")
            }
        }
    }
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = text,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .clickable { /* todo */ }
            .padding(vertical = 8.dp),
        icon = {
            Image(
                painter = painterResource(post.imageThumbId),
                modifier = Modifier.clip(shape = MaterialTheme.shapes.small),
                contentDescription = null
            )
        },
        text = {
            Text(text = post.title)
        },
        secondaryText = {
            PostMetadata(post)
        }
    )
}

@Preview("Post Item")
@Composable
private fun PostItemPreview() {
    val post = remember { PostRepo.getFeaturedPost() }
    Surface {
        PostItem(post = post)
    }
}

@Preview("Featured Post")
@Preview("Featured Post • Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FeaturedPostPreview() {
    val post = remember { PostRepo.getFeaturedPost() }
    JetnewsTheme {
        FeaturedPost(post = post)
    }
}

@Preview("Home")
@Composable
private fun HomePreview() {
    Home(false)
}

@Preview("HomePreviewWithNewTheme")
@Composable
private fun HomePreviewWithNewTheme() {
    Home()
    MaterialTheme.colors.primary
    TopAppBar {}
}
