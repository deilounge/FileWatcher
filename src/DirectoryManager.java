import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryManager {

    public boolean createDirectory(Path directory) {

        try {
            
            Files.createDirectory(directory);
            System.out.println("Utworzono katalog: " + directory);
            return true;

        } catch (FileAlreadyExistsException e) {
            
            System.out.println("Nie udalo sie utworzyc katalogu: '" + directory + "'. Katalog juz istnieje.");
            return true;
        
        } catch (IOException e) {
        
            System.out.println("Problem przy operacji tworzenia katalogow.");
        }
        return false;
    }

    public void deleteDirectory(Path path) {
        
        try {
            Files.deleteIfExists(path);
        
        } catch (IOException e) {
            System.out.println("Problem przy operacji kasowania katalogow.");
        }
    }
}
