package com.app.skyss_companion.view.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.view.bookmark.ServiceModeIcons
import com.app.skyss_companion.view.bookmark.StopGroupTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopGroupListItem(stopGroup: StopGroup, onTap: (StopGroup) -> Unit) {
    val serviceModes = stopGroup.serviceModes ?: emptyList()
    val title = stopGroup.description ?: ""
    Card(
        modifier = Modifier.padding(bottom = 8.dp),
        onClick = { onTap(stopGroup) }
    ) {
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ServiceModeIcons(serviceModes = serviceModes)
                Spacer(modifier = Modifier.width(8.dp))
                StopGroupTitle(title = title)
            }
        }
    }
}