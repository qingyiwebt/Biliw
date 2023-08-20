package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.simplestruct.VideoStreamDurl

data class VideoStreamResponse(
    @SerializedName("last_play_time") var lastPlayTime: Int? = null,
    @SerializedName("durl") var durl: ArrayList<VideoStreamDurl> = arrayListOf(),
)
