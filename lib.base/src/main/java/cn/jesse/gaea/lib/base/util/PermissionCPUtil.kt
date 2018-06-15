package cn.jesse.gaea.lib.base.util

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.hardware.Camera
import android.location.LocationManager
import android.os.Environment
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import cn.jesse.nativelogger.NLogger
import java.io.File


/**
 * 兼容国产ROM权限
 *
 * @author Jesse
 */
object PermissionCPUtil {
    private val TAG = "PermissionCPUtil"

    /**
     * 判断摄像头权限
     *
     * @return boolean
     */
    fun checkCameraPermission(): Boolean {
        try {
            val camera = Camera.open(0)
            camera.startPreview()
            camera.stopPreview()
            camera.release()
        } catch (e: Exception) {
            NLogger.e(TAG, "checkCameraPermission $e.message")
            return false
        }

        return true
    }

    /**
     * 判断通讯录权限
     *
     * @return
     */
    fun checkContactPermission(context: Context): Boolean {
        var phoneCursor: Cursor? = null
        try {
            val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.LOOKUP_KEY)
            val resolver = context.contentResolver
            phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null)
            if (phoneCursor == null) {
                return false
            }
        } catch (e: Exception) {
            NLogger.e(TAG, "checkContactPermission $e.message")
            return false
        } finally {
            if (phoneCursor != null) {
                phoneCursor!!.close()
            }
        }
        return true
    }

    /**
     * 判断定位权限
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    fun checkLocationPermission(context: Context): Boolean {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            NLogger.e(TAG, "checkLocationPermission $e.message")
            return false
        }

        return true
    }

    /**
     * 判断SD卡权限
     */
    fun checkSDCardPermission(): Boolean {
        try {
            val file = File(Environment.getExternalStorageDirectory(), "test")
            if (file.exists()) {
                file.delete()
            } else {
                file.mkdirs()
                file.delete()
            }
        } catch (e: Exception) {
            NLogger.e(TAG, "checkSDCardPermission $e.message")
            return false
        }

        return true
    }



    /**
     * 判断PhoneState权限
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun checkPhoneStatePermission(context: Context): Boolean {
        try {
            val telManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val imsi = telManager.subscriberId
        } catch (e: Exception) {
            NLogger.e(TAG, "checkPhoneStatePermission $e.message")
            return false
        }

        return true
    }
}