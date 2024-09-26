package taboolib.expansion.ioc

@Retention(AnnotationRetention.RUNTIME)
annotation class Configuration(
    /**
     * Bean名称。默认为第一个字母小写的简单类名。
     */
    val value: String = ""
)

@Retention(AnnotationRetention.RUNTIME)
annotation class Component(
    /**
     * Bean名称。默认为第一个字母小写的简单类名。
     */
    val value: String = ""
)

@Retention(AnnotationRetention.RUNTIME)
annotation class Bean(
    val value: String = "",
    val initMethod: String = "",
    val destroyMethod: String = "",
)

@Retention(AnnotationRetention.RUNTIME)
annotation class Resource(
    val value: Boolean = true,
    val name: String = "",
)

@Retention(AnnotationRetention.RUNTIME)
annotation class Order(
    val value: Int = 0
)

@Retention(AnnotationRetention.RUNTIME)
annotation class Primary
