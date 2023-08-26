package el.sft.bw.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object GlobalHttpClientUtils {
    const val userAgent =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36"

    val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request()
            val newRequest: Request = request.newBuilder()
                .header("User-Agent", userAgent)
                .build()
            chain.proceed(newRequest)
        }
        .cookieJar(PrefsUtils.currentCookieJar)
        .followRedirects(false)
        .build()

    fun generateHeaders() = mutableMapOf(
        "Cookie" to PrefsUtils.currentCookieJar.getCookieHeader("bilibili.com"),
        "Referer" to "https://www.bilibili.com/",
        "User-Agent" to userAgent
    )
}