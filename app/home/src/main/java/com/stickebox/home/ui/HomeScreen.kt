package com.stickebox.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.stickebox.common.generateComplimentaryColor
import com.stickebox.common.isDark
import com.stickebox.common.lightOrDark
import com.stickebox.uitheme.theme.TodayTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    colors: List<Color>,
    isBottomDark: ((Boolean) -> Unit)? = null,
    viewModel: HomeScreenViewModel
) {

    require(colors.isNotEmpty()) { "Must have at least 1 color" }
    val interval = 1.0 / colors.size

    val backgroundGradientStops by remember {
        mutableStateOf(
            colors.mapIndexed { index, color ->
                (index * interval).toFloat() to color
            }.toMutableList().also { colorPairs ->
                // If there's only 1 color, its interval will be 1.0 and there won't be anything at 0.0,
                // so copy the color and set the position to 0.0
                if (colors.size == 1) {
                    colorPairs.add(1.0f to generateComplimentaryColor(colorPairs.first().second))
                }
            }.toList()
        )
    }

    val shouldItemsBeDark = backgroundGradientStops.first().second.isDark().not()
    val todoItems by viewModel.todoItems.collectAsState()
    var shouldShowDialog by remember { mutableStateOf(false) }

    isBottomDark?.invoke(backgroundGradientStops.last().second.isDark().not())

    Box {
        AnimatedVisibility(visible = shouldShowDialog) {
            Dialog(onDismissRequest = { shouldShowDialog = false }) {
                Box(
                    modifier = Modifier
                        .shadow(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column {
                        var newItemText by remember { mutableStateOf("") }
                        var fromTimePickerState = rememberTimePickerState()
                        var toTimePickerState = rememberTimePickerState()

                        Text(
                            text = "Add new item",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.padding(top = 16.dp))
                        TextField(
                            value = newItemText,
                            onValueChange = { newItemText = it },
                            label = { Text("What's next") }
                        )

                        Spacer(modifier = Modifier.padding(top = 16.dp))
//                        Text(
//                            text = "From",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 20.sp,
//                            modifier = Modifier
//                                .clickable { shouldShowDialog = false }
//                                .padding(vertical = 12.dp)
//                        )
//                        TimeInput(state = fromTimePickerState)
//                        Text(
//                            text = "To",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 20.sp,
//                            modifier = Modifier
//                                .clickable { shouldShowDialog = false }
//                                .padding(vertical = 12.dp)
//                        )
//                        TimeInput(state = toTimePickerState)
                        Row(
                            modifier = Modifier
                                .align(Alignment.End)
                                .height(IntrinsicSize.Min)
                        ) {
                            Text(
                                text = "Cancel",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .clickable { shouldShowDialog = false }
                                    .padding(12.dp)
                            )
                            Divider(
                                color = Color.LightGray,
                                modifier = Modifier
                                    .padding(vertical = 12.dp, horizontal = 8.dp)
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            Text(
                                text = "Next",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .clickable {
                                        shouldShowDialog = false
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = backgroundGradientStops.toTypedArray()
                    )
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viewModel.currentDate,
                    color = backgroundGradientStops.first().second.lightOrDark(),
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp
                )

                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add new task",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(32.dp)
                        .clickable {
                            shouldShowDialog = true
                        }
                        .background(
                            backgroundGradientStops
                                .first().second
                                .lightOrDark()
                        ),
                    tint = if (backgroundGradientStops.first().second.isDark()) Color.DarkGray else Color.White
                )
            }

            LazyColumn(
                modifier = listModifier
            ) {
                items(todoItems) { item ->
                    TodayItem(
                        modifier = Modifier.padding(16.dp),
                        isCurrent = item.isCurrentItem,
                        item = item,
                        shouldBeDark = shouldItemsBeDark
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    TodayTheme {
//        HomeScreen(colors = listOf(generateRandomColor()))
    }
}
