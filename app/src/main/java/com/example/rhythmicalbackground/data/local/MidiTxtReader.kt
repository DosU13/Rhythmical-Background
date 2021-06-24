package com.example.rhythmicalbackground.data.local

import android.content.Context
import com.example.rhythmicalbackground.R

class MidiTxtReader(ctx: Context) {
    private val _midiMsgList = mutableListOf<Pair<Double, Int>>()
    private var midiMargin: Double = 0.0
    init {
        val lines = ctx.resources.openRawResource(R.raw.midi_highscore_panda_eyes).bufferedReader().readLines()
        midiMargin = lines[1].toDouble()
        for(line in lines.subList(3, lines.size)){
            val entries = line.split(" ")
            _midiMsgList.add(Pair(entries[0].toDouble() + midiMargin, entries[1].toInt()))
        }
    }

    val midiMsgList get() = _midiMsgList
}