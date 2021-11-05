package by.sp.sftpdownloader;

import java.io.*;
import java.util.HashMap;

public class SFTPDownloader {

    HashMap<String, String> settingsMap;

    public static void main(String[] args) {
        if (args.length > 0) {
            new SFTPDownloader().startUp(args[0]);
        } else {
            System.out.println("Please enter a path to a file with settings as the first argument:\n" +
                    "SFTPDownloader path/to/file_with_settings.txt");
        }
    }

    private void startUp(String pathToFile) {
        File fileWithSettings = new File(pathToFile);
        readFile(fileWithSettings);
        readMap();
    }

    private void readFile(File fileWithSettings) {
        settingsMap = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileWithSettings));
            String line;
            while ((line = reader.readLine()) != null) {
                lineToMap(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lineToMap(String lineToParse) {
        String[] result = lineToParse.split("=");
        settingsMap.put(result[0], result[1]);
    }

    private void readMap() {
        System.out.println(settingsMap);
        System.out.println("sftp_host: " + settingsMap.get("sftp_host"));
        System.out.println("sftp_port: " + settingsMap.get("sftp_port"));
        System.out.println("sftp_user: " + settingsMap.get("sftp_user"));
        System.out.println("sftp_password: " + settingsMap.get("sftp_password"));
        System.out.println("sftp_remote_dir: " + settingsMap.get("sftp_remote_dir"));
        System.out.println("local_dir: " + settingsMap.get("local_dir"));
        System.out.println("sql_user: " + settingsMap.get("sql_user"));
        System.out.println("sql_password: " + settingsMap.get("sql_password"));
        System.out.println("sql_database: " + settingsMap.get("sql_database"));
    }
}
