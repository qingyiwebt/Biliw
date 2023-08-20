package el.sft.bw.network.model

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.simplestruct.VideoOwner
import el.sft.bw.network.simplestruct.VideoPage
import el.sft.bw.network.simplestruct.VideoRcmdReason
import el.sft.bw.network.simplestruct.VideoStat

data class VideoModel(
    @SerializedName("id") var id: Long? = null,
    @SerializedName("bvid") var bvid: String? = null,
    @SerializedName("cid") var cid: Int? = null,
    @SerializedName("goto") var goto: String? = null,
    @SerializedName("uri") var uri: String? = null,
    @SerializedName("pic") var pic: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("pubdate") var pubdate: Long? = null,
    @SerializedName("owner") var owner: VideoOwner? = VideoOwner(),
    @SerializedName("stat") var stat: VideoStat? = VideoStat(),
    @SerializedName("avfeature") var avfeature: String? = null,
    @SerializedName("isfollowed") var isfollowed: Int? = null,
    @SerializedName("rcmdreason") var rcmdreason: VideoRcmdReason? = VideoRcmdReason(),
    @SerializedName("showinfo") var showinfo: Int? = null,
    @SerializedName("trackid") var trackid: String? = null,
    @SerializedName("videos") var videos: Int? = null,
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("pages") var pages: ArrayList<VideoPage> = arrayListOf(),
)