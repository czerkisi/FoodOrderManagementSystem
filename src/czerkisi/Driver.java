/*
 * Author: Ian Czerkis
 * Project: Food Order Management System: Driver Class
 * Created: 19 Dec 2021
 */

package czerkisi;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages BasicItems, Restaurants, and
 * Options to create a Food Order Management System
 */
public class Driver {
    private static final int MAX_NUMBER_OF_CHARACTERS = 100;
    private static final String[] MAIN_MENU_OPTIONS = {"Visit",
            "Create", "Checkout", "Cart", "Manage", "Quit"};
    private static final ArrayList<String> MAIN_MENU_OPTIONS_ARRAY_LIST =
            new ArrayList<>(List.of(MAIN_MENU_OPTIONS));
    private static final String[] RESTAURANT_VIEW_OPTIONS = {"Menu", "Order", "Cart", "Exit"};
    private static final ArrayList<String> RESTAURANT_VIEW_OPTIONS_ARRAY_LIST =
            new ArrayList<>(List.of(RESTAURANT_VIEW_OPTIONS));
    private static final String[] MANAGE_OPTIONS = {"Cart", "Restaurant"};
    private static final ArrayList<String> MANAGE_OPTIONS_ARRAY_LIST =
            new ArrayList<>(List.of(MANAGE_OPTIONS));
    private static final String[] CART_MANAGE_OPTIONS = {"Remove", "Edit", "Quit"};
    private static final ArrayList<String> CART_MANAGE_OPTIONS_ARRAY_LIST =
            new ArrayList<>(List.of(CART_MANAGE_OPTIONS));
    private static final String[] RESTAURANT_MANAGE_OPTIONS = {"Remove Item",
            "Edit Item", "Remove Restaurant", "Rename Restaurant"};
    private static final ArrayList<String> RESTAURANT_MANAGE_OPTIONS_ARRAY_LIST =
            new ArrayList<>(List.of(RESTAURANT_MANAGE_OPTIONS));
    private static final String[] ITEM_UPDATE_OPTIONS = {"Name", "Price",
            "Calories", "Add Option", "Replace Option", "Remove Option"};
    private static final ArrayList<String> ITEM_UPDATE_ARRAY_LIST =
            new ArrayList<>(List.of(ITEM_UPDATE_OPTIONS));
    private static final double TAX_RATE = 0.06;

    public static void main(String[] args) {
        ArrayList<BasicItem> cart = new ArrayList<>();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(loadMexicanRestaurant());
        printStart();
        String userInput;
        do {
            userInput = BasicItem.inputString("What would you like " +
                    "to do: Visit a restaurant, Create a restaurant, view Cart, " +
                    "Checkout, Manage restaurants, items, or cart, or Quit?",
                    MAIN_MENU_OPTIONS_ARRAY_LIST);

            switch (userInput) {
                case "Visit":
                    openRestaurants(restaurants, cart);
                    break;
                case "Create":
                    Restaurant newRes = new Restaurant(BasicItem.inputString(
                            "What is the name of the restaurant?"));
                    addItems(newRes);
                    restaurants.add(newRes);
                    break;
                case "Checkout":
                    userInput = checkout(cart);
                    break;
                case "Cart":
                    System.out.print(cartToString(cart));
                    break;
                case "Manage":
                    switch (BasicItem.inputString("What would you like to manage, " +
                            "items in your cart, or a restaurant?", MANAGE_OPTIONS_ARRAY_LIST)) {
                        case "Cart" -> manageCart(cart, restaurants);
                        case "Restaurant" -> manageRestaurants(restaurants);
                        default -> {
                        }
                    }
                    break;
                default:
                    break;
            }
        } while (!userInput.equals("Quit"));
    }

    private static String checkout(ArrayList<BasicItem> cart) {
        //print cart
        System.out.println("Your Cart:");
        System.out.println(cartToString(cart));
        int totalPrice = 0;
        for (BasicItem item : cart) {
            totalPrice += item.getPrice();
        }

        //print total
        System.out.println("Subtotal: $" + totalPrice);
        double tax = totalPrice * TAX_RATE;
        System.out.println("Tax: $" + BasicItem.formatMoney(tax));
        System.out.println("Total: $" + BasicItem.formatMoney((tax + totalPrice)) + "\n");

        //confirm checkout
        if (BasicItem.inputStringYesNo("Would you like to checkout?").equals("Yes")) {
            cart.clear();
            System.out.println(BasicItem.Color.BLUE + "Your order has successfully been placed. " +
                    BasicItem.Color.RESET);
        }

        //ask if the User would like to start over or quit
        if (BasicItem.inputStringYesNo("Would you like to quit?").equals("Yes")){
            return "Quit";
        } else {
            return "Don't Quit";
        }
    }

