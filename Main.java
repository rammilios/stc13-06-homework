package ConsoleEnter;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class Main extends ClassLoader {
    public Main(ClassLoader parent) {
        super (parent);
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String line = "";
        String filepath = "C:\\Users\\user\\Desktop\\Java\\JavaItis5\\classLoaders\\src\\ConsoleEnter\\MyClass.java";
        Scanner scanner = new Scanner(System.in);
        String codeBody = "package ConsoleEnter;\n" +
                "\n" +
                "public class MyClass {\n" +
                "\t\n";
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.equals("")) {
                break;
            } else {
                codeBody += line + "\r\n";
            }
        }
        codeBody += "}";
        try (BufferedOutputStream dataOutputStream = new BufferedOutputStream(new FileOutputStream(filepath))) {
            byte[] buffer = codeBody.getBytes();
            dataOutputStream.write(buffer, 0, codeBody.length());
        }
        System.out.println("Java file created ");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, filepath);

        ClassLoader myClassLoader = MyClass.class.getClassLoader();
        Main myClass1 = new Main(myClassLoader);
        Class myClass = myClass1.loadClass("ConsoleEnter.MyClass");
        myClass.getMethod("doWork").invoke(myClass.newInstance(), null);

    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.equals("ConsoleEnter.MyClass")) {
            String dest = "file:C:\\Users\\user\\Desktop\\Java\\JavaItis5\\classLoaders\\src\\ConsoleEnter\\MyClass.class";
            byte[] classData = null;
            try (InputStream inputStream = new URL(dest).openConnection().getInputStream();){

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                int data = inputStream.read();
                while (data !=-1) {
                    byteArrayOutputStream.write(data);
                    data = inputStream.read();
                }

                classData = byteArrayOutputStream.toByteArray();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return defineClass(name, classData, 0, classData.length);
        } else {
            return super.loadClass(name);
        }
    }
}


