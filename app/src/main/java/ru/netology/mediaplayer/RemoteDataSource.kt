package ru.netology.mediaplayer

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.netology.mediaplayer.model.Album

class RemoteDataSource {
    private val client = OkHttpClient.Builder().build()

    private val gson = Gson()
    private val type = object : TypeToken<Album>() {}.type

    fun getAlbum(): Album {
        val request: Request = Request.Builder()
            .url("https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/album.json")
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: error("body is null")
        return gson.fromJson(body, type)
    }
}