    /**
     * This class allows the User to manage Restaurants in the program
     * @param restaurants an ArrayList of Restaurants that the User can change
     * name, remove, add, update, and remove items on the menu
     */
    private static void manageRestaurants(ArrayList<Restaurant> restaurants) {
        do {
            //print the Restaurant names each with unique numbers
            System.out.println("Restaurants: ");
            for (int i = 1; i <= restaurants.size(); i++) {
                System.out.println(BasicItem.Color.BLUE + "(" + i + ")" + BasicItem.Color.RESET +
                        " " + restaurants.get(i - 1).getName());
            }

            //ask the user to input the corresponding number and determine the selected Restaurant
            int restaurantIndex = (int) BasicItem.inputNumber("Enter the number of the " +
                    "restaurant you want to edit: ", 1, restaurants.size()) - 1;
            Restaurant updateRestaurant = restaurants.get(restaurantIndex);
            switch (BasicItem.inputString("What would you like to update?",
                    RESTAURANT_MANAGE_OPTIONS_ARRAY_LIST)) {
                case "Remove Item":
                    //get the name of the item that will be removed
                    String removeName = BasicItem.inputString("Which item would you " +
                            "like to remove?", restaurants.get(restaurantIndex).getNamesArray());
                    //print a successful message once the item has been removed
                    System.out.println(BasicItem.Color.BLUE + updateRestaurant.removeItem(
                            updateRestaurant.getNamesArray().indexOf(removeName)).getName() +
                            " has been removed" + BasicItem.Color.RESET);
                    break;
                case "Edit Item":
                    do {
                        //select the item that will be updated
                        String updateName = BasicItem.inputString("Which item " +
                                "would you like to update?", updateRestaurant.getNamesArray());
                        int updateIndex = updateRestaurant.getNamesArray().indexOf(updateName);
                        BasicItem updateItem = updateRestaurant.getMenuItem(updateIndex);
                        String oldName = updateRestaurant.getMenuItem(updateIndex).getName();
                        switch (BasicItem.inputString("What would you like to do, change the " +
                                "Name, update the Price or Calories, or Remove, Replace, or Add " +
                                "an option", ITEM_UPDATE_ARRAY_LIST)) {
                            case "Name":
                                updateItem.setName(BasicItem.inputString("Enter the new name of " +
                                        updateItem.getName() + ":"));
                                System.out.println(oldName + " has been renamed to " +
                                        updateItem.getName());
                                break;
                            case "Price":
                                updateItem.setPrice(BasicItem.inputNumber("What is the " +
                                        "new price of " + updateItem.getName() + "?", 0));
                                System.out.println(oldName + "'s price has been set to $" +
                                        updateItem.getFormattedPrice());
                                break;
                            case "Calories":
                                updateItem.setCalories((int) BasicItem.inputNumber("What is the " +
                                        "new calorie count of " + updateItem.getName() + "?", 0));
                                System.out.println(oldName + "'s calorie count has been set to " +
                                        updateItem.getCalories());
                                break;
                            case "Replace Option":
                                for (int i = 0; i < updateItem.optionSize(); i++) {
                                    System.out.println(BasicItem.Color.BLUE + "(" + (i + 1) + ")" +
                                            BasicItem.Color.RESET +
                                            updateItem.getOption(i).getPrompt());
                                }
                                System.out.print("\n");
                                int optionIndex = (int) BasicItem.inputNumber("Enter the number " +
                                        "of the prompt of the option you want to replace:", 1,
                                        updateItem.optionSize()) - 1;
                                updateItem.removeOption(optionIndex);
                                System.out.println("Create a new option to replace " +
                                        "the existing option:");
                                Option newOption = new Option();
                                newOption.create();
                                updateItem.addOption(newOption);
                                break;
                            case "Remove Option":
                                for (int i = 0; i < updateItem.optionSize(); i++) {
                                    System.out.println(BasicItem.Color.BLUE + "(" + (i + 1) +
                                            ")" + BasicItem.Color.RESET +
                                            updateItem.getOption(i).getPrompt());
                                }
                                System.out.print("\n");
                                optionIndex = (int) BasicItem.inputNumber("Enter the number of " +
                                        "the prompt of the option you want to remove:", 1,
                                        updateItem.optionSize()) - 1;
                                updateItem.removeOption(optionIndex);
                                System.out.println("Option " + optionIndex + " (" +
                                        updateItem.getOption(optionIndex) + ") has been removed.");
                                break;
                            case "Add Option":
                                System.out.println("Enter the information to " +
                                        "create and add a new Option:");
                                newOption = new Option();
                                newOption.create();
                                updateItem.addOption(newOption);
                                System.out.println(BasicItem.Color.BLUE +
                                        "This Option has been added" + BasicItem.Color.RESET);
                                break;
                            default:
                                break;
                        }
                    } while (BasicItem.inputStringYesNo("Would you like to update another " +
                            "item at " + updateRestaurant.getName() + "?").equals("Yes"));
                    break;
                case "Remove Restaurant":
                    String oldName = updateRestaurant.getName();
                    //confirm the User wants to remove the Restaurant
                    if (BasicItem.inputStringYesNo("Are you sure you would like to remove " +
                            oldName + "? This will remove all items in the restaurant").equals("Yes")) {
                        restaurants.remove(restaurantIndex);
                        System.out.println(oldName + " has been removed");
                    } else {
                        System.out.println("No changes have been made");
                    }
                    break;
                case "Rename Restaurant":
                    oldName = updateRestaurant.getName();
                    updateRestaurant.setName(BasicItem.inputString("Enter the new name of " +
                            updateRestaurant.getName() + ":"));
                    System.out.println(oldName + " has been changed to " +
                            updateRestaurant.getName());
                    break;
                default:
                    break;
            }
        } while (BasicItem.inputStringYesNo("Would you like to edit " +
                "a different Restaurant?").equals("Yes"));
    }

