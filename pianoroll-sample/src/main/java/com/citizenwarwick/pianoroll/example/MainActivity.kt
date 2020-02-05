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
import com.citizenwarwick.pianoroll.PianoChord
import com.citizenwarwick.pianoroll.chord

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Container {
                    Column {
                        PianoChord("C0 E0 G0".chord)
                        Spacer(modifier = LayoutHeight(16.dp))
                        PianoChord("D0 F0 A0".chord)
                        Spacer(modifier = LayoutHeight(16.dp))
                        PianoChord("E0 G#0 B0".chord)
                    }
                }
            }
        }
    }
}
