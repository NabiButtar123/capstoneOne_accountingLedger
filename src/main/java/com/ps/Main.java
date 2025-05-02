package com.ps;

import java.io.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;


public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transactions> tx = new ArrayList<>();

    public static void main(String[] args) {

        String userInput;

        loadTransactions();
        do {
            System.out.println("\nWelcome to the YearUP Bank teller!");
            System.out.println("Enter 'D' to add a deposit");
            System.out.println("Enter 'P' make a payment");
            System.out.println("Enter 'L' to transfer to the Ledger Screen");
            System.out.println("Enter 'X' to exit the application");
            System.out.println("Enter 'A' for account history");
            System.out.print("Please enter an option: ");
            userInput = scanner.nextLine().toUpperCase();

            switch (userInput) {

                case "D":
                    addDeposit();
                    break;
                case "P":
                    addDebit();
                    break;
                case "L":
                    showLedger();
                    break;
                case "A":
                    accountInformation();
                    break;
                case "X":
                    System.out.println("Thank you, take care");
                    return;
                default:
                    System.out.println("Invalid option. Transferring back to home screen");

            }

        } while (!userInput.equals("X"));


    }

    private static void loadTransactions() {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("transactions.csv"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split("\\|");
                if (fields.length < 6) continue; // needed for out of bounds exception, file whitespace
                String date = fields[0];
                String time = fields[1];
                String description = fields[2];
                String vendor = fields[3];
                double amount = Double.parseDouble(fields[4]);
                String account = fields[5];
                Transactions addTx = new Transactions(date, time, description, vendor, amount, account);
                tx.add(addTx);

            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while reading file");
        }


    }

    private static void accountInformation() {
        double total = 0;
        System.out.print("Please enter account ID to retrieve information: ");
        String accountID = scanner.nextLine();

        System.out.printf("%-12s %-10s %-20s %-20s %10s %15s\n", "Date", "Time", "Description", "Vendor", "Amount", "Account");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Transactions entry : tx) {
            if (entry.getAccount().equals(accountID)) {
                System.out.printf("%-12s %-10s %-20s %-20s %10.2f %15s\n",
                        entry.getDate(),
                        entry.getTime(),
                        entry.getDescription(),
                        entry.getVendor(),
                        entry.getAmount(),
                        entry.getAccount());
                total += entry.getAmount();
            }
        }
        System.out.printf("\nBalance of account: %.2f\n", total);

    }


    private static void addDeposit() {
        System.out.println("Thank you for choosing to deposit! Please enter the following information");
        System.out.print("Date: (yyyy-mm-dd): ");
        String date = scanner.nextLine();
        System.out.print("Time: (hh:mm:ss): ");
        String time = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("account number: ");
        String account = scanner.nextLine();
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive please start over");
            return;
        }
        Transactions addTx = new Transactions(date, time, description, vendor, amount, account);
        tx.add(addTx);
        managingTransaction.writeTransaction(addTx);
        System.out.println("Deposit added successfully!");

    }

    private static void addDebit() {
        System.out.println("Thank you for choosing to make a payment! Please enter the following information");
        System.out.print("Date: (yyyy-mm-dd): ");
        String date = scanner.nextLine();
        System.out.print("Time: (hh:mm:ss): ");
        String time = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        amount = amount * -1;
        System.out.print("account number: ");
        String account = scanner.nextLine();
        Transactions addTx = new Transactions(date, time, description, vendor, amount, account);
        tx.add(addTx);
        managingTransaction.writeTransaction(addTx);
        System.out.println("Debit added successfully!");

    }

    private static void showLedger() {
        String input;
        do {
            System.out.println("\n------Welcome to the Ledger Menu! ------");
            System.out.println("Please enter 'A' to showcase all entries");
            System.out.println("Please enter 'D' to showcase all deposits");
            System.out.println("Please enter 'P' to showcase all payments");
            System.out.println("Please enter 'R' to showcase a custom search");
            System.out.println("Please enter 'H' to go back to home page");
            System.out.print("Choose an option: ");
            input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "A":
                    showAllTransactions();
                    break;
                case "D":
                    showDeposits();
                    break;
                case "P":
                    showPayments();
                    break;
                case "R":
                    showReports();
                    break;
                case "H":
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (!input.equals("H"));
    }

    private static void showAllTransactions() {
        System.out.println("\n-----All Transactions-----");
        System.out.printf("%-12s %-10s %-20s %-20s %10s %15s\n", "Date", "Time", "Description", "Vendor", "Amount", "Account");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Transactions entry : tx) {
            System.out.printf("%-12s %-10s %-20s %-20s %10.2f %15s\n",
                    entry.getDate(),
                    entry.getTime(),
                    entry.getDescription(),
                    entry.getVendor(),
                    entry.getAmount(),
                    entry.getAccount());
        }
    }

    private static void showDeposits() {
        System.out.println("----- Deposits Only -----");
        System.out.printf("%-12s %-10s %-20s %-20s %10s %15s\n", "Date", "Time", "Description", "Vendor", "Amount", "Account");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Transactions entry : tx) {
            if (entry.getAmount() > 0) {
                System.out.printf("%-12s %-10s %-20s %-20s %10.2f %15s\n",
                        entry.getDate(),
                        entry.getTime(),
                        entry.getDescription(),
                        entry.getVendor(),
                        entry.getAmount(),
                        entry.getAccount());
            }
        }

    }

    private static void showPayments() {
        System.out.println("----- Payments Only -----");
        System.out.printf("%-12s %-10s %-20s %-20s %10s %15s\n", "Date", "Time", "Description", "Vendor", "Amount", "Account");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Transactions entry : tx) {
            if (entry.getAmount() < 0) {
                System.out.printf("%-12s %-10s %-20s %-20s %10.2f %15s\n",
                        entry.getDate(),
                        entry.getTime(),
                        entry.getDescription(),
                        entry.getVendor(),
                        entry.getAmount(),
                        entry.getAccount());
            }
        }

    }

    private static void showReports() {
        String input;
        do {
            System.out.println("\n----- Reports Menu -----");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Advanced custom serach");
            System.out.println("0) Back to Ledger");
            System.out.print("Select an option: ");
            input = scanner.nextLine();

            switch (input) {
                case "1":
                    dateReport("monthToDate");
                    break;
                case "2":
                    dateReport("previousMonth");
                    break;
                case "3":
                    dateReport("yearToDate");
                    break;
                case "4":
                    dateReport("previousYear");
                    break;
                case "5":
                    System.out.print("Please enter vendor name to search: ");
                    String vendorSearch = scanner.nextLine().toLowerCase();
                    System.out.println("Transactions for Vendor: " + vendorSearch);
                    for (Transactions entry : tx) {
                        if (entry.getVendor().toLowerCase().contains(vendorSearch)) {
                            System.out.printf("%-12s %-10s %-20s %-20s %10.2f %15s\n",
                                    entry.getDate(),
                                    entry.getTime(),
                                    entry.getDescription(),
                                    entry.getVendor(),
                                    entry.getAmount(),
                                    entry.getAccount());
                        }
                    }
                    break;
                case "6":
                    customSearch();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (!input.equals("0"));
    }

    private static void dateReport(String type) {
        System.out.println("\n----- Report: " + type + " -----");
        LocalDate today = LocalDate.now();
        //forced to initialize
        LocalDate start = null;
        LocalDate end = today;

        switch (type) {
            case "monthToDate":
                start = today.withDayOfMonth(1);
                break;
            case "previousMonth":
                LocalDate prevMonth = today.minusMonths(1);
                start = prevMonth.withDayOfMonth(1);
                end = prevMonth.withDayOfMonth(prevMonth.lengthOfMonth());
                break;
            case "yearToDate":
                start = today.withDayOfYear(1);
                break;
            case "previousYear":
                LocalDate prevYear = today.minusYears(1);
                // 2024 jan 1
                start = prevYear.withDayOfYear(1);
                //hardest part
                end = prevYear.withDayOfYear(prevYear.lengthOfYear());
                break;
        }

        System.out.printf("%-12s %-10s %-20s %-20s %10s %15s\n", "Date", "Time", "Description", "Vendor", "Amount", "Account");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Transactions entry : tx) {
            LocalDate txDate = LocalDate.parse(entry.getDate());
            if ((txDate.isEqual(start) || txDate.isAfter(start)) && (txDate.isEqual(end) || txDate.isBefore(end))) {
                System.out.printf("%-12s %-10s %-20s %-20s %10.2f %15s\n",
                        entry.getDate(),
                        entry.getTime(),
                        entry.getDescription(),
                        entry.getVendor(),
                        entry.getAmount(),
                        entry.getAccount());
            }
        }
    }

    private static void customSearch() {
        System.out.print("Start Date (yyyy-mm-dd): ");
        String startDateString = scanner.nextLine();
        System.out.print("End Date (yyyy-mm-dd): ");
        String endDateString = scanner.nextLine();
        System.out.print("Description: ");
        String descriptionSearch = scanner.nextLine().toLowerCase();
        System.out.print("Vendor: ");
        String vendorSearch = scanner.nextLine().toLowerCase();
        System.out.print("Amount: ");
        String amountStr = scanner.nextLine();


        for (Transactions entry : tx) {
            boolean matches = true;

            // Date range filter
            //assuming user always input start AND date not just start or date
            if (!startDateString.isEmpty() && !endDateString.isEmpty()) {
                LocalDate txDate = LocalDate.parse(entry.getDate());
                LocalDate startDate = LocalDate.parse(startDateString);
                LocalDate endDate = LocalDate.parse(endDateString);
                //different from report
                if (txDate.isBefore(startDate) || txDate.isAfter(endDate)) {
                    matches = false;
                }
            }

            // Description filter
            //choosing contains over equal for fewer bugs
            if (!descriptionSearch.isEmpty() && !entry.getDescription().toLowerCase().contains(descriptionSearch)) {
                matches = false;
            }

            // Vendor filter
            if (!vendorSearch.isEmpty() && !entry.getVendor().toLowerCase().contains(vendorSearch)) {
                matches = false;
            }

            //Amount filter
            //parsing will lead to error, must catch
            if (!amountStr.isEmpty()) {
                try {
                    double amt = Double.parseDouble(amountStr);
                    if (entry.getAmount() != amt) {
                        matches = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount input.");
                    matches = false;
                }
            }
            if (matches) {
                System.out.println(entry);
            }
        }
    }
}








