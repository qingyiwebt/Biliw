package el.sft.bw.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import el.sft.bw.network.dto.BaseResponse
import el.sft.bw.network.dto.FavListsResponse
import el.sft.bw.network.dto.FavVideosResponse
import el.sft.bw.network.dto.NavResponse
import el.sft.bw.network.dto.QrCodeLoginResponse
import el.sft.bw.network.dto.RecommendedVideosResponse
import el.sft.bw.network.dto.SearchResponse
import el.sft.bw.network.dto.UserCardResponse
import el.sft.bw.network.dto.VideoDetailResponse
import el.sft.bw.network.dto.VideoStreamResponse
import el.sft.bw.network.dto.UserSpaceResponse
import el.sft.bw.network.dto.UserVideosResponse
import el.sft.bw.network.model.VideoModel
import el.sft.bw.utils.CryptoUtils
import el.sft.bw.utils.GlobalHttpClientUtils
import el.sft.bw.utils.PrefsUtils
import okhttp3.FormBody
import okhttp3.Request
import java.net.URLEncoder
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

object ApiClient {
    private val baseResponseWithNavResponseType =
        object : TypeToken<BaseResponse<NavResponse>>() {}.type

    private val baseResponseWithRecommendedVideosResponseType =
        object : TypeToken<BaseResponse<RecommendedVideosResponse>>() {}.type

    private val baseResponseWithQrCodeLoginResponseType =
        object : TypeToken<BaseResponse<QrCodeLoginResponse>>() {}.type

    private val baseResponseWithVideoModelType =
        object : TypeToken<BaseResponse<VideoModel>>() {}.type

    private val baseResponseWithAnyType =
        object : TypeToken<BaseResponse<Any>>() {}.type

    private val baseResponseWithVideoStreamResponseType =
        object : TypeToken<BaseResponse<VideoStreamResponse>>() {}.type

    private val baseResponseWithVideoDetailResponseType =
        object : TypeToken<BaseResponse<VideoDetailResponse>>() {}.type

    private val baseResponseWithUserCardResponseType =
        object : TypeToken<BaseResponse<UserCardResponse>>() {}.type

    private val baseResponseWithUserSpaceModelType =
        object : TypeToken<BaseResponse<UserSpaceResponse>>() {}.type

    private val baseResponseWithFavListsResponseType =
        object : TypeToken<BaseResponse<FavListsResponse>>() {}.type

    private val baseResponseWithFavVideosResponseType =
        object : TypeToken<BaseResponse<FavVideosResponse>>() {}.type

    private val baseResponseWithUserVideosResponseType =
        object : TypeToken<BaseResponse<UserVideosResponse>>() {}.type

    private val baseResponseWithSearchResponseType =
        object : TypeToken<BaseResponse<SearchResponse>>() {}.type

    private val baseResponseWithIntType =
        object : TypeToken<BaseResponse<Int>>() {}.type

    private var wbiKey = ""

    fun generateSignedQuery(map: MutableMap<String, String> = HashMap()): String {
        if (wbiKey.isEmpty()) refreshWbiKey()

        map["wts"] = (System.currentTimeMillis() / 1000).toString()
        val queryParams = map.entries
            .sortedBy { it.key }
            .joinToString("&") { it.key + "=" + URLEncoder.encode(it.value, "utf-8") }
        return queryParams + "&w_rid=" + CryptoUtils.md5(queryParams + wbiKey)
    }

    fun refreshWbiKey() {
        val res = getUserInfoFromNav()
        if (res.data == null) throw Exception("Failed to refresh wbi key")

        val data = res.data.wbiImg!!
        val imgKey = Path(data.imgUrl!!).nameWithoutExtension
        val subKey = Path(data.subUrl!!).nameWithoutExtension
        wbiKey = CryptoUtils.getWbiKey(imgKey, subKey)
    }

    fun getCsrf() = PrefsUtils.currentCookieJar.getCookie("bili_jct")

