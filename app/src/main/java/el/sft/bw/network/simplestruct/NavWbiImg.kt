package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName

data class NavWbiImg(
    @SerializedName("img_url") var imgUrl: String? = null,
    @SerializedName("sub_url") var subUrl: String? = null
)