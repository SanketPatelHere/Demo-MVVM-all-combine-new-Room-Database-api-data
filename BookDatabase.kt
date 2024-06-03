package com.demomvvm.MVVM.RoomDatabase

import androidx.room.*

@Database(entities = [Book::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
