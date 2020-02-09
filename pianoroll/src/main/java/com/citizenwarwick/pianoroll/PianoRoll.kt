/*
 Copyright 2020 Ian Warwick

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.citizenwarwick.pianoroll

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.ColoredRect
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.LayoutPadding
import androidx.ui.layout.LayoutSize
import androidx.ui.layout.Row
import androidx.ui.layout.Stack
import androidx.ui.material.ripple.Ripple
import androidx.ui.text.TextStyle
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.citizenwarwick.music.Note
import com.citizenwarwick.music.PitchClass.A
import com.citizenwarwick.music.PitchClass.As
import com.citizenwarwick.music.PitchClass.B
import com.citizenwarwick.music.PitchClass.C
import com.citizenwarwick.music.PitchClass.Cs
import com.citizenwarwick.music.PitchClass.D
import com.citizenwarwick.music.PitchClass.Ds
import com.citizenwarwick.music.PitchClass.E
import com.citizenwarwick.music.PitchClass.F
import com.citizenwarwick.music.PitchClass.Fs
import com.citizenwarwick.music.PitchClass.G
import com.citizenwarwick.music.PitchClass.Gs
import com.citizenwarwick.music.between
import com.citizenwarwick.music.chord
import com.citizenwarwick.music.lower
import com.citizenwarwick.music.upper

@Composable
@Preview
fun PianoRollPreview() {
    PianoChord("C1 E1 G1".chord)
}

@Composable
fun PianoChord(
    chord: Set<Note>,
    showNoteNames: Boolean = true,
    sizeScale: Float = 1.5f,
    onKeyPressed: (Note) -> Unit = {}
) {
    val lower = chord.lower()
    val upper = chord.upper()
    PianoRoll(lower, upper, showNoteNames, chord, sizeScale, onKeyPressed)
}

@Composable
fun PianoRoll(
    from: Note,
    to: Note,
    showNoteNames: Boolean = false,
    highlightedKeys: Set<Note>? = null,
    sizeScale: Float = 1.5f,
    onKeyPressed: (Note) -> Unit = {}
) {
    val startsAtF = from.pitch >= F
    val range = between(from, to) - (if (startsAtF) 5 else 0)
    val toOctave = (range / 12f).toInt() + from.octave

    Row {
        KeyDivider((BASE_KEY_HEIGHT * sizeScale).dp)
        for (octave in from.octave..toOctave) {
            PianoRollOctave(
                startFromF = startsAtF,
                showNoteNames = showNoteNames,
                octave = octave,
                highlightedKeys = highlightedKeys,
                sizeScale = sizeScale,
                onKeyPressed = onKeyPressed
            )
        }
    }
}

@Composable
fun PianoRollOctave(
    startFromF: Boolean = false,
    showNoteNames: Boolean = false,
    octave: Int = 0,
    highlightedKeys: Set<Note>? = null,
    sizeScale: Float = 1.5f,
    onKeyPressed: (Note) -> Unit = {}
) {
    Stack {
        WhiteNotes(startFromF, octave, highlightedKeys, sizeScale, onKeyPressed)
        BlackNotes(startFromF, octave, highlightedKeys, sizeScale, onKeyPressed)
        if (showNoteNames) {
            WhiteNoteLabels(startFromF, octave, sizeScale)
        }
    }
}

@Composable
private fun WhiteNoteLabels(
    startFromF: Boolean = false,
    octave: Int = 0,
    sizeScale: Float = 1.5f
) {
    Row {
        repeat(7) { noteIndex ->
            Container(
                LayoutPadding(right = 1.dp) + LayoutSize(
                    (BASE_KEY_WIDTH * sizeScale).dp, (BASE_KEY_HEIGHT * sizeScale).minus(4).dp
                ),
                alignment = Alignment.BottomCenter
            ) {
                val noteName = formatNoteName(startFromF, noteIndex, octave)
                androidx.ui.core.Text(
                    text = noteName,
                    style = TextStyle(fontSize = (8 * sizeScale).sp)
                )
            }
        }
    }
}

@Composable
private fun BlackNotes(
    startFromF: Boolean = false,
    octave: Int = 0,
    highlightedKeys: Set<Note>? = null,
    sizeScale: Float = 1.5f,
    onKeyPressed: (Note) -> Unit = {}
) {
    val accKeyWidth = BASE_ACC_KEY_WIDTH * sizeScale
    val accKeyHeight = BASE_ACC_KEY_HEIGHT * sizeScale
    val keyWidthWithBorder = (BASE_KEY_WIDTH * sizeScale) + 1
    val halfKeyWidth = accKeyWidth / 2
    val keyAlignment = accKeyWidth / 6

    if (startFromF) {
        val keySpacing: List<Float> = listOf(
            (keyWidthWithBorder + 1) - halfKeyWidth - keyAlignment,
            (keyWidthWithBorder * 2) - halfKeyWidth,
            (keyWidthWithBorder * 3) - halfKeyWidth + keyAlignment,
            (keyWidthWithBorder * 5) - halfKeyWidth - keyAlignment,
            (keyWidthWithBorder * 6) - halfKeyWidth + keyAlignment
        )
        keySpacing.forEachIndexed { index, space ->
            val actualOctave = if (index > 3) octave.plus(1) else octave
            val highlighted =
                highlightedKeys?.any { it.pitch == ACC_NOTES_FROM_F[index] && it.octave == actualOctave }
                    ?: false
            Ripple(bounded = true, color = Color.White) {
                Clickable(onClick = {
                    onKeyPressed(
                        Note(
                            ACC_NOTES_FROM_F[index],
                            actualOctave
                        )
                    )
                }) {
                    AccidentalPianoKey(space.dp, highlighted, accKeyWidth.dp, accKeyHeight.dp)
                }
            }
        }
    } else {
        val keySpacing: List<Float> = listOf(
            (keyWidthWithBorder * 1) - halfKeyWidth - keyAlignment,
            (keyWidthWithBorder * 2) - halfKeyWidth + keyAlignment,
            (keyWidthWithBorder * 4) - halfKeyWidth - keyAlignment,
            (keyWidthWithBorder * 5) - halfKeyWidth,
            (keyWidthWithBorder * 6) - halfKeyWidth + keyAlignment
        )

        keySpacing.forEachIndexed { index, space ->
            val highlighted =
                highlightedKeys?.any { it.pitch == ACC_NOTES[index] && it.octave == octave }
                    ?: false
            Ripple(bounded = true, color = Color.White) {
                Clickable(onClick = {
                    onKeyPressed(
                        Note(
                            ACC_NOTES[index],
                            octave
                        )
                    )
                }) {
                    AccidentalPianoKey(space.dp, highlighted, accKeyWidth.dp, accKeyHeight.dp)
                }
            }
        }
    }
}

@Composable
private fun WhiteNotes(
    startFromF: Boolean = false,
    octave: Int = 0,
    highlightedKeys: Set<Note>? = null,
    sizeScale: Float = 1.5f,
    onKeyPressed: (Note) -> Unit = {}
) {
    val width = (BASE_KEY_WIDTH * sizeScale).dp
    val height = (BASE_KEY_HEIGHT * sizeScale).dp

    Row {
        if (startFromF) {
            // We hit somewhere between 1 and 2 octaves here so we need
            // to shift the octave number up when we hit the next C note
            NATURAL_NOTES_FROM_F.forEachIndexed { index, note ->
                val actualOctave = if (index > 3) octave.plus(1) else octave
                val highlighted = highlightedKeys?.any { it.pitch == note && it.octave == actualOctave } ?: false
                Ripple(bounded = true) {
                    Clickable(onClick = {
                        onKeyPressed(
                            Note(
                                note,
                                actualOctave
                            )
                        )
                    }) {
                        PianoKey(highlighted = highlighted, width = width, height = height)
                    }
                }
                KeyDivider(height)
            }
        } else {
            NATURAL_NOTES.forEach { note ->
                val highlighted = highlightedKeys?.any { it.pitch == note && it.octave == octave } ?: false
                Ripple(bounded = true) {
                    Clickable(onClick = { onKeyPressed(Note(note, octave)) }) {
                        PianoKey(highlighted = highlighted, width = width, height = height)
                    }
                }
                KeyDivider(height)
            }
        }
    }
}

@Composable
private fun PianoKey(highlighted: Boolean = false, width: Dp, height: Dp) {
    Column {
        ColoredRect(
            color = Color.Black,
            height = 1.dp,
            width = width
        )
        ColoredRect(
            color = if (highlighted) Color.Yellow else Color.White,
            height = height.minus(2.dp), // 2 less for border top and bottom
            width = width
        )
        ColoredRect(
            color = Color.Black,
            height = 1.dp,
            width = width
        )
    }
}

@Composable
private fun KeyDivider(height: Dp) {
    ColoredRect(
        color = Color.Black,
        height = height,
        width = 1.dp
    )
}

@Composable
private fun AccidentalPianoKey(leftSpacing: Dp, highlighted: Boolean = false, keyWidth: Dp, keyHeight: Dp) {
    Column(modifier = LayoutPadding(left = leftSpacing)) {
        ColoredRect(
            color = Color.Black,
            height = 1.dp,
            width = keyWidth
        )
        Stack {
            ColoredRect(
                color = Color.Black,
                height = keyHeight.minus(1.dp), // minus one for top border
                width = keyWidth
            )
            if (highlighted) {
                ColoredRect(
                    color = Color.Yellow.copy(alpha = 0.7f),
                    height = keyHeight.minus(1.dp), // minus one for top border
                    width = keyWidth
                )
            }
        }
    }
}

private fun formatNoteName(startFromF: Boolean, it: Int, octave: Int?): String {
    return when {
        startFromF -> {
            val actualOctave = if (it > 3) octave?.plus(1) else octave
            "${NATURAL_NOTES_FROM_F[it].name}${if (octave == null) "" else "$actualOctave"}"
        }
        else -> {
            "${NATURAL_NOTES[it].name}${if (octave == null) "" else "$octave"}"
        }
    }
}

private const val BASE_KEY_WIDTH = 32
private const val BASE_KEY_HEIGHT = 128
private const val BASE_ACC_KEY_WIDTH = 22
private const val BASE_ACC_KEY_HEIGHT = 86

private val NATURAL_NOTES = listOf(C, D, E, F, G, A, B)
private val NATURAL_NOTES_FROM_F = listOf(F, G, A, B, C, D, E)
private val ACC_NOTES = listOf(Cs, Ds, Fs, Gs, As)
private val ACC_NOTES_FROM_F = listOf(Fs, Gs, As, Cs, Ds)
