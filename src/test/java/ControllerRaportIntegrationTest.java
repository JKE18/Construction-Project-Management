import com.example.constructionsystem.HelloController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
public class ControllerRaportIntegrationTest{

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
    void testRaport_employeeByState_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            int result = controller.getEmployeeCountByState("Obecny");

            assertEquals(5, result,"Funkcja powinna zwrocic liczbe 5");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testRaport_employeeBySalary_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            int result = controller.getEmployeeBySalary(4999, 5999);

            assertEquals(6, result,"Funkcja powinna zwrocic liczbe 6");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testRaport_projectByStatus_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            int result = controller.getProjectByStatus("Koniec");

            assertEquals(2, result,"Funkcja powinna zwrocic liczbe 2");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testRaport_projectByOtherStatuses_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            int result = controller.getProjectByOtherStatuses("Koniec");

            assertEquals(2, result,"Funkcja powinna zwrocic liczbe 2");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

    @Test
    void testRaport_projectByTime_Success() throws InterruptedException {
        latch = new CountDownLatch(1); // Licznik oczekiwania

        Platform.runLater(() -> {
            int result = controller.getProjectsOnTime(true);

            assertEquals(1, result,"Funkcja powinna zwrocic liczbe 1");

            latch.countDown(); // Zwalniamy blokadę
        });

        // Oczekiwanie na zakończenie operacji JavaFX
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Kod JavaFX nie zakończył się na czas");
    }

}