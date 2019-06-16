import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sun.tools.javac.api.JavacTool;

import javax.tools.JavaCompiler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Extractor implements RequestHandler<String, String> {
    private static String lambdaTaskRoot = System.getenv("LAMBDA_TASK_ROOT");

    public String handleRequest(String code, Context context) {
        try {
            File tmpDir = Files.createTempDirectory("me").toFile();
            File sourceFile = new File(tmpDir.toString(), "Main.java");
            Writer output = new BufferedWriter(new FileWriter(sourceFile));
            output.write(code);
            output.close();

            compile(sourceFile);
            run(tmpDir);

            return new String(Files.readAllBytes(Paths.get(tmpDir.toString(), "visualization.json")));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private static void compile(File sourceFile) throws Exception {
        JavaCompiler compiler = JavacTool.create();
        try (ByteArrayOutputStream stderr = new ByteArrayOutputStream()) {
            File libDir = new File(lambdaTaskRoot + "/lib");
            File[] libFiles = libDir.listFiles(pathname -> pathname.getName().endsWith(".jar"));
            List<String> libPaths = new ArrayList<>();
            if (libFiles != null) {
                for (File file : libFiles) {
                    libPaths.add(file.toString());
                }
            }

            int exitCode = compiler.run(null, null, stderr, "-cp", String.join(":", libPaths), sourceFile.toString());
            if (exitCode != 0) throw new Exception(stderr.toString());
        }
    }

    private static void run(File tmpDir) throws Exception {
        String[] cmdarray = new String[]{"java", "-cp", lambdaTaskRoot + "/lib/*:.", "Main"};
        String[] envp = new String[]{"ALGORITHM_VISUALIZER=1"};
        Process process = Runtime.getRuntime().exec(cmdarray, envp, tmpDir);

        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line).append("\n");
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) throw new Exception(out.toString());
    }
}
