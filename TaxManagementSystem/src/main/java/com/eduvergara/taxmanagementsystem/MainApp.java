// MainApp.java
package com.eduvergara.taxmanagementsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Eduardo Vergara
 */
public class MainApp extends Application {

    private TaxDAO taxDAO;
    private TaxView taxView;
    private TaxController taxController;

    @Override
    public void start(Stage primaryStage) {
        initializeComponents();
        setupStage(primaryStage);
    }

    private void initializeComponents() {
        taxDAO = new TaxDAO();
        taxView = new TaxView();
        taxController = new TaxController(taxDAO, taxView);
    }

    private void setupStage(Stage primaryStage) {
        Scene scene = new Scene(taxView.getPane(), 450, 280);
        primaryStage.setTitle("Tax Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
