package cn.imrhj.cowlevel.ui.adapter.provider.game

import cn.imrhj.cowlevel.R
import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.network.model.game.PublishContributorsModel
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider

class GameContributorProvider : BaseItemProvider<PublishContributorsModel, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_game_contributors
    }

    override fun viewType(): Int {
        return ItemTypeEnum.TYPE_CONTRIBUTORS.ordinal
    }

    override fun convert(helper: BaseViewHolder?, data: PublishContributorsModel?, position: Int) {
    }
}
