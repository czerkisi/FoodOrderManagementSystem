/*
 * Author: Ian Czerkis
 * Project: Food Order Management System: Restaurant Class
 * Created: 19 Dec 2021
 */

package czerkisi;

import java.util.ArrayList;

/**
 * The Restaurant class stores an ArrayList of BasicItems (menu) and the name of the Restaurant
 */
public class Restaurant {
    private final ArrayList<BasicItem> menuItems = new ArrayList<>();
    private String name;

    Restaurant(String name){
        this.name = name;
    }

    /**
     * This method adds a BasicItem to the menu of the Restaurant
     * @param item The BasicItem that will be added to the menu
     */
    public void addItem(BasicItem item){
        this.menuItems.add(item);
    }

    /**
     * This method returns the Menu of the Restaurant
     * @return A String containing the name, price, and calories
     * of all the items in the Restaurant
     */
    public String getMenu(){
        StringBuilder ret = new StringBuilder("Menu:\n");
        for (BasicItem item : menuItems) {
            ret.append(item.getName()).append(" $").append(item.getFormattedPrice())
                    .append("\nCalories: ").append(item.getCalories()).append("\n\n");
        }
        return ret.toString();
    }

    /**
     * This method returns the names of all the menu items
     * @return An ArrayList containing the names of all the menu items
     */
    public ArrayList<String> getNamesArray(){
        ArrayList<String> ret = new ArrayList<>();
        for (BasicItem item : menuItems) {
            ret.add(item.getName());
        }
        return ret;
    }

    /**
     * This method retrieves a menu item at the specified index
     * @param index The index of the desired item
     * @return The BasicItem at the specified index
     */
    public BasicItem getMenuItem(int index){
        return menuItems.get(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method removes an item from the menu at the specified index
     * @param index The index of the item that will be removed
     * @return the BasicItem that was removed
     */
    public BasicItem removeItem(int index) {
        return menuItems.remove(index);
    }
}
