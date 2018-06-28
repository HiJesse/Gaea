Gaea
---

Gaea是一个基于Atlas实现Android项目组件化和插件化的脚手架. 可以根据此脚手架快速开发一款具有插件化特性的App, 也可以提供参考和帮助现有项目实现插件化.

### 项目模块结构

* 模块命名规约

	| 目录名 | 描述 |
	| --- | ---------- |
	| app | 宿主模块, 负责启动应用和启动页. |
	| lib.* | 插件和宿主都依赖的中间件, 打包进宿主中. |
	| plugin.* | 插件模块, 会在编译阶段编译成so打包进apk. |
	| plugin.\*.lib | 插件依赖库, 会打包进插件中. |
	| remote.* | 远程插件模块, 编译成so不打包进apk. |
	| remote.\*.lib | 远程插件依赖库, 会打包进远程插件中. |

* Gaea 项目结构

	| 目录名 | 描述 |
	| --- | ---------- |
	| app | 宿主模块, 启动application和启动页. |
	| lib.base | 所有模块都需要依赖的工具类, UI组件等(**不包含业务**). |
	| lib.common | 所有模块都需要依赖的公共业务资源等(**只包含业务**). |
	| lib.network | 网络组件, 提供网络接口相关操作. |
	| plugin.main | main插件, 提供App主页等. |
	| plugin.user | user插件, 提供用户中心登录等. |
	| remote.scanner | scanner远程插件, 动态提供图形扫描功能. |
	
	
### APK构建

执行`./gradlew clean assembleDebug publish`. 根据项目规约会将组件`app`和`lib.*`直接打进apk, 组件`plugin.*`编译成so文件打进apk的lib目录下. 最终生成debug apk包, 并将ap基准包以app版本号为单位部署到localmaven中, 方便生成TPatch.

### 远程插件构建和加载

1. 确保根目录`config.gradle`文件中`computeRemoteBundles()`方法配置插件为远程插件.
2. 执行`./gradlew clean assembleDebug`. 远程bundle的编译产物输出在`build/output/remote-bundles`中.
3. 将SO插件部署在后端或者放在静态云上.
4. 请求`/gaea/update/checkUpdate`接口获取插件信息.
5. 通过Common组件的`AtlasUpdateUtil.classNotFoundInterceptorCallback`下载校验和安装插件.
6. `AtlasUpdateUtil.uninstallBundle` 卸载远程插件.

### TPatch构建和加载

TPatch是用来修改或升级宿主和非远程插件的方式. 操作流程以App 1.0.0版本为例.

1. 在master分支执行`./gradlew clean assembleDebug publish`. 打出v1.0.0版本apk包, 并归档基准包.
2. 切换到patch_user分支, 该分支相对master修改了User模块的`UserFragment`. 在版本号文案前面增加`(patch_user)`字符串. 并修改插件gradle中的unit tag(Atlas根据unitTag的版本来标识修改记录的)
3. 执行`./gradlew clean assembleDebug -DapVersion=1.0.0`. 传入apVersion后gradle会根据版本号找到第一步归档的基准包, 并生成TPatch. 输出产物在`build/output/tpatch`.
4. 将包含update信息的json文件和TPatch文件部署在后端或者放在静态云上.
5. 请求`/gaea/update/checkUpdate`接口获取TPatch信息.
6. 下载TPatch文件, 并通过Common组件的`AtlasUpdateUtil.loadTPatch`方法校验和安装patch.
7. 安装成功后重启App进程使patch生效.