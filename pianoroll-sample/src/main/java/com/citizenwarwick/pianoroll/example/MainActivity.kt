package com.citizenwarwick.pianoroll.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.LayoutHeight
import androidx.ui.layout.Spacer
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp
import com.citizenwarwick.pianoroll.Note
import com.citizenwarwick.pianoroll.PianoChord
import com.citizenwarwick.pianoroll.PianoKey

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Container {
                    Column {
                        PianoChord(
                            chord = listOf(
                                PianoKey(Note.C, 0),
                                PianoKey(Note.E, 0),
                                PianoKey(Note.G, 0)
                            )
                        )
                        Spacer(modifier = LayoutHeight(16.dp))
                        PianoChord(
                            chord = listOf(
                                PianoKey(Note.D, 0),
                                PianoKey(Note.F, 0),
                                PianoKey(Note.A, 0)
                            )
                        )
                        Spacer(modifier = LayoutHeight(16.dp))
                        PianoChord(
                            chord = listOf(
                                PianoKey(Note.E, 0),
                                PianoKey(Note.Gs, 0),
                                PianoKey(Note.B, 0)
                            )
                        )
                    }
                }
            }
        }
    }
}
