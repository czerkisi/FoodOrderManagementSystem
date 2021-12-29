/*
 * Author: Ian Czerkis
 * Project: Food Order Management System: BasicItem Class
 * Created: 19 Dec 2021
 */

package czerkisi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * This class stores the name, calories, price,
 * description, and Options of an item in the Restaurant
 */
public class BasicItem {
    private static final int MAX_NUMBER_OF_CHARACTERS = 100;
    private String name;
    private int calories;
    private double price;
    private final ArrayList<String> description = new ArrayList<>();
    static Scanner in = new Scanner(System.in);
    private final ArrayList<Option> options = new ArrayList<>();


    BasicItem(String name, int calories, double price){
        this.name = name;
        this.calories = calories;
        this.price = price;
    }

    BasicItem(){

    }

    /**
     * This method repeats a prompt until the User enters a number greater than the min number
     * @param prompt The message that will prompt the User to enter a number
     * @param min the minimum number the User can enter
     * @return A number the User entered that is greater than the minimum required number
     */
    public static double inputNumber(String prompt, int min) {
        double ret;
        boolean advance = false;
        do {
            ret = inputNumber(prompt + "\nEnter a number greater than " + min);
            if (ret > min) {
                advance = true;
            } else if (ret <= min){
                printError("The response must be greater than " + min);
            } else {
                printError("Invalid Entry");
            }
        } while (!advance);
        return ret;
    }

    /**
     * This method calls the inputString method using "Yes" and "No" as the acceptable responses
     * @param prompt The message that will prompt the User to enter Yes or No
     * @return Yes or No depending on the User input
     */
    public static String inputStringYesNo(String prompt) {
        String[] yesNoArray = {"Yes", "No"};
        ArrayList<String> yesNoArrayList = new ArrayList<>();
        Collections.addAll(yesNoArrayList, yesNoArray);
        return inputString(prompt, yesNoArrayList);
    }

    /**
     * This method repeats a message to prompt the User to enter
     * a non-case sensitive response the matches an entry in the
     * ArrayList of acceptable responses or "Quit"
     * @param prompt The message that prompts the User to enter a response
     * @param verifyList An ArrayList of acceptable responses
     * @return An entry from verifyList the User entered that matches the case
     * of the item in verifyList
     */
    public static String inputStringNoReprint(String prompt, ArrayList<String> verifyList) {
        String ret;
        boolean advance = false;

        ArrayList<String> upperCaseList = new ArrayList<>();
        for (String verify : verifyList) {
            upperCaseList.add(verify.toUpperCase());
        }
        upperCaseList.add("QUIT");
        do {
            ret = inputString(prompt).toUpperCase();
            if (upperCaseList.contains(ret)){
                advance = true;
                if (ret.equals("QUIT")){
                    ret = "Quit";
                } else {
                    ret = verifyList.get(upperCaseList.indexOf(ret));
                }
            } else {
                printError("Response not found");
            }
        } while (!advance);
        return ret;
    }

    /**
     * A collection of different Colors that can be used in printing messages
     */
    enum Color {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        BLUE("\u001B[34m");

        private final String ansi;

        Color(String ansi){
            this.ansi = ansi;
        }
        public String toString(){
            return this.ansi;
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(name + "\n");
        for (String desc : description) {
            ret.append(desc).append("\n");
        }
        ret.append("$").append(getFormattedPrice()).append("\n")
                .append(calories).append(" calories");
        return ret.toString();
    }

    /**
     * This method repeats a prompt until the User enters a double
     * @param prompt the message that prompts the User to enter a number
     * @return the double the User entered
     */
    public static double inputNumber(String prompt){
        boolean advance = false;
        double userNumber = 0;
        do {
            System.out.println(prompt);
            String ret = in.nextLine();
            try {
                userNumber = Double.parseDouble(ret);
                advance = true;
            } catch (NumberFormatException ignored) {
            }
            if (!advance){
                printError("Invalid Entry");
            }
        } while (!advance);
        return userNumber;
    }

