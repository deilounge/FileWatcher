import java.nio.file.Path;

public class Directory {
    
    private String dirName;
    private Path dirPath;
    private int filesCount;
    
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

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }
}
