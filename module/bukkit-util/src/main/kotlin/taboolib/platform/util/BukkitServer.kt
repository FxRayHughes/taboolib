package taboolib.platform.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.tabooproject.reflex.Reflex.Companion.getProperty

/**
 * 获取所有在线玩家
 */
val onlinePlayers: List<Player>
    get() = Bukkit.getOnlinePlayers().toList()

/**
 * 将数据作为字符串发送给所有玩家和控制台
 */
fun Any?.broadcast() = Bukkit.broadcastMessage(toString())