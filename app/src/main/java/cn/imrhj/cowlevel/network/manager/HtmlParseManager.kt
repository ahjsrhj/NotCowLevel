package cn.imrhj.cowlevel.network.manager

import android.widget.Toast
import cn.imrhj.cowlevel.App
import cn.imrhj.cowlevel.manager.UserManager
import cn.imrhj.cowlevel.network.exception.AuthException
import cn.imrhj.cowlevel.network.model.element.ElementHomeModel
import cn.imrhj.cowlevel.network.model.game.GameHomeModel
import cn.imrhj.cowlevel.network.model.home.FeedHomeModel
import cn.imrhj.cowlevel.network.parse.parseElementJSString
import cn.imrhj.cowlevel.network.parse.parseGameJSString
import cn.imrhj.cowlevel.network.parse.parseHomeJSString
import com.elvishew.xlog.XLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

const val SCRIPT_REGEX = "<script src=\"(https://cowlevel\\.net/(\\w+/)*_\\.\\w+\\.jo\\..{10}\\.js)\"></script>"

object HtmlParseManager {
    private val mScriptRegex by lazy { Regex(SCRIPT_REGEX) }

    private fun getJSData(url: String, index: Int, reverse: Boolean = true): Observable<String?> {
        return OkHttpManager.getServerData(COW_LEVEL_URL + url)
                .map {
                    if (it.indexOf("请填写账号与密码") > 0 && it.indexOf("账号验证") > 0) {
                        throw AuthException()
                    }
                    val result = mScriptRegex.findAll(it, it.indexOf("<body")).toList()
                    if (index > result.size) {
                        throw Exception("数据源错误!")
                    }
                    result[if (reverse) result.size - index else index].groups[1]?.value
                }
                .flatMap { OkHttpManager.getServerData(it) }
                .doOnError {
                    XLog.b().st(3).e("HtmlParseManager getJSData doOnError :$it")
                    if (it is AuthException) {
                        Observable.just(1)
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe { _ ->
                                    Toast.makeText(App.app.getLastActivity(), "认证失败,请重新登录", Toast.LENGTH_LONG).show()
                                    UserManager.logout()
                                }

                    }
                }
    }

    fun getHomeHtml(): Observable<FeedHomeModel> {
        return getJSData("feed", 1).map(parseHomeJSString)
    }

    fun getElement(id: Int): Observable<ElementHomeModel> {
        return getJSData("element/$id", 2).map(parseElementJSString)
    }

    fun getGame(name: String): Observable<GameHomeModel> {
        return getJSData("game/$name", 1).map(parseGameJSString)

    }
}