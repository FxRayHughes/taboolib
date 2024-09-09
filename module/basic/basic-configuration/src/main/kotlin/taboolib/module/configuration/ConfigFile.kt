package taboolib.module.configuration

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.FileNotFoundAction
import com.electronwill.nightconfig.core.io.ConfigParser
import com.electronwill.nightconfig.core.io.ParsingMode
import org.tabooproject.reflex.Reflex.Companion.setProperty
import taboolib.common.platform.function.warning
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.text.SimpleDateFormat

/**
 * TabooLib
 * taboolib.module.configuration.ConfigFile
 *
 * @author mac
 * @since 2021/11/22 12:49 上午
 */
open class ConfigFile(root: Config) : ConfigSection(root), Configuration {

    override var file: File? = null
    override var name: String = ""

    private val reloadCallback = ArrayList<Runnable>()

    override fun onReload(runnable: Runnable) {
        reloadCallback.add(runnable)
    }

    override fun saveToString(): String {
        return toString()
    }

    override fun saveToFile(file: File?) {
        (file ?: this.file)?.writeText(saveToString()) ?: error("File not specified")
    }

    override fun loadFromFile(file: File) {
        this.file = file
        this.name = file.nameWithoutExtension
        try {
            clear()
            parser().parse(file, root, ParsingMode.REPLACE, FileNotFoundAction.THROW_ERROR)
        } catch (ex: Exception) {
            if (file.extension != "bak") {
                file.copyTo(File(file.parent, file.name + "_" + SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".bak"))
            }
            warning("File: $file")
            throw ex
        }
        reloadCallback.forEach { it.run() }
    }

    override fun loadFromString(contents: String) {
        try {
            clear()
            parser().parse(contents, root, ParsingMode.REPLACE)
        } catch (t: Exception) {
            warning("Source: \n$contents")
            throw t
        }
        reloadCallback.forEach { it.run() }
    }

    override fun loadFromReader(reader: Reader) {
        clear()
        parser().parse(reader, root, ParsingMode.REPLACE)
        reloadCallback.forEach { it.run() }
    }

    override fun loadFromInputStream(inputStream: InputStream) {
        clear()
        parser().parse(inputStream, root, ParsingMode.REPLACE)
        reloadCallback.forEach { it.run() }
    }

    override fun reload() {
        loadFromFile(file ?: return)
    }

    override fun changeType(type: Type) {
        val format = type.newFormat()
        fun process(value: Any) {
            when (value) {
                is Map<*, *> -> value.forEach { process(it.value ?: return@forEach) }
                is List<*> -> value.forEach { process(it ?: return@forEach) }
                is Config -> {
                    value.setProperty("configFormat", format)
                    value.valueMap().forEach { process(it.value ?: return) }
                }
            }
        }
        process(root)
    }

    private fun parser(): ConfigParser<out Config> {
        return root.configFormat().createParser()
    }
}