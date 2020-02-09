package com.citizenwarwick.pianoroll

import com.citizenwarwick.music.Note
import com.citizenwarwick.music.PitchClass
import com.citizenwarwick.music.between
import junit.framework.Assert.assertEquals
import org.junit.Test

class NotesTest {
    @Test
    fun betweenTest() {
        val numNoteBetween = between(Note(PitchClass.C, 0), Note(PitchClass.C, 0))
        assertEquals(0, numNoteBetween)

        val numNoteBetween2 = between(Note(PitchClass.C, 0), Note(PitchClass.B, 0))
        assertEquals(11, numNoteBetween2)

        val numNoteBetween3 = between(Note(PitchClass.C, 0), Note(PitchClass.C, 1))
        assertEquals(12, numNoteBetween3)

        val numNoteBetween4 = between(Note(PitchClass.C, 0), Note(PitchClass.Cs, 1))
        assertEquals(13, numNoteBetween4)

        val numNoteBetween5 = between(Note(PitchClass.C, 0), Note(PitchClass.C, 2))
        assertEquals(24, numNoteBetween5)
    }
}
