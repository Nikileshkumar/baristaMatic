import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BaristaSimulator {

    private Map<String, Map<String, Integer>> drink;
    private Map<Integer, String> menu;
    private Map<String, Double> ingredientCost;
    private Map<String, Integer> ingredientQuantity;

    public static void main(String[] args) throws IOException {
        BaristaSimulator baristaSimulator = new BaristaSimulator();
        baristaSimulator.load(); //loading the inventory for the first time
        while (true) {
            System.out.println("Inventory:");
            baristaSimulator.ingredientQuantity.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(x -> System.out.println(x.getKey() + ", " + x.getValue()));
            System.out.println("Menu:");
            //trying to get the cost and availability status of each drink
            baristaSimulator.menu.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEach(x -> System.out.println(x.getKey() + ", " + x.getValue() + ", $" + String.format("%.2f", baristaSimulator.getCost(x.getValue())) + ", " + baristaSimulator.availStatus(x.getValue())));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//reading the input from command line
            String input = br.readLine().trim();
            if (!Pattern.matches("[RrQq123456]", input)) {//validating the input values
                System.out.println("Invalid Selection: " + input);
            } else {
                if (input.equalsIgnoreCase("R")) {
                    baristaSimulator.ingredientQuantity.replaceAll((x, y) -> 10);    //if the input is R or r, reloading the inventory
                } else if (input.equalsIgnoreCase("Q")) { //if the input is Q or q, quitting the program
                    break;
                } else if (!baristaSimulator.dispenseItem(baristaSimulator.menu.get(Integer.valueOf(input)))) { //Dispensing the inventory
                    System.out.println("Out of Stock: " + baristaSimulator.menu.get(Integer.valueOf(input)));
                } else {
                    System.out.println("Dispensing: " + baristaSimulator.menu.get(Integer.valueOf(input)));
                }
            }
        }
    }

    public Double getCost(String drink) { //getting the cost of each drink
        return this.drink.get(drink).entrySet().stream().map(y -> this.ingredientCost.get(y.getKey()) * y.getValue()).mapToDouble(Double::valueOf).sum();
    }

    public boolean availStatus(String drink) { //checking the availability status of each drink
        return this.drink.get(drink).entrySet().stream().allMatch(y -> this.ingredientQuantity.get(y.getKey()) >= y.getValue());
    }

    public boolean dispenseItem(String drink) { //dispensing the item based on availability of drink
        if (!availStatus(drink))
            return false;
        this.drink.get(drink).forEach((key, value) -> this.ingredientQuantity.put(key, this.ingredientQuantity.get(key) - value));
        return true;
    }

    public void load() { //initialising the inventory
        drink = Map.of(
        "Coffee", Map.of("Coffee", 3, "Sugar", 1, "Cream", 1),
        "Decaf Coffee", Map.of("Decaf Coffee", 3, "Sugar", 1, "Cream", 1),
        "Caffe Latte", Map.of("Espresso", 2, "Steamed Milk", 1),
        "Caffe Americano", Map.of("Espresso", 3),
        "Caffe Mocha", Map.of("Espresso", 1, "Cocoa", 1, "Steamed Milk", 1, "Whipped Cream", 1),
        "Cappuccino", Map.of("Espresso", 2, "Steamed Milk", 1, "Foamed Milk", 1));

        menu = Map.of(1, "Caffe Americano",2, "Caffe Latte",3, "Caffe Mocha",
                4,"Cappuccino",5, "Coffee",6, "Decaf Coffee");

        ingredientCost = Map.of("Coffee", 0.75, "Decaf Coffee", 0.75, "Sugar", 0.25,
        "Cream", 0.25, "Steamed Milk", 0.35, "Foamed Milk", 0.35,
        "Espresso", 1.10, "Cocoa", 0.90, "Whipped Cream", 1.00);

        ingredientQuantity = new HashMap<>();
        ingredientCost.keySet().forEach(x -> ingredientQuantity.put(x, 10));
    }
}