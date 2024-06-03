package com.demomvvm.MVVM

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.demomvvm.MVVM.GiftCard.GiftCardMovieAdapter
import com.demomvvm.MVVM.GiftCard.GiftCardResult
import com.demomvvm.MVVM.RoomDatabase.Book
import com.demomvvm.MVVM.RoomDatabase.BookDao
import com.demomvvm.MVVM.RoomDatabase.BookDatabase
import com.demomvvm.R
import com.demomvvm.Singleton
import com.demomvvm.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//api calling using mvvm(observer)
//MainActivity=>MovieViewModel=>GiftCardMovieAdapter
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter : MovieAdapter
    private lateinit var GiftCardMovieAdapter : GiftCardMovieAdapter

    private lateinit var bookDao: BookDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))
        window.navigationBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))

        prepareRecyclerView()
        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        /*viewModel.getPopularMovies()
        viewModel.observeMovieLiveData().observe(this, Observer { movieList ->
            movieAdapter.setMovieList(movieList)
        })*/


        //1.room
        // - First Load From Room Database then load from API
        //allowMainThreadQueries = for use of  main thread's query other place(adapter,model)
        val db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book_database").allowMainThreadQueries().build()
        bookDao = db.bookDao()
        bookDao.insertBook(Book(0,"Java","Alex"))
        bookDao.insertBook(Book(0,"PHP","Mike"))
        bookDao.insertBook(Book(0,"Kotlin","Amelia"))
        val books = bookDao.getAllBooks()
        var array : List<GiftCardResult> = emptyList()
        for(book in books){
            Log.i("MyTAG","onCreate()  ---  id: ${book.id} name: ${book.name} author: ${book.author}")
            val giftCardRoom = GiftCardResult(book.id.toString(), book.name, book.author)
            ///array.add(giftCardRoom)
            array = array+giftCardRoom
        }
        //first time not come in this
        //every time if array not 0 then get from room
        if(array.size!=0){
            Log.i("MyTAG","onCreate() inside if(array.size!=0){} ")
            GiftCardMovieAdapter.setMovieList(array,false)
        }





        //2.api
        //then update new data from api and save in room
        viewModel.getPopularMovies2(applicationContext, binding.progressBar)
        viewModel.observeMovieLiveData2().observe(this, Observer { movieList ->
            Log.i("MyTAG","onCreate() inside observeMovieLiveData2() ")
            if(movieList.isNotEmpty()){
                GiftCardMovieAdapter.setMovieList(movieList,true)
            }
        })



        //val db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book_database").build()
        //bookDao = db.bookDao()
        //testDB()

    }


    private fun testDB(){
        lifecycleScope.launch(Dispatchers.IO) {


            val books = bookDao.getAllBooks()

            //Insert
            //if(books.size==0){
                Log.i("MyTAG","*****     Inserting 3 Books     **********")
                bookDao.insertBook(Book(0,"Java","Alex"))
                bookDao.insertBook(Book(0,"PHP","Mike"))
                bookDao.insertBook(Book(0,"Kotlin","Amelia"))
                Log.i("MyTAG","*****     Inserted 3 Books       **********")
                Log.i("MyTAG","*****  before  ${books.size} books there *****")
                //books = bookDao.getAllBooks()
            //}
            //Query
            Log.i("MyTAG","*****  after  ${books.size} books there *****")
            for(book in books){
                Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
            }


            // Updating a book
            Log.i("MyTAG","*****      Updating a book      **********")
            bookDao.updateBook(Book(1,"PHPUpdated","Mike"))
            //Query
            val books2 = bookDao.getAllBooks()
            Log.i("MyTAG","*****   ${books2.size} books there *****")
            for(book in books2){
                Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
            }


            // Deleting a book by whole object
            Log.i("MyTAG","*****       Deleting a book by whole object     **********")
            bookDao.deleteBook(Book(2,"Kotlin","Amelia"))
            //Query
            val books3 = bookDao.getAllBooks()
            Log.i("MyTAG","*****   ${books3.size} books there *****")
            for(book in books3){
                Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
            }


            // Deleting a book by specific id
            Log.i("MyTAG","*****       Deleting a book by specific id     **********")
            bookDao.deleteBySpecificBookId(1)
            val book4 = bookDao.getAllBooks()
            Log.i("MyTAG","*****   ${book4.size} books there *****")
            for(book in book4){
                Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
            }


            // Selecting a book by specific id
            Log.i("MyTAG","*****       Selecting a book by specific id    **********")
            val book5 = bookDao.selectBySpecificBookId(3) as ArrayList<Book>
            Log.i("MyTAG","*****   ${book5[0]}  *****")
            Log.i("MyTAG","*****  book4[0].id = ${book5[0].id}  *****")
            Log.i("MyTAG","*****  book4[0].name = ${book5[0].name}  *****")
            Log.i("MyTAG","*****  book4[0].author = ${book5[0].author}  *****")
            Log.i("MyTAG","*****************************************************")


            // Updating a book by specific id
            Log.i("MyTAG","*****       Updating name of a book by specific id    **********")
            val book6 = bookDao.updateBySpecificBookId("DP",3)
            Log.i("MyTAG","*****   ${book6}  *****")
            val book7 = bookDao.getAllBooks()
            for(book in book7){
                Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
            }
            Log.i("MyTAG","*****************************************************")



        }
    }


    /*private fun prepareRecyclerView() {
        movieAdapter = MovieAdapter()
        binding.rvMovies.apply {
            //layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
            layoutManager = GridLayoutManager(applicationContext,2)
            adapter = movieAdapter
        }
    }*/

    private fun prepareRecyclerView() {
        GiftCardMovieAdapter = GiftCardMovieAdapter()
        binding.rvMovies.apply {
            //layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
            layoutManager = GridLayoutManager(applicationContext,2)
            adapter = GiftCardMovieAdapter
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }




}
