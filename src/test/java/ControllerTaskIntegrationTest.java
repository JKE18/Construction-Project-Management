import com.example.constructionsystem.HelloController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class ControllerTaskIntegrationTest {

    private HelloController controller;
    private CountDownLatch latch;

    //W wyniku Alertow, testy działały błędnie, bo nie przechodzily przez nie. Stąd
    //Stąd odpalamy testy na jednym wątku. Uruchamiamy najpierw JavaFX przed testami
    //Platform.runLater pozwala na odpalenie na tym samym wątku, by osiagnąc zamierzony efekt
    //CountDownLatch ma za zadanie wstrzymanie zadania by poprawnie wykonał się test
    //w ciagu 5 sekund(ustawionych przeze mnuie) bez tego test sie konczyl bez ostatecznego rezultatu
    //poniewaz brakowalo czasu
    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {
        });
    }

    @BeforeEach
    void setUp() {
        controller = new HelloController();
    }

    @Test
    void testAddTask_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskAdd("Jutro musze załatwic pozwolenie w urzedzie", LocalDate.now().plusDays(7));

            assertTrue(result, "Zadanie pomyślnie dodane.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddTask_emptyOpis() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskAdd("", LocalDate.now().plusDays(7));

            assertFalse(result, "Zadanie nie doda sie, opis jest pusty.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddTask_emptyDate() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskAdd("Jutro musze załatwic pozwolenie w urzedzie", null);

            assertFalse(result, "Zadanie nie doda sie, opis jest pusty.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddTask_PastDate() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskAdd("Jutro musze załatwic pozwolenie w urzedzie", LocalDate.now().minusDays(7));

            assertFalse(result, "Zadanie nie doda sie, opis jest pusty.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    //
    //EDYCJA
    //

    @Test
    void testUpdateTask_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskEdit(4, "Jutro musze załatwic pozwolenie w powiecie", LocalDate.now().plusDays(5));

            assertTrue(result, "Zadanie edytowane pomyślnie.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateTask_opisEmpty() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskEdit(4, "", LocalDate.now().plusDays(7));

            assertFalse(result, "Zadanie zawiera puste pole description.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateTask_dateNull() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskEdit(4, "Jutro musze załatwic pozwolenie w powiecie", null);

            assertFalse(result, "Zadanie zawiera puste pole deadline.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateTask_PastDate() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskEdit(4, "Jutro musze załatwic pozwolenie w powiecie", LocalDate.now().minusDays(7));

            assertFalse(result, "Zadanie zawiera deadline z przeszlosci");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateTask_idDoesntExist() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskEdit(999, "Jutro musze załatwic pozwolenie w powiecie", LocalDate.now().minusDays(7));

            assertFalse(result, "Nie istnieje zadanie o takim id");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }


    //
    //USUWANIE
    //

    @Test
    void testDeleteTask_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskRemove(4);

            assertTrue(result, "Zadanie usuniete pomyślnie.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testDeleteTask_idLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskRemove(-1);

            assertFalse(result, "Zadanie nie usunie sie. Id jest ujemne");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testDeleteTask_idDoesntExist() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.taskRemove(999);

            assertFalse(result, "Zadanie nie usunie sie. Id nie istnieje");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

}