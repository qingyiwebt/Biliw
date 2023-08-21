package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.model.VideoModelVariant2

data class UserVideosList(
    @SerializedName("vlist") val videoList: List<VideoModelVariant2>
)
