package de.dytanic.cloudnetwrapper.util;

import com.google.gson.Gson;
import de.dytanic.cloudnetwrapper.models.PaperMCProject;
import de.dytanic.cloudnetwrapper.models.PaperMCProjectVersion;
import jline.console.ConsoleReader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;

public final class PaperBuilder {

    private static Gson gson = new Gson();
    private static Process exec;
    private static String apiProjectUrl = "https://papermc.io/api/v1/paper";
    private static String apiProjectVersionDownload = "https://papermc.io/api/v1/paper/%s/%s/download";
    private static String API_PROJECT_VERSION_URL = "https://papermc.io/api/v1/paper/%s";

    /**
     * Start the process of choice the paper version And build after choice
     *
     * @param reader Read the answer from console
     */
    public static void start(ConsoleReader reader) {
        try {
            System.out.println("Fetch Versions");
            URLConnection connection = new URL(apiProjectUrl).openConnection();
            connection.connect();
            PaperMCProject paperMCProject = gson
                    .fromJson(new InputStreamReader(connection.getInputStream()), PaperMCProject.class);
            System.out.println("Available PaperSpigot Versions");
            System.out
                    .println("-----------------------------------------------------------------------------");
            System.out.println("PaperSpigot Version");
            System.out.println();
            System.out
                    .println("-----------------------------------------------------------------------------");
            for (String version : paperMCProject.getVersions()) {
                System.out.printf("%s\n", version);
            }
            System.out
                    .println("-----------------------------------------------------------------------------");
            String answer = null;
            while (answer == null) {
                String name = null;
                try {
                    name = reader.readLine().toLowerCase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String finalAnswer = name;
                if (Arrays.stream(paperMCProject.getVersions())
                        .anyMatch(e -> e.equalsIgnoreCase(finalAnswer))) {
                    answer = name;
                    buildPaperVersion(answer);
                } else if (Arrays.stream(paperMCProject.getVersions())
                        .noneMatch(e -> e.equalsIgnoreCase(finalAnswer))) {
                    System.out.println("Version does not exist!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Build or copy the paper spigot
     *
     * @param version the version of the paper jar
     * @throws Exception If a connection error or something
     */
    private static void buildPaperVersion(String version) throws Exception {
        System.out.println("Fetch Builds");
        URLConnection connection = new URL(String.format(API_PROJECT_VERSION_URL, version))
                .openConnection();
        connection.connect();
        PaperMCProjectVersion paperMCProjectVersion = gson
                .fromJson(new InputStreamReader(connection.getInputStream()), PaperMCProjectVersion.class);

        connection = new URL(String.format(
                apiProjectVersionDownload, version, paperMCProjectVersion.getBuilds().getLatest()))
                .openConnection();
        connection.connect();
        File builder = new File("local/builder/papermc");
        File buildFolder = new File(builder, version);
        buildFolder.mkdirs();
        File paperclip = new File(buildFolder, "paperclip.jar");
        if (!paperclip.exists()) {
            runPaperClip(connection, buildFolder, paperclip);
        } else {
            if (Objects.requireNonNull(
                    buildFolder.listFiles(pathname -> pathname.getName().startsWith("paperclip"))).length
                    > 0) {
                System.out.println("Skipping build");
                System.out.println("Copy spigot.jar");
                try {
                    Files.copy(new FileInputStream(Objects.requireNonNull(
                            buildFolder.listFiles(pathname -> pathname.getName().startsWith("paper")))[0]),
                            Paths.get("local/spigot.jar"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                SpigotBuilder.deleteBuildFolder(buildFolder);
                runPaperClip(connection, buildFolder, paperclip);
                return;
            }

        }

    }

    /**
     * Run paperclip if the jar not exists
     *
     * @param connection  the connection of the jar
     * @param buildFolder the build folder of the jar
     * @param paperclip   the jar file path
     * @throws IOException Throws if input stream null
     */
    private static void runPaperClip(URLConnection connection, File buildFolder, File paperclip)
            throws IOException {
        System.out.println("Download PaperClip");
        try (InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, Paths.get(paperclip.toURI()), StandardCopyOption.REPLACE_EXISTING);
        }
        exec = Runtime.getRuntime()
                .exec("java -jar pa perclip.jar", null, buildFolder);
        printProcessOutputToConsole(exec);

        Files.copy(new FileInputStream(Objects.requireNonNull(
                buildFolder.listFiles(pathname -> pathname.getName().startsWith("paperclip")))[0]),
                Paths.get("local/spigot.jar"), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Print the process output to wrapper console
     *
     * @param exec the running process
     * @throws IOException throws if readline null
     */
    static void printProcessOutputToConsole(Process exec) throws IOException {
        while (exec.isAlive()) {
            InputStream inputStream = exec.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            bufferedReader.lines().forEach(System.out::println);
            InputStream errorStream = exec.getErrorStream();
            InputStreamReader errorInputStreamReader = new InputStreamReader(errorStream);
            BufferedReader errorBufferedReader = new BufferedReader(errorInputStreamReader);
            errorBufferedReader.lines().forEach(System.err::println);
        }
        System.out.println("Build finish!");
        System.out.println("Copy spigot.jar");
    }

    public static Process getExec() {
        return exec;
    }
}
