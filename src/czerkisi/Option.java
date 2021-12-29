/*
 * Author: Ian Czerkis
 * Project: Food Order Management System: Option Class
 * Created: 19 Dec 2021
 */

package czerkisi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This Class contains a prompt that potentially modifies
 * the description, calories, or price of a BasicItem
 */
public class Option {
    private String prompt;
    private ArrayList<String> responses = new ArrayList<>();
    private ArrayList<Double> priceIncrements = new ArrayList<>();
    private ArrayList<Integer> calorieIncrements = new ArrayList<>();
    private ArrayList<String> addToDescription = new ArrayList<>();
    private static final String[] INC_DEC_SAME_ARRAY = {"Increase", "Decrease", "Remain the Same"};

    Option(){

    }

    Option(String prompt, ArrayList<String> responses, ArrayList<Integer> calorieIncrements,
            ArrayList<Double> priceIncrements, ArrayList<String> addToDescription){
        this.prompt = prompt;
        this.responses = responses;
        this.calorieIncrements = calorieIncrements;
        this.priceIncrements = priceIncrements;
        this.addToDescription = addToDescription;
    }

    /**
     * This class prompts the User to enter and then stores
     * of the prompt, responses, and increments of the Option
     */
    public void create(){
        //enter the prompt
        System.out.println("\nEnter the prompt for the option:" + BasicItem.Color.BLUE +
                "\nEXAMPLES OF PROMPTS:\nDo you want fries with your burger?\nWhat toppings " +
                "would you like on your pizza?" + BasicItem.Color.RESET);
        prompt = BasicItem.inputString("Enter the prompt: ");

        //determine if the prompt is a Yes or No question
        String userInput = BasicItem.inputStringYesNo("Is this prompt a Yes/No question? ");
        ArrayList<String> incDecSameArrayList = new ArrayList<>(List.of(INC_DEC_SAME_ARRAY));

        if (userInput.equals("Yes")){
            //if the prompt is a yes or no question, set the responses to Yes and No
            String[] yesNoArray = {"Yes", "No"};
            responses.addAll(Arrays.asList(yesNoArray));
        } else {
            //prompt the User to enter the responses
            System.out.println("\nEnter the first acceptable response for the prompt:" +
                    BasicItem.Color.BLUE + "\nEXAMPLES RESPONSES:\nMedium Rare, " +
                    "Medium, Medium Well, Well Done\nPepperoni, Bacon, Sausage, " +
                    "Ham" + BasicItem.Color.RESET);
            userInput = BasicItem.inputString("Enter the response");
            do {
                responses.add(userInput);
                userInput = BasicItem.inputString("Enter another acceptable response" +
                        " for the prompt or enter 'Done' if finished: ");
            } while (!userInput.equalsIgnoreCase("DONE"));
        }
        //store how each response affects the price, calories, and description of the item
        for (String response : responses) {
            userInput = BasicItem.inputString((BasicItem.Color.BLUE + "\nPROMPT: " +
                    prompt + "\nRESPONSE: " + response + BasicItem.Color.RESET) +
                    "\nHow does this response affect the price?", incDecSameArrayList);
            if (userInput.equals("Increase")) {
                priceIncrements.add(BasicItem.inputNumber(
                        "By how many dollars does this response increase the price?", 0));
            } else if (userInput.equals("Decrease")) {
                priceIncrements.add(BasicItem.inputNumber(
                        "By how many dollars does this response decrease the price?", 0)*-1);
            } else {
                priceIncrements.add((double) 0);
            }

            userInput = BasicItem.inputString(((BasicItem.Color.BLUE + "\nPROMPT: " + prompt +
                    "\nRESPONSE: " + response + BasicItem.Color.RESET) +
                    "\nHow does this response affect the number of calories?"), incDecSameArrayList);
            if (userInput.equals("Increase")) {
                calorieIncrements.add((int) BasicItem.inputNumber(
                        "How many calories does this item increase by?", 0));
            } else if (userInput.equals("Decrease")) {
                calorieIncrements.add((int) BasicItem.inputNumber(
                        "How many calories does this item decrease by?", 0)*-1);
            } else {
                calorieIncrements.add(0);
            }
            System.out.print(BasicItem.Color.BLUE + "\nPROMPT: " +
                    prompt + "\nRESPONSE: " + response + BasicItem.Color.RESET);
            if (BasicItem.inputStringYesNo(
                    ("\nDoes this option add to the description of the item?" +
                            BasicItem.Color.BLUE +
                            "\nEXAMPLES DESCRIPTIONS:" +
                            "\nOn a gluten free bun" +
                            "\nTemperature: Medium" + BasicItem.Color.RESET)).equals("Yes")) {
                addToDescription.add(BasicItem.inputString("Enter the description:"));
            } else {
                addToDescription.add("");
            }
        }
    }

    /**
     * This class prints the prompt which prompts the User to enter a response
     * that updates the calories, price, and description of the BasicItem
     * @param addItem the BasicItem that will be updated
     */
    public void update(BasicItem addItem){
        String userInput = BasicItem.inputString(prompt, responses);
        int index = responses.indexOf(userInput);
        addItem.setCalories((addItem.getCalories() + calorieIncrements.get(index)));
        addItem.setPrice(addItem.getPrice() + priceIncrements.get(index));
        if (!addToDescription.get(index).isEmpty()){
            addItem.addDescription(addToDescription.get(index));
        }
    }

    public String getPrompt() {
        return prompt;
    }
}
