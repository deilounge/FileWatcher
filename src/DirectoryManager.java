import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;

public class DirectoryManager {

    public boolean createDirectory(Directory directory) {

        try {
            
            Files.createDirectory(directory.getDirPath());
            System.out.println("Utworzono katalog: " + directory.getDirName());
            return true;

        } catch (FileAlreadyExistsException e) {
            
            System.out.println("Nie udalo sie utworzyc katalogu: '" + directory.getDirName() + "'. Katalog juz istnieje.");
            return true;
        
        } catch (IOException e) {
        
            System.out.println("Problem przy operacji tworzenia katalogow.");
            System.exit(-1);
        }
        return false;
    }

    public void deleteDirectory(Directory directory) {
        
        try {
            Files.deleteIfExists(directory.getDirPath());
        
        } catch (IOException e) {
            System.out.println("Problem przy operacji kasowania katalogow.");
        }
    }

    public String getFileExtension(Path filename) {

        return filename.toString().substring(filename.toString().length()-4);
    }

    public boolean isFileTimeCreationEven(Path filename) {

        try {
              
            BasicFileAttributes fileAttributes = Files.readAttributes(filename, BasicFileAttributes.class);
            FileTime fileTime = fileAttributes.creationTime();

            LocalDateTime fileCreationTimestamp = LocalDateTime.parse(fileTime.toString().substring(0,19));
            int fileCreationHour = fileCreationTimestamp.getHour();

            System.out.println("Godzina utworzenia pliku to: " + fileCreationTimestamp + ".");

            if (fileCreationHour % 2 == 0) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
        
            System.out.println("Nie udalo sie odczytac atrybutow pliku. Wychodze z programu.");
            System.exit(-1);
        }
        return false;
    }

    public boolean moveFile(Path sourceFile, Path destinationFile) {

        try {
            
            Files.move(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            
        } catch (IOException e){
            
            System.out.println("Przenoszenie pliku '" + sourceFile + "' nie powiodlo sie. Wychodze z programu.");
            System.exit(-1);
        }
        return true;
    }

    public static void increaseFileCounter() {

    }
}
