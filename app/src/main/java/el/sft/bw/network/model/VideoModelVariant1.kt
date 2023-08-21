package el.sft.bw.network.model

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.simplestruct.VideoOwner
import el.sft.bw.network.simplestruct.VideoPage
import el.sft.bw.network.simplestruct.VideoRcmdReason
import el.sft.bw.network.simplestruct.VideoStat
import el.sft.bw.network.simplestruct.VideoStatVariant1

data class VideoModelVariant1(
    @SerializedName("bvid") var bvid: String? = null,
    @SerializedName("cover") var cover: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("pubdate") var pubdate: Long? = null,
    @SerializedName("upper") var upper: VideoOwner? = VideoOwner(),
    @SerializedName("cnt_info") var stat: VideoStatVariant1? = VideoStatVariant1(),
)