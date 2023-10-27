package com.stickebox.food

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.stickebox.common.isDark
import com.stickebox.common.lightOrDark
import com.stickebox.food.NavigationEvent.AddFood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed class NavigationEvent {
    data object AddFood : NavigationEvent()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodScreen(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    isBottomDark: ((Boolean) -> Unit)? = null,
    onNavigationEvent: ((NavigationEvent) -> Unit)? = null,
    viewModel: FoodScreenViewModel,
) {
    isBottomDark?.invoke(false)

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viewModel.currentDate,
                    color = MaterialTheme.colorScheme.onBackground,
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
                            onNavigationEvent?.invoke(AddFood)
                        }
                        .background(Color.DarkGray),
                    tint = Color.White
                )
            }

            fun Int.toTimeOfDay(): String {
                return when (this) {
                    0 -> "Morning"
                    1 -> "Afternoon"
                    else -> "Evening"
                }
            }

            val foodItems by viewModel.foodItems.collectAsState(emptyMap())

            LazyColumn(modifier = listModifier.padding(top = 24.dp)) {
                foodItems.forEach { section ->
                    stickyHeader {
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .shadow(12.dp, shape = RoundedCornerShape(12.dp))
                                .wrapContentHeight()
                                .background(Color.DarkGray)
                        ) {
                            Text(
                                text = section.key.toTimeOfDay(),
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                    items(section.value) { foodItem ->
                        FoodListItem(
                            modifier = Modifier.padding(vertical = 18.dp),
                            timeAdded = foodItem.timeAdded,
                            description = foodItem.description,
                            image = foodItem.image
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodListItem(
    modifier: Modifier = Modifier,
    timeAdded: String,
    description: String,
    image: ImageBitmap,
) {

    var dominantColor by remember { mutableStateOf(Color.LightGray) }
    Palette.Builder(image.asAndroidBitmap()).generate { palette ->
        dominantColor =
            Color(palette?.getDominantColor(Color.LightGray.toArgb()) ?: Color.LightGray.toArgb()).copy(
                alpha = 0.4f
            )
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .background(dominantColor)
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row {
            Box {
                Image(
                    bitmap = image,
                    contentDescription = "",
                    modifier = Modifier
                        .shadow(8.dp, shape = RoundedCornerShape(26.dp), spotColor = Color.DarkGray)
                        .clip(RoundedCornerShape(26.dp))
                        .size(100.dp)
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Column {
                Text(
                    text = timeAdded,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                Text(
                    text = description,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}