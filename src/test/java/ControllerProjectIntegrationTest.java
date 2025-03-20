import com.example.constructionsystem.HelloController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
public class ControllerProjectIntegrationTest {

    private HelloController controller;
    private CountDownLatch latch;

    //W wyniku Alertow, testy działały błędnie, bo nie przechodzily przez nie. Stąd
    //Stąd odpalamy testy na jednym wątku. Uruchamiamy najpierw JavaFX przed testami
    //Platform.runLater pozwala na odpalenie na tym samym wątku, by osiagnąc zamierzony efekt
    //CountDownLatch ma za zadanie wstrzymanie zadania by poprawnie wykonał się test
    //w ciagu 5 sekund(ustawionych przeze mnuie) bez tego test sie konczyl bez ostatecznego rezultatu

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        controller = new HelloController();
    }

    @Test
    void testAddProject_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.addProject("Remont mieszkania w kamienicy", "Jakub Kleszcz", "Bochnia",
                    LocalDate.parse("2025-06-13"), "Nie dotyczy", "250000",
                    "200000", "Stare mieszkanie w starej kamienicy, kompleksowy remont");

            assertTrue(result, "Projekt pomyślnie dodany.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddProject_emptyNazwa() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.addProject("", "Jakub Kleszcz", "Bochnia",
                    LocalDate.parse("2025-06-13"), "Nie dotyczy", "250000",
                    "200000", "Stare mieszkanie w starej kamienicy, kompleksowy remont");

            assertFalse(result, "Projekt nie dodany. Pole nazwa jest puste");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddProject_kosztorysLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.addProject("Remont mieszkania w kamienicy", "Jakub Kleszcz", "Bochnia",
                    LocalDate.parse("2025-06-13"), "Nie dotyczy", "-250000",
                    "200000", "Stare mieszkanie w starej kamienicy, kompleksowy remont");

            assertFalse(result, "Projekt nie dodany. Pole kosztorys jest ujemne!");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddProject_oplaconaLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.addProject("Remont mieszkania w kamienicy", "Jakub Kleszcz", "Bochnia",
                    LocalDate.parse("2025-06-13"), "Nie dotyczy", "250000",
                    "-200000", "Stare mieszkanie w starej kamienicy, kompleksowy remont");

            assertFalse(result, "Projekt nie dodany. Pole oplacona jest ujemne!");
            latch.countDown();
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddProject_pastDeadline() throws InterruptedException {
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.addProject("Remont mieszkania w kamienicy", "Jakub Kleszcz", "Bochnia",
                    LocalDate.now().minusDays(1), "Nie dotyczy", "250000",
                    "-200000", "Stare mieszkanie w starej kamienicy, kompleksowy remont");

            assertFalse(result, "Nie można dodać projektu z datą w przeszłości.");
            latch.countDown();
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testAddProject_oplaconaBiggerThanKosztorys() throws InterruptedException {
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.addProject("Remont mieszkania w kamienicy", "Jakub Kleszcz", "Bochnia",
                    LocalDate.parse("2025-06-13"), "Nie dotyczy", "200000",
                    "250000", "Stare mieszkanie w starej kamienicy, kompleksowy remont");

            assertFalse(result, "Oplacona kwota jest wieksza od kosztorysu");
            latch.countDown();
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    //
    //EDYCJA
    //


    @Test
    void testUpdateProject_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.updateProject(14,"Remont mieszkania w bloku", "Jakub Malek", "Kraków",
                    LocalDate.parse("2025-06-13"), "Nie dotyczy", "100000",
                    "50000", "Lekki remont kuchni w bloku");

            assertTrue(result, "Projekt pomyślnie edytowany.");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateProject_emptyMiasto() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.updateProject(14,"Remont mieszkania w bloku", "Jakub Malek", "",
                    LocalDate.parse("2025-06-13"), "Nie dotyczy", "100000",
                    "50000", "Lekki remont kuchni w bloku");

            assertFalse(result, "Projekt nie aktualizowany. Pole miejscowosc jest puste");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateProject_kosztorysLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.updateProject(14,"Remont mieszkania w bloku", "Jakub Malek", "Kraków",
                    LocalDate.parse("2025-06-13"), "Nie dotyczy", "-100000",
                    "50000", "Lekki remont kuchni w bloku");

            assertFalse(result, "Projekt nie dodany. Pole kosztorys jest ujemne!");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateProject_oplaconaLessThan0() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            boolean result = controller.updateProject(14,"Remont mieszkania w bloku", "Jakub Malek", "Kraków",
                    LocalDate.parse("2025-06-13"), "Nie dotyczy", "100000",
                    "-50000", "Lekki remont kuchni w bloku");

            assertFalse(result, "Projekt nie dodany. Pole kosztorys jest ujemne!");
            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateProject_pastDeadline() throws InterruptedException {
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.updateProject(14,"Remont mieszkania w bloku", "Jakub Malek", "Kraków",
                    LocalDate.now().minusDays(1), "Nie dotyczy", "100000",
                    "50000", "Lekki remont kuchni w bloku");

            assertFalse(result, "Nie można dodać projektu z datą w przeszłości.");
            latch.countDown();
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testUpdateProject_oplaconaBiggerThanKosztorys() throws InterruptedException {
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.updateProject(14,"Remont mieszkania w bloku", "Jakub Malek", "Kraków",
                    LocalDate.now().parse("2025-06-13"), "Nie dotyczy", "50000",
                    "100000", "Lekki remont kuchni w bloku");

            assertFalse(result, "Oplacona kwota jest wieksza od kosztorysu");
            latch.countDown();
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    //
    //USUWANIE
    //

    @Test
    void testDeleteProject_Success() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(()-> {
            boolean result = controller.deleteProject(15);

            assertTrue(result, "Pomyslne usuniecie rekordu 15");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testDeleteProject_idError() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(()-> {
            boolean result = controller.deleteProject(999);

            assertFalse(result, "Usuniecie projektu o id 999 powinna zakonczyc sie bledem. Nie istnieje taki rekord");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testDeleteProject_idLessThan0() throws InterruptedException{
        latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            boolean result = controller.deleteEmployee(-1);

            assertFalse(result, "Usuniecie projektu o id -1 powinna zakonczyc sie bledem. Nie istnieje ujemny rekord");
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

}
