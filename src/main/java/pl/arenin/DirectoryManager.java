package pl.arenin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;

public class DirectoryManager {

    private static final String PROPERTIES_FILE = "./home/count.txt";
    
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

    public void saveStatistics(Map<String, Directory> directories) {

        Properties properties = new Properties();

        properties.setProperty(directories.get("DEV").getDirName(), directories.get("DEV").getFilesCount().toString());
        properties.setProperty(directories.get("HOME").getDirName(), directories.get("HOME").getFilesCount().toString());
        properties.setProperty(directories.get("TEST").getDirName(), directories.get("TEST").getFilesCount().toString());
        properties.setProperty("TOTAL_FILES", Directory.getTotalFilesCount().toString());
        
        try {
        
            properties.store(new FileOutputStream(PROPERTIES_FILE), null);
                
        } catch (IOException e) {
            
            System.out.println("Nie udalo sie zapisac statystyk do pliku 'count.txt'. Wychodze z programu!");
            return;
        }
    }
}
