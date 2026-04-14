package com.pdm0126.taller1_00012424

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm0126.taller1_00012424.ui.theme.AndroidPediaByArguetaTheme

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

    var screen by remember { mutableStateOf("welcome") } //nota para el video: esto controla que pantalla se muestra
    var finalScore by remember { mutableStateOf(0) } // y esto otro va guardando el puntaje final

    when (screen) {
        "welcome" -> WelcomeScreen {
            finalScore = 0
            screen = "quiz"
        }

        "quiz" -> QuizScreen { score ->
            finalScore = score // va guardando el puntaje
            screen = "result" // y esto lo lleva a resultados
        }

        "result" -> ResultScreen(score = finalScore) {
            screen = "welcome"
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

    var index by remember { mutableStateOf(0) } // muestra por que pregunta va
    var score by remember { mutableStateOf(0) } // es el puntaje
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    val question = questionList[index]

    Scaffold {
        Column(modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Pregunta ${index + 1} de 3", fontSize = 18.sp)
            Text(
                "Puntaje: $score / 3",
                fontSize = 16.sp,
                color = Color.Gray
            )

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

            question.options.forEach { option ->

                Button(
                    onClick = {
                        if (selectedAnswer == null) { // evita multiples repuestas
                            selectedAnswer = option
                            if (option == question.correctAnswer) score++// suma los puntos si la pregunta es correcta
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            selectedAnswer == null -> Color(0xFFE0E0E0)
                            option == question.correctAnswer -> Color(0xFF4CAF50)
                            option == selectedAnswer -> Color(0xFFF44336)
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
                        if (index < 2) {
                            index++
                            selectedAnswer = null
                        } else {
                            onFinish(score)
                        }
                    },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text(if (index == 2) "Ver Resultado" else "Siguiente")
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