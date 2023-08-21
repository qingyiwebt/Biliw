package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.simplestruct.UserVideosList

data class UserVideosResponse(
    @SerializedName("list") val videoList: UserVideosList
)
