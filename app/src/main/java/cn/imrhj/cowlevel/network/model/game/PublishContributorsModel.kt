package cn.imrhj.cowlevel.network.model.game

import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.network.model.BaseModel
import cn.imrhj.cowlevel.network.model.common.NameIDModel
import cn.imrhj.cowlevel.network.model.common.SimpleUserModel
import com.google.gson.annotations.SerializedName

data class PublishContributorsModel(
        @SerializedName("publisher_list") val publisherList: List<NameIDModel?>?,
        @SerializedName("developer_list") val developerList: List<NameIDModel?>?,
        @SerializedName("game_contributors") val gameContributors: List<SimpleUserModel?>?
) : BaseModel() {
    override fun getType(): ItemTypeEnum {
        return ItemTypeEnum.TYPE_CONTRIBUTORS
    }
}