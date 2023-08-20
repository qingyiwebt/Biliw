package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.model.VideoModel

data class RecommendedVideosResponse(
    @SerializedName("item") var item: ArrayList<VideoModel> = arrayListOf()
)
