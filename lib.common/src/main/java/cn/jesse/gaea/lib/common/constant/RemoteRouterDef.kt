package cn.jesse.gaea.lib.common.constant

/**
 * 跨bundle通讯时需要的常量汇总
 */
object RemoteRouterDef {

    object LibCommon {
        // base
        val BASE = "${CommonPackageDef.MAIN}.${CommonPackageDef.LIB}.${CommonPackageDef.PackageLib.COMMON}"
        val BASE_ACTIVITY = "${BASE}.${CommonPackageDef.UI}.activity"
        val BASE_FRAGMENT = "${CommonPackageDef.ACTION_FRAGMENT}.${CommonPackageDef.PackageLib.COMMON}"

        // activity 相关
        val ACTIVITY_PATTERN_LOCK = "${BASE_ACTIVITY}.PatternLockActivity"
        val PARAMS_LOCK_CLOSEABLE = "PARAMS_LOCK_CLOSEABLE"
    }

    /**
     * Main 插件
     */
    object PluginMain {
        // base
        val BASE = "${CommonPackageDef.MAIN}.${CommonPackageDef.PLUGIN}.${CommonPackageDef.PackagePlugin.MAIN}"
        val BASE_ACTIVITY = "${BASE}.${CommonPackageDef.UI}.activity"
        val BASE_FRAGMENT = "${CommonPackageDef.ACTION_FRAGMENT}.${CommonPackageDef.PackagePlugin.MAIN}"

        // activity 相关
        val ACTIVITY_MAIN = "${BASE_ACTIVITY}.MainActivity"
    }

    /**
     * User 插件
     */
    object PluginUser {
        // base
        val BASE = "${CommonPackageDef.MAIN}.${CommonPackageDef.PLUGIN}.${CommonPackageDef.PackagePlugin.USER}"
        val BASE_ACTIVITY = "${BASE}.${CommonPackageDef.UI}.activity"
        val BASE_FRAGMENT = "${CommonPackageDef.ACTION_FRAGMENT}.${CommonPackageDef.PackagePlugin.USER}"

        // activity 相关
        val ACTIVITY_LOGIN = "${BASE_ACTIVITY}.LoginActivity"
        val RESULT_CODE_LOGIN_STATUS = 100
        val RESULT_LOGIN_STATUS = "PARAMS_LOGIN_STATUS"

        // fragment相关
        val FRAGMENT_USER_CENTER = "${BASE_FRAGMENT}.userCenter"

        // transactor
        val TRANSACTOR_USER = "${CommonPackageDef.ACTION_TRANSACTOR}.${CommonPackageDef.PackagePlugin.USER}"
    }

    /**
     * 扫描插件
     */
    object PluginScanner {
        // base
        val BASE = "${CommonPackageDef.MAIN}.${CommonPackageDef.REMOTE}.${CommonPackageDef.PackagePlugin.SCANNER}"
        val BASE_ACTIVITY = "${BASE}.${CommonPackageDef.UI}.activity"
        val BASE_FRAGMENT = "${CommonPackageDef.ACTION_FRAGMENT}.${CommonPackageDef.PackagePlugin.SCANNER}"

        // activity 相关
        val ACTIVITY_SCAN = "${BASE_ACTIVITY}.ScannerActivity"

        // fragment相关
        val FRAGMENT_SCAN = "${BASE_FRAGMENT}.scanner"
    }

    /**
     * 基础包协议
     */
    object CommonPackageDef {

        // 主包
        val MAIN = "cn.jesse.gaea"

        // ui 路径
        val UI = "ui"

        // lib 路径
        val LIB = "lib"

        // plugin 路径
        val PLUGIN = "plugin"

        // remote plugin 路径
        val REMOTE = "remote"

        // remote fragment 前缀
        val ACTION_FRAGMENT = "atlas.fragment.intent.action"

        // remote transactor 前缀
        val ACTION_TRANSACTOR = "atlas.transaction.intent.action"

        // 中间件, 或插件lib的包路径
        object PackageLib {
            val BASE = "base"
            val COMMON = "common"
        }

        // 插件路径
        object PackagePlugin {
            val MAIN = "main"
            val USER = "user"
            val SCANNER = "scanner"
        }
    }
}