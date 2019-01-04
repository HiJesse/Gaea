package cn.jesse.gaea.lib.base.vm

import android.arch.lifecycle.ViewModel
import cn.jesse.gaea.lib.base.util.CheckUtil
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

/**
 * MVVM 中的ViewModel, 提供生命周期结束后清除请求
 *
 * @author Jesse
 */
open class BaseViewModel : ViewModel() {
    // 需要取消请求的弱引用列表
    private val cancelableRequest = ArrayList<WeakReference<Disposable>>()

    /**
     * 将可以取消的disposable对象的弱引用添到列表中
     */
    fun addCancelableRequest(disposable: Disposable) {
        if (disposable.isDisposed) {
            return
        }

        cancelableRequest.add(WeakReference(disposable))
    }

    override fun onCleared() {
        super.onCleared()
        for (reference in cancelableRequest) {
            val disposable = reference.get()
            if (CheckUtil.isNull(disposable) || disposable!!.isDisposed) {
                return
            }
            disposable.dispose()
        }
    }
}