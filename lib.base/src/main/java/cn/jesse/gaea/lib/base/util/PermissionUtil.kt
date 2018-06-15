package cn.jesse.gaea.lib.base.util

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.lib.base.ui.BaseFragment
import cn.jesse.nativelogger.NLogger

/**
 * 运行时权限工具
 *
 * @author Jesse
 */
object PermissionUtil {

    /**
     * 通过activity获取permission obj
     *
     * @param activity Base
     */
    fun with(activity: BaseActivity): PermissionObject {
        return PermissionObject(activity)
    }

    /**
     * 通过fragment获取permission obj
     *
     * @param fragment Base
     */
    fun with(fragment: BaseFragment): PermissionObject {
        return PermissionObject(fragment)
    }

    /**
     * 权限对象, 提供获取权限和判断是否具有权限的功能
     */
    class PermissionObject {
        private var mActivity: BaseActivity? = null
        private var mFragment: BaseFragment? = null

        constructor(activity: BaseActivity) {
            mActivity = activity
        }

        constructor(fragment: BaseFragment) {
            mFragment = fragment
        }

        /**
         * 当前请求方是否具有权限 permissionName
         *
         * @param permissionName 权限名称
         */
        fun has(permissionName: String): Boolean {
            val permissionCheck = PackageManager.PERMISSION_DENIED
            when (CheckUtil.isNull(mActivity)) {
                true -> ContextCompat.checkSelfPermission(mFragment!!.context!!, permissionName)
                false -> ContextCompat.checkSelfPermission(mActivity!!, permissionName)
            }

            return permissionCheck == PackageManager.PERMISSION_GRANTED
        }

        /**
         * 请求权限
         *
         * @param permissionName 权限名称
         */
        fun request(permissionName: String): PermissionUtil.PermissionRequestObject {
            return if (mActivity != null) {
                PermissionUtil.PermissionRequestObject(mActivity, arrayOf(permissionName))
            } else {
                PermissionUtil.PermissionRequestObject(mFragment, arrayOf(permissionName))
            }
        }

        /**
         * 请求权限组
         *
         * @param permissionNames 权限组
         */
        fun request(vararg permissionNames: String): PermissionUtil.PermissionRequestObject {
            return if (mActivity != null) {
                PermissionRequestObject(mActivity, arrayOf(*permissionNames))
            } else {
                PermissionRequestObject(mFragment, arrayOf(*permissionNames))
            }
        }
    }

    class PermissionRequestObject {
        private val TAG = "PermissionRequestObject"
        private var mActivity: BaseActivity? = null
        private var mFragment: BaseFragment? = null
        private var mPermissionNames: Array<String>
        private var mPermissionsWeDontHave: ArrayList<SinglePermission>? = null
        private var mRequestCode: Int = 0
        private var mDenyFunc: (() -> Unit)? = null
        private var mGrantFunc: (() -> Unit)? = null
        private var mRationalFunc: ((permissionName: String) -> Unit)? = null
        private var mResultFunc: ((requestCode: Int, permissions: Array<String>, grantResult: IntArray) -> Unit)? = null

        constructor(activity: BaseActivity?, permissionNames: Array<String>) {
            mActivity = activity
            mPermissionNames = permissionNames
        }

        constructor(fragment: BaseFragment?, permissionNames: Array<String>) {
            mFragment = fragment
            mPermissionNames = permissionNames
        }

        /**
         * 真实发起请求
         *
         * @param reqCode 请求code
         */
        fun ask(reqCode: Int): PermissionRequestObject {
            mRequestCode = reqCode
            val length = mPermissionNames.size
            mPermissionsWeDontHave = ArrayList(length)

            for (mPermissionName in mPermissionNames) {
                mPermissionsWeDontHave!!.add(SinglePermission(mPermissionName))
            }

            if (needToAsk()) {
                NLogger.i(TAG, "Asking for permission")
                if (mActivity != null) {
                    ActivityCompat.requestPermissions(mActivity!!, mPermissionNames, reqCode)
                } else {
                    mFragment!!.requestPermissions(mPermissionNames, reqCode)
                }
            } else {
                NLogger.i(TAG, "No need to ask for permission")
                if (mGrantFunc != null) {
                    mGrantFunc!!.invoke()
                }
            }
            return this
        }

