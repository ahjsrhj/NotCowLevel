package cn.imrhj.cowlevel.utils

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import cn.imrhj.cowlevel.App

/**
 * Created by rhj on 12/12/2017.
 */
object ResourcesUtils {
    private val res = App.app.resources

    @ColorInt
    fun getColor(@ColorRes id: Int): Int {
        return ResourcesCompat.getColor(res, id, null)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ResourcesCompat.getDrawable(res, id, null)
    }

}
