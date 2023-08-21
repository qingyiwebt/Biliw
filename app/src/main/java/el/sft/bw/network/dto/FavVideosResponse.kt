package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.model.VideoModelVariant1

data class FavVideosResponse(
    @SerializedName("medias") val items: ArrayList<VideoModelVariant1>
)
