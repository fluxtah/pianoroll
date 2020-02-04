package com.citizenwarwick.pianoroll.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.layout.Container
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.citizenwarwick.pianoroll.Note
import com.citizenwarwick.pianoroll.PianoChord
import com.citizenwarwick.pianoroll.PianoKey

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Container {
                    PianoChord(
                        chord = listOf(
                            PianoKey(
                                Note.C,
                                0
                            ),
                            PianoKey(
                                Note.E,
                                0
                            ),
                            PianoKey(
                                Note.G,
                                0
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Greeting("Android")
    }
}
