package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.model.UserCardModel

data class UserCardResponse(
    @SerializedName("card") var card: UserCardModel? = null
)
