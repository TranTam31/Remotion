package com.example.hope.dass21.ui.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hope.dass21.data.model.ResultResponse


@Composable
fun ResultScreen(
    resultResponse: ResultResponse?,
    onNextStage: () -> Unit,
    onReturnHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        Spacer(modifier = Modifier.height(8.dp))

        // Result Section
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                if (resultResponse != null) {
                    ResultCard(
                        title = "Anxiety Score",
                        score = resultResponse.anxiety_score.toInt(),
                        message = getAnxietyMessage(resultResponse.anxiety_score.toInt())
                    )
                }
            }
            item {
                if (resultResponse != null) {
                    ResultCard(
                        title = "Depression Score",
                        score = resultResponse.depression_score.toInt(),
                        message = getDepressionMessage(resultResponse.depression_score.toInt())
                    )
                }
            }
            item {
                if (resultResponse != null) {
                    ResultCard(
                        title = "Stress Score",
                        score = resultResponse.stress_score.toInt(),
                        message = getStressMessage(resultResponse.stress_score.toInt())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next Stage Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onNextStage,

                ) {
                Text(text = "Test Again")
            }

            Button(
                onClick = onReturnHome,

                ) {
                Text(text = "Return Home")
            }
        }
    }
}

@Composable
fun ResultCard(title: String, score: Int, message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,

                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Score: $score",

                )
            Text(
                text = message,

                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// Helper Functions to Get Messages
fun getAnxietyMessage(score: Int): String {
    return when {
        score < 8 -> "Your anxiety is normal. Keep maintaining your current lifestyle."
        score < 10 -> "Mild anxiety. Try to adopt relaxation techniques."
        score < 15 -> "Moderate anxiety. Consider regular exercise and mindfulness."
        score < 20 -> "Severe anxiety. Seek professional support if necessary."
        else -> "Extremely severe anxiety. Consult a mental health professional."
    }
}

fun getDepressionMessage(score: Int): String {
    return when {
        score < 10 -> "Your depression level is normal. Stay socially active."
        score < 14 -> "Mild depression. Consider setting small achievable goals."
        score < 21 -> "Moderate depression. Engage in physical activity and hobbies."
        score < 28 -> "Severe depression. Connect with a counselor for support."
        else -> "Extremely severe depression. Seek immediate help."
    }
}

fun getStressMessage(score: Int): String {
    return when {
        score < 10 -> "Your stress is under control. Keep up your healthy habits."
        score < 14 -> "Mild stress. Practice meditation and take regular breaks."
        score < 21 -> "Moderate stress. Spend time in nature and focus on breathing."
        score < 28 -> "Severe stress. Seek professional advice for stress management."
        else -> "Extremely severe stress. Immediate intervention is recommended."
    }
}