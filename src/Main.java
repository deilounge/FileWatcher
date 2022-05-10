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
    private static int counter;
    
    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

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


        for(;;) {

            WatchKey loopKey;
            
            try {
                
                loopKey = watcher.take();

            } catch (InterruptedException exception) {
                
                return;
            }


            if (loopKey == keys.get(dirHOME)) {
                System.out.println("Klucz sie zgadza!");
                System.out.println(keys.get(dirHOME));
                System.out.println(loopKey);
            }

            Thread.sleep(2000);

            System.out.println(loopKey.reset());
            key.pollEvents();
                


            // for (WatchEvent<?> event: key.pollEvents()) {
            //     WatchEvent.Kind kind = event.kind();

            //     // Context for directory entry event is the file name of entry
            //     WatchEvent<Path> ev = cast(event);
            //     Path name = ev.context();
            //     Path child = dirHOME.resolve(name);

            //     // print out event

            //     System.out.format("%s: %s\n", event.kind().name(), child);
            // }   
            
            // key.reset();
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
