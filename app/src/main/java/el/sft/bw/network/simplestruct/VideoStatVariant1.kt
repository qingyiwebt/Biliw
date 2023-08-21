package el.sft.bw.network.simplestruct


import com.google.gson.annotations.SerializedName


data class VideoStatVariant1(
    @SerializedName("play") var view: Long? = null,
    @SerializedName("collect") var collect: Int? = null,
    @SerializedName("danmaku") var danmaku: Int? = null
)