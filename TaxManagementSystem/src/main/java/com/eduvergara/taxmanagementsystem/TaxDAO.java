/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eduvergara.taxmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * @author Eduardo Vergara
 */
public class TaxDAO {

    //private TaxView taxView;

    public TaxDAO() {
        this.db = getDBConnection();
    }

    // Create TaxCalculation object
    TaxCalculation taxCalc = new TaxCalculation();

    // Alert Pane
    Alert a = new Alert(AlertType.NONE);

    // Data from TextField in the GUI
    String tfnNumberGUI = null;
    String financialYearGUI = null;
    Double taxableIncomeGUI = null;
    Double taxGUI = null;
    String idGUI = null;

    // Last valid record showed in the GUI
    String tfnNumberGUILastValid = null;
    String financialYearGUILastValid = null;
    Double taxableIncomeGUILastValid = null;
    String idGUILastValid = null;

    Connection db = null;

    GridPane pane;

    // Method to establish a connection to the database
    public Connection getDBConnection() {

        // Initialize DB Connection
        String database = "jdbc:mysql://localhost:3306/TaxManagementSystem?characterEncoding=latin1";
        String userName = "student";
        String userPassword = "student";
        db = dBConnection(database,userName , userPassword);

        // Create Table
        //dbCreateTable();

        return db;
    }


    // Database Connection
    public static Connection dBConnection(String database, String userName, String userPassword) {

        try {

            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Loaded the MySQL JDBC Driver");

            // Connect to a database
            Connection connection = DriverManager.getConnection(database, userName, userPassword);
            System.out.println("Successfully connected to Database");

            return connection;

        } catch (Exception e) {

            System.out.println("Error in the Connection!");
            return null;
        }

    }


    // Create Table in Database
    public void dbCreateTable (Connection db) {

        try {

            // Create a statement
            Statement statement = db.createStatement();

            //Create Table (Can create the table in the existing DataMining database
            String TaxInfo = "create table TaxInfo (ID varchar(20), TaxFileNumber varchar(10) not null, "
                    + "FinancialYear varchar(10) not null, TaxableIncome float not null, "
                    + "Tax float not null, primary key (ID))";

            statement.execute(TaxInfo);

            System.out.println("Table TaxInfo Successfully created in the Database");

        } catch (Exception e) {

            System.out.println("Error creating the table!");

        }

    }


    // Calculate Tax
    public void dbCalculateTax (TextField taxNumber) {

        try {

            // Create a statement
            Statement statement = db.createStatement();

            // Execute a statement
            String query = "SELECT * FROM TaxInfo WHERE ID = '" + idGUI + "'";

            // ResultSet from the query
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {

                // Show alert
                alertMessage(AlertType.INFORMATION, "There is a previous record with the provided info");

            }else{

                if(tfnNumberGUI == null || financialYearGUI == null || taxableIncomeGUI == null){

                    // Show alert
                    alertMessage(AlertType.INFORMATION, "There is missing information to calculate the taxes");

                }else{

                    // Calculate the tax and add the data to the database
                    taxCalc.calculateTax(taxableIncomeGUI);
                    Double calculatedTax = taxCalc.getTax();

                    // Show the value of the calculated tax in the GUI
                    taxNumber.setText("$"+String.valueOf(calculatedTax));

                    // Insert data with the new record to the database
                    String insertNewDataDB = "INSERT into TaxInfo (ID, TaxFileNumber, FinancialYear, TaxableIncome, Tax)"
                            + "VALUES ('" +idGUI+ "','" + tfnNumberGUI + "', '" + financialYearGUI + "', '"+ taxableIncomeGUI + "', '" + calculatedTax + "')";

                    // Execute statement
                    statement.executeUpdate(insertNewDataDB);

                    // Show alert
                    alertMessage(AlertType.INFORMATION, "New record ADDED to the database");

                    // Update the last valid records in the GUI
                    lastValidGUIRecord(tfnNumberGUI, financialYearGUI, taxableIncomeGUI);

                }

            }

        } catch (Exception e) {

            // Show alert
            alertMessage(AlertType.ERROR, "Error calculating the tax");

        }

    }


