package el.sft.bw.network.simplestruct


import com.google.gson.annotations.SerializedName


data class VideoOwner(
    @SerializedName("mid") var mid: Long? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("face") var face: String? = null
)