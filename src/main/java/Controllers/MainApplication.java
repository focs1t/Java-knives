package Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import utils.DBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainApplication extends Application {
    private final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    @Getter
    private  static  Stage mainstage;

    @Getter
    private static Connection connection;

    public void stop() throws Exception {
        if(connection != null){
            connection.close();
        }
        super.stop();
    }

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Приложение запущено");
        try{
            connection = DBHelper.getConnection();
            logger.debug("Соединение к БД получено");
        }
        catch (SQLException e){
            logger.error("Ошибка подключения к базе данных", e);
            System.out.println(e.getMessage());
        }
        this.mainstage = stage;
        Locale.setDefault(Locale.GERMANY);
        //ResourceBundle bundle = ResourceBundle.getBundle("Locales_text_fr_CA",
        //        Locale.getDefault());
        ResourceBundle bundle = ResourceBundle.getBundle("Locales_text", Locale.getDefault());
        logger.info("Загружены ресурсы для локали {}", Locale.getDefault());
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/javaknifes/main-view.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle(bundle.getString("App.Title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}