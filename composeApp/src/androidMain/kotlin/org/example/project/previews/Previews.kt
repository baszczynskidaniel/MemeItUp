package org.example.project.previews

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.RulerScope
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.dto.GameMode
import org.example.project.meme.data.dto.RulesDto

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

@Composable
fun PlayerItem(
    modifier: Modifier = Modifier
) {

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
//            ScoreTable(
//                modifier = Modifier.fillMaxWidth(),
//                content = rows,
//                horizontalDivider = {
//                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
//                }
//            )
            RulesItem(
                rules = RulesDto(
                    numberOfRounds = 4,
                    sameMemeForEveryone = true,
                    everyoneIsTheJudge = false,
                    gameMode = GameMode.FILL_IN_BLANK
                ),
                onRulesChange = {}

            )
        }
    }
}