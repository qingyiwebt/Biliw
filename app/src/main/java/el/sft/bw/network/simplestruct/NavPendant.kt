package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName


data class NavPendant(
    @SerializedName("pid") var pid: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("expire") var expire: Int? = null,
    @SerializedName("image_enhance") var imageEnhance: String? = null,
    @SerializedName("image_enhance_frame") var imageEnhanceFrame: String? = null
)