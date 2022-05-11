package pl.arenin;

import java.nio.file.Path;

public class Directory {
    
    private String dirName;
    private Path dirPath;
    private Integer filesCount = 0;
    private static Integer totalFilesCount = 0;
    
    public Directory(String dirName, Path dirPath) {
        this.dirName = dirName;
        this.dirPath = dirPath;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public Path getDirPath() {
        return dirPath;
    }

    public void setDirPath(Path dirPath) {
        this.dirPath = dirPath;
    }

    public void setFilesCount(Integer filesCount) {
        this.filesCount = filesCount;
    }

    public Integer getFilesCount() {
        return filesCount;
    }

    public static void setTotalFilesCount(Integer totalFilesCount) {
        Directory.totalFilesCount = totalFilesCount;
    }

    public static Integer getTotalFilesCount() {
        return totalFilesCount;
    }

    public void increaseFileCounter() {
        totalFilesCount++;
        filesCount++;
    }
}
