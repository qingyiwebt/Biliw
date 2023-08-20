package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName


data class VideoRcmdReason (

    @SerializedName("content"    ) var content    : String? = null,
    @SerializedName("reasontype" ) var reasontype : Int?    = null

)