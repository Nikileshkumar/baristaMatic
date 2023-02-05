import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class BaristaSimulator {

    private Map<String, List<Map<String, Integer>>> drink;
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
                    baristaSimulator.load();    //if the input is R or r, reloading the inventory
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

    //getting the cost of each drink
    public Double getCost(String drink) {
        return this.drink.get(drink).stream().flatMap(x -> x.entrySet().stream()).map(y -> this.ingredientCost.get(y.getKey()) * y.getValue()).mapToDouble(Double::valueOf).sum();
    }
    //checking the availability status of each drink
    public boolean availStatus(String drink) {
        return this.drink.get(drink).stream().flatMap(x -> x.entrySet().stream()).allMatch(y -> this.ingredientQuantity.get(y.getKey()) >= y.getValue());
    }

    //dispensing the item based on availability of drink
    public boolean dispenseItem(String drink) {
        if (!availStatus(drink))
            return false;
        this.drink.get(drink).stream().flatMap(x -> x.entrySet().stream()).forEach(y -> this.ingredientQuantity.put(y.getKey(), this.ingredientQuantity.get(y.getKey()) - y.getValue()));
        return true;
    }

    //initialising the inventory
    public void load() {
        drink = new HashMap<>();
        drink.put("Coffee", List.of(Collections.singletonMap("Coffee", 3), Collections.singletonMap("Sugar", 1), Collections.singletonMap("Cream", 1)));
        drink.put("Decaf Coffee", List.of(Collections.singletonMap("Decaf Coffee", 3), Collections.singletonMap("Sugar", 1), Collections.singletonMap("Cream", 1)));
        drink.put("Caffe Latte", List.of(Collections.singletonMap("Espresso", 2), Collections.singletonMap("Steamed Milk", 1)));
        drink.put("Caffe Americano", List.of(Collections.singletonMap("Espresso", 3)));
        drink.put("Caffe Mocha", List.of(Collections.singletonMap("Espresso", 1), Collections.singletonMap("Cocoa", 1), Collections.singletonMap("Steamed Milk", 1), Collections.singletonMap("Whipped Cream", 1)));
        drink.put("Cappuccino", List.of(Collections.singletonMap("Espresso", 2), Collections.singletonMap("Steamed Milk", 1), Collections.singletonMap("Foamed Milk", 1)));

        menu = new HashMap<>();
        menu.put(1, "Caffe Americano");
        menu.put(2, "Caffe Latte");
        menu.put(3, "Caffe Mocha");
        menu.put(4, "Cappuccino");
        menu.put(5, "Coffee");
        menu.put(6, "Decaf Coffee");

        ingredientCost = new HashMap<>();
        ingredientCost.put("Coffee", 0.75);
        ingredientCost.put("Decaf Coffee", 0.75);
        ingredientCost.put("Sugar", 0.25);
        ingredientCost.put("Cream", 0.25);
        ingredientCost.put("Steamed Milk", 0.35);
        ingredientCost.put("Foamed Milk", 0.35);
        ingredientCost.put("Espresso", 1.10);
        ingredientCost.put("Cocoa", 0.90);
        ingredientCost.put("Whipped Cream", 1.00);

        ingredientQuantity = new HashMap<>();
        ingredientQuantity.put("Coffee", 10);
        ingredientQuantity.put("Decaf Coffee", 10);
        ingredientQuantity.put("Sugar", 10);
        ingredientQuantity.put("Cream", 10);
        ingredientQuantity.put("Steamed Milk", 10);
        ingredientQuantity.put("Foamed Milk", 10);
        ingredientQuantity.put("Espresso", 10);
        ingredientQuantity.put("Cocoa", 10);
        ingredientQuantity.put("Whipped Cream", 10);
    }
}
