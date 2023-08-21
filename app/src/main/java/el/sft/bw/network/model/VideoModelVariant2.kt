package el.sft.bw.network.model

import com.google.gson.annotations.SerializedName

data class VideoModelVariant2(
    @SerializedName("bvid") var bvid: String? = null,
    @SerializedName("pic") var pic: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("created") var createdDate: Long? = null,
    @SerializedName("author") var author: String? = null,
    @SerializedName("play") var playCount: Long? = 0,
)
