package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName

data class VideoPage(
    @SerializedName("cid") var cid: Int? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("part") var part: String? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("vid") var vid: String? = null,
    @SerializedName("weblink") var weblink: String? = null,
)
