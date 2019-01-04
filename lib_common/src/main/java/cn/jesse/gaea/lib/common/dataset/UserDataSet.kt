package cn.jesse.gaea.lib.common.dataset

import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.common.constant.SPDef
import cn.jesse.gaea.lib.common.util.SPUtil

/**
 * 用户信息数据集
 *
 * @author Jesse
 */
class UserDataSet {
    private val userSP = SPUtil.userSP

    // 登录 token
    var accessToken: String? = null
    get() {
        if (CheckUtil.isNull(field)) {
            field = userSP.getStringFlag(SPDef.User.KEY_ACCESS_TOKEN)
        }
        return field
    }
    set(value) {
        if (CheckUtil.isStringEquals(field, value)) {
            return
        }
        field = value
        userSP.setFlag(SPDef.User.KEY_ACCESS_TOKEN, field)
    }

    // 昵称
    var nickname: String? = null
        get() {
            if (CheckUtil.isNull(field)) {
                field = userSP.getStringFlag(SPDef.User.KEY_NICKNAME)
            }
            return field
        }
        set(value) {
            if (CheckUtil.isStringEquals(field, value)) {
                return
            }
            field = value
            userSP.setFlag(SPDef.User.KEY_NICKNAME, field)
        }
}