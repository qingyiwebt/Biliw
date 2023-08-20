package el.sft.bw.network.dto

import com.google.gson.annotations.SerializedName
import el.sft.bw.network.simplestruct.NavLevelInfo
import el.sft.bw.network.simplestruct.NavOfficial
import el.sft.bw.network.simplestruct.NavOfficialVerify
import el.sft.bw.network.simplestruct.NavPendant
import el.sft.bw.network.simplestruct.NavVip
import el.sft.bw.network.simplestruct.NavVipLabel
import el.sft.bw.network.simplestruct.NavWallet
import el.sft.bw.network.simplestruct.NavWbiImg

data class NavResponse(
    @SerializedName("isLogin") var isLogin: Boolean? = null,
    @SerializedName("email_verified") var emailVerified: Int? = null,
    @SerializedName("face") var face: String? = null,
    @SerializedName("face_nft") var faceNft: Long? = null,
    @SerializedName("face_nft_type") var faceNftType: Int? = null,
    @SerializedName("level_info") var levelInfo: NavLevelInfo? = NavLevelInfo(),
    @SerializedName("mid") var mid: Long? = null,
    @SerializedName("mobile_verified") var mobileVerified: Int? = null,
    @SerializedName("money") var money: Double? = null,
    @SerializedName("moral") var moral: Int? = null,
    @SerializedName("official") var official: NavOfficial? = NavOfficial(),
    @SerializedName("officialVerify") var officialVerify: NavOfficialVerify? = NavOfficialVerify(),
    @SerializedName("pendant") var pendant: NavPendant? = NavPendant(),
    @SerializedName("scores") var scores: Int? = null,
    @SerializedName("uname") var uname: String? = null,
    @SerializedName("vipDueDate") var vipDueDate: Long? = null,
    @SerializedName("vipStatus") var vipStatus: Int? = null,
    @SerializedName("vipType") var vipType: Int? = null,
    @SerializedName("vip_pay_type") var vipPayType: Int? = null,
    @SerializedName("vip_theme_type") var vipThemeType: Int? = null,
    @SerializedName("vip_label") var vipLabel: NavVipLabel? = NavVipLabel(),
    @SerializedName("vip_avatar_subscript") var vipAvatarSubscript: Int? = null,
    @SerializedName("vip_nickname_color") var vipNicknameColor: String? = null,
    @SerializedName("vip") var vip: NavVip? = NavVip(),
    @SerializedName("wallet") var wallet: NavWallet? = NavWallet(),
    @SerializedName("has_shop") var hasShop: Boolean? = null,
    @SerializedName("shop_url") var shopUrl: String? = null,
    @SerializedName("allowance_count") var allowanceCount: Int? = null,
    @SerializedName("answer_status") var answerStatus: Int? = null,
    @SerializedName("is_senior_member") var isSeniorMember: Int? = null,
    @SerializedName("wbi_img") var wbiImg: NavWbiImg? = NavWbiImg(),
    @SerializedName("is_jury") var isJury: Boolean? = null
)