    /**
     * This method calls the inputNumber method until the User enters a number
     * greater than or equal to the min and less than or equal to the max
     * @param prompt the message to prompt the User to enter the number
     * @param min the minimum acceptable number
     * @param max the maximum acceptable number
     * @return a number the User entered between the min and max
     */
    public static double inputNumber(String prompt, int min, int max){
        double ret;
        boolean advance = false;
        do {
            ret = inputNumber(prompt + "\nEnter a number between " + min + " and " + max + ":");
            if (ret>=min&&ret<=max){
                advance = true;
            } else {
                printError("Invalid Entry");
            }
        } while (!advance);
        return ret;
    }

    /**
     * This method prints a message and allows the User to input a response
     * @param prompt the message that will prompt the User to input a response
     * @return the response the User entered
     */
    public static String inputString(String prompt){
        System.out.println(prompt);
        return in.nextLine();
    }

    /**
     * This method inputs a String from the user until a String
     * matching a non-case sensitive item in verifyList in inputted
     * @param prompt What should be asked by the program to prompt the user to input the String
     * @param verifyList A list of acceptable responses
     * @return The selected
     */
    public static String inputString(String prompt, ArrayList<String> verifyList){
        String ret;
        boolean advance = false;

        StringBuilder acceptable = new StringBuilder();
        if (verifyList.size() > 2) {
            int acceptableLength = 0;
            for (int i = 0; i < verifyList.size() - 1; i++) {
                acceptable.append("'").append(verifyList.get(i)).append("', ");
                acceptableLength += acceptable.length();

                if (acceptableLength > MAX_NUMBER_OF_CHARACTERS){
                    acceptable.append("\n");
                    acceptableLength = 0;
                }
            }
        } else {
            acceptable.append("'").append(verifyList.get(0)).append("' ");
        }

        if (verifyList.size()!=1) {
            acceptable.append("or ").append("'").append(verifyList.get(verifyList.size() - 1))
                    .append("'");
        }

        ArrayList<String> upperCaseList = new ArrayList<>();
        for (String s : verifyList) {
            upperCaseList.add(s.toUpperCase());
        }
        do {
            ret = inputString(prompt + "\nEnter: " + acceptable).toUpperCase();
            if (upperCaseList.contains(ret)){
                advance = true;
                ret = verifyList.get(upperCaseList.indexOf(ret));
            } else {
                printError("Response not found");
            }
        } while (!advance);
        return ret;
    }

    private static void printError(String message) {
        System.out.print(Color.RED);
        System.out.println("ERROR: " + message);
        System.out.print(Color.RESET);
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public double getPrice() {
        return price;
    }

    public String getFormattedPrice(){
        return formatMoney(price);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * This method adds a description to the ArrayList of descriptions
     * @param description the description to add
     */
    public void addDescription(String description){
        this.description.add(description);
    }

    /**
     * This method formats a double, so it includes zero or two decimal points
     * @param money the double to format
     * @return A string containing the formatted double
     */
    public static String formatMoney(double money){
        DecimalFormat formatter = new DecimalFormat("#.##");
        return formatter.format(money);
    }

    public ArrayList<String> getDescription(){
        return description;
    }

    /**
     * This method adds an Option to the ArrayList of Options in the item
     * @param option the Option that will be added
     */
    public void addOption(Option option){
        options.add(option);
    }

    /**
     * this method returns the number of Options the item has
     * @return the size of the options ArrayList
     */
    public int optionSize(){
        return options.size();
    }

    /**
     * this method retrieves an Option from the options ArrayList at the specified index
     * @param index the index of the desired Option
     * @return the Option at the specified index
     */
    public Option getOption(int index){
        return options.get(index);
    }

    /**
     * This method removes the Option at the specified index
     * @param index the index of the Option that will be removed
     */
    public void removeOption(int index){
        options.remove(index);
    }

}
