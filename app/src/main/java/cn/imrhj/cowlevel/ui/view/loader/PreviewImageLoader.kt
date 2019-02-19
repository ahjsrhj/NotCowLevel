package cn.imrhj.cowlevel.ui.view.loader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.previewlibrary.loader.IZoomMediaLoader
import com.previewlibrary.loader.MySimpleTarget

class PreviewImageLoader : IZoomMediaLoader {
    private fun <T> getListener(simpleTarget: MySimpleTarget): RequestListener<T> {
        return object : RequestListener<T> {
            override fun onLoadFailed(e: GlideException?, model: Any?,
                                      target: Target<T>?,
                                      isFirstResource: Boolean): Boolean {
                simpleTarget.onLoadFailed(null)
                return false
            }

            override fun onResourceReady(resource: T?, model: Any?,
                                         target: Target<T>?, dataSource: DataSource?,
                                         isFirstResource: Boolean): Boolean {
                simpleTarget.onResourceReady()
                return false
            }

        }
    }

    override fun displayImage(context: Fragment, path: String, imageView: ImageView, simpleTarget: MySimpleTarget) {
        Glide.with(context)
                .asBitmap()
                .load(path)
                .listener(getListener(simpleTarget))
                .into(imageView)
    }

    override fun displayGifImage(context: Fragment, path: String, imageView: ImageView, simpleTarget: MySimpleTarget) {
        Glide.with(context)
                .asGif()
                .load(path)
                .dontAnimate()
                .listener(getListener(simpleTarget))
                .into(imageView)
    }

    override fun clearMemory(c: Context) {
        Glide.get(c).clearMemory()
    }


    override fun onStop(context: Fragment) {
        Glide.with(context).onStop()
    }
}