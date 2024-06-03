package com.demomvvm.MVVM.RoomDatabase

import androidx.room.*


@Dao
interface BookDao {

    //----------------------------------- By  @Insert, @Update, @Delete
    @Insert
    fun insertBook(book: Book)

    @Update
    fun updateBook(book: Book)

    @Delete
    fun deleteBook(book: Book)

    //-----------------------------------By  @Query

    @Query("SELECT * FROM books_table")
    fun getAllBooks(): List<Book>

    @Query("DELETE FROM books_table")
    fun deleteAllBooks()

    @Query("SELECT * FROM books_table WHERE id = :id")
    fun selectBySpecificBookId(id: Long): List<Book?>

    @Query("DELETE FROM books_table WHERE id = :id")
    fun deleteBySpecificBookId(id: Long)

    @Query("UPDATE books_table SET  name = :name WHERE id = :id")
    fun updateBySpecificBookId(name: String, id: Long)


}