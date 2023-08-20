package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName

data class NavOfficial(
    @SerializedName("role") var role: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("type") var type: Int? = null
)