module com.example.constructionsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.calendarfx.view;
    requires org.apache.poi.ooxml;
    requires com.dlsc.formsfx;
    requires jbcrypt;
    requires java.desktop;
    requires java.net.http;
    requires javax.mail;



    opens com.example.constructionsystem to javafx.fxml;
    exports com.example.constructionsystem;
}