package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName


data class NavOfficialVerify(
    @SerializedName("type") var type: Int? = null,
    @SerializedName("desc") var desc: String? = null
)