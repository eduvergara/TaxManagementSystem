/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eduvergara.taxmanagementsystem;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Eduardo Vergara
 */
public class TaxCalculation {
    
    private double taxableIncome = 0.0;
    private double taxChargeBase = 0.0;
    private double taxChargeByDolar = 0.0;
    private double tax = 0.0;
    private double taxRange = 0.0;
    private ArrayList<ArrayList<Double>> fileData = null;
    
    // Default Constructor
    public TaxCalculation(){
        
        // ArrayList to store the data from file
        this.fileData = readFile();    

    }
    

    // Getters
    public double getTax() {
        return tax;
    }

    public double getTaxChargeBase() {
        return taxChargeBase;
    }
    
    
    // Tax Calculation
    public void calculateTax(double taxableIncome){
        
        // Number of Tax categories
        int taxCategoriesNum = fileData.size();
        
        // Check with category fits with the taxable income
        for (int i = taxCategoriesNum-1; i >= 0; i--) {
            if(taxableIncome >= fileData.get(i).get(0)){
                this.taxableIncome = taxableIncome;
                this.taxChargeBase = fileData.get(i).get(2);
                this.taxChargeByDolar = fileData.get(i).get(3);
                this.taxRange = fileData.get(i).get(5);
                //Calculate Tax
                this.tax = this.taxChargeBase + (this.taxableIncome-this.taxRange)*this.taxChargeByDolar;
                break;
            }
        }
    }
    

    // Readfile with taxrates information
    public ArrayList<ArrayList<Double>> readFile(){
        
        // ArrayList to store the data from file
        ArrayList<ArrayList<Double>> fileDataToStore = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> instanceTaxRange = new ArrayList<Double>();
        
        // Read the file 
        try {
            
            File file = new File ("taxrates.txt");
            Scanner input = new Scanner(file);
            input.nextLine(); // Jumps the header line
            
            Path path = Paths.get("taxrates.txt");
            
            // Count many lines in the file
            long lines = 0;
            long lineReadCounter = 0;
            lines = Files.lines(path).count();
            int overWordCount = 0; // For the last line in the file
            
            while(input.hasNextLine()){
                // Count lines read
                lineReadCounter++;
                
                // Read the all line
                String array = input.useDelimiter("\n").next();
                // Split the line by elements an store them in a String array
                String lineSplitted[] = array.split("\\s+");
                
                // Check if the element is a number. If that the case, store them
                for (int i = 0; i < lineSplitted.length; i++) {

                    int numbersAddCount = 0;
                    
                    // First character of the string element 
                    char c = lineSplitted[i].charAt(0);
                    
                    // Check if the element starts with a number
                    if ((c >= '0' && c <= '9')){
                        
                        // Check if ends with a character 'c' of "cents"
                        // This one covers the second case of the taxrates.txt file
                        if (lineSplitted[i].substring(lineSplitted[i].length() - 1).equals("c")){
                            String numberClean = lineSplitted[i].replace("c", ""); 
                            
                            // Add a zero in the array as the base amount to pay
                            // before to include de cents for each dollar
                            instanceTaxRange.add(0.0);

                            instanceTaxRange.add(Double.parseDouble(numberClean));
                        }else{
                            // Add the value to the array
                            instanceTaxRange.add(Double.parseDouble(lineSplitted[i]));
                            // Count the number addes

                        }
                        
                    };  
                    
                    // Check if the first character starts with a dollar sign '$'
                    if (c == '$'){
                        // Deletes the dollar sign and commas
                        String numberClean = lineSplitted[i].replaceAll("[$,]", ""); 
                        // Add the value to the array
                        instanceTaxRange.add(Double.parseDouble(numberClean));

                    }
                    
                    // if word over is find, add a zero. This covers the last line
                    
                    if(lineSplitted[i].equals("over") && (lineReadCounter==lines-1 && overWordCount<1) ){
                        instanceTaxRange.add(0.0);
                        overWordCount++;
                    }
                    
                }
                
                // Fill with zeros the instances with less than 6 values
                // This is useful for the first line
                if(instanceTaxRange.size() < 6){
                    int actualInstanceSize = instanceTaxRange.size();
                    for (int i = 0; i < (6-actualInstanceSize); i++) {
                        instanceTaxRange.add(0.0);
                    }
                }

                // Transforms the cents valuues to dolars    
                instanceTaxRange.set(3, instanceTaxRange.get(3)/100.0);
                
                // Add the instances to the ArrayList 
                ArrayList<Double> instanceTaxRangeCopy = (ArrayList<Double>) instanceTaxRange.clone();
                fileDataToStore.add(instanceTaxRangeCopy);
                
                // Clear the values of the instances
                instanceTaxRange.clear();

            }
 
        } catch (Exception e) {
        
            System.out.println("Error reading the taxrates.txt file");
            
            return null;
        
        }
        
        System.out.println("File 'taxrates.txt' was read correctly");

        return fileDataToStore;
        
    }
    

}
