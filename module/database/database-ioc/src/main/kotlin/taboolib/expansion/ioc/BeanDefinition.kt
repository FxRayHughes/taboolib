package taboolib.expansion.ioc

import org.tabooproject.reflex.ClassField
import org.tabooproject.reflex.ClassMethod
import org.tabooproject.reflex.ReflexClass

data class BeanDefinition(
    // 全局唯一名称
    var name: String,
    // 本体或所属类
    val clazz: ReflexClass,
    // 本体方法
    val method: ClassMethod?,
    // 本体变量
    val field: ClassField?,
    // Bean顺序
    val order: Int = 0,
    // 是否为主要
    val primary: Boolean = false,
    val initMethod: ClassMethod? = null,
    val destroyMethod: ClassMethod? = null
) {

    var instance: Any? = null

}
