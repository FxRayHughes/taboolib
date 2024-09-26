package taboolib.expansion.ioc

import org.tabooproject.reflex.ReflexClass

fun getBeanName(reflexClass: ReflexClass): String {
    var name: String? = null
    if (reflexClass.hasAnnotation(Component::class.java)) {
        val component = reflexClass.getAnnotation(Component::class.java)
        runCatching { component.enum<String>("value") }.getOrNull()?.let {
            if (it.isNotEmpty()) {
                name = it
            }
        }
    }
    if (name.isNullOrEmpty()) {
        name = reflexClass.simpleName
        name = Character.toLowerCase(name!![0]) + name!!.substring(1);
    }
    return name!!
}

fun getOrder(reflexClass: ReflexClass): Int {
    var order = 0
    if (reflexClass.hasAnnotation(Order::class.java)) {
        val component = reflexClass.getAnnotation(Order::class.java)
        runCatching { component.enum<Int>("value") }.getOrNull()?.let {
            order = it
        }
    }
    return order
}

fun isPrimary(reflexClass: ReflexClass): Boolean {
    return reflexClass.hasAnnotation(Primary::class.java)
}
