package com.duyguabbasoglu.hw2.adapter

import com.duyguabbasoglu.hw2.model.Tupper

interface TupperListener {
    fun onTupperClicked(tupper: Tupper)
    fun onTupperDeleted(tupper: Tupper)
}