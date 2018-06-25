package cn.jesse.gaea.lib.common.dataset

import cn.jesse.gaea.lib.base.util.CheckUtil

/**
 * 数据集管理工具
 *
 * @author Jesse
 */
object DataSetManager {

    @Volatile
    private var userDataSet: UserDataSet? = null
    @Volatile
    private var appDataSet: AppDataSet? = null

    /**
     * 单例获取用户数据集
     */
    fun getUserDataSet(): UserDataSet {
        if (CheckUtil.isNull(userDataSet)) {
            synchronized(DataSetManager::class.java) {
                if (CheckUtil.isNull(userDataSet)) {
                    userDataSet = UserDataSet()
                }
            }
        }
        return userDataSet!!
    }

    /**
     * 单例获取应用数据集
     */
    fun getAppDataSet(): AppDataSet {
        if (CheckUtil.isNull(appDataSet)) {
            synchronized(DataSetManager::class.java) {
                if (CheckUtil.isNull(appDataSet)) {
                    appDataSet = AppDataSet()
                }
            }
        }
        return appDataSet!!
    }
}