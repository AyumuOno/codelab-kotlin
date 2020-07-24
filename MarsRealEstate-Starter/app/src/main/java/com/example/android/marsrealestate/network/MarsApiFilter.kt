package com.example.android.marsrealestate.network

import retrofit2.http.Query

enum class MarsApiFilter(val value: String) {
  SHOW_RENT("rent"),
  SHOW_BUY("buy"),
  SHOW_ALL("all")
}
