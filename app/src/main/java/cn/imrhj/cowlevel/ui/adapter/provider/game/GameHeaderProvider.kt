package cn.imrhj.cowlevel.ui.adapter.provider.game

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import cn.imrhj.cowlevel.R
import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.extensions.reduceNullable
import cn.imrhj.cowlevel.network.model.game.GameModel
import cn.imrhj.cowlevel.ui.view.SimpleGameTagView
import cn.imrhj.cowlevel.utils.ScreenSizeUtil
import cn.imrhj.cowlevel.utils.ScreenSizeUtil.dp2px
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider

class GameHeaderProvider : BaseItemProvider<GameModel, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_game_header
    }

    override fun viewType(): Int {
        return ItemTypeEnum.TYPE_GAME_HEADER.ordinal
    }

    override fun convert(helper: BaseViewHolder, data: GameModel, position: Int) {
        helper.setText(R.id.tv_title, data.title)
                .setText(R.id.tv_score_number, data.totalStarAvg)
                .setText(R.id.tv_time, "${data.playTimeAvg ?: 0}h")
                .setText(R.id.tv_want_play, "${data.wishCount} 人想玩")
                .setText(R.id.tv_played, "${data.playedCount} 人玩过")
                .setText(R.id.tv_release_date, "${data.gamePublishDateShow}")
                .setText(R.id.tv_platform, data.platformSupportList?.map { it?.name }
                        ?.reduceNullable { acc, s -> "$acc / $s" })
                .setText(R.id.tv_language, data.language?.map { it?.name }
                        ?.reduceNullable { acc, s -> "$acc / $s" })
                .setText(R.id.tv_number_network, data.playType?.map { it?.name }
                        ?.reduceNullable { acc, s -> "$acc / $s" })
                .setText(R.id.tv_desc, data.summary)

        val tagParent = helper.getView<LinearLayout>(R.id.ll_tags)
        data.tags?.forEach {
            if (it != null) {
                val tagView = SimpleGameTagView(tagParent.context, it)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.marginEnd = dp2px(8)
                tagParent.addView(tagView, params)
            }
        }

        val ranksLayout = helper.getView<LinearLayout>(R.id.ll_ranks)
        data.rankInfo?.forEach {
            if (it?.tagName?.length ?: 0 > 0) {
                val textView = TextView(ranksLayout.context)
                val spannableString = SpannableStringBuilder("好于${it?.percent}%的「${it?.tagName}」游戏")
                spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 2, 2 + "${it?.percent}".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                textView.text = spannableString
                textView.textSize = 14f
                textView.setTextColor(ContextCompat.getColor(ranksLayout.context, R.color.colorGreyText))
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.bottomMargin = ScreenSizeUtil.dp2px(5)
                ranksLayout.addView(textView, params)
            }
        }

        if (data.youFollowedPlayedInfo != null) {
            val textView = TextView(ranksLayout.context)
            val info = data.youFollowedPlayedInfo
            val totalLength = "${info.total}".length
            val timeLength = "${info.playTimeAvg}".length
            val scoreLength = "${info.starAvg}".length
            val spannableString = SpannableStringBuilder("你关注的 ${info.total} 人玩过 平均玩过  ${info.playTimeAvg}h  ${info.starAvg}分")
            spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 5, 5 + totalLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(textView.context, R.color.colorTimeBlue)),
                    totalLength + 16, totalLength + 17 + timeLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(textView.context, R.color.colorScoreYellow)),
                    totalLength + 19 + timeLength, totalLength + 20 + timeLength + scoreLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            textView.text = spannableString
            textView.textSize = 14f
            textView.setTextColor(ContextCompat.getColor(ranksLayout.context, R.color.colorGreyText))
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = ScreenSizeUtil.dp2px(5)
            ranksLayout.addView(textView, params)

            if (data.youFollowedPlayedInfo.users != null) {
                val avatarLayout = LinearLayout(ranksLayout.context)
                data.youFollowedPlayedInfo.users.forEach {
                    val avatarImage = ImageView(avatarLayout.context)
//                    Glide.with(avatarImage)
//                            .load(cdnImageFor)
                }
            }
        }

    }
}