import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class AdventureGame {

    // Map of the rooms in the game
    static Map<String, Room> rooms = new HashMap<>();
    static Player player = new Player();

    public static void main(String[] args) {
        // Initialize the game
        initializeRooms();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Adventure Game!");
        System.out.println("You find yourself in a mysterious world.");

        // Game loop
        while (player.isAlive()) {
            System.out.println("\nYou are in " + player.currentRoom.name + ".");
            System.out.println(player.currentRoom.description);

            if (player.currentRoom.monster != null && player.currentRoom.monster.isAlive()) {
                System.out.println("There is a " + player.currentRoom.monster.name + " here!");
                System.out.println("What will you do? (fight/run)");
                String action = scanner.nextLine();

                if (action.equalsIgnoreCase("fight")) {
                    fightMonster(scanner);
                } else {
                    System.out.println("You ran away!");
                }
            } else {
                System.out.println("What will you do? (move/look/exit)");
                String action = scanner.nextLine();

                if (action.equalsIgnoreCase("move")) {
                    movePlayer(scanner);
                } else if (action.equalsIgnoreCase("look")) {
                    System.out.println("You see: " + player.currentRoom.treasure);
                } else if (action.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the game. Goodbye!");
                    break;
                }
            }
        }

        if (!player.isAlive()) {
            System.out.println("You have died. Game over!");
        }

        scanner.close();
    }

    // Initialize the rooms and link them
    public static void initializeRooms() {
        Room room1 = new Room("Forest", "You are in a dark forest.");
        Room room2 = new Room("Cave", "You are in a damp, cold cave.");
        Room room3 = new Room("Mountain", "You are at the base of a towering mountain.");
        Room room4 = new Room("Village", "You have arrived at a peaceful village.");

        room1.addExit("north", room2);
        room2.addExit("south", room1);
        room2.addExit("east", room3);
        room3.addExit("west", room2);
        room3.addExit("north", room4);
        room4.addExit("south", room3);

        room2.monster = new Monster("Goblin", 20, 5);
        room3.monster = new Monster("Dragon", 50, 10);

        room1.treasure = "a shiny sword";
        room3.treasure = "a chest of gold";

        rooms.put("Forest", room1);
        rooms.put("Cave", room2);
        rooms.put("Mountain", room3);
        rooms.put("Village", room4);

        player.currentRoom = room1; // Player starts in the forest
    }

    // Handle player movement
    public static void movePlayer(Scanner scanner) {
        System.out.println("Where do you want to go? (north/south/east/west)");
        String direction = scanner.nextLine();

        if (player.currentRoom.exits.containsKey(direction)) {
            player.currentRoom = player.currentRoom.exits.get(direction);
            System.out.println("You moved to " + player.currentRoom.name + ".");
        } else {
            System.out.println("You can't go that way.");
        }
    }

    // Handle monster fighting logic
    public static void fightMonster(Scanner scanner) {
        Monster monster = player.currentRoom.monster;
        Random random = new Random();

        while (monster.isAlive() && player.isAlive()) {
            System.out.println("You attack the " + monster.name + "!");
            int damage = random.nextInt(10) + 1; // Random damage between 1 and 10
            monster.takeDamage(damage);
            System.out.println("You dealt " + damage + " damage.");

            if (monster.isAlive()) {
                System.out.println("The " + monster.name + " attacks you!");
                player.takeDamage(monster.attackPower);
                System.out.println("You took " + monster.attackPower + " damage. Health: " + player.health);
            }
        }

        if (player.isAlive()) {
            System.out.println("You defeated the " + monster.name + "!");
        } else {
            System.out.println("You were killed by the " + monster.name + "...");
        }
    }
}

// Room class
class Room {
    String name;
    String description;
    String treasure;
    Monster monster;
    Map<String, Room> exits = new HashMap<>();

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addExit(String direction, Room room) {
        exits.put(direction, room);
    }
}

// Player class
class Player {
    int health = 100;
    Room currentRoom;

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }
}

// Monster class
class Monster {
    String name;
    int health;
    int attackPower;

    public Monster(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
