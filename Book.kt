package com.demomvvm.MVVM.RoomDatabase

import androidx.room.*


@Entity(tableName = "books_table")
data class Book(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var name: String,

    @ColumnInfo(name = "published_author")
    var author: String
)
