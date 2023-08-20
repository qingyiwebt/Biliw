package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.model.UserCardModel

data class VideoDetailCard(
    @SerializedName("card") val videoOwner: UserCardModel?,
    @SerializedName("following") val followed: Boolean?,
    @SerializedName("follower") val followerCount: Long?
)