        /**
         * 判断mPermissionsWeDontHave中
         */
        private fun needToAsk(): Boolean {
            val neededPermissions = ArrayList(mPermissionsWeDontHave)
            for (i in 0 until mPermissionsWeDontHave!!.size) {
                val perm = mPermissionsWeDontHave!![i]
                val checkRes: Int

                checkRes = when (CheckUtil.isNull(mActivity)) {
                    true -> ContextCompat.checkSelfPermission(mFragment!!.context!!, perm.mPermissionName)
                    false -> ContextCompat.checkSelfPermission(mActivity!!, perm.mPermissionName)
                }

                if (checkRes == PackageManager.PERMISSION_GRANTED) {
                    neededPermissions.remove(perm)
                } else {
                    val shouldShowRequestPermissionRationale = when (CheckUtil.isNull(mActivity)) {
                        true -> mFragment!!.shouldShowRequestPermissionRationale(perm.mPermissionName)
                        false -> ActivityCompat.shouldShowRequestPermissionRationale(mActivity!!, perm.mPermissionName)
                    }

                    if (shouldShowRequestPermissionRationale) {
                        perm.isRationalNeeded = true
                    }
                }
            }
            mPermissionsWeDontHave = neededPermissions
            mPermissionNames = Array(mPermissionsWeDontHave!!.size, { "" })
            for (i in 0 until mPermissionsWeDontHave!!.size) {
                mPermissionNames[i] = mPermissionsWeDontHave!![i].mPermissionName
            }
            return mPermissionsWeDontHave!!.size !== 0
        }

        /**
         * Called for the first denied permission if there is need to show the rational
         */
        fun onRational(rationalFunc: (permissionName: String) -> Unit): PermissionRequestObject {
            mRationalFunc = rationalFunc
            return this
        }

        /**
         * 设置所有权限都被授予后的回调
         */
        fun onAllGranted(grantFunc: (() -> Unit)): PermissionRequestObject {
            mGrantFunc = grantFunc
            return this
        }

        /**
         * 设置申请权限被拒绝的回调
         */
        fun onAnyDenied(denyFunc: (() -> Unit)): PermissionRequestObject {
            mDenyFunc = denyFunc
            return this
        }

        /**
         * 设置onRequestPermissionsResult 原生回调, 交给外部自己消费
         */
        fun onResult(resultFunc: (requestCode: Int, permissions: Array<String>, grantResult: IntArray) -> Unit): PermissionRequestObject {
            mResultFunc = resultFunc
            return this
        }

        /**
         * 需要在activity 的onRequestPermissionsResult中调用, 否则拿不到授权结果
         */
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            if (mRequestCode != requestCode) {
                return
            }

            if (mResultFunc != null) {
                NLogger.i(TAG, "Calling Results Func")
                mResultFunc!!.invoke(requestCode, permissions, grantResults)
                return
            }

            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_DENIED) {
                    continue
                }

                if (mPermissionsWeDontHave!![i].isRationalNeeded && mRationalFunc != null) {
                    NLogger.i(TAG, "Calling Rational Func")
                    mRationalFunc!!.invoke(mPermissionsWeDontHave!![i].mPermissionName)
                } else if (mDenyFunc != null) {
                    NLogger.i(TAG, "Calling Deny Func")
                    mDenyFunc!!.invoke()
                } else {
                    NLogger.e(TAG, "NUll DENY FUNCTIONS")
                }

                // terminate if there is at least one deny
                return

            }

            // there has not been any deny
            if (mGrantFunc != null) {
                NLogger.i(TAG, "Calling Grant Func")
                mGrantFunc!!.invoke()
            } else {
                NLogger.e(TAG, "NUll GRANT FUNCTIONS")
            }
        }
    }

    /**
     * 权限请求载体
     */
    class SinglePermission(permissionName: String, reason: String = "") {
        var mPermissionName = permissionName
        var isRationalNeeded = false
        var mReason = reason
    }
}