    // Search TFN
    public void dbSearchTFN (TextField taxableIncomeNumber, TextField taxNumber, TextField financialYearNumber, TextField tfnNumber) {

        boolean recordFound = false;
        boolean queryInDB = false;

        try {
            // Create a statement
            Statement statement = db.createStatement();

            if(idGUI != null && recordFound == false){

                // Execute a statement
                String query = "SELECT * FROM TaxInfo WHERE ID = '" + idGUI + "'";

                // ResultSet from the query
                ResultSet resultSet = statement.executeQuery(query);

                // query was performed in database
                queryInDB = true;

                if (resultSet.next() ) {

                    // Result set form the database
                    ResultSetMetaData rsmd = resultSet.getMetaData();

                    // Get the values from DB
                    String taxableIncomeDB = resultSet.getString(4);
                    String taxDB = resultSet.getString(5);

                    // Show the value of the taxable income in the record
                    taxableIncomeNumber.setText(String.valueOf(taxableIncomeDB));
                    // Show the value of the tax in the record
                    taxNumber.setText("$"+String.valueOf(taxDB));

                    // Show alert
                    alertMessage(AlertType.INFORMATION, "The record was found in the DB");

                    recordFound = true;

                }

            }else if(tfnNumberGUI != null && recordFound == false){

                // Execute a statement
                String query = "SELECT * FROM TaxInfo WHERE TaxFileNumber = '" + tfnNumberGUI + "'";

                // ResultSet from the query
                ResultSet resultSet = statement.executeQuery(query);

                // query was performed in database
                queryInDB = true;

                if (resultSet.next() ) {

                    ResultSetMetaData rsmd = resultSet.getMetaData();

                    // Get the values from DB
                    String financialYearDB = resultSet.getString(3);
                    String taxableIncomeDB = resultSet.getString(4);
                    String taxDB = resultSet.getString(5);

                    // Show the value of the taxable income in the record
                    financialYearNumber.setText(String.valueOf(financialYearDB));
                    // Show the value of the taxable income in the record
                    taxableIncomeNumber.setText(String.valueOf(taxableIncomeDB));
                    // Show the value of the tax in the record
                    taxNumber.setText("$"+String.valueOf(taxDB));

                    // Show alert
                    alertMessage(AlertType.INFORMATION, "The record was found in the DB");
                    recordFound = true;

                }

            }else if(financialYearGUI != null && recordFound == false){

                // Execute a statement
                String query = "SELECT * FROM TaxInfo WHERE FinancialYear = '" + financialYearGUI + "'";

                // ResultSet from the query
                ResultSet resultSet = statement.executeQuery(query);

                // query was performed in database
                queryInDB = true;

                if (resultSet.next() ) {

                    ResultSetMetaData rsmd = resultSet.getMetaData();

                    // Get the values from DB
                    String tfnDB = resultSet.getString(2);
                    String taxableIncomeDB = resultSet.getString(4);
                    String taxDB = resultSet.getString(5);

                    // Show the value of the taxable income in the record
                    tfnNumber.setText(String.valueOf(tfnDB));
                    // Show the value of the taxable income in the record
                    taxableIncomeNumber.setText(String.valueOf(taxableIncomeDB));
                    // Show the value of the tax in the record
                    taxNumber.setText("$"+String.valueOf(taxDB));

                    // Show alert
                    alertMessage(AlertType.INFORMATION, "The record was found in the DB");
                    recordFound = true;

                }

            }else{

                // Show alert
                alertMessage(AlertType.INFORMATION, "You must include TFN or Financial Year to search a record!");

            }

            if(queryInDB == true && recordFound == true){

                // Update the last valid records in the GUI
                lastValidGUIRecord(tfnNumber.getText(), financialYearNumber.getText(), Double.parseDouble(taxableIncomeNumber.getText()));

            } else if(queryInDB == true && recordFound == false ){


                // Show alert
                alertMessage(AlertType.INFORMATION, "No matching records in the database!");

                // Reset queryInDb value
                queryInDB = false;

            }

        } catch (Exception e) {


        }


    }


