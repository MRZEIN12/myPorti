package com.beirova.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class Controls {
    public static VBox vbox(int spacing, Node... children) {
        VBox v = new VBox(spacing, children);
        v.setPadding(new Insets(12));
        return v;
    }
    public static HBox hbox(int spacing, Node... children) {
        HBox h = new HBox(spacing, children);
        h.setPadding(new Insets(12));
        return h;
    }
    public static Label title(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", 22));
        return l;
    }
    public static Button primary(String text) {
        Button b = new Button(text);
        b.setDefaultButton(true);
        return b;
    }
}
