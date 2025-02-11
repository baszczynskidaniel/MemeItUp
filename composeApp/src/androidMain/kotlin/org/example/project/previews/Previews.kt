package org.example.project.previews

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.RulerScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.dto.GameMode
import org.example.project.meme.data.dto.RulesDto
import kotlin.math.roundToInt

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
fun ScoreTable(
    content: List<List<String>>,
    modifier: Modifier = Modifier,
    horizontalDivider:  @Composable() (BoxScope.() -> Unit)? = null,
    ) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for(i in content.indices) {
            Column(
                modifier = Modifier


            ) {
                for (j in content[0].indices) {

                    Text(
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .fillMaxWidth()
                            .weight(1f, false),
                        text = content[j][i],
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    horizontalDivider?.let {
                        Box(
                            modifier = Modifier
                                .width(IntrinsicSize.Min)
                        ) {
                            horizontalDivider()
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun RulesItem(
    rules: RulesDto,
    modifier: Modifier = Modifier,
    onRulesChange: (RulesDto) -> Unit,
) {
    ElevatedCard(
        modifier = modifier

    ) {
        Column(
            modifier = Modifier
                .padding(LocalDimensions.current.mediumPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.smallPadding)
        ) {
            Text(
                text = "Rules",
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Text(
                "Game mode",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.smallPadding),
            ) {
                GameMode.entries.forEach { entry ->
                    ElevatedFilterChip(
                        selected = entry == rules.gameMode,
                        onClick = {
                            onRulesChange(rules.copy(gameMode = entry))
                        },
                        label = {
                            Text(entry.name)
                        },
                        trailingIcon = {
                            if(rules.gameMode == entry) {
                                Icon(
                                    imageVector = AppIcons.DONE,
                                    null
                                )
                            }
                        }
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,


            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, false),
                    text = "Number of rounds",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = rules.numberOfRounds.toString(),
                    style = MaterialTheme.typography.titleMedium
                )

            }
            Slider(
                value = 2f,
                onValueChange = {
                    onRulesChange(rules.copy(numberOfRounds = it.toInt()))
                },
                valueRange = 1f..5f,
                steps = 5,

                )
            AnimatedVisibility(
                rules.gameMode == GameMode.FILL_IN_BLANK
            ) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, false),
                        text = "Same meme for everyone",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = rules.sameMemeForEveryone,
                        onCheckedChange = {
                            onRulesChange(rules.copy(sameMemeForEveryone = false))
                        }
                    )
                }
            }
            AnimatedVisibility(
                rules.gameMode == GameMode.FILL_IN_BLANK
            ) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, false),
                        text = "Everyone is the judge",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = rules.everyoneIsTheJudge,
                        onCheckedChange = {
                            onRulesChange(rules.copy(everyoneIsTheJudge = true))
                        }
                    )
                }
            }
        }
    }
}

data class RectangleData(val offset: Offset, val size: Dp)

@Composable
fun PlayerItem(
    modifier: Modifier = Modifier
) {

}

@Composable
fun DraggableResizableRectangles() {
    val bigRectangleColor = Color.LightGray
    val smallRectangleColor = Color.Red
    val bigRectangleSize = 300.dp

    var rectangles by remember { mutableStateOf(listOf<RectangleData>()) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(bigRectangleSize)
                .background(bigRectangleColor)
        ) {
            rectangles.forEachIndexed { index, rectangle ->
                SmallRectangle(
                    rectangleData = rectangle,
                    onUpdate = { updatedRectangle ->
                        rectangles = rectangles.toMutableList().apply { this[index] = updatedRectangle }
                    },
                    onRemove = {
                        rectangles = rectangles.toMutableList().apply { removeAt(index) }
                    }
                )
            }

            Box(
                Modifier
                    .size(40.dp)
                    .background(Color.Blue)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            rectangles = rectangles + RectangleData(
                                offset = Offset(50f, 50f),
                                size = 50.dp
                            )
                        }
                    }
            )
        }
    }
}

@Composable
fun SmallRectangle(
    rectangleData: RectangleData,
    onUpdate: (RectangleData) -> Unit,
    onRemove: () -> Unit
) {
    var position by remember { mutableStateOf(rectangleData.offset) }
    var size by remember { mutableStateOf(rectangleData.size) }

    Box(
        Modifier
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
            .size(size)
            .background(Color.Red)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    position += dragAmount
                    onUpdate(RectangleData(position, size))
                }
            }
            .pointerInput(Unit) {

                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        size += Dp(dragAmount.x + dragAmount.y)
                        onUpdate(RectangleData(position, size))
                    }

                }
            .zIndex(1f)
    )
}


@Preview
@Composable
private fun TablePreview() {

    MaterialTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            DraggableResizableRectangles()
        }
    }
}