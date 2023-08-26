package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.model.VideoModel
import el.sft.bw.network.simplestruct.VideoDetailCard

data class VideoDetailResponse(
    @SerializedName("View") val videoModel: VideoModel,
    @SerializedName("Card") val card: VideoDetailCard,
)
