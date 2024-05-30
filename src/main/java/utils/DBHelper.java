/**
 * Класс DBHelper предоставляет методы для подключения к базе данных PostgreSQL.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    private static String URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    private static String LOGIN = "postgres";
    private static String PASSWORD = "admin";

    /**
     * Устанавливает соединение с базой данных PostgreSQL, используя указанный URL, имя пользователя и пароль.
     *
     * @return объект Connection, представляющий соединение с базой данных
     * @throws SQLException если происходит ошибка доступа к базе данных
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, LOGIN, PASSWORD);
    }
}