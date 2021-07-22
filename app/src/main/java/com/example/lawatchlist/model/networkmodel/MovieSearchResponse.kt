package com.example.lawatchlist.model.networkmodel


import android.text.Html
import com.example.lawatchlist.model.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
   @field:Json(name = "show") val movie: MovieAPIModel
)



fun List<SearchResponse>.toDomainAPIModel() : List<MovieAPIModel> = map { it.movie }

@JsonClass(generateAdapter = true)
data class MovieAPIModel(
   @field:Json(name = "id") val id: Int? = null,
   @field:Json(name = "name") val title: String,
   @field:Json(name = "summary") var summary: String?,
   @field:Json(name = "premiered") val releaseDate: String?,
   @field:Json(name = "runtime") val duration: Int? = "N/A".toIntOrNull(),
   @field:Json(name = "image") val imageUrl: Image?
) {
   init {
      // format the Json file's summary field from Html code to human-readable text
      summary?.let {
         summary = Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString()
      }
   }
}