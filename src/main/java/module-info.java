/**
 * Модуль приложения с использованием JavaFX.
 */
module com.example.javaknifes {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires org.slf4j;


    opens com.example.javaknifes to javafx.fxml;
    exports Controllers; // Экспортируем пакет Controllers для использования из других модулей
    opens Controllers to javafx.fxml; // Открываем пакет Controllers для использования в JavaFX
}