    // Update register
    public void dbUpdateReg (TextField taxNumber) {

        try {

            if(tfnNumberGUILastValid != null && tfnNumberGUILastValid.equals(tfnNumberGUI)){

                if(tfnNumberGUI != null && financialYearGUI != null && taxableIncomeGUI != null ){

                    // Create a statement
                    Statement statement = db.createStatement();

                    // Execute a statement
                    String query = "SELECT * FROM TaxInfo WHERE ID = '" + idGUILastValid + "'";

                    // ResultSet from the query
                    ResultSet resultSet = statement.executeQuery(query);


                    if (resultSet.next() ) {

                        // Calculate the tax and add the data to the database
                        taxCalc.calculateTax(taxableIncomeGUI);
                        Double calculatedTax = taxCalc.getTax();

                        // Show the value of the calculated tax in the GUI
                        taxNumber.setText("$"+String.valueOf(calculatedTax));


                        // Update data in the last valid record in GUI
                        String updateLastRecodDB = "UPDATE TaxInfo SET ID = '" +idGUI+ "',"
                                + "TaxFileNumber = '" + tfnNumberGUI + "',FinancialYear = '" + financialYearGUI + "',"
                                + "TaxableIncome = '"+ taxableIncomeGUI + "',Tax= '" + calculatedTax + "' WHERE ID = '" + idGUILastValid + "' ";

                        // Execute statement
                        statement.executeUpdate(updateLastRecodDB);
                        // Show alert
                        alertMessage(AlertType.INFORMATION, "Record UPDATED in the database");


                        // Update the last valid records in the GUI
                        lastValidGUIRecord(tfnNumberGUI, financialYearGUI, taxableIncomeGUI);

                    }else{

                        // Show alert
                        alertMessage(AlertType.INFORMATION, "No matching records in the database!");

                    }

                }else{

                    // Show alert
                    alertMessage(AlertType.INFORMATION, "Missing information to update the record!");

                }

            }else{

                // Show alert
                alertMessage(AlertType.INFORMATION, "A search must be done first to update a record!");

            }

        } catch (Exception e) {

            // Show alert
            alertMessage(AlertType.ERROR, "Error updating the record");
        }

    }


    // Delete Register
    public void dbDeleteReg (TextField taxNumber) {

        try {

            if(tfnNumberGUI != null && financialYearGUI != null){

                // Create a statement
                Statement statement = db.createStatement();

                // Execute a statement
                String query = "SELECT * FROM TaxInfo WHERE ID = '" + idGUI + "'";

                // ResultSet from the query
                ResultSet resultSet = statement.executeQuery(query);

                if (resultSet.next() ) {

                    // Update data in the last valid record in GUI
                    String deleteRecodDB = "DELETE FROM TaxInfo WHERE ID = '" + idGUI + "' ";

                    // Execute statement
                    statement.executeUpdate(deleteRecodDB);

                    // Show alert
                    alertMessage(AlertType.INFORMATION, "Record DELETED from the database");

                    // Show the value of the tax in the record
                    taxNumber.setText("");

                    // Update the last valid records in the GUI
                    lastValidGUIRecord(tfnNumberGUI, financialYearGUI, taxableIncomeGUI);

                }else{

                    // Show alert
                    alertMessage(AlertType.INFORMATION, "No matching records in the database!");

                }

            }else{

                // Show alert
                alertMessage(AlertType.INFORMATION, "Missing information to delete the record!");

            }

        } catch (Exception e) {

            // Show alert
            alertMessage(AlertType.ERROR, "Error trying to delete the record!");

        }

    }


/*    // Get GUI values
    public void getGUIValues (TextField tfnNumber, TextField financialYearNumber, TextField taxableIncomeNumber){

        // Reset Data from TextField in the GUI
        tfnNumberGUI = null;
        financialYearGUI = null;
        taxableIncomeGUI = null;
        taxGUI = null;
        idGUI = null;

        // Data from TextField in the GUI
        if(tfnNumber.getText().isEmpty() != true){
            tfnNumberGUI = tfnNumber.getText();
        }

        if(financialYearNumber.getText().isEmpty() != true){
            financialYearGUI = financialYearNumber.getText();
        }

        if(taxableIncomeNumber.getText().isEmpty() != true){
            taxableIncomeGUI = Double.parseDouble(taxableIncomeNumber.getText());
        }


        if(tfnNumber.getText().isEmpty() != true && financialYearNumber.getText().isEmpty() != true){
            idGUI = tfnNumber.getText() + financialYearNumber.getText();
        }

    }*/

    public void lastValidGUIRecord(String tfnNumberGUI, String financialYearGUI, Double taxableIncomeGUI){
        tfnNumberGUILastValid = tfnNumberGUI;
        financialYearGUILastValid = financialYearGUI;
        taxableIncomeGUILastValid = taxableIncomeGUI;
        idGUILastValid = tfnNumberGUI+financialYearGUI;
    }


    // Method that shows a window alert
    public void alertMessage(AlertType alert, String alertMessage){

        // Show alert
        a.setAlertType(alert);

        // set content text
        a.setContentText(alertMessage);

        // show the dialog
        a.show();

    }


}