package com.eduvergara.taxmanagementsystem;

import javafx.scene.control.TextField;

/**
 * @author Eduardo Vergara
 */
public class TaxController {

    private TaxDAO taxDAO;
    private TaxView taxView;

    public TaxController(TaxDAO taxDAO, TaxView taxView) {
        this.taxDAO = taxDAO;
        this.taxView = taxView;
        initializeButtonActions();
    }

    private void initializeButtonActions() {
        taxView.btUpdate.setOnAction(e -> handleUpdate());
        taxView.btCalculate.setOnAction(e -> handleCalculate());
        taxView.btSearch.setOnAction(e -> handleSearch());
        taxView.btDelete.setOnAction(e -> handleDelete());
    }

    private void handleUpdate() {
        getGUIValues(taxView.tfnNumber, taxView.financialYearNumber, taxView.taxableIncomeNumber);
        taxDAO.dbUpdateReg(taxView.taxNumber);

    }

    private void handleCalculate() {
        getGUIValues(taxView.tfnNumber, taxView.financialYearNumber, taxView.taxableIncomeNumber);
        taxDAO.dbCalculateTax(taxView.taxNumber);

    }

    private void handleSearch() {
        getGUIValues(taxView.tfnNumber, taxView.financialYearNumber, taxView.taxableIncomeNumber);
        taxDAO.dbSearchTFN(taxView.taxableIncomeNumber, taxView.taxNumber, taxView.financialYearNumber, taxView.tfnNumber);

    }

    private void handleDelete() {
        getGUIValues(taxView.tfnNumber, taxView.financialYearNumber, taxView.taxableIncomeNumber);
        taxDAO.dbDeleteReg(taxView.taxNumber);

    }

    // Get GUI values
    public void getGUIValues (TextField tfnNumber, TextField financialYearNumber, TextField taxableIncomeNumber){

        // Reset Data from TextField in the GUI
        taxDAO.tfnNumberGUI = null;
        taxDAO.financialYearGUI = null;
        taxDAO.taxableIncomeGUI = null;
        taxDAO.taxGUI = null;
        taxDAO.idGUI = null;

        // Data from TextField in the GUI
        if(tfnNumber.getText().isEmpty() != true){
            taxDAO.tfnNumberGUI = tfnNumber.getText();
        }

        if(financialYearNumber.getText().isEmpty() != true){
            taxDAO.financialYearGUI = financialYearNumber.getText();
        }

        if(taxableIncomeNumber.getText().isEmpty() != true){
            taxDAO.taxableIncomeGUI = Double.parseDouble(taxableIncomeNumber.getText());
        }


        if(tfnNumber.getText().isEmpty() != true && financialYearNumber.getText().isEmpty() != true){
            taxDAO.idGUI = tfnNumber.getText() + financialYearNumber.getText();
        }

    }




}

