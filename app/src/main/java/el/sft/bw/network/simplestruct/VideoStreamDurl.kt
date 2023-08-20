package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName

data class VideoStreamDurl(
    @SerializedName("order") var order: Int? = null,
    @SerializedName("length") var length: Int? = null,
    @SerializedName("size") var size: Int? = null,
    @SerializedName("ahead") var ahead: String? = null,
    @SerializedName("vhead") var vhead: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("backup_url") var backupUrl: ArrayList<String> = arrayListOf()
)
