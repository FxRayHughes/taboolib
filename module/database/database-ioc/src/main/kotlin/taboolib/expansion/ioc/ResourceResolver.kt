package taboolib.expansion.ioc

import org.tabooproject.reflex.ReflexClass
import taboolib.common.Inject
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor

@Inject
object ResourceResolver : ClassVisitor(1) {

    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.INIT
    }

    override fun visitStart(clazz: ReflexClass) {
        if (clazz.hasAnnotation(Component::class.java)) {
            val beanName = getBeanName(clazz)
            val bean = BeanDefinition(
                beanName, clazz, null, null,
                getOrder(clazz), isPrimary(clazz),
            )
            registerBeanDefinition(bean)
        }
    }

    fun registerBeanDefinition(beanDefinition: BeanDefinition) {
        if (ApplicationContext.beans.containsKey(beanDefinition.name)) {
            error("Bean definition ${beanDefinition.name} already exists.")
        }
        ApplicationContext.beans[beanDefinition.name] = beanDefinition
    }

}
