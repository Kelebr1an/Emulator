package ru.tk.ms.fts.emul.customer.reg;

import java.io.InputStream;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemClassLoader;

public class TestUtils {
    private TestUtils() {
    }

    public static Optional<InputStream> readFile(String name) {
        return Optional.ofNullable(getSystemClassLoader().getResourceAsStream(name));
    }
}
