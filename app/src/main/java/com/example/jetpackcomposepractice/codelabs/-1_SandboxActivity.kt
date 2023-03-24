package com.example.jetpackcomposepractice.codelabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Locale

class SandboxActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SandboxScreen()
        }
    }

}

enum class MySpacing {
    SPACE_BETWEEN, SPACE_AROUND, SPACE_EVENLY
}

@Preview
@Composable
private fun SandboxScreen() {
    Row {
        val content = @Composable {
            Text("Hello World")
            Text("Hello")
        }

        for (spacing in MySpacing.values()) {
            MySpacingColumn(content = {
                Text(text = spacing.name.lowercase().capitalize(Locale.getDefault()))
                content()
            }, spaceType = spacing)
        }
    }
}

@Composable
fun MySpacingColumn(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    spaceType: MySpacing = MySpacing.SPACE_BETWEEN,
    spacing: Dp = 30.dp
) {
    val initialOffset = when (spaceType) {
        MySpacing.SPACE_BETWEEN -> 0.dp
        MySpacing.SPACE_AROUND -> spacing / 2
        MySpacing.SPACE_EVENLY -> spacing
    }

    Layout(
        content, modifier.border(
            width = 2.dp,
            color = Color.Black,
            shape = RectangleShape
        )
    ) { measurables, constraint ->
        val placeables = measurables.map { it.measure(constraint) }

        val inBetweenSpacingCount = placeables.size - 1

        val totalSpacing = (initialOffset * 2) + spacing * inBetweenSpacingCount.coerceAtLeast(0)

        val columnWidth = placeables.maxOf { it.width }
        val columnHeight = placeables.sumOf { it.height } + totalSpacing.roundToPx()

        layout(columnWidth + 4.dp.roundToPx(), columnHeight + 4.dp.roundToPx()) {
            var yOffset = (initialOffset + 2.dp).roundToPx()

            placeables.forEach {
                it.place(2.dp.roundToPx(), yOffset)
                yOffset += it.height + spacing.roundToPx()
            }
        }
    }
}