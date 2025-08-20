package com.beirova.ui;

import com.beirova.models.*;
import com.beirova.services.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;

public class SuperAdminDashboard extends AdminDashboard {
    private final AuthService auth;
    private final TableView<User> tableUsers = new TableView<>();

    public SuperAdminDashboard(DataStore store, User user) {
        super(store, user);
        this.auth = new AuthService(store);

        TabPane tabs = (TabPane) root.getCenter();
        Tab usersTab = new Tab("Users");
        usersTab.setClosable(false);
        tabs.getTabs().add(usersTab);

        TableColumn<User, String> u1 = new TableColumn<>("Name"); u1.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<User, String> u2 = new TableColumn<>("Email"); u2.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<User, Role> u3 = new TableColumn<>("Role"); 
        u3.setCellValueFactory(new PropertyValueFactory<>("role"));
        u3.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(Role.values())));
        tableUsers.getColumns().addAll(u1,u2,u3);

        Button btnSave = new Button("Save Changes");
        btnSave.setOnAction(e -> { auth.saveUsers(tableUsers.getItems()); new Alert(Alert.AlertType.INFORMATION, "Users saved.").showAndWait(); });
        HBox top = new HBox(10, btnSave); top.setPadding(new Insets(8));

        usersTab.setContent(new BorderPane(tableUsers, top, null, null, null));

        reloadUsers();
    }

    private void reloadUsers() {
        ObservableList<User> data = FXCollections.observableArrayList(auth.getUsers());
        tableUsers.setItems(data);
    }
}
