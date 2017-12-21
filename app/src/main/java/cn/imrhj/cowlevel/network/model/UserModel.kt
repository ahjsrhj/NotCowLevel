package cn.imrhj.cowlevel.network.model

/**
 * Created by rhj on 2017/12/4.
 * {
"name": "乌拉拉",
"url_slug": "wulala",
"avatar": "https:\/\/pic1.cdncl.net\/user\/avatar\/7e2e69e111ee99a1c2c25acd851d1c5b.jpeg",
"cover": "https:\/\/pic1.cdncl.net\/user\/wulala\/cover\/fee00dde747bf48b5100c85dbc73e5ff.jpeg",
"intro": "奶牛关运营，很高兴认识你",
"level": 100,
"following_count": 6248,
"follower_count": 6116,
"recommend_count": 1009,
"played_count": 151,
"comment_count": 5,
"vote_count": 24,
"comment_voted_count": 13,
"answer_voted_count": 0,
"photo_voted_count": 0,
"article_voted_count": 0,
"question_count": 9,
"answer_count": 1,
"article_count": 0,
"user_bio": "有什么不明白的欢迎私信我哦~",
"invite_code_total": 21,
"invite_code_remain": 0,
"total_voted_count": 13,
"feed_count": 2837,
"tag_count": 33,
"image_count": 9477,
"video_count": 562,
"is_follow": 1,
"is_follow_by": 1,
"is_follow_both": 1,
"is_block": 0,
"user_site": {
"nintendo_3ds": "",
"nintendo_ns": "",
"nintendo_wiiu": "",
"blizzard": "",
"gog": "",
"origin": "",
"uplay": "",
"steam": "",
"psn": "",
"xbox": "",
"zhihu": "https:\/\/www.zhihu.com\/people\/ellioseee?utm_source=cowlevel",
"weibo": "",
"douban": "",
"github": "",
"bilibili": "",
"douyu": "",
"acfun": "",
"twitch": "",
"user_link": ""
},
"pro_games": [],
"is_pro": 0,
"collection_count": 1
}
 */
val TYPE_USER = 2

data class UserModel(
        val name: String? = null,
        val url_slug: String? = null,
        val avatar: String? = null,
        val cover: String? = null,
        val intro: String? = null,
        val level: Int = 0,
        val following_count: Int = 0,
        val follower_count: Int = 0,
        val recommend_count: Int = 0,
        val played_count: Int = 0,
        val comment_count: Int = 0,
        val vote_count: Int = 0,
        val comment_voted_count: Int = 0,
        val answer_voted_count: Int = 0,
        val photo_voted_count: Int = 0,
        val article_voted_count: Int = 0,
        val question_count: Int = 0,
        val answer_count: Int = 0,
        val article_count: Int = 0,
        val user_bio: String? = null,
        val invite_code_total: Int = 0,
        val invite_code_remain: Int = 0,
        val is_follow: Int = 0,
        val is_follow_by: Int = 0,
        val is_follow_both: Int = 0,
        val is_block: Int = 0,
        val is_pro: Int = 0,
        val total_voted_count: Int = 0,
        val pro_games: List<*>? = null,
        val invite_code_count: Int = 0,
        val draft_list_count: Int = 0,
        val followed_question_count: Int = 0,
        val followed_tag_count: Int = 0,
        val followed_game_count: Int = 0,
        val collected_game_count: Int = 0,
        val created_game_count: Int = 0,
        val bind_status: BindStatus? = null,
        val is_curator: Int = 0,
        val is_developer: Int = 0,
        val video_count: Int = 0,
        val inbox_count: Int = 0,
        var token: String? = null,
        val collection_count: Int = 0,
        val user_site: UserSiteModel? = null,
        val image_count: Int = 0,
        val tag_count: Int = 0,
        val feed_count: Int = 0
) : BaseModel() {
    override fun getType(): Int {
        return TYPE_USER
    }
}
