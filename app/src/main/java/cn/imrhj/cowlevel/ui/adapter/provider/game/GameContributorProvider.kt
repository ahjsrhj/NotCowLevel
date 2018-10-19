package cn.imrhj.cowlevel.ui.adapter.provider.game

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.imrhj.cowlevel.R
import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.extensions.gotColor
import cn.imrhj.cowlevel.manager.SchemeUtils
import cn.imrhj.cowlevel.network.model.common.NameIDModel
import cn.imrhj.cowlevel.network.model.game.PublishContributorsModel
import cn.imrhj.cowlevel.utils.ScreenSizeUtil.dp2px
import cn.imrhj.cowlevel.utils.cdnImageForDPSquare
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider

class GameContributorProvider : BaseItemProvider<PublishContributorsModel, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_game_contributors
    }

    override fun viewType(): Int {
        return ItemTypeEnum.TYPE_CONTRIBUTORS.ordinal
    }

    override fun convert(helper: BaseViewHolder, data: PublishContributorsModel?, position: Int) {
        val developerParent = helper.getView<LinearLayout>(R.id.ll_developer)
        val publisherParent = helper.getView<LinearLayout>(R.id.ll_publisher)
        val contributorParent = helper.getView<LinearLayout>(R.id.ll_avatar)

        val addChildFunc = { parent: LinearLayout ->
            { it: NameIDModel? ->
                val context = parent.context
                val child = TextView(context)
                child.text = it?.name
                child.setOnClickListener { _ -> SchemeUtils.openCow("element/${it?.id}") }
                child.setTextColor(context.gotColor(R.color.white))
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                params.marginEnd = dp2px(8)
                parent.addView(child, params)
            }
        }

        data?.developerList?.forEach(addChildFunc(developerParent))
        data?.publisherList?.forEach(addChildFunc(publisherParent))

        data?.gameContributors?.forEach {
            val context = contributorParent.context
            val child = ImageView(context)
            val params = LinearLayout.LayoutParams(dp2px(26), dp2px(26))
            params.marginEnd = dp2px(8)
            contributorParent.addView(child, params)
            Glide.with(child)
                    .load(cdnImageForDPSquare(it?.avatar, 26))
                    .apply(RequestOptions().circleCrop().placeholder(R.drawable.round_place_holder))
                    .into(child)
        }
    }
}
