package el.sft.bw.network.simplestruct

import com.google.gson.annotations.SerializedName

data class NavWallet(
    @SerializedName("mid") var mid: Long? = null,
    @SerializedName("bcoin_balance") var bcoinBalance: Int? = null,
    @SerializedName("coupon_balance") var couponBalance: Int? = null,
    @SerializedName("coupon_due_time") var couponDueTime: Long? = null
)