    /**
     * This class provides options for the User to update items in the cart
     * @param cart an ArrayList of BasicItems that are in the cart
     * @param restaurants the list of restaurants is necessary to
     * update items since the program must recall the Options
     * on each menu item and base calories, price, and name
     */
    public static void manageCart(ArrayList<BasicItem> cart, ArrayList<Restaurant> restaurants) {
        do {
            if (cart.isEmpty()){
                System.out.println("is empty\n");
            } else {
                String userInput = BasicItem.inputString("What would you like to do: Remove " +
                        "an item in your cart, Edit an item in your cart, or Quit?",
                        CART_MANAGE_OPTIONS_ARRAY_LIST);
                //print the cart with each item having a unique number
                for (int i = 1; i <= cart.size(); i++) {
                    System.out.println(BasicItem.Color.BLUE + "(" + i + ")" +
                            BasicItem.Color.RESET + " " + cart.get(i - 1).getName() +
                            ", $" + cart.get(i - 1).getFormattedPrice());
                    if (!cart.get(i - 1).getDescription().isEmpty()) {
                        for (int j = 0; j < cart.get(i - 1).getDescription().size(); j++) {
                            System.out.println("    " + cart.get(i - 1).getDescription().get(j));
                        }
                    }
                }
                switch (userInput) {
                    case "Remove":
                        //ask the User to select a number, remove the
                        // item at that index, and print a success message
                        System.out.println(BasicItem.Color.BLUE +
                                cart.remove((int) BasicItem.inputNumber(
                                        "Enter the number of the item you want to remove: ", 1,
                                        cart.size())-1).getName() +" has been removed" +
                                        BasicItem.Color.RESET);
                        break;
                    case "Edit":
                        int index = (int) BasicItem.inputNumber("Enter the number of the " +
                                "item you want to edit: ", 1, cart.size())-1;
                        //locate the original item
                        int restaurantIndex = 0;
                        int itemIndex = 0;
                        for (int i = 0; i < restaurants.size(); i++) {
                            for (int j = 0; j < restaurants.get(i).getNamesArray().size(); j++) {
                                if (restaurants.get(i).getNamesArray().get(j).equals(cart.get(index).getName())){
                                    restaurantIndex = i;
                                    itemIndex = j;
                                }
                            }
                        }
                        //make a copy
                        BasicItem original =
                                restaurants.get(restaurantIndex).getMenuItem(itemIndex);
                        BasicItem add = new BasicItem(original.getName(),
                                original.getCalories(), original.getPrice());
                        //update any options
                        System.out.println(BasicItem.Color.BLUE + "Editing: " +
                                add.getName() + BasicItem.Color.RESET);
                        for (int i = 0; i < original.optionSize(); i++) {
                            original.getOption(i).update(add);
                        }
                        cart.remove(index);
                        cart.add(index, add);
                        System.out.println(BasicItem.Color.BLUE + add.getName() +
                                " has been updated" + BasicItem.Color.RESET);
                    default:
                        break;
                }
            }
        } while (BasicItem.inputStringYesNo("Would you like to edit another item?").equals("Yes"));
    }

