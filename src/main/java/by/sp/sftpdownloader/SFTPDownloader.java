package by.sp.sftpdownloader;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.*;
import java.sql.SQLException;
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
        sftpFilesCopy();
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

    private void sftpFilesCopy() {
        JschClient sftpClient = new JschClient(
                settingsMap.get("sftp_host"),
                Integer.parseInt(settingsMap.get("sftp_port")),
                settingsMap.get("sftp_user"),
                settingsMap.get("sftp_password"),
                settingsMap.get("sftp_remote_dir"),
                settingsMap.get("local_dir"),
                settingsMap.get("sql_user"),
                settingsMap.get("sql_password"),
                settingsMap.get("sql_database")
        );
        try {
            sftpClient.downloadFilesFromRemoteDir();
        } catch (JSchException | SftpException | SecurityException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
