package com.demomvvm.MVVM.GiftCard

import com.demomvvm.MVVM.Result

data class GiftCardMovies (
    val page: Int,
    val status: Int,
    val data: List<GiftCardResult>,
    val total_pages: Int,
    val total_results: Int
)