    /**
     * this class prints the start message for the program
     */
    private static void printStart() {
        System.out.println("""
                This programs allows restaurant owners to digitize their menus to allow for online\s
                ordering and allows customers to place online food orders. Visit a restaurant to view\s
                their menu and place an order, or create a restaurant to make your own restaurant and menu.
                A Mexican restaurant, Tijuana Cafe, has been created as an example.
                """);
    }

    /**
     * This class prompts the user for the necessary fields to add items to a Restaurant
     * @param newRes the restaurant in which the items will be added
     */
    public static void addItems(Restaurant newRes) {
        do {
            //set the name price and calories
            BasicItem addItem = new BasicItem();
            addItem.setName(BasicItem.inputString("Enter the name of the item:"));
            addItem.setPrice(BasicItem.inputNumber(
                    "How much (in dollars) does this item cost? ", 0));
            addItem.setCalories((int) BasicItem.inputNumber(
                    "How many calories does this item have?", 0));
            System.out.print("\nDoes this item have any options?" + BasicItem.Color.BLUE +
                    "\nEXAMPLES OF OPTIONS:" +
                    "\nHow would you like your burger cooked?" +
                    "\nDo you want the sauce on the side?" + BasicItem.Color.RESET);
            //if the item has at least one Option
            if (BasicItem.inputStringYesNo("").equals("Yes")){
                do {
                    Option tempOption = new Option();
                    tempOption.create();
                    addItem.addOption(tempOption);
                } while (BasicItem.inputStringYesNo(
                        "Would you like to add another option?").equals("Yes"));
            }
            newRes.addItem(addItem);
        } while (BasicItem.inputStringYesNo("Would you like to add another item?").equals("Yes"));
    }

    /**
     * This program collects the name, price, and calories of each
     * item in the ArrayList (cart) and returns them as a String
     * which can easily be printed
     * @param cart An ArrayList of BasicItems that will be printed
     * @return A String containing the descriptions of each BasicItem in the cart;
     * any duplicate items are grouped together
     */
    private static String cartToString(ArrayList<BasicItem> cart) {
        StringBuilder ret = new StringBuilder();
        ArrayList<String> checkNames = new ArrayList<>();
        ArrayList<ArrayList<String>> checkDescriptions = new ArrayList<>();
        ArrayList<Integer> checkQuantities = new ArrayList<>();
        ArrayList<BasicItem> uniqueItems = new ArrayList<>();
        for (BasicItem item : cart) {
            if (checkNames.contains(item.getName()) &&
                    checkDescriptions.get(checkNames.indexOf(item.getName())).equals(item.getDescription())) {
                //if the item is a duplicate of an item that was already iterated over
                checkQuantities.set(checkNames.indexOf(item.getName()),
                        checkQuantities.get(checkNames.indexOf(item.getName())) + 1);
            } else {
                // if the item has not been previously found in the cart
                checkNames.add(item.getName());
                checkDescriptions.add(item.getDescription());
                checkQuantities.add(1);
                uniqueItems.add(item);
            }
        }
        //print the quantity of each unique item
        ret.append("Cart:\n\n");
        for (int i = 0; i < uniqueItems.size(); i++) {
            ret.append("(").append(checkQuantities.get(i)).append(") ").append(uniqueItems.get(i).getName());
            for (int j = 0; j < uniqueItems.get(i).getDescription().size(); j++) {
                ret.append("\n    ").append(uniqueItems.get(i).getDescription().get(j));
            }
            ret.append("\n    $")
                    .append(uniqueItems.get(i).getFormattedPrice())
                    .append("\n    ")
                    .append(uniqueItems.get(i).getCalories())
                    .append(" Calories\n\n");
        }
        return ret.toString();
    }


