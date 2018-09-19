package cn.imrhj.cowlevel.ui.adapter.provider

import cn.imrhj.cowlevel.R
import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.network.model.game.GameModel
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider

class GameHeaderProvider : BaseItemProvider<GameModel, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_game_header
    }

    override fun viewType(): Int {
        return ItemTypeEnum.TYPE_GAME_HEADER.ordinal
    }

    override fun convert(helper: BaseViewHolder?, data: GameModel?, position: Int) {
    }
}