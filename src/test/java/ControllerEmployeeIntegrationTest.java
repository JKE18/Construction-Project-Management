import com.example.constructionsystem.HelloController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class ControllerEmployeeIntegrationTest {

    private HelloController controller;
    private CountDownLatch latch;

    //W wyniku Alertow, testy działały błędnie, bo nie przechodzily przez nie. Stąd
    //Stąd odpalamy testy na jednym wątku co program. Uruchamiamy najpierw JavaFX przed testami
    //Platform.runLater pozwala na odpalenie na tym samym wątku, by osiagnąc zamierzony efekt
    //CountDownLatch ma za zadanie wstrzymanie zadania by poprawnie wykonał się test
    //w ciagu 5 sekund(ustawionych przeze mnuie) bez tego test sie konczyl bez ostatecznego rezultatu
    //poniewaz brakowalo czasu
    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        controller = new HelloController();
    }

    @Test
    void testAddEmployee_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.addEmployee("Jakub", "Kleszcz", "Delegacja",
                    "2000", "5001", "eniu@o2.pl",
                    "123456789", "Kierownik");

            assertTrue(result, "Pracownik pomyślnie dodany.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }


    @Test
    void testAddEmployee_failNumber() throws InterruptedException{
        latch = new CountDownLatch(1);
        //numer 8 cyfrowy
        Platform.runLater(() -> {
            boolean result = controller.addEmployee("Jakub", "Kleszcz", "Delegacja",
                    "2000", "5001", "eniu@o2.pl",
                    "12345678", "Kierownik");

            assertFalse(result, "Pracownik zawiera 8 cyfrowy numer.");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddEmployee_failYearofBirth() throws InterruptedException {
        latch = new CountDownLatch(1);

        //rok urodzenia przed 1900
        Platform.runLater(() -> {
            boolean result = controller.addEmployee("Jakub", "Kleszcz", "Delegacja",
                    "1880", "5001", "eniu@o2.pl",
                    "123456789", "Kierownik");

            assertFalse(result, "Pracownik zawiera rok urodzenia przed 1900.");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddEmployee_phoneLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.addEmployee("Jakub", "Kleszcz", "Delegacja",
                    "1880", "5001", "eniu@o2.pl",
                    "-123456789", "Kierownik");

            assertFalse(result, "Pracownik zawiera numer telefonu ponizej 0.");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddEmployee_salaryLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.addEmployee("Jakub", "Kleszcz", "Delegacja",
                    "1880", "-500", "eniu@o2.pl",
                    "123456789", "Kierownik");

            assertFalse(result, "Pracownik zawiera numer telefonu ponizej 0.");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddEmployee_emptyImie() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.addEmployee("", "Kleszcz", "Delegacja",
                    "2000", "5001", "eniu@o2.pl",
                    "123456789", "Kierownik");

            assertFalse(result, "Pracownik zawiera puste pole imie");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }


    //edycja

    @Test
    void testUpdateEmployee_Success() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.updateEmployee(34, "Franek", "Meszek", "Obecny",
                "2002", "5000", "franek@o2.pl", "513531533", "Pomocnik");

            assertTrue(result, "Edycja pracownika o id 34 powinna przejsc pomyslnie");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateEmployee_failYearofBirth() throws InterruptedException{
        latch = new CountDownLatch(1);

        //rok urodzenia przed 1900
        Platform.runLater(() -> {
            boolean result = controller.updateEmployee(34, "Franek", "Meszek", "Obecny",
                    "1880", "5000", "franek@o2.pl", "513531533", "Pomocnik");

            assertFalse(result, "Edycja pracownika o id 34 powinna zakonczyc sie bledem. Bład roku urodzenia");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateEmployee_phoneLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.updateEmployee(34, "Franek", "Meszek", "Obecny",
                    "2002", "5000", "franek@o2.pl", "-513531533", "Pomocnik");

            assertFalse(result, "Pracownik zawiera numer telefonu ponizej 0.");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateEmployee_salaryLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.updateEmployee(34, "Franek", "Meszek", "Obecny",
                    "2002", "-5000", "franek@o2.pl", "513531533", "Pomocnik");


            assertFalse(result, "Pracownik zawiera wynagrodzenie ponizej 0.");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateEmployee_failNumber() throws InterruptedException{
        latch = new CountDownLatch(1);

        //numer z 7 cyframi
        Platform.runLater(() -> {
            boolean result = controller.updateEmployee(34, "Franek", "Meszek", "Obecny",
                    "2002", "5000", "franek@o2.pl", "5135315", "Pomocnik");

            assertFalse(result, "Edycja pracownika o id 34 powinna zakonczyc sie bledem. 7 cyfr w numerze telefonu");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateEmployee_emptynazwisko() throws InterruptedException{
        latch = new CountDownLatch(1);

        //numer z 7 cyframi
        Platform.runLater(() -> {
            boolean result = controller.updateEmployee(34, "Franek", "", "Obecny",
                    "2002", "5000", "franek@o2.pl", "513531533", "Pomocnik");

            assertFalse(result, "Edycja pracownika o id 34 powinna zakonczyc sie bledem. Puste pole nazwisko");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateEmployee_IDerror() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.updateEmployee(999, "Franek", "Meszek", "Obecny",
                    "2002", "5000", "franek@o2.pl", "513531533", "Pomocnik");

            assertFalse(result, "Edycja pracownika o id 999 powinna zakonczyc sie bledem. Nie istnieje taki rekord");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }
    @Test
    void testUpdateEmployee_idLessThan0() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.updateEmployee(-1, "Franek", "Meszek", "Obecny",
                    "2002", "5000", "franek@o2.pl", "513531533", "Pomocnik");

            assertFalse(result, "Edycja pracownika o id -1 powinna zakonczyc sie bledem. Nie istnieje taki rekord");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    //usuwanie

    @Test
    void testDeleteEmployee_Success() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(()-> {
            boolean result = controller.deleteEmployee(35);

            assertTrue(result, "Pomyslne usuniecie rekordu 35");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testDeleteEmployee_idError() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.deleteEmployee(999);

            assertFalse(result, "Usuniecie pracownika o id 999 powinna zakonczyc sie bledem. Nie istnieje taki rekord");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testDeleteEmployee_idLessThan0() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.deleteEmployee(-1);

            assertFalse(result, "Usuniecie pracownika o id -1 powinna zakonczyc sie bledem. Nie istnieje taki rekord");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

}
