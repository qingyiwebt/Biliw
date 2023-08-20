package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName

data class UserSpaceResponse(
    @SerializedName("mid") var mid: Long? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("face") var face: String? = null,
    @SerializedName("is_senior_member") val isSeniorMember: Int? = null,
    @SerializedName("is_followed") val isFollowed: Boolean = false
)
