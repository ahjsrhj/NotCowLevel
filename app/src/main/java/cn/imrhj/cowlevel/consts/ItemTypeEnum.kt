package cn.imrhj.cowlevel.consts

enum class ItemTypeEnum {
    TYPE_UNKNOWN,
    TYPE_FEED,
    TYPE_USER,
    TYPE_HEADER_POST,
    TYPE_HEADER_TAG,
    TYPE_ELEMENT_RELATED,
    TYPE_BANNER,
    TYPE_GAME_HEADER,
    TYPE_GAME_IMAGE,
    TYPE_URLS,
    TYPE_CONTRIBUTORS
    ;

    companion object {
        fun valueOf(ordinal: Int): ItemTypeEnum {
            return values()[ordinal]
        }
    }

}