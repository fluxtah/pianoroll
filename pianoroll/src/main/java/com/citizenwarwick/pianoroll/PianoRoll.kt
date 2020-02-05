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
import com.citizenwarwick.pianoroll.Note.A
import com.citizenwarwick.pianoroll.Note.As
import com.citizenwarwick.pianoroll.Note.B
import com.citizenwarwick.pianoroll.Note.C
import com.citizenwarwick.pianoroll.Note.Cs
import com.citizenwarwick.pianoroll.Note.D
import com.citizenwarwick.pianoroll.Note.Ds
import com.citizenwarwick.pianoroll.Note.E
import com.citizenwarwick.pianoroll.Note.F
import com.citizenwarwick.pianoroll.Note.Fs
import com.citizenwarwick.pianoroll.Note.G
import com.citizenwarwick.pianoroll.Note.Gs

@Composable
@Preview
fun PianoRollPreview() {
    val chord = listOf(
        PianoKey(C, 0),
        PianoKey(Ds, 0),
        PianoKey(G, 0)
    )

    PianoChord(chord)
}

@Composable
fun PianoChord(
    chord: List<PianoKey>,
    showNoteNames: Boolean = true,
    onKeyPressed: (PianoKey) -> Unit = {}
) {
    val lower = chord.lowerKey()
    val upper = chord.upperKey()
    PianoRoll(lower, upper, showNoteNames, chord, onKeyPressed)
}

