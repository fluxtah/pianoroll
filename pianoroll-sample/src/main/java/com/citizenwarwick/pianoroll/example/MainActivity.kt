package com.citizenwarwick.pianoroll.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.platform.setContent
import com.citizenwarwick.pianoroll.PianoChord
import com.citizenwarwick.music.chord

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Box {
                    val selectedNote = savedInstanceState { "" }
                    Column {
                        Text(
                            "You selected ${selectedNote.value}",
                            style = MaterialTheme.typography.h4
                        )
                        PianoChord("F0 A0 C1".chord) {
                            selectedNote.value = it.toString()
                        }
                    }
                }
            }
        }
    }
}