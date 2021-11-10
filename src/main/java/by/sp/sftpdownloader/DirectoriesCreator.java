package by.sp.sftpdownloader;

import java.io.File;

public class DirectoriesCreator {

     public static void createDir(String dirName) throws SecurityException {
        File localDir = new File(dirName);
        if (!localDir.exists()) {
            System.out.println("The directory “" + dirName + "” does not exist.");
            if (localDir.mkdirs()) {
                System.out.println("The directory “" + dirName + "” is created successfully.");
            } else {
                System.out.println("The directory “" + dirName + "” cannot be created.");
                throw new SecurityException();
            }
        }

    }
}
