import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class HorseRace {

    private Map<Integer, String> horses;
    private Map<Integer, Integer> odds;
    private TreeMap<Integer, Integer> denomination;
    private TreeMap<Integer, Integer> payout;
    private Integer winningHorse = 1;

    public static void main(String[] args) throws IOException {
        HorseRace horseRace = new HorseRace();
        horseRace.load();
        while (true) {
            horseRace.payout.replaceAll((x, y) -> 0);
            System.out.println("Inventory:");
            horseRace.denomination.forEach((x, y) -> System.out.println("$" + x + ", " + y));
            System.out.println("Horses:");
            horseRace.horses.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEach(x -> System.out.println(x.getKey() + ", " + x.getValue() + ", " + horseRace.odds.get(x.getKey()) + ", " + (Objects.equals(x.getKey(), horseRace.winningHorse) ? "won" : "lost")));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//reading the input from command line
            String input = br.readLine().trim();
            if (!Pattern.matches("[RrQq]|[Ww]\\s+[0-9]+|[0-9]+\\s+[0-9]+\\.?[0-9]*", input)) {
                System.out.println("Invalid Command: " + input);
            } else {
                if (input.equalsIgnoreCase("R")) {
                    horseRace.denomination.replaceAll((x, y) -> 10);    //if the input is R or r, reloading the inventory
                } else if (input.equalsIgnoreCase("Q")) { //if the input is Q or q, quitting the program
                    break;
                } else {
                    String[] entry = input.replaceAll("\\s+", " ").split(" ");
                    if (entry[0].equalsIgnoreCase("W")) {
                        Integer num = Integer.valueOf(entry[1]);
                        if (horseRace.horses.containsKey(num)) {
                            horseRace.winningHorse = num;
                        } else {
                            System.out.printf("Invalid Horse Number: %d\n", num);
                        }
                    } else {
                        Integer horseNum = Integer.valueOf(entry[0]);
                        if (!horseRace.horses.containsKey(horseNum)) {
                            System.out.printf("Invalid Horse Number: %d\n", horseNum);
                        } else if (!Pattern.matches("[0-9]+", entry[1])) {
                            System.out.printf("Invalid Bet: %s\n", entry[1]);
                        } else {
                            if (!horseNum.equals(horseRace.winningHorse)) {
                                System.out.printf("No Payout: %s\n", horseRace.horses.get(horseNum));
                            } else {
                                horseRace.dispensingAmount(horseNum, Integer.valueOf(entry[1]));
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isDenominationsAvailable(Integer amount) {
        for (Integer key : this.denomination.descendingKeySet()) {
            Integer value = amount / key;
            if (this.denomination.get(key) >= value) {
                this.payout.put(key, value);
                amount = amount % key;
            } else {
                this.payout.put(key, this.denomination.get(key));
                amount = amount - (key * this.denomination.get(key));
            }
        }
        return amount <= 0;
    }

    public void dispensingAmount(Integer horseNum, Integer bet) {
        Integer totalAmt = this.odds.get(horseNum) * bet;
        if (!isDenominationsAvailable(totalAmt)) {
            System.out.printf("Insufficient Funds: $%d\n", totalAmt);
        } else {
            System.out.printf("Payout: %s, $%d\n", this.horses.get(horseNum), totalAmt);
            this.payout.forEach((x, y) -> {
                this.denomination.put(x, this.denomination.get(x) - y);
                System.out.println("$" + x + ", " + y);
            });
        }
    }

    public void load() {
        horses = Map.of(1, "That Darn Gray Cat", 2, "Fort Utopia", 3, "Count Sheep",
                4, "Ms Traitour", 5, "Real Princess", 6, "Pa Kettle", 7, "Gin Stinger");

        odds = Map.of(1, 5, 2, 10, 3, 9, 4, 4, 5, 3, 6, 5, 7, 6);

        denomination = new TreeMap<>();
        payout = new TreeMap<>();
        Stream.of(1, 5, 10, 20, 100).forEach(x -> {
            denomination.put(x, 10);
            payout.put(x, 0);
        });
    }
}