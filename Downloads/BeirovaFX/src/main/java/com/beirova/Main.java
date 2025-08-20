package com.beirova;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.beirova.ui.LoginView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("BEÏROVA - Login");
        Scene scene = new Scene(new LoginView().getRoot(), 760, 420);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
