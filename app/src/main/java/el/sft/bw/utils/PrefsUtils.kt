package el.sft.bw.utils

import android.content.Context
import com.google.gson.Gson
import el.sft.bw.App
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


object PrefsUtils {
    private const val prefsId = "el.sft.bw"
    private const val cookieJarKey = "COOKIE_JAR"

    val currentPreferences =
        App.current.applicationContext.getSharedPreferences(prefsId, Context.MODE_PRIVATE)

    val currentCookieJar = PrefsCookieJar()

    class PrefsCookieJar : CookieJar {
        data class CookieStore(
            var cookieList: MutableList<Cookie> = ArrayList()
        )

        val cookieCount get() = cookieStore.cookieList.size
        private val cookieStore: CookieStore

        init {
            var cookieJarRaw = currentPreferences.getString(cookieJarKey, "")
            if (cookieJarRaw == null) cookieJarRaw = ""
            cookieStore =
                if (cookieJarRaw.isEmpty()) CookieStore()
                else Gson().fromJson(cookieJarRaw, CookieStore::class.java)
        }

        fun getCookieHeader(domain: String): String {
            return cookieStore.cookieList.filter { it.domain.contains(domain) }
                .joinToString(";") { it.name + "=" + it.value }
        }

        private fun mergeCookieList(cookies: List<Cookie>) {
            val origin = cookieStore.cookieList
            val list = cookies.toMutableList()
            for (i in 0 until origin.size) {
                val item = origin[i]
                val itemReplace =
                    list.firstOrNull { x ->
                        x.name == item.name
                                && x.domain == item.domain
                                && x.path == item.path
                    } ?: continue
                origin[i] = itemReplace
                list.remove(itemReplace)
            }
            for (item in list) origin.add(item)
        }

        private fun storeCookie() {
            val jsonStr = Gson().toJson(cookieStore)
            currentPreferences.edit()
                .putString(cookieJarKey, jsonStr)
                .apply()
        }

        fun clearCookies() {
            cookieStore.cookieList.clear()
            storeCookie()
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> = cookieStore.cookieList

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            mergeCookieList(cookies)
            storeCookie()
        }
    }
}