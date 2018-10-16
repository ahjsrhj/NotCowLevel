package cn.imrhj.cowlevel.network.model.common

import cn.imrhj.cowlevel.network.model.BaseModel
import cn.imrhj.cowlevel.network.model.element.PriceInfoModel
import com.google.gson.annotations.SerializedName

data class UrlModel(
        @field:SerializedName("data") var data: PriceInfoModel? = null,
        @SerializedName("goto") val jsonMemberGoto: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("id") val id: Int? = null,
        @SerializedName("pic") val pic: String? = null,
        @field:SerializedName("url") val url: String? = null
) : BaseModel()