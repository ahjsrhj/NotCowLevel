package cn.imrhj.cowlevel.network.model.game

import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.network.model.BaseModel

data class ContentModel(val content: String) : BaseModel() {
    override fun getType(): ItemTypeEnum {
        return ItemTypeEnum.TYPE_GAME_CONTENT
    }
}