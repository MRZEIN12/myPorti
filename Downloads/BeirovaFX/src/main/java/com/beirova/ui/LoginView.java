package com.beirova.ui;

import com.beirova.models.Role;
import com.beirova.models.User;
import com.beirova.services.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginView {
    private final BorderPane root = new BorderPane();
    private final DataStore store = new DataStore();
    private final AuthService auth = new AuthService(store);

    public LoginView() {
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/assets/logo.png")));
        logo.setFitWidth(220);
        logo.setPreserveRatio(true);

        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField pass = new PasswordField();
        pass.setPromptText("Password");
        Button login = new Button("Login");
        Button register = new Button("Register");

        VBox form = new VBox(10, Controls.title("Born to Be  BEÏROVA"), email, pass,
                new HBox(10, login, register));
        form.setPadding(new Insets(12));
        form.setAlignment(Pos.CENTER_LEFT);

        HBox box = new HBox(20, logo, form);
        box.setAlignment(Pos.CENTER);
        root.setCenter(box);
        root.setPadding(new Insets(20));

        login.setOnAction(e -> {
            User u = auth.login(email.getText().trim(), pass.getText());
            if (u == null) {
                new Alert(Alert.AlertType.WARNING, "Invalid credentials.").showAndWait();
                return;
            }
            Stage stage = (Stage) root.getScene().getWindow();
            stage.hide();
            Stage next = new Stage();
            if (u.getRole() == Role.Customer) {
                next.setTitle("BEÏROVA - Store");
                next.setScene(new Scene(new CustomerView(store, u).getRoot(), 980, 640));
            } else if (u.getRole() == Role.Admin) {
                next.setTitle("BEÏROVA - Admin");
                next.setScene(new Scene(new AdminDashboard(store, u).getRoot(), 1100, 680));
            } else {
                next.setTitle("BEÏROVA - Super Admin");
                next.setScene(new Scene(new SuperAdminDashboard(store, u).getRoot(), 1200, 720));
            }
            next.setOnCloseRequest(ev -> stage.close());
            next.show();
        });

        register.setOnAction(e -> {
            RegisterDialog dlg = new RegisterDialog(auth);
            dlg.initOwner(getStage());
            dlg.showAndWait();
        });
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }

    public BorderPane getRoot() { return root; }
}
