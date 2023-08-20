package el.sft.bw.network.simplestruct


import com.google.gson.annotations.SerializedName


data class NavVip(
    @SerializedName("type") var type: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("due_date") var dueDate: Long? = null,
    @SerializedName("vip_pay_type") var vipPayType: Int? = null,
    @SerializedName("theme_type") var themeType: Int? = null,
    @SerializedName("label") var label: NavLabel? = NavLabel(),
    @SerializedName("avatar_subscript") var avatarSubscript: Int? = null,
    @SerializedName("nickname_color") var nicknameColor: String? = null,
    @SerializedName("role") var role: Int? = null,
    @SerializedName("avatar_subscript_url") var avatarSubscriptUrl: String? = null,
    @SerializedName("tv_vip_status") var tvVipStatus: Int? = null,
    @SerializedName("tv_vip_pay_type") var tvVipPayType: Int? = null,
    @SerializedName("tv_due_date") var tvDueDate: Long? = null
)