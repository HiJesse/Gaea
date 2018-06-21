package cn.jesse.gaea.plugin.user.api

import java.io.Serializable

class LoginBean : Serializable {
    var accessToken: String? = null
    var nickname: String? = null
}