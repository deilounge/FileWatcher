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
                        //directoryManager.increaseFileCounter();

                    } else {

                        System.out.println("Godzina jest nieparzysta! -> TEST");
                        System.out.println("------------------------------");
                        Path destinationfile = Paths.get(directories.get("TEST").getDirName() + "/" + event.context());
                        directoryManager.moveFile(sourcefile, destinationfile);
                        //directoryManager.increaseFileCounter();
                    }
                    
                } else if (directoryManager.getFileExtension(sourcefile).equals(".xml")) {
                    
                    Path destinationfile = Paths.get(directories.get("DEV").getDirName() + "/" + event.context());
                    directoryManager.moveFile(sourcefile, destinationfile);
                    System.out.println("Plik XML -> DEV");
                    System.out.println("------------------------------");
                    //directoryManager.increaseFileCounter();
                }
            }
            key.reset();
        }
    }
}

// plik z rozszerzeniem .jar, którego godzina utworzenia jest parzysta przenosimy do folderu DEV
// plik z rozszerzeniem .jar, którego godzina utworzenia jest nieparzysta przenosimy do folderu TEST
// plik z rozszerzeniem .xml, przenosimy do folderu DEV

// Dodatkowo w nowo stworzonym pliku /home/count.txt należy przechowywać liczbę przeniesionych plików (wszystkich i w podziale na
// katalogi), plik powinien w każdym momencie działania programu przechowywać aktualną liczbę przetworzonych plików.

// Wymagania techniczne
// Można użyć dowolnych bibliotek i frameworków na licencjach otwartych (np. MIT, Apache, itp.).
// Projekt powinien być budowany i uruchamiany przez wybrany system do budowania (np. maven, gradle, itp..).
// Do projektu powinna być dołączona instrukcja budowania i uruchamiania

// Rozwiązanie
// Przesłane rozwiązanie powinno zawierać:
// repozytorium kodu z rozwiązaniem (jako archiwum .zip)
// dostarczone materiały, oprócz przesłanego kodu źródłowego powinny zawierać również informację jak skompilować / zbudować
// dostarczone źródła i sposób uruchomienia kodu 
