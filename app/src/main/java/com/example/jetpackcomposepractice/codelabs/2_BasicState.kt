package com.example.jetpackcomposepractice.codelabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.versionedparcelable.VersionedParcelize
import com.example.jetpackcomposepractice.ui.theme.JetpackComposePracticeTheme

class BasicState : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetpackComposePracticeTheme {
                WellnessScreen()
            }
        }
    }
}

class WellnessViewModel : ViewModel() {

    private var _tasks = getWellnessTasks().toMutableStateList()

    val tasks: List<WellnessTask>
        get() = _tasks

    fun removeTask(task: WellnessTask) {
        _tasks.remove(task)
    }

    /**
     * Unlike `LiveData`, resetting the reference of the List does not seem to lead to
     * recomposition for `SnapShotStateList`.
     *
     * Perhaps somewhat obviously, sorting also leads to a recomposition! (I guess you need to
     * add and remove items to sort..)
     */
//    fun setChecked(task: WellnessTask, checked: Boolean) {
//        val changedTask = _tasks.find { it == task }
//
//        changedTask?.let {
//            _tasks = _tasks.map {
//                if (it != changedTask) it else changedTask.copy(
//                    isChecked = mutableStateOf(checked)
//                )
//            }.toMutableStateList()
//        }
//    }

    fun setChecked(task: WellnessTask, checked: Boolean) {
        val changedTask = _tasks.find { it == task }

        _tasks.remove(changedTask)
        changedTask?.let { _tasks.add(it.copy(isChecked = mutableStateOf(checked))) }
        _tasks.sortBy { it.id }
    }

    private fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # ${i + 1}") }
}

@Composable
fun WellnessScreen(
    wellnessViewModel: WellnessViewModel = viewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = androidx.compose.material.MaterialTheme.colors.background
    ) {
        Column {
            var showTask by remember { mutableStateOf(true) }
            WaterCounterHoistable(showTask, onClose = { showTask = false })

            WellnessTaskList(
                list = wellnessViewModel.tasks,
                onCloseTask = {
                    wellnessViewModel.removeTask(it)
                },
                onCheckedChanged = { task, isChecked ->
                    wellnessViewModel.setChecked(task, isChecked)
                }
            )
        }
    }

}

@VersionedParcelize
data class WellnessTask(
    val id: Int, val label: String, val isChecked: MutableState<Boolean> = mutableStateOf(false)
)

@Composable
fun WellnessTaskList(
    modifier: Modifier = Modifier,
    list: List<WellnessTask>,
    onCloseTask: (WellnessTask) -> Unit,
    onCheckedChanged: (WellnessTask, Boolean) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(list, key = { task -> task.id }) { task ->
            WellnessTaskItem(
                taskName = task.label,
                isChecked = task.isChecked.value,
                onClose = { onCloseTask(task) },
                onCheckedChanged = { isChecked -> onCheckedChanged(task, isChecked) }
            )
        }
    }
}

/**
 * This is made redundant by the `WellnessViewModel`.
 */

//@Preview(showBackground = true)
//@Composable
//fun WellnessTaskItem(
//    taskName: String = "Have you taken your 15 minute walk today?",
//    onClose: () -> Unit = {},
//) {
//    var isChecked by rememberSaveable { mutableStateOf(false) }
//
//    WellnessTaskItem(
//        taskName = taskName,
//        isChecked = isChecked,
//        onClose = onClose,
//        onCheckedChanged = { isChecked = !isChecked })
//}

@Preview(showBackground = true)
@Composable
fun WellnessTaskItem(
    taskName: String = "Have you taken your 15 minute walk today?",
    onClose: () -> Unit = {},
    isChecked: Boolean = false,
    onCheckedChanged: (Boolean) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .heightIn(64.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = taskName, modifier = Modifier.weight(1f))
        Checkbox(checked = isChecked, onCheckedChange = onCheckedChanged)
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, "Close")
        }
    }
}

@Composable
fun StatelessCounter(count: Int, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (count > 0) {
            Text(text = "You have had $count glasses", modifier = Modifier.padding(16.dp))
        }
        Button(onClick, Modifier.padding(top = 8.dp), enabled = count < 5) {
            Text(text = "Add one")
        }
    }
}

@Composable
fun StatefulCounter() {
    var count by rememberSaveable { mutableStateOf(0) }
    StatelessCounter(count = count, onClick = { count++ })
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun WaterCounter() {
    Column(modifier = Modifier.padding(16.dp)) {
        var count by rememberSaveable { mutableStateOf(0) }
        if (count > 0) {
            var showTask by remember { mutableStateOf(true) } // Doesn't work as intended!
            if (showTask) {
                WellnessTaskItem(
                    onClose = { showTask = false }
                )
            }
            Text(text = "You have had $count glasses", modifier = Modifier.padding(16.dp))
        }
        Row {
            Button(onClick = { count++ }, enabled = count < 5) {
                Text(text = "Add one")
            }
            Button(onClick = { count = 0 }) {
                Text(text = "Clear water count")
            }
        }

    }
}

@Composable
fun WaterCounterHoistable(showTask: Boolean, onClose: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        var count by rememberSaveable { mutableStateOf(0) }
        if (count > 0) {
            if (showTask) {
                WellnessTaskItem(onClose = onClose)
            }
            Text(text = "You have had $count glasses", modifier = Modifier.padding(16.dp))
        }
        Row {
            Button(onClick = { count++ }, enabled = count < 5) {
                Text(text = "Add one")
            }
            Button(onClick = { count = 0 }) {
                Text(text = "Clear water count")
            }
        }

    }
}