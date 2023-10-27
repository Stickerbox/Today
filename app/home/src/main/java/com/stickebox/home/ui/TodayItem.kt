package com.stickebox.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stickebox.common.TodoItem
import com.stickebox.uitheme.theme.TodayTheme
import java.time.LocalDateTime

@Composable
fun TodayItem(
    modifier: Modifier = Modifier,
    item: TodoItem,
    shouldBeDark: Boolean,
    isCurrent: Boolean
) {
    val alpha = if (isCurrent) 0.75f else 0.4f
    val background = if (shouldBeDark) Color.Black else Color.White
    val onTextColor = if (shouldBeDark) Color.White else Color.Black

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .alpha(alpha)
            .background(background)
            .padding(20.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = item.time,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            color = onTextColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = item.text,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = onTextColor
        )
    }
}

@Preview
@Composable
fun TodayItemPreview() {
    TodayTheme {
        TodayItem(
            isCurrent = true,
            item = TodoItem(
                time = "11 am - 12 pm",
                text = "Do French",
                isCurrentItem = false,
                fromTime = LocalDateTime.now(),
                toTime = LocalDateTime.now()
            ),
            shouldBeDark = true
        )
    }
}
