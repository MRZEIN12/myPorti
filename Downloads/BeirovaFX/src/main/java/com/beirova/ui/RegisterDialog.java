package com.beirova.ui;

import com.beirova.services.AuthService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegisterDialog extends Stage {
    public RegisterDialog(AuthService auth) {
        setTitle("Create Customer Account");
        initModality(Modality.APPLICATION_MODAL);

        TextField name = new TextField(); name.setPromptText("Full name");
        TextField email = new TextField(); email.setPromptText("Email");
        PasswordField pass = new PasswordField(); pass.setPromptText("Password");
        Button create = new Button("Create account");
        VBox root = new VBox(10, name, email, pass, create);
        root.setPadding(new Insets(12));
        setScene(new Scene(root, 360, 220));

        create.setOnAction(e -> {
            StringBuilder err = new StringBuilder();
            if (auth.registerCustomer(name.getText(), email.getText(), pass.getText(), err)) {
                new Alert(Alert.AlertType.INFORMATION, "Account created. You can now login.").showAndWait();
                close();
            } else {
                new Alert(Alert.AlertType.WARNING, err.toString()).showAndWait();
            }
        });
    }
}
