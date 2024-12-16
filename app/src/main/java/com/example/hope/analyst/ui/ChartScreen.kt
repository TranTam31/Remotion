package com.example.hope.analyst.ui

import android.telecom.Call.Details
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hope.theme.Pink80
import com.example.hope.theme.Purple40
import com.example.hope.theme.Purple80
import com.example.hope.theme.PurpleGrey80
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


import com.example.hope.R
import com.example.hope.mood_tracker.data.database.Note
import kotlinx.coroutines.flow.Flow
import com.example.hope.mood_tracker.data.repository.NoteRepositoryImpl
import kotlinx.coroutines.launch

val emotionList: Map<Int, Int> = mapOf(
    0 to R.drawable.happy,
    1 to R.drawable.relax,
    2 to R.drawable.bored,
    3 to R.drawable.sad,
    4 to R.drawable.angry
)

@Composable
fun ChartScreen() {
    val noteRepository: NoteRepositoryImpl = NoteRepositoryImpl()
    val coroutineScope = rememberCoroutineScope()

    // State lưu trữ map với key là emotionId và value là số lượng
    val emotionCountMap = remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }

    // Gọi hàm getAllNotes() và tính toán số lượng emotion
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            noteRepository.getAllNotes().collect { notes ->
                // Tính số lượng của từng emotion
                val countMap = notes.groupingBy { it.emotion }
                    .eachCount() // nhóm theo emotion và đếm số lượng
                emotionCountMap.value = countMap
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        PieChart(emotionCountMap.value)
    }
}

@Composable
fun PieChart(
    data: Map<Int, Int>,
    radiusOuter: Dp = 90.dp,
    chartBarWidth: Dp = 20.dp,
    animDuration: Int = 1000,
) {
    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    data.values.forEachIndexed { index, values ->
        floatValue.add(index, 360*values.toFloat() / totalSum)
    }

    val colors = listOf(
        Purple80,
        PurpleGrey80,
        Pink80,
        Purple40,
        Color.Blue
    )

    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(120.dp))
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.size(radiusOuter * 2f).rotate(animateRotation),
                ) {
                    floatValue.forEachIndexed { index, value ->
                        drawArc(
                            color = colors[index],
                            lastValue,
                            value,
                            useCenter = false,
                            style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                        )
                        lastValue += value
                    }
                }
        }

        DetailsPieChart(
            data = data,
            colors = colors
        )
    }
}

@Composable
fun DetailsPieChart(
    data: Map<Int, Int>,
    colors: List<Color>
) {
    Column(
        modifier = Modifier
            .padding(top = 60.dp)
            .fillMaxWidth()
    ) {
        data.values.forEachIndexed { index, value ->
            DetailsPieChartItem(
                data = Pair(data.keys.elementAt(index), value),
                color = colors[index]
            )
        }
    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<Int, Int>,
    height: Dp = 45.dp,
    color: Color
) {
    Surface(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 30.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Box(modifier = Modifier
//                .background(
//                    color = color,
//                    shape = RoundedCornerShape(10.dp)
//                )
//                .size(height)
//            )
            Image(painter = painterResource(emotionList.keys.elementAt(data.first)),
                contentDescription = "Emotion Pic")
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = data.first.toString(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color.Gray
                )
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = data.second.toString(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color.Gray
                )
            }
        }
    }
}





