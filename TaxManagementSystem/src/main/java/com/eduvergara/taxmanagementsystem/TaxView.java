// TaxView.java
package com.eduvergara.taxmanagementsystem;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


/**
 * @author Eduardo Vergara
 */
public class TaxView {

    protected TextField tfnNumber;
    protected TextField financialYearNumber;
    protected TextField taxableIncomeNumber;
    protected TextField taxNumber;
    protected Button btCalculate;
    protected Button btSearch;
    protected Button btUpdate;
    protected Button btDelete;


    public TaxView() {

        // initialize buttons and text fields
        tfnNumber = new TextField();
        financialYearNumber = new TextField();
        taxableIncomeNumber = new TextField();
        taxNumber = new TextField();
        btCalculate = new Button("Calculate");
        btSearch = new Button("Search");
        btUpdate = new Button("Update");
        btDelete = new Button("Delete");
        

    }

    public GridPane getPane() {

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);
        pane.add(new Label("Tax File Number:"), 0, 0);
        pane.add(tfnNumber, 1, 0);
        pane.add(new Label("Financial Year:"), 0, 1);
        pane.add(financialYearNumber, 1, 1);
        pane.add(new Label("Taxable Income:"), 0, 2);
        pane.add(taxableIncomeNumber, 1, 2);
        pane.add(new Label("Income tax payable:"), 0, 3);
        taxableIncomeNumber.setAlignment(Pos.CENTER_RIGHT);
        pane.add(taxNumber, 1, 3);
        taxNumber.setAlignment(Pos.CENTER_RIGHT);
        taxNumber.setEditable(false);
        pane.add(btCalculate, 1, 4);
        GridPane.setHalignment(btCalculate, HPos.RIGHT);
        pane.add(btSearch, 0, 5);
        pane.add(btUpdate, 1, 5);
        pane.add(btDelete, 3, 5);
        GridPane.setHalignment(btDelete, HPos.LEFT);

        return pane;
    }

}
