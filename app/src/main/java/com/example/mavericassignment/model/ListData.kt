package com.example.mavericassignment.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListData(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id:Int,

    val Search: ArrayList<MovieData> ?= null
)
