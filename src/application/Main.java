package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/Accueil.fxml"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(root);
        primaryStage.setTitle("Calculatrice graphique");
        primaryStage.show();
    }
    public static void main(String[] args) throws IOException {      
        Application.launch();
    }
}