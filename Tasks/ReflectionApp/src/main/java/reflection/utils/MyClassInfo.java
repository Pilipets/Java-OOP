package reflection.utils;

import jdk.internal.loader.Loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MyClassInfo {
    /**
     * Prints all constructors of a class
     * @param cl a class
     */
    public static String printConstructors(Class cl)
    {
        StringBuilder builder = new StringBuilder();
        Constructor[] constructors = cl.getDeclaredConstructors();

        for (Constructor c : constructors)
        {
            String name = c.getName();
            builder.append("   ");
            String modifiers = Modifier.toString(c.getModifiers());
            if (modifiers.length() > 0) {
                builder.append(modifiers + " ");
            }
            builder.append(name + "(");

            // print parameter types
            Class[] paramTypes = c.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (j > 0) {
                    builder.append(", ");
                }
                builder.append(paramTypes[j].getName());
            }
            builder.append(");\n");
        }
        return builder.toString();
    }

    /**
     * Prints all methods of a class
     * @param cl a class
     */
    public static String printMethods(Class cl)
    {
        StringBuilder builder = new StringBuilder();
        Method[] methods = cl.getDeclaredMethods();

        for (Method m : methods)
        {
            Annotation[] a = m.getDeclaredAnnotations();
            for(Annotation an : a) {
                builder.append("   "+an.toString()+"\n");
            }
            Class retType = m.getReturnType();
            String name = m.getName();

            builder.append("   ");
            // print modifiers, return type and method name
            String modifiers = Modifier.toString(m.getModifiers());
            if (modifiers.length() > 0) {
                builder.append(modifiers + " ");
            }
            builder.append(retType.getName() + " " + name + "(");

            // print parameter types
            Class[] paramTypes = m.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (j > 0) {
                    builder.append(", ");
                }
                builder.append(paramTypes[j].getName());
            }
            builder.append(");\n");
        }
        return builder.toString();
    }

    public static String printFields(Class cl)
    {
        StringBuilder builder = new StringBuilder();
        Field[] fields = cl.getDeclaredFields();

        for (Field f : fields)
        {
            Class type = f.getType();
            String name = f.getName();
            builder.append("   ");
            String modifiers = Modifier.toString(f.getModifiers());
            if (modifiers.length() > 0) {
                builder.append(modifiers + " ");
            }
            builder.append(type.getName() + " " + name + ";\n");
        }
        return builder.toString();
    }

    public static String printInfo(String className, String[] dirPath) throws ClassNotFoundException {
        StringBuilder builder = new StringBuilder();

        Class cl = Class.forName(className, false, new MyLoader(dirPath));
        Class supercl = cl.getSuperclass();
        Class[] interfaces = cl.getInterfaces();
        String modifiers = Modifier.toString(cl.getModifiers());
        if (modifiers.length() > 0) {
            builder.append(modifiers + " ");
        }
        builder.append("class " + className);
        if (supercl != null && supercl != Object.class) {
            builder.append(" extends " + supercl.getName());
        }
        if(interfaces.length != 0) {
            builder.append("\n        implements ");
            for(int j = 0; j < interfaces.length; ++j) {
                builder.append(interfaces[j].getName());
                if(j != interfaces.length-1) {
                    builder.append(", ");
                }
            }
        }

        builder.append("\n{\n");
        builder.append(printConstructors(cl)+"\n");
        builder.append("\n");
        builder.append(printMethods(cl)+"\n");
        builder.append("\n");
        builder.append(printFields(cl)+"\n");
        builder.append("}");
        return builder.toString();
    }
}
