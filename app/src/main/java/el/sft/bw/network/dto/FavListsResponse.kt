package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.model.FavListModel

data class FavListsResponse(
    @SerializedName("count") val count: Int = -1,
    @SerializedName("list") val list: ArrayList<FavListModel> = arrayListOf()
)