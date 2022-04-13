package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.GameDetail
import com.google.gson.annotations.SerializedName

data class GameDetailsResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_original")
    val nameOriginal: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("metacritic")
    val metaCritic: Int,
    @SerializedName("metacritic_platforms")
    val metaCriticPlatforms: List<MetaCriticPlatformResponse>,
    @SerializedName("released")
    val released: String,
    @SerializedName("tba")
    val tba: Boolean,
    @SerializedName("updated")
    val updated: String,
    @SerializedName("background_image")
    val backgroundImage: String,
    @SerializedName("background_image_additional")
    val backgroundImageAdditional: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("rating_top")
    val ratingTop: Int,
    @SerializedName("ratings")
    val ratings: List<RatingResponse>,
    @SerializedName("reactions")
    val reactions: ReactionResponse,
    @SerializedName("added")
    val added: Int,
    @SerializedName("added_by_status")
    val addedByStatus: AddedByStatusResponse,
    @SerializedName("playtime")
    val playtime: Int,
    @SerializedName("screenshots_count")
    val screenshotsCount: Int,
    @SerializedName("movies_count")
    val moviesCount: Int,
    @SerializedName("creators_count")
    val creatorsCount: Int,
    @SerializedName("achievements_count")
    val achievementsCount: Int,
    @SerializedName("parent_achievements_count")
    val parentAchievementsCount: Int,
    @SerializedName("reddit_url")
    val redditUrl: String,
    @SerializedName("reddit_name")
    val redditName: String,
    @SerializedName("reddit_description")
    val redditDescription: String,
    @SerializedName("reddit_logo")
    val redditLogo: String,
    @SerializedName("reddit_count")
    val redditCount: Int,
    @SerializedName("twitch_count")
    val twitchCount: Int,
    @SerializedName("youtube_count")
    val youtubeCount: Int,
    @SerializedName("reviews_text_count")
    val reviewsTextCount: Int,
    @SerializedName("ratings_count")
    val ratingsCount: Int,
    @SerializedName("suggestions_count")
    val suggestionsCount: Int,
    @SerializedName("alternative_names")
    val alternativeNames: List<String>,
    @SerializedName("metacritic_url")
    val metaCriticUrl: String,
    @SerializedName("parents_count")
    val parentsCount: Int,
    @SerializedName("additions_count")
    val additionsCount: Int,
    @SerializedName("game_series_count")
    val gameSeriesCount: Int,
    @SerializedName("user_game")
    val userGame: String,
    @SerializedName("reviews_count")
    val reviewsCount: Int,
    @SerializedName("saturated_color")
    val saturatedColor: String,
    @SerializedName("dominant_color")
    val dominantColor: String,
    @SerializedName("parent_platforms")
    val parentPlatforms: List<ParentPlatformResponse>,
    @SerializedName("platforms")
    val platforms: List<PlatformResponse>,
    @SerializedName("stores")
    val stores: List<StoreResponse>,
    @SerializedName("developers")
    val developers: List<DeveloperResponse>,
    @SerializedName("genres")
    val genres: List<GenreResponse>,
    @SerializedName("tags")
    val tags: List<TagResponse>,
    @SerializedName("publishers")
    val publishers: List<PublisherResponse>,
    @SerializedName("esrb_rating")
    val esrbRating: EsrbRatingResponse,
    @SerializedName("clip")
    val clip: String,
    @SerializedName("description_raw")
    val descriptionRaw: String
) {

    fun toEntity() = GameDetail(
        id,
        name,
        descriptionRaw,
        rating,
        released,
        backgroundImage,
        parentPlatforms.map { it.toEntity() },
        platforms.map { it.toEntity() },
        stores.map { it.toEntity() },
        developers.map { it.toEntity() },
        genres.map { it.toEntity() },
        publishers.map { it.toEntity() }
    )
}