package el.sft.bw.network.simplestruct


import com.google.gson.annotations.SerializedName


data class NavVipLabel(
    @SerializedName("path") var path: String? = null,
    @SerializedName("text") var text: String? = null,
    @SerializedName("label_theme") var labelTheme: String? = null,
    @SerializedName("text_color") var textColor: String? = null,
    @SerializedName("bg_style") var bgStyle: Int? = null,
    @SerializedName("bg_color") var bgColor: String? = null,
    @SerializedName("border_color") var borderColor: String? = null,
    @SerializedName("use_img_label") var useImgLabel: Boolean? = null,
    @SerializedName("img_label_uri_hans") var imgLabelUriHans: String? = null,
    @SerializedName("img_label_uri_hant") var imgLabelUriHant: String? = null,
    @SerializedName("img_label_uri_hans_static") var imgLabelUriHansStatic: String? = null,
    @SerializedName("img_label_uri_hant_static") var imgLabelUriHantStatic: String? = null
)