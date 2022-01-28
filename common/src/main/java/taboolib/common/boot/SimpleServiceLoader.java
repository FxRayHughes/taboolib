package taboolib.common.boot;

import org.jetbrains.annotations.Nullable;
import taboolib.common.TabooLib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * TabooLib
 * taboolib.common.boot.SimpleServiceLoader
 *
 * @author 坏黑
 * @since 2022/1/25 2:37 AM
 */
public class SimpleServiceLoader {

    private static final String PREFIX = "META-INF/services-default/";

    SimpleServiceLoader() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> clazz) {
        for (T service : ServiceLoader.load(clazz, TabooLib.class.getClassLoader())) {
            return service;
        }
        try {
            String fullName = PREFIX + clazz.getName();
            Enumeration<URL> configs = TabooLib.class.getClassLoader().getResources(fullName);
            if (configs.hasMoreElements()) {
                return (T) Class.forName(parse(configs.nextElement())).getDeclaredConstructor().newInstance();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        throw new IllegalStateException("Not yet implemented");
    }

    @Nullable
    static String parse(URL u) throws ServiceConfigurationError {
        try (InputStream in = u.openStream(); BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return r.readLine();
        } catch (IOException x) {
            x.printStackTrace();
        }
        return null;
    }
}
