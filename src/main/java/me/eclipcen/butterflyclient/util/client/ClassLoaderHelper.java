package me.eclipcen.butterflyclient.util.client;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import sun.net.www.protocol.file.FileURLConnection;

public class ClassLoaderHelper {
   private static final Pattern PATTERN_FILE_SEPARATORS = Pattern.compile("[\\\\|\\/]");

   public static List<Path> getClassPathsInPackage(ClassLoader classLoader, String packageDir, boolean recursive) throws IOException {
      List<Path> results = new ArrayList<>();
      String pkgdir = asFilePath(packageDir);
      Enumeration<URL> inside = classLoader.getResources(pkgdir);

      for (URL url : Collections.list(inside)) {
         try {
            URLConnection connection = url.openConnection();
            String path = URLDecoder.decode(url.getPath(), "UTF-8").replace('\\', '/');
            path = path.substring(path.indexOf(47) + 1);
            if (!System.getProperty("os.name").startsWith("Windows")) {
               path = "/" + path;
            }

            String rootDir = path.substring(0, path.indexOf(pkgdir));
            String packDir = path.substring(path.lastIndexOf(pkgdir));
            if (connection instanceof FileURLConnection) {
               Path root = Paths.get(rootDir).normalize();

               for (Path p : getClassPathsInDirectory(Paths.get(path), recursive)) {
                  Path relativize = root.relativize(p);
                  results.add(relativize);
               }
            } else if (connection instanceof JarURLConnection) {
               results.addAll(getClassPathsInJar(((JarURLConnection) connection).getJarFile(), packDir, recursive));
            }
         } catch (Exception var16) {
            throw new RuntimeException(var16);
         }
      }

      return results;
   }

   public static List<Path> getClassPathsInDirectory(Path directory, boolean recursive) {
      List<Path> results = new ArrayList<>();
      searchDirectory(directory, (path) -> {
         if (Files.isDirectory(path)) {
            return recursive;
         } else {
            if (com.google.common.io.Files.getFileExtension(path.toString()).equals("class")) {
               results.add(path);
            }

            return false;
         }
      });
      return results;
   }

   public static List<Path> getClassPathsInJar(JarFile jarFile, String packageDir, boolean recursive) throws IOException {
      ArrayList<Path> results = new ArrayList<>();
      FileSystem fs = newFileSystem(jarFile.getName(), null);
      Path root = fs.getRootDirectories().iterator().next();
      Path packagePath = root.resolve(packageDir);
      Iterator<JarEntry> var7 = Collections.list(jarFile.entries()).iterator();

      while (true) {
         Path path;
         do {
            do {
               if (!var7.hasNext()) {
                  return results;
               }

               JarEntry entry = var7.next();
               path = root.resolve(entry.getName());
            } while (!com.google.common.io.Files.getFileExtension(path.toString()).equals("class"));
         } while (!recursive && path.getNameCount() != packagePath.getNameCount() + 1);

         if (path.toString().startsWith(path.getFileSystem().getSeparator() + packageDir) && path.toString().length() > packageDir.length() + 2) {
            results.add(path);
         }
      }
   }

   public static List<Path> getClassPathsInPackage(ClassLoader classLoader, String packageDir) throws IOException {
      boolean recursive = packageDir.endsWith(".*");
      return getClassPathsInPackage(classLoader, recursive ? packageDir.substring(0, packageDir.length() - 2) : packageDir, recursive);
   }

   public static List<Class<?>> getLoadedClasses(ClassLoader classLoader, Collection<Path> paths) {
      List<Class<?>> results = new ArrayList<>();

      for (Path path : paths) {
         try {
            Class<?> clazz = Class.forName(asPackagePath(path.toString()), false, classLoader);
            results.add(clazz);
         } catch (ClassNotFoundException var6) {
            return null;
         }
      }

      return results;
   }

   private static void searchDirectory(Path directory, Function<Path, Boolean> function) {
      if (Files.exists(directory) && Files.isDirectory(directory)) {
         try {

            for (Path path : Files.list(directory).collect(Collectors.toList())) {
               if (function.apply(path)) {
                  searchDirectory(path, function);
               }
            }
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

   }

   public static String asPackagePath(@Nullable String filePath) {
      if (filePath == null) {
         return "";
      } else {
         String str = getPathWithoutExtension(filePath);
         str = PATTERN_FILE_SEPARATORS.matcher(str).replaceAll(".");
         if (str.startsWith(".")) {
            str = str.substring(1);
         }

         if (str.endsWith(".")) {
            str = str.substring(0, str.length() - 1);
         }

         return str;
      }
   }

   public static String getPathWithoutExtension(String path) {
      String ext = com.google.common.io.Files.getFileExtension(path);
      return !Strings.isNullOrEmpty(ext) ? path.substring(0, path.lastIndexOf("." + ext)) : path;
   }

   public static String asFilePath(@Nullable String packagePath) {
      return packagePath == null ? "" : packagePath.replace('.', '/');
   }

   public static FileSystem newFileSystem(String filePath, ClassLoader parent) throws IOException {
      return FileSystems.newFileSystem(Paths.get(filePath), parent);
   }
}
