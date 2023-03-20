package com.example.jetpackcomposepractice.codelabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposepractice.R
import com.example.jetpackcomposepractice.ui.theme.JetpackComposePracticeTheme
import java.util.Locale

class BasicLayouts : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetpackComposePracticeTheme {
                BasicLayoutsScreen()
            }
        }
    }
}

@Composable
fun HomeSection(
    @StringRes title: Int,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier) {
        Text(
            stringResource(id = title).uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 8.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicLayoutsScreen() {
    var searchBarString by remember { mutableStateOf("") }

    JetpackComposePracticeTheme {
        Scaffold(bottomBar = {
            MyBottomNavigation()
        }) { padding ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                SearchBar(value = searchBarString, onValueChange = { s -> searchBarString = s })
                HomeSection(title = R.string.ab1_inversions, modifier = Modifier) {
                    AlignYourBodyRow()
                }
                HomeSection(title = R.string.fc1_short_mantras, modifier = Modifier) {
                    FavouriteCollectionsGrid()
                }
                Spacer(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun MyBottomNavigation(
    modifier: Modifier = Modifier,
    onFirstItemClicked: () -> Unit = {},
    onSecondItemClicked: () -> Unit = {},
) {
    var isFirstItemSelected by remember { mutableStateOf(true) }

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background, modifier = modifier
    ) {
        BottomNavigationItem(icon = {
            Icon(
                imageVector = Icons.Default.Spa, contentDescription = null
            )
        }, label = {
            Text(stringResource(id = R.string.bottom_navigation_home))
        }, selected = isFirstItemSelected, onClick = {
            isFirstItemSelected = true
            onFirstItemClicked()
        })
        BottomNavigationItem(icon = {
            Icon(
                imageVector = Icons.Default.AccountCircle, contentDescription = null
            )
        }, label = {
            Text(stringResource(id = R.string.bottom_navigation_profile))
        }, selected = !isFirstItemSelected, onClick = {
            onSecondItemClicked()
            isFirstItemSelected = false
        })
    }
}

@Composable
@Preview
fun BasicLayoutScreenPreview() {
    BasicLayoutsScreen()
}

@Composable
@Preview
fun FavouriteCollectionsGrid() {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier.height(120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(favoriteCollectionsData) {
            FavouriteCollectionCard(it.drawable, it.text)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AlignYourBodyRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(alignYourBodyData) {
            AlignYourBodyElement(
                drawableRes = it.drawable,
                stringRes = it.text,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
fun FavouriteCollectionCard(
    @DrawableRes drawable: Int = R.drawable.fc2_nature_meditations,
    @StringRes text: Int = R.string.fc2_nature_meditations,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .width(192.dp)
//                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(
                    id = drawable
                ),
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = stringResource(id = text),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = androidx.compose.material.MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
@Preview
fun SearchBar(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    TextField(value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        },
        modifier = modifier
            .heightIn(min = 56.dp)
            .fillMaxWidth(1f),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = { Text(stringResource(id = R.string.placeholder_search)) })
}

@Preview
@Composable
fun AlignYourBodyElement(
    @DrawableRes drawableRes: Int = R.drawable.ab1_inversions,
    @StringRes stringRes: Int = R.string.ab1_inversions,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = null,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = stringResource(id = stringRes),
            modifier = Modifier.paddingFromBaseline(24.dp, 8.dp),
            style = androidx.compose.material.MaterialTheme.typography.body2 // Says h3?
        )
    }
}


private val alignYourBodyData = listOf(
    R.drawable.ab1_inversions to R.string.ab1_inversions,
    R.drawable.ab2_quick_yoga to R.string.ab2_quick_yoga,
    R.drawable.ab3_stretching to R.string.ab3_stretching,
    R.drawable.ab4_tabata to R.string.ab4_tabata,
    R.drawable.ab5_hiit to R.string.ab5_hiit,
    R.drawable.ab6_pre_natal_yoga to R.string.ab6_pre_natal_yoga
).map { DrawableStringPair(it.first, it.second) }

private val favoriteCollectionsData = listOf(
    R.drawable.fc1_short_mantras to R.string.fc1_short_mantras,
    R.drawable.fc2_nature_meditations to R.string.fc2_nature_meditations,
    R.drawable.fc3_stress_and_anxiety to R.string.fc3_stress_and_anxiety,
    R.drawable.fc4_self_massage to R.string.fc4_self_massage,
    R.drawable.fc5_overwhelmed to R.string.fc5_overwhelmed,
    R.drawable.fc6_nightly_wind_down to R.string.fc6_nightly_wind_down
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int, @StringRes val text: Int
)