package com.example.movie.model

import retrofit2.Response
import retrofit2.http.GET

interface SimpleApi {

    @GET ("trending/movie/day?api_key=c82fef4dc3aba619d211c8f365739545")
    suspend fun fetchMovies():Response<Posts>

}

/*
{
    "page": 1,
    "results": [
    {
        "adult": false,
        "backdrop_path": "/44immBwzhDVyjn87b3x3l9mlhAD.jpg",
        "id": 934433,
        "title": "Scream VI",
        "original_language": "en",
        "original_title": "Scream VI",
        "overview": "Following the latest Ghostface killings, the four survivors leave Woodsboro behind and start a fresh chapter.",
        "poster_path": "/wDWwtvkRRlgTiUr6TyLSMX8FCuZ.jpg",
        "media_type": "movie",
        "genre_ids": [
        27,
        9648,
        53
        ],
        "popularity": 609.941,
        "release_date": "2023-03-08",
        "video": false,
        "vote_average": 7.374,
        "vote_count": 684
    },
    ],
}

*/