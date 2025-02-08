package org.example.project.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TableColumn(
    modifier: Modifier = Modifier,

) {
    
}

@Composable
fun TableItem(
    modifier: Modifier
) {

}

@Composable
fun Table(
    content: List<List<String>>,
    modifier: Modifier = Modifier,
    horizontalDivider:  @Composable() (ColumnScope.() -> Unit)? = null,
    ) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for(i in content.indices) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .fillMaxWidth()
                    .weight(1f, false)
            ) {
                for (j in content[0].indices) {

                    Text(
                        content[j][i],
                        color = MaterialTheme.colorScheme.onSurface
                    )


                }
                horizontalDivider?.let {
                    horizontalDivider()
                }
            }
        }
    }
}

@Preview
@Composable
private fun TablePreview() {
    val rows = listOf(
        listOf("1", "daba", "7"),
        listOf("123", "dabeusz", "7"),
        listOf("1", "konstantynapoli", "7"),
    )
    MaterialTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Table(
                modifier = Modifier.fillMaxWidth(),
                content = rows,
                horizontalDivider = {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }
            )
        }
    }
}