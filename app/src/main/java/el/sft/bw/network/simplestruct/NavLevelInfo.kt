package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName


data class NavLevelInfo(
    @SerializedName("current_level") var currentLevel: Int? = null,
    @SerializedName("current_min") var currentMin: Int? = null,
    @SerializedName("current_exp") var currentExp: Int? = null,
    @SerializedName("next_exp") var nextExp: String? = null
)