package cn.jesse.gaea.lib.base.manager

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.nativelogger.NLogger
import com.google.gson.Gson

/**
 * shared preference 管理类, 提供基础参数读写
 *
 * @author Jesse
 */
class SharedPreferenceManager(context: Context, spName: String, spMode: Int) {
    private val TAG = "SharedPreferenceManager"
    private var sharedPreference: SharedPreferences = context.getSharedPreferences(spName, spMode)
    private val gson = Gson()

    fun setFlag(key: String, value: String?) {
        sharedPreference.edit().putString(key, value).apply()
    }

    fun setFlag(key: String, value: Int) {
        sharedPreference.edit().putInt(key, value).apply()
    }

    fun setFlag(key: String, value: Boolean) {
        sharedPreference.edit().putBoolean(key, value).apply()
    }

    fun setFlag(key: String, value: Long) {
        sharedPreference.edit().putLong(key, value).apply()
    }

    fun setJsonFlag(key: String, json: Any?) {
        setFlag(key, gson.toJson(json))
    }

    fun containsFlag(key: String): Boolean {
        return sharedPreference.contains(key)
    }

    fun getIntFlag(key: String): Int {
        return getIntFlag(key, 0)
    }

    fun getIntFlag(key: String, defValue: Int): Int {
        return sharedPreference.getInt(key, defValue)
    }

    fun getLongFlag(key: String): Long {
        return getLongFlag(key, 0)
    }

    fun getLongFlag(key: String, defValue: Long): Long {
        return sharedPreference.getLong(key, defValue)
    }

    fun getBooleanFlag(key: String): Boolean {
        return getBooleanFlag(key, false)
    }

    fun getBooleanFlag(key: String, defValue: Boolean): Boolean {
        return sharedPreference.getBoolean(key, defValue)
    }

    fun getStringFlag(key: String): String {
        return getStringFlag(key, null)
    }

    fun getStringFlag(key: String, defValue: String?): String {
        return sharedPreference.getString(key, defValue)
    }

    /**
     * 根据key 直接获取json字符串反序列化之后的对象
     */
    fun <T>getJsonObjectFlag(key: String, clazz: Class<T>): T? {
        val source = getStringFlag(key)
        if (CheckUtil.isNull(source)) {
            return null
        }

        var jsonObject: T? = null

        try {
            jsonObject = gson.fromJson(source, clazz)
        } catch (e: Exception) {
            NLogger.e(TAG, "getJsonObjectFlag ${e.message}")
        }

        return jsonObject
    }

    fun clearFlag(key: String) {
        sharedPreference.edit().remove(key).apply()
    }

    fun clear() {
        sharedPreference.edit().clear().apply()
    }

    fun removeFlag(key: String): Boolean {
        return sharedPreference.edit().remove(key).commit()
    }

    fun clearData(context: Context, name: String) {
        val preferences = context.getSharedPreferences(name, MODE_PRIVATE)
        val edit = preferences.edit()
        edit.clear()
        edit.apply()
    }
}