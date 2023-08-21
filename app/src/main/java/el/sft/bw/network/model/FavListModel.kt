package el.sft.bw.network.model

import com.google.gson.annotations.SerializedName

data class FavListModel(
    @SerializedName("id") val id: Long = -1,
    @SerializedName("mid") val mid: Long = -1,
    @SerializedName("title") val title: String = ""
)
