package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName

data class UserCardLevel(
    @SerializedName("current_level") var level: Int
)
