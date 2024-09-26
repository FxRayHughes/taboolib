package taboolib.expansion.ioc

import taboolib.common.Inject
import java.util.concurrent.ConcurrentHashMap

@Inject
object ApplicationContext {

    val beans = ConcurrentHashMap<String,BeanDefinition>()



}
