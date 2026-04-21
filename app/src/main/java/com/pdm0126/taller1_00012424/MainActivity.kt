package com.pdm0126.taller1_00012424

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


sealed class Screen {
    object Welcome : Screen()
    object Quiz : Screen()
    object Result : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPediaApp()
        }
    }
}

@Composable
fun AndroidPediaApp() {

    var screen by remember { mutableStateOf<Screen>(Screen.Welcome) }
    var finalScore by remember { mutableStateOf(0) }

    when (screen) {

        is Screen.Welcome -> WelcomeScreen {
            finalScore = 0
            screen = Screen.Quiz
        }

        is Screen.Quiz -> QuizScreen { score ->
            finalScore = score
            screen = Screen.Result
        }

        is Screen.Result -> ResultScreen(score = finalScore) {
            screen = Screen.Welcome
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WelcomeScreen(onStart: () -> Unit) {

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("AndroidPedia", fontSize = 28.sp)
            Text("¿Cuánto sabes de Android?")

            Spacer(modifier = Modifier.height(20.dp))

            Text("Héctor Argueta")
            Text("Carnet: 00012424")

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onStart) {
                Text("Comenzar Quiz")
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizScreen(onFinish: (Int) -> Unit) {

    var index by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }

    val question = questionList[index]

    Scaffold {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Pregunta ${index + 1} de ${questionList.size}", fontSize = 18.sp)
            Text("Puntaje: $score / ${questionList.size}", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = question.question,
                    modifier = Modifier.padding(20.dp),
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            question.options.forEachIndexed { i, option ->

                Button(
                    onClick = {
                        if (selectedAnswer == null) {
                            selectedAnswer = i
                            if (i == question.correctAnswerIndex) score++
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            selectedAnswer == null -> Color(0xFFE0E0E0)
                            i == question.correctAnswerIndex -> Color(0xFF4CAF50)
                            i == selectedAnswer -> Color(0xFFF44336)
                            else -> Color(0xFFE0E0E0)
                        },
                        contentColor = Color.Black
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(option)
                }
            }


            if (selectedAnswer != null) {

                Card(
                    modifier = Modifier.padding(top = 10.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Fun Fact: ${question.funFact}",
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (index < questionList.size - 1) {
                            index++
                            selectedAnswer = null
                        } else {
                            onFinish(score)
                        }
                    },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text(if (index == questionList.size - 1) "Ver Resultado" else "Siguiente")
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultScreen(score: Int, onRestart: () -> Unit) {

    val message = when (score) {
        3 -> "¡Excelente! Dominas Android"
        2 -> "Muy bien, pero puedes mejorar"
        else -> "Sigue estudiando Android"
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Obtuviste $score de 3")
            Text(message)

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onRestart) {
                Text("Reiniciar Quiz")
            }
        }
    }
}