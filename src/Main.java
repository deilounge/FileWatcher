import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.StandardWatchEventKinds;

public class Main {

    private static WatchKey key;
    private static Map<Path, WatchKey> keys = new HashMap<>();
    
    public static void main(String[] args) throws Exception  {

        Path dirDEV = Paths.get("DEV");
        Path dirTEST = Paths.get("TEST");
        Path dirHOME = Paths.get("HOME");

        DirectoryManager directoryManager = new DirectoryManager();
        directoryManager.createDirectory(dirDEV);
        directoryManager.createDirectory(dirTEST);
        directoryManager.createDirectory(dirHOME);

        WatchService watcher = FileSystems.getDefault().newWatchService();

        try {
            key = dirHOME.register(watcher, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE});
            keys.put(dirHOME, key);
                    
        } catch (IOException e) {
            System.out.println("Uruchomienie watchera nie powiodło się.");
            e.printStackTrace();
        }

        while(true) {

            //WatchKey loopKey;
            
            try {
                key = watcher.take();
            } catch (InterruptedException exception) {
                System.out.println("Wyjątek");
                return;
            }

            for (WatchEvent event : key.pollEvents()) {

                System.out.println(event.kind() + ": " +dirHOME.getFileName() + "/" + event.context().toString());
                
                Path sourcefile = Paths.get(dirHOME + "/" + event.context());
                
                if (directoryManager.getFileExtension(sourcefile).equals(".jar")) {
                
                    Path destinationfile = Paths.get(dirDEV + "/" + event.context());
                    directoryManager.moveFile(sourcefile, destinationfile);

                } else if (directoryManager.getFileExtension(sourcefile).equals(".xml")) {
                    
                    Path destinationfile = Paths.get(dirTEST + "/" + event.context());
                    directoryManager.moveFile(sourcefile, destinationfile);
                }
            }

            key.reset();
        }

        // directoryManager.deleteDirectory(dirDEV);
        // directoryManager.deleteDirectory(dirTEST);
        // directoryManager.deleteDirectory(dirHOME);
    }
}

// Zadanie - aplikacja do segregowania plików (1-2h)

// Twoim zadaniem jest napisanie programu, który będzie umożliwiał segregowanie plików. Program powinien:
// stworzyć strukturę katalogów
// HOME
// DEV
// TEST

// W momencie pojawienia się w katalogu HOME pliku w zależności od rozszerzenia przeniesie go do folderu wg następujących reguł
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