@Composable
fun PianoRoll(
    from: PianoKey,
    to: PianoKey,
    showNoteNames: Boolean = false,
    highlightedKeys: List<PianoKey>? = null,
    onKeyPressed: (PianoKey) -> Unit = {}
) {
    val startsAtF = from.note >= F
    Row {
        KeyDivider()
        for (octave in from.octave..to.octave) {
            PianoRollOctave(
                startFromF = startsAtF,
                showNoteNames = showNoteNames,
                octave = octave,
                highlightedKeys = highlightedKeys,
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
    highlightedKeys: List<PianoKey>? = null,
    onKeyPressed: (PianoKey) -> Unit = {}
) {
    Stack {
        WhiteNotes(startFromF, octave, highlightedKeys, onKeyPressed)
        BlackNotes(startFromF, octave, highlightedKeys, onKeyPressed)
        if (showNoteNames) {
            WhiteNoteLabels(startFromF, octave)
        }
    }
}

@Composable
private fun WhiteNoteLabels(
    startFromF: Boolean = false,
    octave: Int? = null
) {
    Row {
        repeat(7) { noteIndex ->
            Container(
                LayoutPadding(right = 1.dp) + LayoutSize(KEY_WIDTH.dp, KEY_HEIGHT.minus(4).dp),
                alignment = Alignment.BottomCenter
            ) {
                val noteName = formatNoteName(startFromF, noteIndex, octave)
                androidx.ui.core.Text(
                    text = noteName,
                    style = TextStyle(fontSize = (8 * DEFAULT_SCALE).sp)
                )
            }
        }
    }
}

@Composable
private fun BlackNotes(
    startFromF: Boolean = false,
    octave: Int = 0,
    highlightedKeys: List<PianoKey>? = null,
    onKeyPressed: (PianoKey) -> Unit = {}
) {
    if (startFromF) {
        ACC_NOTE_SPACING_FROM_F.forEachIndexed { index, space ->
            val actualOctave = if (index > 3) octave.plus(1) else octave
            val highlighted =
                highlightedKeys?.any { it.note == ACC_NOTES_FROM_F[index] && it.octave == actualOctave }
                    ?: false
            Ripple(bounded = true, color = Color.White) {
                Clickable(onClick = { onKeyPressed(PianoKey(ACC_NOTES_FROM_F[index], actualOctave)) }) {
                    AccidentalPianoKey(space.dp, highlighted)
                }
            }
        }
    } else {
        ACC_NOTE_SPACING.forEachIndexed { index, space ->
            val highlighted =
                highlightedKeys?.any { it.note == ACC_NOTES[index] && it.octave == octave }
                    ?: false
            Ripple(bounded = true, color = Color.White) {
                Clickable(onClick = { onKeyPressed(PianoKey(ACC_NOTES[index], octave)) }) {
                    AccidentalPianoKey(space.dp, highlighted)
                }
            }
        }
    }
}

@Composable
private fun WhiteNotes(
    startFromF: Boolean = false,
    octave: Int = 0,
    highlightedKeys: List<PianoKey>? = null,
    onKeyPressed: (PianoKey) -> Unit = {}
) {
    Row {
        if (startFromF) {
            // We hit somewhere between 1 and 2 octaves here so we need
            // to shift the octave number up when we hit the next C note
            NATURAL_NOTES_FROM_F.forEachIndexed { index, note ->
                val actualOctave = if (index > 3) octave.plus(1) else octave
                val highlighted = highlightedKeys?.any { it.note == note && it.octave == actualOctave } ?: false
                Ripple(bounded = true) {
                    Clickable(onClick = { onKeyPressed(PianoKey(note, actualOctave)) }) {
                        PianoKey(highlighted = highlighted)
                    }
                }
                KeyDivider()
            }
        } else {
            NATURAL_NOTES.forEach { note ->
                val highlighted = highlightedKeys?.any { it.note == note && it.octave == octave } ?: false
                Ripple(bounded = true) {
                    Clickable(onClick = { onKeyPressed(PianoKey(note, octave)) }) {
                        PianoKey(highlighted = highlighted)
                    }
                }
                KeyDivider()
            }
        }
    }
}

@Composable
private fun PianoKey(highlighted: Boolean = false) {
    Column {
        ColoredRect(
            color = Color.Black,
            height = 1.dp,
            width = KEY_WIDTH.dp
        )
        ColoredRect(
            color = if (highlighted) Color.Yellow else Color.White,
            height = KEY_HEIGHT.minus(2).dp, // 2 less for border top and bottom
            width = KEY_WIDTH.dp
        )
        ColoredRect(
            color = Color.Black,
            height = 1.dp,
            width = KEY_WIDTH.dp
        )
    }
}

@Composable
private fun KeyDivider() {
    ColoredRect(
        color = Color.Black,
        height = KEY_HEIGHT.dp,
        width = 1.dp
    )
}

@Composable
private fun AccidentalPianoKey(leftSpacing: Dp, highlighted: Boolean = false) {
    Column(modifier = LayoutPadding(left = leftSpacing)) {
        ColoredRect(
            color = Color.Black,
            height = 1.dp,
            width = ACC_KEY_WIDTH.dp
        )
        Stack {
            ColoredRect(
                color = Color.Black,
                height = ACC_KEY_HEIGHT.minus(1).dp, // minus one for top border
                width = ACC_KEY_WIDTH.dp
            )
            if (highlighted) {
                ColoredRect(
                    color = Color.Yellow.copy(alpha = 0.7f),
                    height = ACC_KEY_HEIGHT.minus(1).dp, // minus one for top border
                    width = ACC_KEY_WIDTH.dp
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

fun List<PianoKey>.lowerKey(): PianoKey = min()!!
fun List<PianoKey>.upperKey(): PianoKey = max()!!
data class PianoKey(val note: Note, val octave: Int) : Comparable<PianoKey> {
    override fun compareTo(other: PianoKey): Int = Comparator<PianoKey> { t, t2 ->
        when {
            t.note == t2.note && t.octave == t2.octave -> 0
            t.note > t2.note && t.octave >= t2.octave -> 1
            t.note < t2.note && t.octave > t2.octave -> 1
            else -> -1
        }
    }.compare(this, other)

    override fun toString(): String = "$note$octave"
}

enum class Note(val index: Int) {
    C(0),
    Cs(1),
    D(2),
    Ds(3),
    E(4),
    F(5),
    Fs(6),
    G(7),
    Gs(8),
    A(9),
    As(10),
    B(11)
}

inline val String.chord: List<PianoKey>
    get() = split(" ").map {
        when (it.length) {
            3 -> {
                val sharpFlat = it.substring(1, 2)
                val note = it.substring(0, 1)
                val octave = it.substring(2, 3).toInt()
                val noteSum = Note.values()[Note.valueOf(note).ordinal + (if (sharpFlat == "#") 1 else -1)]
                PianoKey(noteSum, octave)
            }
            2 -> {
                val note = it.substring(0, 1)
                val octave = it.substring(1, 2).toInt()
                PianoKey(Note.valueOf(note), octave)
            }
            else -> throw RuntimeException("Invalid format in $it")
        }
    }

private const val DEFAULT_SCALE = 1.5f
private const val KEY_WIDTH = 32 * DEFAULT_SCALE
private const val KEY_AND_BORDER_WIDTH = KEY_WIDTH + 1
private const val KEY_HEIGHT = 128 * DEFAULT_SCALE
private const val ACC_KEY_WIDTH = 22 * DEFAULT_SCALE
private const val ACC_KEY_HEIGHT = 86 * DEFAULT_SCALE
private const val ACC_KEY_WIDTH_HALF = ACC_KEY_WIDTH / 2
private const val ACC_KEY_OFFSET = ACC_KEY_WIDTH / 6
private val ACC_NOTE_SPACING: List<Float> = listOf(
    (KEY_AND_BORDER_WIDTH * 1) - ACC_KEY_WIDTH_HALF - ACC_KEY_OFFSET,
    (KEY_AND_BORDER_WIDTH * 2) - ACC_KEY_WIDTH_HALF + ACC_KEY_OFFSET,
    (KEY_AND_BORDER_WIDTH * 4) - ACC_KEY_WIDTH_HALF - ACC_KEY_OFFSET,
    (KEY_AND_BORDER_WIDTH * 5) - ACC_KEY_WIDTH_HALF,
    (KEY_AND_BORDER_WIDTH * 6) - ACC_KEY_WIDTH_HALF + ACC_KEY_OFFSET
)
private val ACC_NOTE_SPACING_FROM_F: List<Float> = listOf(
    (KEY_AND_BORDER_WIDTH + 1) - ACC_KEY_WIDTH_HALF - ACC_KEY_OFFSET,
    (KEY_AND_BORDER_WIDTH * 2) - ACC_KEY_WIDTH_HALF,
    (KEY_AND_BORDER_WIDTH * 3) - ACC_KEY_WIDTH_HALF + ACC_KEY_OFFSET,
    (KEY_AND_BORDER_WIDTH * 5) - ACC_KEY_WIDTH_HALF - ACC_KEY_OFFSET,
    (KEY_AND_BORDER_WIDTH * 6) - ACC_KEY_WIDTH_HALF + ACC_KEY_OFFSET
)

private val NATURAL_NOTES = listOf(C, D, E, F, G, A, B)
private val NATURAL_NOTES_FROM_F = listOf(F, G, A, B, C, D, E)
private val ACC_NOTES = listOf(Cs, Ds, Fs, Gs, As)
private val ACC_NOTES_FROM_F = listOf(Fs, Gs, As, Cs, Ds)

