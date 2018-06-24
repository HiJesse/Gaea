package cn.jesse.gaea.lib.base.util

import android.content.Context
import android.content.pm.PackageManager
import cn.jesse.nativelogger.NLogger
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.regex.Pattern

/**
 * 签名相关工具
 *
 * @author Jesse
 */
object SignatureUtil {
    private val TAG = SignatureUtil::class.java.simpleName

    /**
     * 校验文件的签名是否和载体一致
     */
    @Throws(CertificateException::class, PackageManager.NameNotFoundException::class, IOException::class)
    fun checkFileIdentity(ctx: Context, filePath: String): Boolean {
        val local = getLocalSignature(ctx)
        val external = getJarFileSignature(filePath)
        NLogger.i(TAG, "checkFileIdentity local: $local  external: $external")
        return CheckUtil.isStringEquals(local, external)
    }

    /**
     * 获取外部文件签名
     */
    @Throws(IOException::class)
    fun getJarFileSignature(filePath: String): String? {
        var signCode: String? = null
        val readBuffer = ByteArray(8192)
        val jarFile = JarFile(filePath)
        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val je = entries.nextElement() as JarEntry
            if (je.isDirectory || je.name.startsWith("META-INF/") || !je.name.endsWith("dex")) {
                continue
            }
            val certificates = loadCertificates(jarFile, je, readBuffer)
            if (null == certificates || certificates.isEmpty()) {
                continue
            }
            val cert = certificates[0] as X509Certificate
            val pubKey = cert.publicKey.toString()
            var ss = subPublicSignature(pubKey)
            ss = ss.replace(",", "")
            ss = ss.toLowerCase()
            val aa = ss.indexOf("modulus")
            val bb = ss.indexOf("publicexponent")
            signCode = ss.substring(aa + 8, bb)
            IOUtil.close(jarFile)
            return signCode
        }
        IOUtil.close(jarFile)
        return signCode
    }

    /**
     * 获取app本身的签名信息
     */
    @Throws(IOException::class, PackageManager.NameNotFoundException::class, CertificateException::class)
    fun getLocalSignature(ctx: Context): String {
        var signCode: String? = null
        //get signature info depends on package name
        val packageInfo = ctx.packageManager.getPackageInfo(ctx.packageName, PackageManager.GET_SIGNATURES)
        val signs = packageInfo.signatures
        val sign = signs[0]
        val certFactory = CertificateFactory.getInstance("X.509")
        val cert = certFactory.generateCertificate(ByteArrayInputStream(sign.toByteArray())) as X509Certificate
        val pubKey = cert.publicKey.toString()
        var ss = subPublicSignature(pubKey)
        ss = ss.replace(",", "")
        ss = ss.toLowerCase()
        val aa = ss.indexOf("modulus")
        val bb = ss.indexOf("publicexponent")
        signCode = ss.substring(aa + 8, bb)

        return signCode
    }

    /**
     * 根据源码中PackageParser的实现, 加载证书
     */
    @Throws(IOException::class)
    private fun loadCertificates(jarFile: JarFile, je: JarEntry?, readBuffer: ByteArray): Array<Certificate>? {
        // We must read the stream for the JarEntry to retrieve
        // its certificates.
        val bis = BufferedInputStream(jarFile.getInputStream(je))
        while (bis.read(readBuffer, 0, readBuffer.size) != -1) {
            // not using
        }
        IOUtil.close(bis)

        return if (CheckUtil.isNull(je)) null else je?.certificates
    }

    /**
     * 从签名信息中切除匹配数据
     */
    private fun subPublicSignature(sub: String): String {
        val pp = Pattern.compile("\\s*|\t|\r|\n")
        val mm = pp.matcher(sub)
        return mm.replaceAll("")
    }

}