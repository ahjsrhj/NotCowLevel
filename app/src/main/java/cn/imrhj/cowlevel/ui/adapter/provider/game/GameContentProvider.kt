package cn.imrhj.cowlevel.ui.adapter.provider.game

import cn.imrhj.cowlevel.R
import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.extensions.parseHtml
import cn.imrhj.cowlevel.network.model.game.ContentModel
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.ms.square.android.expandabletextview.ExpandableTextView

class GameContentProvider : BaseItemProvider<ContentModel, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_game_content
    }

    override fun viewType(): Int {
        return ItemTypeEnum.TYPE_GAME_CONTENT.ordinal
    }

    override fun convert(helper: BaseViewHolder, data: ContentModel, position: Int) {
        helper.setText(R.id.tv_title, "详细介绍")
        val contentView = helper.getView<ExpandableTextView>(R.id.et_content)
        contentView.text = data.content.parseHtml()
    }
}