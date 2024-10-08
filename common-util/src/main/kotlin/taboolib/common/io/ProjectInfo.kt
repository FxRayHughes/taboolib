package taboolib.common.io

import taboolib.common.PrimitiveSettings

/**
 * 是否为开发模式
 */
val isDebugMode: Boolean
    get() = PrimitiveSettings.IS_DEBUG_MODE

/**
 * 是否为开发模式
 */
val isDevelopmentMode: Boolean
    get() = PrimitiveSettings.IS_DEV_MODE

/**
 * taboolib 字符串
 */
val taboolibId: String
    get() = charArrayOf('t', 'a', 'b', 'o', 'o', 'l', 'i', 'b').concatToString()

/**
 * 组名（项目标识）
 * 例如：org.tabooproject
 */
val groupId = "taboolib".substringBefore(".$taboolibId")
    get() {
        if (field == "taboolib") return System.getProperty("taboolib.group", field)
        return field
    }

/**
 * taboolib 路径
 * 例如：org.tabooproject.taboolib
 */
val taboolibPath: String
    get() = "$groupId.$taboolibId"

/**
 * 特定类的组名
 * 传入：org.tabooproject.taboolib.platform.BukkitPlugin
 * 传出：org.tabooproject
 */
val Class<*>.groupId: String
    get() = name.substringBefore(taboolibId).dropLast(1)