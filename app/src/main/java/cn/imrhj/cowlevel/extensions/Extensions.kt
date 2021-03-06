package cn.imrhj.cowlevel.extensions

import androidx.lifecycle.LifecycleOwner
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.TextView
import cn.imrhj.cowlevel.App
import cn.imrhj.cowlevel.network.model.game.GameHomeModel
import cn.imrhj.cowlevel.ui.base.BaseFragment
import cn.imrhj.cowlevel.utils.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.*
import io.reactivex.android.MainThreadDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * 函数扩展中心
 * Created by rhj on 2017/11/28.
 */

fun <T : BaseFragment> Fragment.newInstance(fc: Class<T>, bundle: Bundle): T {
    val instance = fc.newInstance()
    instance.onConfigFragment(bundle)
    return instance
}

fun TextView.setTextAndShow(text: String?) {
    if (StringUtils.isNotBlank(text)) {
        this.text = text
        visibility = View.VISIBLE
    }
}

fun Request.toLogString(): String {
    return "Request: method=${method()}, url = ${url()},\nheader=${headers()}" +
            if (body() != null) ",\nbody=${body()}" else ""
}

fun <T> List<T>.getLastOrEmpty(): T? {
    if (this.isNotEmpty()) {
        return this[0]
    }
    return null
}

fun Fragment.getColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(context ?: App.app, id)
}

@Suppress("DEPRECATION")
fun String.parseHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, 0) else
        Html.fromHtml(this)
}

/**
 * 添加非空检查的添加函数
 */
fun <T> BaseQuickAdapter<T, BaseViewHolder>.addNullableData(data: T) {
    if (data != null) {
        addData(data)
    }
}

inline fun <S, T : S> Iterable<T>.reduceNullable(operation: (acc: S, T) -> S): S? {
    if (this.count() <= 0) {
        return null
    }
    return this.reduce(operation)
}

fun <T> Observable<T>.bindLifecycleOnMainThread(lifecycle: LifecycleOwner): ObservableSubscribeProxy<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable<T>(AndroidLifecycleScopeProvider.from(lifecycle)))
}

fun <T> Observable<T>.bindLifecycle(lifecycle: LifecycleOwner): ObservableSubscribeProxy<T> {
    return this.`as`(AutoDispose.autoDisposable<T>(AndroidLifecycleScopeProvider.from(lifecycle)))
}

fun Context.gotColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun View.clicks(): Observable<Unit> {
    return ViewClickObservable(this)
}

private fun checkMainThread(observer: Observer<*>): Boolean {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        observer.onSubscribe(Disposables.empty())
        observer.onError(IllegalStateException(
                "Expected to be called on the main thread but was " + Thread.currentThread().name))
        return false
    }
    return true
}

private class ViewClickObservable(private val view: View) : Observable<Unit>() {
    override fun subscribeActual(observer: Observer<in Unit>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnClickListener(listener)
    }

    private class Listener(private val view: View, private val observer: Observer<in Unit>)
        : MainThreadDisposable(), View.OnClickListener {
        override fun onClick(v: View) {
            if (!isDisposed) {
                observer.onNext(Unit)
            }
        }

        override fun onDispose() {
            view.setOnClickListener(null)
        }
    }
}