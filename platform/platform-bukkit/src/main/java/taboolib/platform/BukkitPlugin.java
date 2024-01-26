package taboolib.platform;

import org.bukkit.Bukkit;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tabooproject.reflex.Reflex;
import taboolib.common.LifeCycle;
import taboolib.common.PrimitiveIO;
import taboolib.common.TabooLib;
import taboolib.common.classloader.IsolatedClassLoader;
import taboolib.common.platform.Platform;
import taboolib.common.platform.PlatformImplementationKt;
import taboolib.common.platform.PlatformSide;
import taboolib.common.platform.Plugin;
import taboolib.common.platform.function.ExecutorKt;

import java.io.File;
import java.util.Set;

/**
 * TabooLib
 * taboolib.platform.BukkitPlugin
 *
 * @author sky
 * @since 2021/6/26 8:22 下午
 */
@SuppressWarnings({"Convert2Lambda", "DuplicatedCode", "CallToPrintStackTrace"})
@PlatformSide(Platform.BUKKIT)
public class BukkitPlugin extends JavaPlugin {

    @Nullable
    private static final Plugin pluginInstance;
    private static BukkitPlugin instance;

    static {
        // 初始化 IsolatedClassLoader
        try {
            IsolatedClassLoader.init(BukkitPlugin.class);
        } catch (Throwable ex) {
            // 关闭插件
            TabooLib.setStopped(true);
            // 提示信息
            PrimitiveIO.error("[TabooLib] Failed to initialize primitive loader, the plugin \"%s\" will be disabled!", PrimitiveIO.getRunningFileName());
            // 打印错误信息
            ex.printStackTrace();
        }
        // 生命周期任务
        TabooLib.lifeCycle(LifeCycle.CONST);
        // 检索 TabooLib Plugin 实现
        pluginInstance = PlatformImplementationKt.findImplementation(Plugin.class);
    }

    public BukkitPlugin() {
        instance = this;
        // 修改访问提示（似乎有用）
        injectIllegalAccess();
        // 生命周期任务
        TabooLib.lifeCycle(LifeCycle.INIT);
    }

    @Override
    public void onLoad() {
        // 生命周期任务
        TabooLib.lifeCycle(LifeCycle.LOAD);
        // 调用 Plugin 实现的 onLoad() 方法
        if (pluginInstance != null && !TabooLib.isStopped()) {
            pluginInstance.onLoad();
        }
    }

    @Override
    public void onEnable() {
        // 生命周期任务
        TabooLib.lifeCycle(LifeCycle.ENABLE);
        // 判断插件是否关闭
        if (!TabooLib.isStopped()) {
            // 调用 Plugin 实现的 onEnable() 方法
            if (pluginInstance != null) {
                pluginInstance.onEnable();
            }
            // 启动调度器
            ExecutorKt.startExecutor();
        }
        // 再次判断插件是否关闭
        // 因为插件可能在 onEnable() 下关闭
        if (!TabooLib.isStopped()) {
            // 创建调度器，执行 onActive() 方法
            Bukkit.getScheduler().runTask(this, new Runnable() {
                @Override
                public void run() {
                    // 生命周期任务
                    TabooLib.lifeCycle(LifeCycle.ACTIVE);
                    // 调用 Plugin 实现的 onActive() 方法
                    if (pluginInstance != null) {
                        pluginInstance.onActive();
                    }
                }
            });
        }
    }

    @Override
    public void onDisable() {
        // 在插件未关闭的前提下，执行 onDisable() 方法
        if (pluginInstance != null && !TabooLib.isStopped()) {
            pluginInstance.onDisable();
        }
        // 生命周期任务
        TabooLib.lifeCycle(LifeCycle.DISABLE);
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        if (pluginInstance instanceof BukkitWorldGenerator) {
            return ((BukkitWorldGenerator) pluginInstance).getDefaultWorldGenerator(worldName, id);
        }
        return null;
    }

    @Nullable
    @Override
    public BiomeProvider getDefaultBiomeProvider(@NotNull String worldName, @Nullable String id) {
        if (pluginInstance instanceof BukkitBiomeProvider) {
            return ((BukkitBiomeProvider) pluginInstance).getDefaultBiomeProvider(worldName, id);
        }
        return null;
    }

    @NotNull
    @Override
    public File getFile() {
        return super.getFile();
    }

    @Nullable
    public static Plugin getPluginInstance() {
        return pluginInstance;
    }

    @NotNull
    public static BukkitPlugin getInstance() {
        return instance;
    }

    /**
     * 移除 Spigot 的访问警告：
     * Loaded class {0} from {1} which is not a depend, softdepend or loadbefore of this plugin
     */
    @SuppressWarnings("DataFlowIssue")
    public static void injectIllegalAccess() {
        try {
            PluginDescriptionFile description = Reflex.Companion.getProperty(BukkitPlugin.class.getClassLoader(), "description", false, true, false);
            Set<String> accessSelf = Reflex.Companion.getProperty(BukkitPlugin.class.getClassLoader(), "seenIllegalAccess", false, true, false);
            for (org.bukkit.plugin.Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                if (plugin.getClass().getName().endsWith("platform.BukkitPlugin")) {
                    Set<String> accessOther = Reflex.Companion.getProperty(plugin.getClass().getClassLoader(), "seenIllegalAccess", false, true, false);
                    accessOther.add(description.getName());
                    accessSelf.add(plugin.getName());
                }
            }
        } catch (Throwable ignored) {
        }
    }
}
