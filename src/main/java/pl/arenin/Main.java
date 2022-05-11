package pl.arenin;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import static java.nio.file.StandardWatchEventKinds.*;

public class Main{

    private static WatchKey key;
    private static WatchService watcher;
    
    public static void main(String[] args){

        Map<String, Directory> directories = new HashMap<>();

        directories.put("DEV", new Directory("DEV", Paths.get("./dev")));
        directories.put("TEST", new Directory("TEST", Paths.get("./test")));
        directories.put("HOME", new Directory("HOME", Paths.get("./home")));

        DirectoryManager directoryManager = new DirectoryManager();
        
        directoryManager.createDirectory(directories.get("DEV"));
        directoryManager.createDirectory(directories.get("TEST"));
        directoryManager.createDirectory(directories.get("HOME"));
        
        try {
            watcher = FileSystems.getDefault().newWatchService();
            directories.get("HOME").getDirPath().register(watcher, new WatchEvent.Kind[]{ENTRY_CREATE});
            directoryManager.saveStatistics(directories);
            System.out.println("------------------------------");
            
        } catch (IOException e) {
            System.out.println("Uruchomienie watchera nie powiodlo sie.");
            return;
        }

        while(true) {
            
            try {
                key = watcher.take();
            } catch (InterruptedException e){
                System.out.println("Nie udało się odczytac klucza z watchera.");
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {

                System.out.println(event.kind() + ": " + directories.get("HOME").getDirName() + "/" + event.context().toString());
                Path sourcefile = Paths.get(directories.get("HOME").getDirName() + "/" + event.context());
                
                if (directoryManager.getFileExtension(sourcefile).equals(".jar")) {
                
                    if(directoryManager.isFileTimeCreationEven(sourcefile)) {
                        
                        System.out.println("Godzina jest parzysta! -> DEV");
                        System.out.println("------------------------------");
                        Path destinationfile = Paths.get(directories.get("DEV").getDirName() + "/" + event.context());
                        directoryManager.moveFile(sourcefile, destinationfile);
                        directories.get("DEV").increaseFileCounter();
                        directoryManager.saveStatistics(directories);

                    } else {

                        System.out.println("Godzina jest nieparzysta! -> TEST");
                        System.out.println("------------------------------");
                        Path destinationfile = Paths.get(directories.get("TEST").getDirName() + "/" + event.context());
                        directoryManager.moveFile(sourcefile, destinationfile);
                        directories.get("TEST").increaseFileCounter();
                        directoryManager.saveStatistics(directories);
                    }
                    
                } else if (directoryManager.getFileExtension(sourcefile).equals(".xml")) {
                    
                    Path destinationfile = Paths.get(directories.get("DEV").getDirName() + "/" + event.context());
                    directoryManager.moveFile(sourcefile, destinationfile);
                    System.out.println("Plik XML -> DEV");
                    System.out.println("------------------------------");
                    directories.get("DEV").increaseFileCounter();
                    directoryManager.saveStatistics(directories);
                }
            }
            key.reset();
        }
    }
}