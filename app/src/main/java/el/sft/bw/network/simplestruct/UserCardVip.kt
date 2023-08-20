package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName

data class UserCardVip(
    @SerializedName("vipType") var vipType: Int,
    @SerializedName("vipStatus") var vipStatus: Int
)
