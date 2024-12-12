package com.example.hope.dass21.ui.form

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hope.dass21.data.model.FormData
import com.example.hope.dass21.ui.viewmodel.SurveyViewModel


@Composable
fun StressAnxietyDepressionForm(viewModel: SurveyViewModel, onSubmit: () -> Unit, onHomeBack: ()->Unit) {
    val questions = listOf(
        "I was aware of dryness of my mouth.",
        "I couldn't experience any positive feeling at all.",
        "I found it hard to wind down.",
        "I experienced breathing difficulty (e.g., excessively rapid breathing).",
        "I found it difficult to work up the initiative to do things.",
        "I tended to over-react to situations.",
        "I experienced trembling (e.g., in the hands).",
        "I felt that I had nothing to look forward to.",
        "I felt that I was using a lot of nervous energy.",
        "I was worried about situations in which I might panic and make a fool of myself.",
        "I felt down-hearted and blue.",
        "I found myself getting agitated.",
        "I felt I was close to panic.",
        "I was unable to become enthusiastic about anything.",
        "I found it difficult to relax.",
        "I was aware of the action of my heart in the absence of physical exertion.",
        "I felt I wasn't worth much as a person.",
        "I was intolerant of anything that kept me from getting on with what I was doing.",
        "I felt scared without any good reason.",
        "I felt that life wasn't worthwhile.",
        "I felt that I was rather touchy."
    )

    val sliderValues = remember { mutableStateListOf<Float>().apply { addAll(List(questions.size) { 0f }) } }

    fun submitForm() {
        val formData = FormData(
            q1 = sliderValues[0].toInt(),
            q2 = sliderValues[1].toInt(),
            q3 = sliderValues[2].toInt(),
            q4 = sliderValues[3].toInt(),
            q5 = sliderValues[4].toInt(),
            q6 = sliderValues[5].toInt(),
            q7 = sliderValues[6].toInt(),
            q8 = sliderValues[7].toInt(),
            q9 = sliderValues[8].toInt(),
            q10 = sliderValues[9].toInt(),
            q11 = sliderValues[10].toInt(),
            q12 = sliderValues[11].toInt(),
            q13 = sliderValues[12].toInt(),
            q14 = sliderValues[13].toInt(),
            q15 = sliderValues[14].toInt(),
            q16 = sliderValues[15].toInt(),
            q17 = sliderValues[16].toInt(),
            q18 = sliderValues[17].toInt(),
            q19 = sliderValues[18].toInt(),
            q20 = sliderValues[19].toInt(),
            q21 = sliderValues[20].toInt()
        )
        // Log the formData
        Log.d("SubmitForm", "Form Data: $formData")
        viewModel.submitForm(formData)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {



        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = "Taylor Scale For Stress/Anxiety/Depression",
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn for Questions and Sliders
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(questions) { index, question ->
                QuestionSlider(
                    index = index,
                    question = question,
                    sliderValue = sliderValues[index],
                    onValueChange = { sliderValues[index] = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                onSubmit()
                submitForm()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun QuestionSlider(index: Int, question: String, sliderValue: Float, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "${index + 1}) $question")
        Slider(
            value = sliderValue,
            onValueChange = onValueChange,
            valueRange = 0f..3f,
            steps = 2,
            modifier = Modifier.fillMaxWidth()
        )
    }
}