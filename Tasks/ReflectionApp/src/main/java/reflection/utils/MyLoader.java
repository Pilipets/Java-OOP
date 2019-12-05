package reflection.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyLoader extends ClassLoader{
    private Map classMap = new java.util.HashMap();
    public final String[] classPath;

    public MyLoader(String[] classPath) {
        this.classPath = classPath;
    }

    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class result = findClass(name);
        if (resolve) {
            resolveClass(result);
        }
        return result;
    }


    protected Class findClass(String name) throws ClassNotFoundException {
        Class result = (Class)classMap.get(name);
        if (result != null) {
            return result;
        }

        File f = findFile(name.replace('.', '\\'), ".class");
        if (f == null) {
            return findSystemClass(name); // Invoke SystemLoader method
        }

        try {
            byte[] classBytes = loadFileAsBytes(f);
            result = defineClass(name, classBytes, 0,
                    classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(String.format(
                    "Cannot load class %s: %s",name,e));
        } catch (ClassFormatError e) {
            throw new ClassNotFoundException(String.format(
                    "Format of class file is incorrect for class %s: %s",
                            name, e));
        }
        classMap.put(name, result);
        return result;
    }


    protected java.net.URL findResource(String name) {
        File f = findFile(name, "");
        if (f == null)
            return null;
        try {
            return f.toURL();
        } catch (java.net.MalformedURLException e) {
            return null;
        }
    }

    private File findFile(String name, String extension) {
        File f;
        for (int k = 0; k < classPath.length; k++) {
            f = new File((new File(classPath[k])).getPath()
                    + File.separatorChar
                    + name.replace('/',
                    File.separatorChar)
                    + extension);
            if (f.exists())
                return f;
        }
        return null;
    }

    public static byte[] loadFileAsBytes(File file) throws IOException {
        byte[] result = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        f.read(result, 0, result.length);
        f.close();
        return result;
    }
}
