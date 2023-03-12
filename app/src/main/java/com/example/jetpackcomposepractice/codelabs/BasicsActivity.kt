package com.example.jetpackcomposepractice.codelabs

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposepractice.ui.theme.JetpackComposePracticeTheme

class BasicsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BasicsActivityScreen(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview
@Composable
fun BasicsActivityScreen(
    modifier: Modifier = Modifier, names: List<String> = listOf("Kotlin", "Python")
) {
    JetpackComposePracticeTheme {
        Surface(
            modifier = modifier, color = MaterialTheme.colors.background
        ) {
            LazyColumn {
                items(names) {
                    Greeting(it)
                }
            }
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val extraPadding by remember { mutableStateOf(if (expanded) 48.dp else 0.dp) } // Interesting how this can make a bug! 

    Surface(
        color = MaterialTheme.colors.primary, modifier = modifier
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Hello,")
                Text(text = "$name!", Modifier.padding(bottom = extraPadding))
            }
            ElevatedButton(onClick = { expanded = !expanded }) {
                Text(
                    text = if (expanded) "Show Less" else "Show more",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    }
}


@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    Greeting(name = "Android")
}