    fun requestQrCodeLogin(): BaseResponse<QrCodeLoginResponse> {
        val query = generateSignedQuery()
        val req = Request.Builder()
            .url("https://passport.bilibili.com/qrcode/getLoginUrl?$query")
            .header("Referer", "https://www.bilibili.com/")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithQrCodeLoginResponseType)
    }

    fun finishQrCodeLogin(authKey: String): BaseResponse<Any> {
        val query = generateSignedQuery()
        val req = Request.Builder()
            .url("https://passport.bilibili.com/qrcode/getLoginInfo?$query")
            .header("Referer", "https://www.bilibili.com/")
            .post(
                FormBody.Builder()
                    .add("oauthKey", authKey)
                    .build()
            )
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()
        return Gson().fromJson(str, baseResponseWithAnyType)
    }

    fun getRecommendedVideos(): BaseResponse<RecommendedVideosResponse> {
        val query = generateSignedQuery()
        val req = Request.Builder()
            .url("https://api.bilibili.com/x/web-interface/index/top/rcmd?$query")
            .header("Referer", "https://www.bilibili.com/")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithRecommendedVideosResponseType)
    }

    fun getUserInfoFromNav(): BaseResponse<NavResponse> {
        val req = Request.Builder()
            .url("https://api.bilibili.com/x/web-interface/nav")
            .header("Referer", "https://www.bilibili.com")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithNavResponseType)
    }

    fun getVideoInfo(bvId: String): BaseResponse<VideoModel> {
        val query = generateSignedQuery(
            mutableMapOf(
                "bvid" to bvId
            )
        )
        val req = Request.Builder()
            .url("https://api.bilibili.com/x/web-interface/view?$query")
            .header("Referer", "https://www.bilibili.com/video/$bvId")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithVideoModelType)
    }

    fun getVideoDetail(bvId: String): BaseResponse<VideoDetailResponse> {
        val query = generateSignedQuery(
            mutableMapOf(
                "bvid" to bvId
            )
        )

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/web-interface/view/detail?$query")
            .header("Referer", "https://www.bilibili.com/video/$bvId")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithVideoDetailResponseType)
    }

    fun getUserCardInfo(userId: Long): BaseResponse<UserCardResponse> {
        val query = generateSignedQuery(
            mutableMapOf(
                "mid" to userId.toString()
            )
        )

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/web-interface/card?$query")
            .header("Referer", "https://space.bilibili.com/$userId")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithUserCardResponseType)
    }

    fun getUserSpaceInfo(userId: Long): BaseResponse<UserSpaceResponse> {
        val query = generateSignedQuery(
            mutableMapOf(
                "mid" to userId.toString()
            )
        )

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/space/wbi/acc/info?$query")
            .header("Referer", "https://space.bilibili.com/$userId")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithUserSpaceModelType)
    }

    fun reportHistory(bvId: String, cid: Int) {
        val query = generateSignedQuery()

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/click-interface/web/heartbeat?$query")
            .header("Referer", "https://www.bilibili.com/video/$bvId")
            .post(
                FormBody.Builder()
                    .add("bvid", bvId)
                    .add("cid", cid.toString())
                    .add("dt", "2")
                    .add("played_time", "0")
                    .add("realtime", "10")
                    .add("play_type", "0")
                    .add("type", "3")
                    .add("csrf", getCsrf())
                    .build()
            )
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()
        Log.v("ApiClient", str ?: "")
    }

    fun getVideoStream(
        bvId: String,
        cId: Int,
        quality: Int = 32
    ): BaseResponse<VideoStreamResponse> {
        val query = generateSignedQuery(
            mutableMapOf(
                "bvid" to bvId,
                "cid" to cId.toString(),
                "qn" to quality.toString(),
                "fnval" to "1",
            )
        )
        val req = Request.Builder()
            .url("https://api.bilibili.com/x/player/playurl?$query")
            .header("Referer", "https://www.bilibili.com/video/$bvId")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithVideoStreamResponseType)
    }

    fun getFavLists(userId: Long): BaseResponse<FavListsResponse> {
        val query = generateSignedQuery(
            mutableMapOf(
                "up_mid" to userId.toString()
            )
        )

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/v3/fav/folder/created/list-all?$query")
            .header("Referer", "https://space.bilibili.com/$userId")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithFavListsResponseType)
    }

    fun getFavVideos(mlid: Long, page: Int): BaseResponse<FavVideosResponse> {
        val query = generateSignedQuery(
            mutableMapOf(
                "media_id" to mlid.toString(),
                "ps" to "20",
                "pn" to page.toString()
            )
        )

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/v3/fav/resource/list?$query")
            .header("Referer", "https://space.bilibili.com/")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithFavVideosResponseType)
    }

    fun getUserVideos(userId: Long, page: Int): BaseResponse<UserVideosResponse> {
        val query = generateSignedQuery(
            mutableMapOf(
                "mid" to userId.toString(),
                "ps" to "20",
                "pn" to page.toString()
            )
        )

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/space/wbi/arc/search?$query")
            .header("Referer", "https://space.bilibili.com/$userId")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        Log.v("Request", str.toString())

        return Gson().fromJson(str, baseResponseWithUserVideosResponseType)
    }


    fun setLikeState(bvId: String, videoLiked: Boolean): BaseResponse<Any> {
        val query = generateSignedQuery()

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/web-interface/archive/like?$query")
            .header("Referer", "https://www.bilibili.com/video/$bvId")
            .post(
                FormBody.Builder()
                    .add("bvid", bvId)
                    .add("like", if (videoLiked) "1" else "2")
                    .add("csrf", getCsrf())
                    .build()
            )
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithAnyType)
    }

    fun getLikedStat(bvId: String): BaseResponse<Int> {
        val query = generateSignedQuery(
            mutableMapOf(
                "bvid" to bvId
            )
        )

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/web-interface/archive/has/like?$query")
            .header("Referer", "https://www.bilibili.com/video/$bvId")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithIntType)
    }

    fun basicSearch(keyword: String): BaseResponse<SearchResponse> {
        val query = generateSignedQuery(
            mutableMapOf(
                "keyword" to keyword,
            )
        )

        val req = Request.Builder()
            .url("https://api.bilibili.com/x/web-interface/search/all/v2?$query")
            .header("Referer", "https://search.bilibili.com/")
            .get()
            .build()

        val res = GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()

        val str = res.body?.string()

        return Gson().fromJson(str, baseResponseWithSearchResponseType)
    }

    fun reloadCookie() {
        if (PrefsUtils.currentCookieJar.cookieCount != 0) return

        val req = Request.Builder()
            .url("https://www.bilibili.com")
            .get()
            .build()

        GlobalHttpClientUtils.httpClient
            .newCall(req)
            .execute()
    }
}