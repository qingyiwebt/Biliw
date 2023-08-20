package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName

data class QrCodeLoginResponse(
    @SerializedName("url")
    val url: String = "",
    @SerializedName("oauthKey")
    val authKey: String = "",
)
