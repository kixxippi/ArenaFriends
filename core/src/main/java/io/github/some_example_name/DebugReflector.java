package io.github.some_example_name;

import java.lang.reflect.Field;

public final class DebugReflector {
    private DebugReflector() {}

    public static String dump(Object obj) {
        if (obj == null) return "null";

        StringBuilder sb = new StringBuilder();
        Class<?> c = obj.getClass();
        sb.append(c.getName()).append(" {");

        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            sb.append("\n  ").append(f.getName()).append(" = ");
            try {
                sb.append(f.get(obj));
            } catch (IllegalAccessException e) {
                sb.append("<inaccessible>");
            }
        }
        sb.append("\n}");
        return sb.toString();
    }
}
