package cn.imrhj.cowlevel.ui.adapter.provider.game

import android.graphics.Rect
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import cn.imrhj.cowlevel.App
import cn.imrhj.cowlevel.R
import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.network.model.game.FeatureImageListModel
import cn.imrhj.cowlevel.ui.view.model.PreviewImageModel
import cn.imrhj.cowlevel.utils.ScreenSizeUtil.dp2px
import cn.imrhj.cowlevel.utils.cdnImageForSize
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.previewlibrary.GPreviewBuilder

class GameImageProvider : BaseItemProvider<FeatureImageListModel, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_game_images
    }

    override fun viewType(): Int {
        return ItemTypeEnum.TYPE_GAME_IMAGE.ordinal
    }

    override fun convert(helper: BaseViewHolder, data: FeatureImageListModel, position: Int) {
        helper.setText(R.id.tv_title, "相关图片 （${data.photoCount}）")
        val parent = helper.getView<LinearLayout>(R.id.ll_image)
        data.list?.forEachIndexed { index, imageModel ->
            val image = ImageView(parent.context)
            val height = dp2px(170)
            Glide.with(parent)
                    .load(cdnImageForSize(imageModel.pic, 0, height))
                    .into(image)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height)
            params.marginEnd = dp2px(8)
            image.tag = index
            image.setOnClickListener {
                val previewModelList = mutableListOf<PreviewImageModel>()
                data.list.forEachIndexed { innerIndex, innerModel ->
                    val innerImage = parent.findViewWithTag<ImageView>(innerIndex)
                    val bounds = Rect()
                    innerImage.getGlobalVisibleRect(bounds)
                    previewModelList.add(innerIndex, PreviewImageModel(innerModel.pic, null, bounds))
                }

                GPreviewBuilder.from(App.app.getLastActivity())
                        .setCurrentIndex(index)
                        .setData(previewModelList)
                        .setType(GPreviewBuilder.IndicatorType.Number)
                        .start()
            }
            parent.addView(image, params)
        }
        val moreView = LayoutInflater.from(parent.context).inflate(R.layout.item_image_more, parent, false)
        moreView.setOnClickListener {
            //todo 跳转到更多图片页面
        }
        parent.addView(moreView)
    }
}
