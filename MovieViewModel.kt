package com.demomvvm.MVVM

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.demomvvm.MVVM.GiftCard.GiftCardMovies
import com.demomvvm.MVVM.GiftCard.GiftCardResult
import com.demomvvm.MVVM.RoomDatabase.Book
import com.demomvvm.MVVM.RoomDatabase.BookDatabase
import kotlinx.coroutines.launch
import retrofit2.*
import kotlin.concurrent.thread


class MovieViewModel : ViewModel() {

    //not use this
    private var movieLiveData = MutableLiveData<List<Result>>()
    fun getPopularMovies() {
        RetrofitInstance.api.getPopularMovies("69d66957eebff9666ea46bd464773cf0").enqueue(object  : Callback<Movies>{
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.body()!=null){
                    movieLiveData.value = response.body()!!.results
                }
                else{
                    return
                }
            }
            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.i("TAG",t.message.toString())
            }
        })
    }

    //not use this
    fun observeMovieLiveData() : LiveData<List<Result>> {
        return movieLiveData
    }

    private var movieLiveData2 = MutableLiveData<List<GiftCardResult>>()
    fun getPopularMovies2(con:Context, progressBar:ProgressBar) {
        progressBar.visibility = View.VISIBLE
        val hashMap : HashMap<String, String> = HashMap()
        hashMap["site_id"] = "117"
        hashMap["user_id"] = "7178"
        hashMap["page_number"] = "0"
        hashMap["category_id"] = ""
        RetrofitInstance.api.getEgiftCardLiveURL(hashMap).enqueue(object : Callback<GiftCardMovies>{
            override fun onResponse(call: Call<GiftCardMovies>, response: Response<GiftCardMovies>) {
                progressBar.visibility = View.INVISIBLE
                Log.i("My MovieViewModel","onResponse response.body() = "+response.body().toString())

                if (response.body()!=null){
                    Log.i("My MovieViewModel","onResponse response.body()!!.data = "+response.body()!!.data.toString())

                    if(response.body()!!.status == 200){
                        movieLiveData2.value = response.body()!!.data
                        deleteAndInsertData(response.body()!!.data,con)
                    }
                    else{
                        Toast.makeText(con, "Something Went Wrong!", Toast.LENGTH_LONG).show()
                    }

                    /*val db = Room.databaseBuilder(con, BookDatabase::class.java, "book_database").build()
                    val bookDao = db.bookDao()
                    bookDao.deleteAllBooks()
                    bookDao.insertBook(Book(0,"Java","Alex"))*/

                }
                else{
                    return
                }
            }
            override fun onFailure(call: Call<GiftCardMovies>, t: Throwable) {
                progressBar.visibility = View.INVISIBLE
                Log.i("My MovieViewModel","onFailure Throwable t = "+t.message.toString())
            }
        })
    }
    fun observeMovieLiveData2() : LiveData<List<GiftCardResult>> {
        //continue observe data
        return movieLiveData2
    }

    fun deleteAndInsertData(array: List<GiftCardResult>, con: Context) {

        //first remove all old data from room and then insert new data from api
        val db = Room.databaseBuilder(con, BookDatabase::class.java, "book_database").allowMainThreadQueries().build()
        val bookDao = db.bookDao()
        bookDao.deleteAllBooks()

        //and then insert new data from api
        viewModelScope.launch {
            for(i in array.indices){
                bookDao.insertBook(Book(0,/* array[i].ecard_id, */array[i].ecard_name, array[i].ecard_image))
            }
            val books = bookDao.getAllBooks()
            for(book in books){
                Log.i("My MovieViewModel","id: ${book.id} ecard_name: ${book.name} ecard_image: ${book.author}")
            }
        }
    }



}