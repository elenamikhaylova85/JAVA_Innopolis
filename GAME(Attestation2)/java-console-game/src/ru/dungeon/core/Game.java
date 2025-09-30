package ru.dungeon.core;



import ru.dungeon.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final GameState state = new GameState();
    private final Map<String, Command> commands = new LinkedHashMap<>();
    static {
        WorldInfo.touch("Game");
    }

    public Game() {
        registerCommands();
        bootstrapWorld();
    }

    private void registerCommands() {
        commands.put("help", (ctx, a) -> System.out.println("Команды: " + String.join(", ", commands.keySet())));
        commands.put("gc-stats", (ctx, a) -> {printGcStats();});
        commands.put("about",(ctx, args) -> {
            System.out.println("Dungeon Mini\nВерсия: 1.0\nАвтор: Михайлова Е.А.\nJava-версия: " + System.getProperty("java.version"));
        });
        commands.put("look", (ctx, a) -> System.out.println(ctx.getCurrent().describe()));
        commands.put("move", (ctx, a) -> {
            try {
                 if (a.isEmpty()) throw new InvalidCommandException("Укажите направление для перемещения: <north|south|east|west>");
                String direction = a.get(0).toLowerCase(Locale.ROOT);
                Room currentRoom = ctx.getCurrent();
                Room nextRoom = currentRoom.getNeighbors().get(direction);
                if (nextRoom == null) throw new InvalidCommandException("Невозможно переместиться в этом направлении.");
                ctx.setCurrent(nextRoom);
                System.out.println("Вы перешли в " + nextRoom.describe());
            } catch (InvalidCommandException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
            System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        });
        commands.put("take", (ctx, a) -> {
            try {
                if (a.isEmpty()) throw new InvalidCommandException("Укажите название предмета, который хотите взять.");
                String itemName = String.join(" ",a);
                Room currentRoom = ctx.getCurrent();
                Item item = currentRoom.getItems().stream()
                        .filter(i -> i.getName().equalsIgnoreCase(itemName))
                        .findFirst()
                        .orElse(null);
                if (item == null)
                    throw new InvalidCommandException("Предмет '" + itemName + "' не найден в текущей комнате.");
                ctx.getPlayer().getInventory().add(item); //добавляем предмет в инвентарь
                currentRoom.getItems().remove(item);//удаляем из комнаты
                System.out.println("Взято: " + itemName);
            } catch (InvalidCommandException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        });
        commands.put("inventory", (ctx, a) -> {
            try {
                Player player = ctx.getPlayer();
                List<Item> inventory = player.getInventory();
                if (inventory.isEmpty()) {
                    System.out.println("Инвентарь пуст.");
                    return;
                }
                else {
                    System.out.println("Инвентарь:");
                    player.getInventory().stream()
                            .collect(Collectors.groupingBy(Item::getClass))
                            .forEach((cls, items) -> {
                                String className = cls.getSimpleName();
                                Map<String, Long> itemCounts = items.stream()
                                        .collect(Collectors.groupingBy(Item::getName, Collectors.counting()));
                                StringBuilder itemNames = new StringBuilder();
                                for (Map.Entry<String, Long> entry : itemCounts.entrySet()) {
                                    if (itemNames.length() > 0) itemNames.append(", ");
                                    itemNames.append(entry.getKey());
                                }
                                int totalCount = items.size();
                                System.out.println(className + "(" + totalCount + "): " + itemNames);
                            });
                }
            } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());;
            }
        });
        commands.put("use", (ctx, a) -> {
            try {
                if (a.isEmpty()) throw new InvalidCommandException("Укажите название предмета для использования.");
                String itemName = String.join(" ",a);;
                Player player = ctx.getPlayer();
                Item item = player.getInventory().stream()
                        .filter(i -> i.getName().equalsIgnoreCase(itemName))
                        .findFirst()
                        .orElse(null);
                if (item == null)
                    throw new InvalidCommandException("Предмет '" + itemName + "' не найден в инвентаре.");
                item.apply(ctx);
                player.getInventory().remove(item);
            } catch (InvalidCommandException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        });
        commands.put("fight", (ctx, a) -> {
            try {
                Room currentRoom = ctx.getCurrent();
                Monster monster = currentRoom.getMonster();
                if (monster == null) throw new InvalidCommandException("В этой комнате нет монстра.");
                Player player = ctx.getPlayer();
                // Атака игрока
                int playerDamage = player.getAttack();
                monster.setHp(monster.getHp() - playerDamage);
                System.out.println("Вы бьете " + monster.getName() + " на " + playerDamage +". HP монстра: " + monster.getHp());;

                // Проверка смерти монстра
                if (monster.getHp() <= 0) {
                    System.out.println("УРААА!!! Вы победили монстра!");
                    ctx.addScore(monster.getLevel()); // Награда за победу (кол-во очков=уровню монстра)
                    System.out.println("Вы получаете " + monster.getLevel() + " очков");
                    Weapon loot = new Weapon("Сокрушительный меч", 3*monster.getLevel()); // Лут - оружие с бонусом x3 от уровня монстра
                    player.getInventory().add(loot);
                    System.out.println("Вы получили: " + loot.getName() + " с бонусом " + loot.getBonus());
                    currentRoom.setMonster(null); //убираем монстра из комнаты
                    return;
                }
                // Атака монстра
                player.setHp(player.getHp() - monster.getLevel());
                System.out.println("Монстр отвечает на " + monster.getLevel() +". Ваше HP " + player.getHp());
                // Проверка смерти игрока
                if (player.getHp() <= 0) {
                    System.out.println(player.getName() + " погиб. Игра окончена.");
                    System.exit(0);
                }
            } catch (InvalidCommandException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        });
        commands.put("save", (ctx, a) -> SaveLoad.save(ctx));
        commands.put("load", (ctx, a) -> SaveLoad.load(ctx));
        commands.put("scores", (ctx, a) -> SaveLoad.printScores());
        commands.put("exit", (ctx, a) -> {
            System.out.println("Пока-Пока!");
            System.exit(0);
        });
    }

    private void bootstrapWorld() {
        Player hero = new Player("Hero", 20, 5);
        state.setPlayer(hero);

        Room square = new Room("Площадь", "Каменная площадь с фонтаном.");
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");
        Room cave = new Room("Пещера", "Темно и сыро.");
        Room field = new Room("Поле", "Колышется пшеница. Солнечно и тепло.");
        square.getNeighbors().put("north", forest);
        forest.getNeighbors().put("south", square);
        forest.getNeighbors().put("east", cave);
        forest.getNeighbors().put("west",field);
        cave.getNeighbors().put("west", forest);
        field.getNeighbors().put("west",forest);

        square.getItems().add(new Weapon("Ножичек",2));

        forest.getItems().add(new Potion("Малое зелье", 5));
        forest.setMonster(new Monster("Волк", 2, 8));

        cave.getItems().add(new Weapon("Кувалда",10));

        field.getItems().add(new Potion("Большое зелье", 20));
        field.setMonster(new Monster("Злой хомяк", 10,50));

        state.setCurrent(square);
    }

    public void run() {
        System.out.println("Добро пожаловать в игру DungeonMini(version 1.0.) 'help' — команды.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;
                List<String> parts = Arrays.asList(line.split("\s+"));
                String cmd = parts.getFirst().toLowerCase(Locale.ROOT);
                List<String> args = parts.subList(1, parts.size());
                Command c = commands.get(cmd);
                try {
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args);
                    state.addScore(1);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
    private void printGcStats() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        System.out.println("Информация о памяти:");
        System.out.println("Всего памяти: " + convertSize(totalMemory));
        System.out.println("Свободно: " + convertSize(freeMemory));
        System.out.println("Занято: " + convertSize(usedMemory));
    }

    // Форматируем размеры памяти в удобочитаемый формат
    private String convertSize(long sizeInBytes) {
        float kb = sizeInBytes / 1024F;
        float mb = kb / 1024F;
        float gb = mb / 1024F;

        if (gb > 1) return String.format("%.2f ГБ", gb);
        if (mb > 1) return String.format("%.2f МБ", mb);
        return String.format("%.2f КБ", kb);
    }

}
