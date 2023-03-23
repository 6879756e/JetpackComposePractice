/*
 * Copyright 2020 Google LLC
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

package com.google.samples.apps.sunflower.plantdetail

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

@Composable
fun PlantDetailDescription(viewModel: PlantDetailViewModel) {
    val plant by viewModel.plant.observeAsState()

    plant?.let {
        PlantDetailContent(it)
    }
}

@Composable
fun PlantDescription(description: String) {
    val htmlDescription by remember {
        mutableStateOf(HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT))
    }

    AndroidView(factory = { content -> TextView(content) }) { tv ->
        tv.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = htmlDescription
        }
    }
}

@Preview
@Composable
fun PlantDescriptionPreview() {
    MdcTheme {
        PlantDescription("HTML<br><br>description")
    }
}

@Composable
private fun PlantDetailContent(plant: Plant) {
    Surface {
        Column(Modifier.padding(dimensionResource(id = R.dimen.margin_normal))) {
            PlantName(name = plant.name)
            PlantHeader()
            PlantWatering(wateringInterval = plant.wateringInterval)
            PlantDescription(description = plant.description)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlantHeaderWithWateringInformation() {
    Column {
        PlantHeader()
        PlantWatering(wateringInterval = 7)
    }
}


@Composable
fun PlantHeader(title: String = stringResource(id = R.string.watering_needs_prefix)) {
    Text(
        text = title,
        color = MaterialTheme.colors.primaryVariant,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.margin_small),
                end = dimensionResource(id = R.dimen.margin_small),
                top = dimensionResource(id = R.dimen.margin_normal)

            )
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}


@Composable
fun PlantWatering(wateringInterval: Int) {
    val quantityString = LocalContext.current.resources.getQuantityString(
        R.plurals.watering_needs_suffix, wateringInterval, wateringInterval
    )

    Text(
        text = quantityString,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.margin_small))
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Preview
@Composable
fun PlantDetailContentPreview() {
    val plant = Plant("id", "Apple", "HTML<br><br>description", 3, 30, "")

    MdcTheme {
        PlantDetailContent(plant)
    }
}

@Composable
fun PlantName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.margin_small))
            .wrapContentWidth(Alignment.CenterHorizontally),
    )
}

@Preview(showBackground = true)
@Composable
fun PlantNamePreview(name: String = "Apple") {
    PlantName(name = name)
}

