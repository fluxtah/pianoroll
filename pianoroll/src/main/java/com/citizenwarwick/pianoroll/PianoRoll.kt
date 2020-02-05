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
import kotlin.math.max

@Composable
@Preview
fun PianoRollPreview() {
    val chord = listOf(
        PianoKey(F, 0),
        PianoKey(A, 0),
        PianoKey(B, 0)
    )

    PianoChord(chord = chord)
}

@Composable
fun PianoChord(
    chord: List<PianoKey>,
    showNoteNames: Boolean = true,
    sizeScale: Float = 1.5f,
    onKeyPressed: (PianoKey) -> Unit = {}
) {
    val lower = chord.lowerKey()
    val upper = chord.upperKey()
    PianoRoll(lower, upper, showNoteNames, chord, sizeScale, onKeyPressed)
}

@Composable
fun PianoRoll(
    from: PianoKey,
    to: PianoKey,
    showNoteNames: Boolean = false,
    highlightedKeys: List<PianoKey>? = null,
    sizeScale: Float = 1.5f,
    onKeyPressed: (PianoKey) -> Unit = {}
) {
    val startsAtF = from.note >= F
    Row {
        KeyDivider((BASE_KEY_HEIGHT * sizeScale).dp)
        for (octave in from.octave..to.octave) {
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
    highlightedKeys: List<PianoKey>? = null,
    sizeScale: Float = 1.5f,
    onKeyPressed: (PianoKey) -> Unit = {}
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
    highlightedKeys: List<PianoKey>? = null,
    sizeScale: Float = 1.5f,
    onKeyPressed: (PianoKey) -> Unit = {}
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
                highlightedKeys?.any { it.note == ACC_NOTES_FROM_F[index] && it.octave == actualOctave }
                    ?: false
            Ripple(bounded = true, color = Color.White) {
                Clickable(onClick = { onKeyPressed(PianoKey(ACC_NOTES_FROM_F[index], actualOctave)) }) {
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
                highlightedKeys?.any { it.note == ACC_NOTES[index] && it.octave == octave }
                    ?: false
            Ripple(bounded = true, color = Color.White) {
                Clickable(onClick = { onKeyPressed(PianoKey(ACC_NOTES[index], octave)) }) {
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
    highlightedKeys: List<PianoKey>? = null,
    sizeScale: Float = 1.5f,
    onKeyPressed: (PianoKey) -> Unit = {}
) {
    val width = (BASE_KEY_WIDTH * sizeScale).dp
    val height = (BASE_KEY_HEIGHT * sizeScale).dp

    Row {
        if (startFromF) {
            // We hit somewhere between 1 and 2 octaves here so we need
            // to shift the octave number up when we hit the next C note
            NATURAL_NOTES_FROM_F.forEachIndexed { index, note ->
                val actualOctave = if (index > 3) octave.plus(1) else octave
                val highlighted = highlightedKeys?.any { it.note == note && it.octave == actualOctave } ?: false
                Ripple(bounded = true) {
                    Clickable(onClick = { onKeyPressed(PianoKey(note, actualOctave)) }) {
                        PianoKey(highlighted = highlighted, width = width, height = height)
                    }
                }
                KeyDivider(height)
            }
        } else {
            NATURAL_NOTES.forEach { note ->
                val highlighted = highlightedKeys?.any { it.note == note && it.octave == octave } ?: false
                Ripple(bounded = true) {
                    Clickable(onClick = { onKeyPressed(PianoKey(note, octave)) }) {
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

    override fun toString(): String {
        return if (ACC_NOTES.contains(note)) {
            val shiftedNote = note.dec()
            "$shiftedNote#$octave"
        } else {
            "$note$octave"
        }
    }
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
    B(11);

    operator fun dec(): Note {
        val index = max(values().indexOf(this) - 1, 0).toInt()
        return values()[index]
    }
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

private const val BASE_KEY_WIDTH = 32
private const val BASE_KEY_HEIGHT = 128
private const val BASE_ACC_KEY_WIDTH = 22
private const val BASE_ACC_KEY_HEIGHT = 86

private val NATURAL_NOTES = listOf(C, D, E, F, G, A, B)
private val NATURAL_NOTES_FROM_F = listOf(F, G, A, B, C, D, E)
private val ACC_NOTES = listOf(Cs, Ds, Fs, Gs, As)
private val ACC_NOTES_FROM_F = listOf(Fs, Gs, As, Cs, Ds)

