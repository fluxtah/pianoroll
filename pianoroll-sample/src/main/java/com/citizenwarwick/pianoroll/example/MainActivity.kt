package com.citizenwarwick.pianoroll.example

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.citizenwarwick.music.Note
import com.citizenwarwick.music.PitchClass
import com.citizenwarwick.music.chord
import com.citizenwarwick.pianoroll.PianoRoll
import com.citizenwarwick.pianoroll.PianoRollOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Content()
            }
        }
    }

    @Composable
    @Preview
    private fun Content() {
        Box {
            val selectedNote = rememberSaveable { mutableStateOf("") }
            val scrollState = remember { ScrollState(0) }

            Column {
                Text(
                    "You selected ${selectedNote.value}",
                    style = MaterialTheme.typography.h4
                )
                Row(
                    Modifier
                        .horizontalScroll(scrollState)
                        .fillMaxWidth()
                ) {
                    PianoRoll(
                        startNote = Note(PitchClass.C, 0),
                        endNote = Note(PitchClass.C, 1),
                        options = PianoRollOptions(
                            highlightedNotes = "C0 E0 G0".chord
                        )
                    ) {
                        selectedNote.value = it.toString()
                    }
                }
            }
        }
    }
}