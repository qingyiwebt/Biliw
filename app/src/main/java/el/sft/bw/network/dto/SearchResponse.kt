package el.sft.bw.network.dto

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class SearchResponse (
    @SerializedName("result") val result: ArrayList<JsonObject>
)