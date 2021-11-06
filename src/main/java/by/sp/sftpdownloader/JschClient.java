package by.sp.sftpdownloader;

import com.jcraft.jsch.*;

import java.io.File;
import java.util.Vector;

public class JschClient {

    private final String remoteHost;
    private final int port;
    private final String username;
    private final String password;
    private final String remoteDirName;
    private final String localDirName;

    Session jschSession = null;

    public JschClient(String sftp_host, int sftp_port, String sftp_user, String sftp_password,
                      String sftp_remote_dir, String local_dir) {
        remoteHost = sftp_host;
        port = sftp_port;
        username = sftp_user;
        password = sftp_password;
        remoteDirName = sftp_remote_dir;
        if ((local_dir.charAt(local_dir.length() - 1)) != '/') {
            localDirName = local_dir + "/";
        } else {
            localDirName = local_dir;
        }
    }

    private ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
        jschSession = jsch.getSession(username, remoteHost, port);
        jschSession.setConfig("StrictHostKeyChecking", "no");
        jschSession.setPassword(password);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }

    public void downloadFilesFromRemoteDir() throws JSchException, SftpException, SecurityException {
        ChannelSftp channelSftp = setupJsch();
        channelSftp.connect();

        channelSftp.cd(remoteDirName);
        // Creates a Vector for objects of files and directories
        Vector<ChannelSftp.LsEntry> fileList = channelSftp.ls(remoteDirName);
        File localDir = new File(localDirName);
        if (!localDir.exists()) {
            System.out.println("The directory “" + localDirName + "” does not exist.");
            if (localDir.mkdirs()) {
                System.out.println("The directory “" + localDirName + "” is created successfully.");
            } else {
                System.out.println("The directory “" + localDirName + "” cannot be created.");
                throw new SecurityException();
            }
        }
        int filesCopiedCount = 0;
        for (ChannelSftp.LsEntry f : fileList) {
            if (!f.getAttrs().isDir()) {
                String fileName = f.getFilename();
                channelSftp.get(fileName, localDirName + fileName);
                System.out.println(fileName);
                ++filesCopiedCount;
            }
        }
        if (filesCopiedCount > 0) {
            System.out.println("\nA total of " + filesCopiedCount + " files were copied"
                    + " to the “" + localDirName + "” directory.");
        } else {
            System.out.println("No files found in the “" + remoteDirName + "” remote directory.");
        }

        channelSftp.exit();
        jschSession.disconnect();
        System.out.println("\nThe session is disconnected.");
    }
}
