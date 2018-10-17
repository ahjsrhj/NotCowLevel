package cn.imrhj.cowlevel.ui.adapter.provider.game

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.imrhj.cowlevel.R
import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.manager.SchemeUtils
import cn.imrhj.cowlevel.network.model.common.UrlListModel
import cn.imrhj.cowlevel.utils.cdnImageForDPSquare
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider

class GameStoreProvider : BaseItemProvider<UrlListModel, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_game_store
    }

    override fun viewType(): Int {
        return ItemTypeEnum.TYPE_URLS.ordinal
    }

    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder, data: UrlListModel, position: Int) {
        helper.setText(R.id.tv_title, "购买与下载")
        val parent = helper.getView<LinearLayout>(R.id.ll_store)
        data.list.forEach {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_game_store_row, parent, false)
            itemView.findViewById<TextView>(R.id.tv_name).text = it?.name
            val imageView = itemView.findViewById<ImageView>(R.id.iv_store_tag)
            Glide.with(parent.context)
                    .load(cdnImageForDPSquare(it?.pic, 28))
                    .into(imageView)
            if (it?.data?.cnyPrice?.length ?: 0 > 0) {
                val priceView = itemView.findViewById<TextView>(R.id.tv_price)
                priceView.visibility = View.VISIBLE
                priceView.text = "¥ ${it?.data?.cnyPrice}"
            }
            itemView.setOnClickListener { _ ->
                if (it?.url?.length ?: 0 > 0) {
                    SchemeUtils.openWithChromeTabs(it?.url!!)
                }
            }
            parent.addView(itemView)
        }
    }
}