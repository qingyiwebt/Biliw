package el.sft.bw.network.model

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.simplestruct.UserCardLevel
import el.sft.bw.network.simplestruct.UserCardVip

data class UserCardModel(
    @SerializedName("mid") var mid: Long? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("face") var face: String? = null,
    @SerializedName("fans") var followerCount: Long? = null,
    @SerializedName("attention") var followCount: Long? = null,
    @SerializedName("sign") var description: String? = null,
    @SerializedName("level_info") var levelInfo: UserCardLevel? = null,
    @SerializedName("vip") var vip: UserCardVip? = null
)