    /**
     * this class loads a pre-made Mexican Restaurant
     * @return a Mexican Restaurant with many items and an Option
     */
    public static Restaurant loadMexicanRestaurant(){
        //Calorie source: USDA Food Data Central
        //https://fdc.nal.usda.gov/
        String[] namesArray = {"Quesadilla", "Taco", "Chilaquiles", "Burrito", "Enchiladas"};
        Integer[] calArray = {528, 156, 355, 447, 323};
        Double[] priceArray = {10.0, 3.0, 9.0, 11.0, 9.0};

        String[] responsesArray = {"Chicken", "Pork", "Beef", "Steak", "Chorizo",
                "Shrimp", "Vegetarian"};
        ArrayList<String> responsesArrayList = new ArrayList<>(List.of(responsesArray));

        Integer[] calIncArray = {355, 206, 213, 679, 129, 99, 59};
        ArrayList<Integer> calIncArrayList = new ArrayList<>(List.of(calIncArray));

        Double[] priceIncArray = {2.0, 1.5, 1.5, 3.0, 2.5, 3.0, 0.0};
        ArrayList<Double> priceIncArrayList = new ArrayList<>(List.of(priceIncArray));

        Restaurant ret = new Restaurant("Tijuana Cafe");
        Option meatChoice = new Option("What kind of meat would you like?", responsesArrayList,
                calIncArrayList, priceIncArrayList, responsesArrayList);
        for (int i = 0; i < namesArray.length; i++) {
            BasicItem newItem = new BasicItem(namesArray[i], calArray[i], priceArray[i]);
            newItem.addOption(meatChoice);
            ret.addItem(newItem);
        }
        return ret;
    }

    /**
     * This class allows the User to "visit" a Restaurant
     * Once the User visits the restaurant, they can view the menu or order BasicItems,
     * @param restaurants the Restaurant they are "visiting"
     */
    private static void openRestaurants(ArrayList<Restaurant> restaurants, ArrayList<BasicItem> cart) {
        ArrayList<String> names = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            names.add(restaurant.getName());
        }

        StringBuilder namesToString = new StringBuilder();
        if (names.size() > 2) {
            int acceptableLength = 0;
            for (int i = 0; i < names.size() - 1; i++) {
                namesToString.append(names.get(i)).append(", ");
                acceptableLength += namesToString.length();
                if (acceptableLength > MAX_NUMBER_OF_CHARACTERS){
                    namesToString.append("\n");
                    acceptableLength = 0;
                }
            }
        } else {
            namesToString.append(names.get(0)).append(" ");
        }
        if (names.size()!=1) {
            namesToString.append("or ").append(names.get(names.size() - 1));
        }

        String userInput;
        userInput = BasicItem.inputStringNoReprint(
                "Enter which restaurant would you like to visit, or 'Quit' to quit." +
                        "\nRESTAURANTS: " + namesToString, names);
        if (!userInput.equals("Quit")) {
            int index = names.indexOf(userInput);
            String userAction;
            System.out.println("Welcome to " + names.get(index) + "!\n");
            Restaurant selected = restaurants.get(index);
            do {
                userAction = BasicItem.inputString("What would you like to do? View menu, " +
                        "place order, view cart, or exit?", RESTAURANT_VIEW_OPTIONS_ARRAY_LIST);
                switch (userAction) {
                    case "Menu" -> System.out.print(selected.getMenu());
                    case "Order" -> {
                        //Prompt the User to order an item
                        BasicItem add = getOrder(selected);

                        //Get the quantity of the item and print the corresponding success message
                        int quantity = (int) BasicItem.inputNumber("How many would you like?", 0);
                        for (int i = 0; i < quantity; i++) {
                            cart.add(add);
                        }
                        if (quantity == 1) {
                            System.out.println(
                                    "1 " + cart.get(cart.size() - 1).getName() +
                                            " has been added to your cart\n");
                        } else {
                            System.out.println(
                                    quantity + " " +
                                            cart.get(cart.size() - 1).getName() +
                                            " have been added to your cart\n");
                        }
                    }
                    case "Cart" -> System.out.print(cartToString(cart));
                    default -> {
                    }
                }
            } while (!userAction.equals("Exit"));


        }
    }

    /**
     * This class reads the menu of a Restaurant and prompts the User to
     * enter the name and any Options the menu item they selected has
     * @param restaurant The Restaurant to read the menu from
     * @return The BasicItem containing the correct name, price,
     * number of calories and description the User selected
     */
    private static BasicItem getOrder(Restaurant restaurant) {
        ArrayList<String> itemNames = restaurant.getNamesArray();

        //User selects which item to purchase
        String userInput = BasicItem.inputString(
                "Which item would you like to purchase?", itemNames);
        BasicItem toCopy = restaurant.getMenuItem(itemNames.indexOf(userInput));

        //create a copy of the selected item
        BasicItem ret = new BasicItem(toCopy.getName(), toCopy.getCalories(), toCopy.getPrice());

        //iterate over each Option to potentially modify any attributes of the item
        for (int i = 0; i < toCopy.optionSize(); i++) {
            toCopy.getOption(i).update(ret);
        }
        return ret;
    }
}
