import com.example.constructionsystem.HelloController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerReportIntegrationTest {

    private HelloController controller;
    private CountDownLatch latch;

    //W wyniku Alertow, testy działały błędnie, bo nie przechodzily przez nie. Stąd
    //Stąd odpalamy testy na jednym wątku. Uruchamiamy najpierw JavaFX przed testami
    //Platform.runLater pozwala na odpalenie na tym samym wątku, by osiagnąc zamierzony efekt
    //CountDownLatch ma za zadanie wstrzymanie zadania by poprawnie wykonał się test
    //w ciagu 5 sekund(ustawionych przeze mnuie) bez tego test sie konczyl bez ostatecznego rezultatu

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
    void testReportAdd_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.reportAdd(13, "Rozpoczynamy prace",  LocalDate.parse("2025-06-13"));

            assertTrue(result, "Raport powinien sie pomyslnie dodac.");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportAdd_contentEmpty() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.reportAdd(13, "",  LocalDate.parse("2025-06-13"));

            assertFalse(result, "Raport nie doda się, tresc jest pusta");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportAdd_idLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.reportAdd(-1, "Rozpoczynamy prace",  LocalDate.parse("2025-06-13"));

            assertFalse(result, "Raport nie doda się, id nie moze byc ujemne");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportAdd_idProjectDoesntExist() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.reportAdd(999, "Rozpoczynamy prace",  LocalDate.parse("2025-06-13"));

            assertFalse(result, "Raport nie doda się, nie istnieje takie id");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportAdd_pastDate() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.reportAdd(13, "Rozpoczynamy prace",  LocalDate.now().minusDays(1));

            assertFalse(result, "Raport nie doda się, Data dodania jest z przeszlosci");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportAdd_nullDate() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.reportAdd(13, "Rozpoczynamy prace",  null);

            assertFalse(result, "Raport nie doda się, Data dodania jest pusta");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    //
    //EDYCJA
    //

    @Test
    void testReportUpdate_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.updateReport(13, "Zakończono szalowanie stropu",  LocalDate.now());

            assertTrue(result, "Raport powinien sie pomyslnie dodac.");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportUpdate_ContentEmpty() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.updateReport(13, "",  LocalDate.now());

            assertFalse(result, "Raport nie doda się, treść jest pusta");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportUpdate_idLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.updateReport(-1, "Rozpoczynamy prace", LocalDate.now());

            assertFalse(result, "Raport nie doda się, id nie moze byc ujemne");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportUpdate_idProjectDoesntExist() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.updateReport(999, "Rozpoczynamy prace",  LocalDate.now());

            assertFalse(result, "Raport nie doda się, nie istnieje takie id");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportUpdate_pastDate() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.updateReport(13, "Rozpoczynamy prace",  LocalDate.now().minusDays(1));

            assertFalse(result, "Raport nie doda się, Data dodania jest z przeszlosci");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    //
    //USUWANIE
    //

    @Test
    void testReportDelete_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.deleteReport(14);

            assertTrue(result, "Raport usuniety poprawnie");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportDelete_idLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.deleteReport(-1);

            assertFalse(result, "Raport nie zostanie usuniety, id jest ujemne");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testReportDelete_idProjectDoesntExist() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.deleteReport(999);

            assertFalse(result, "Raport nie zostanie usuniety, id nie istnieje");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }


}