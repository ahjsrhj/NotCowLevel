package cn.imrhj.cowlevel.ui.view.model

import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import com.previewlibrary.enitity.IThumbViewInfo

data class PreviewImageModel(private var url: String?, private var videoUrl: String?, private var bounds: Rect) : IThumbViewInfo {
    override fun getUrl(): String? {
        return this.url
    }

    override fun getVideoUrl(): String? {
        return this.videoUrl
    }

    override fun getBounds(): Rect {
        return this.bounds
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Rect::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(videoUrl)
        parcel.writeParcelable(bounds, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PreviewImageModel> {
        override fun createFromParcel(parcel: Parcel): PreviewImageModel {
            return PreviewImageModel(parcel)
        }

        override fun newArray(size: Int): Array<PreviewImageModel?> {
            return arrayOfNulls(size)
        }
    }

}

