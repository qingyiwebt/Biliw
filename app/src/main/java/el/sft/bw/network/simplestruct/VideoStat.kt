package el.sft.bw.network.simplestruct


import com.google.gson.annotations.SerializedName


data class VideoStat(

    @SerializedName("view") var view: Long? = null,
    @SerializedName("like") var like: Int? = null,
    @SerializedName("danmaku") var danmaku: Int? = null

)