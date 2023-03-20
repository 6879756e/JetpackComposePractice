package com.example.jetpackcomposepractice.codelabs

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposepractice.ui.theme.JetpackComposePracticeTheme

class BasicsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetpackComposePracticeTheme {
                BasicsActivityScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    var shouldShowOnBoarding by remember { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnBoarding) {
            OnboardingPreview { shouldShowOnBoarding = !shouldShowOnBoarding }
        } else {
            Greetings()
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun MyAppPreview() {
    JetpackComposePracticeTheme {
        MyApp(Modifier.fillMaxSize())
    }
}

@Composable
fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1) { "$it" }
) {
    LazyColumn(modifier = modifier.padding(4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
            Spacer(modifier = modifier.padding(2.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun GreetingsPreview() {
    JetpackComposePracticeTheme {
        Greetings()
    }
}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview(onContinueClicked: () -> Unit = {}) {
    JetpackComposePracticeTheme {
        OnboardingScreen(onContinueClicked = onContinueClicked)
    }
}

@Preview
@Composable
fun BasicsActivityScreen(
    modifier: Modifier = Modifier, names: List<String> = List(1000) { "$it" }
) {
    JetpackComposePracticeTheme {
        Surface {
            LazyColumn {
                items(names) {
                    Greeting(
                        it,
                        modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.padding(4.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .animateContentSize(
                    animationSpec = spring(
                        Spring.DampingRatioHighBouncy,
                        Spring.StiffnessLow
                    )
                )
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Hello,")
                Text(
                    text = "$name!",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                if (expanded) Text(text = "loremIpsum")
            }
            Button(onClick = { expanded = !expanded }) {
                if (expanded)
                    Icon(
                        imageVector = Icons.Filled.ExpandLess,
                        contentDescription = "Show Less"
                    )
                else
                    Icon(
                        imageVector = Icons.Filled.ExpandMore,
                        contentDescription = "Show More"
                    )
            }
        }
    }
}


@Preview(uiMode = UI_MODE_NIGHT_NO, name = "Light Mode")
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Night Mode")
@Composable
fun GreetingPreview() {
    JetpackComposePracticeTheme {
        Greeting(name = "Android")
    }
}