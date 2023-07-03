package com.app.skyss_companion.view.planner.location_search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.skyss_companion.R
import com.app.skyss_companion.model.geocode.GeocodingFeature
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


/*@Composable
fun LocationSearchComposeDialogPreview(
) {
    val data = GeocodingFeaturesSampleData.getSample1()
    val isLoading = true
    val searchText by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = {}
    ) {
        Column {
            ButtonRow {}
            SearchField(searchText) {}
            if (isLoading) LoadingIndicator() else FeaturesList(properties = data) {}
        }
    }
}*/


@Composable
fun LocationSearchComposeDialog(
    onTextUpdated: (t: String) -> Unit,
    onPropertySelected: (GeocodingFeature) -> Unit,
    onDialogDismissed: () -> Unit,
    lastUsedGeocodingFeatures: List<GeocodingFeature>?,
    currentGeocodingFeatures: List<GeocodingFeature>?,
    isLoadingGeocodingFeatures: Boolean,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        LocationSearchBody(
            lastUsedFeatures = lastUsedGeocodingFeatures,
            currentFeatures = currentGeocodingFeatures,
            isLoading = isLoadingGeocodingFeatures,
            onPropertySelected = onPropertySelected,
            onDialogDismissed = onDialogDismissed,
            onTextUpdated = onTextUpdated
        )
    }
}

@Composable
fun LocationSearchBody(
    onDialogDismissed: () -> Unit,
    onTextUpdated: (t: String) -> Unit,
    onPropertySelected: (GeocodingFeature) -> Unit,
    currentFeatures: List<GeocodingFeature>?,
    lastUsedFeatures: List<GeocodingFeature>?,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Column {
            ButtonRow(onDialogDismissed)
            SearchField(onTextChanged = onTextUpdated)
            if (isLoading)
                LoadingIndicator()
            else DialogListContent(
                currentFeatures = currentFeatures ?: emptyList(),
                lastUsedFeatures = lastUsedFeatures ?: emptyList(),
            ) { prop ->
                onPropertySelected(prop)
            }
        }
    }
}

/*@Preview
@Composable
fun LocationSearchPreviewBody() {
    val isLoading = false
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column {
            ButtonRow({ })
            SearchField("", {})
            if (isLoading)
                LoadingIndicator()
            else DialogListContent(
                searchText = "searchText",
                currentFeatures = GeocodingFeaturesSampleData.getSample1(),
                lastUsedFeatures = emptyList(),
                onPropertySelected = { }
            )
        }
    }
}*/


@Composable
fun ButtonRow(onCloseTapped: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onCloseTapped, content = @Composable {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_close_24),
                contentDescription = "Show Saved"
            )
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(onTextChanged: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = searchText) {
        if (searchText.isBlank()) {
            isTyping = false
            return@LaunchedEffect
        }
        isTyping = true
        delay(1.seconds)
        isTyping = false
        onTextChanged(searchText)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchText,
            onValueChange = { t -> searchText = t },
            trailingIcon = @Composable {
                if (isTyping) {
                    Box( modifier = Modifier.padding(4.dp)) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            },
            placeholder = @Composable { Text("Søk etter sted...") },
            leadingIcon = @Composable {
                Icon(
                    contentDescription = "",
                    painter = painterResource(R.drawable.ic_baseline_search_24)
                )
            }
        )
    }
}

@Composable
fun LoadingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun DialogListContent(
    lastUsedFeatures: List<GeocodingFeature>?,
    currentFeatures: List<GeocodingFeature>?,
    onFeatureSelected: (GeocodingFeature) -> Unit
) {
    val features: List<GeocodingFeature> = currentFeatures ?: (lastUsedFeatures ?: emptyList())
    FeaturesList(features, onFeatureSelected)
}

@Composable
fun FeaturesList(
    features: List<GeocodingFeature>,
    onFeatureSelected: (GeocodingFeature) -> Unit
) {
    Column {
        features.map { feat -> FeatureElement(feat, onFeatureSelected) }
    }
}

@Composable
fun FeatureElement(
    feature: GeocodingFeature,
    onFeatureSelected: (GeocodingFeature) -> Unit
) {
    val category = feature.properties.category
    val county = feature.properties.county
    val label = feature.properties.label
    Box(
        modifier = Modifier
            .height(64.dp)
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .clickable { onFeatureSelected(feature) }
            .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(4.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(verticalArrangement = Arrangement.Center) {
                    Icon(
                        painter = painterResource(CategoryIconResolver.resolveIcon(category)),
                        contentDescription = "Icon"
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(county ?: "", fontSize = 10.sp)
                    Text(label ?: "")
                }
            }
        }
    }
}