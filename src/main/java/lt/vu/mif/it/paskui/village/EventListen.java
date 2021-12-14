package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.npc.events.NPCDeathEvent;
import lt.vu.mif.it.paskui.village.npc.services.tables.FisherLootTable;
import lt.vu.mif.it.paskui.village.npc.services.tables.LumberjackLootTable;
import lt.vu.mif.it.paskui.village.npc.services.tables.MinerLootTable;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import lt.vu.mif.it.paskui.village.util.Failure;
import lt.vu.mif.it.paskui.village.util.ReceiveGoods;
import lt.vu.mif.it.paskui.village.util.Teleport;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers.getItem;

public class EventListen implements Listener {

    private final NPCManager npcManager;
    private final DataManager dataManager;

    public EventListen(NPCManager npcManager, DataManager dataManager) {
        this.npcManager = npcManager;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        boolean req = event.getClickedInventory() == null;
        req = req || !(event.getClickedInventory().getHolder() instanceof SelectionScreen);
        req = req || event.getCurrentItem() == null;

        if (req) return;

        req = event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY);
        req = req || event.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD);
        req = req || event.getAction().equals(InventoryAction.HOTBAR_SWAP);

        if (req) {
            event.setCancelled(true);
            return;
        }

        SelectionScreen screen = (SelectionScreen) event.getClickedInventory().getHolder();
        Player p = (Player) event.getWhoClicked();
        int temp;

        switch(event.getCurrentItem().getType()) {
            case BOOK:
                p.sendMessage(Component.text("Welcome to WokeVillage plugin helper!")
                        .color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
                p.sendMessage(Component.text("Woke Villagers are here to trade and " +
                        "help you gather large amounts of resources in a relatively " +
                        "short time. In the NPC trading menu, you can see various " +
                        "gathering tools, which when hovered over, display trade " +
                        "offers and details. ").color(NamedTextColor.GOLD)
                );
                p.sendMessage(Component.text("TASK - ")
                        .color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD)
                        .append(Component.text("displays offered items."))
                );
                p.sendMessage(Component.text("PRICE - ")
                        .color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD)
                        .append(Component.text("displays resources needed to pay for the service."))
                );
                p.closeInventory();
                break;

            //LumberJack
            case STONE_AXE:
                LumberjackLootTable treasureLJ = LumberjackLootTable.fromInt(
                        random_int(
                                0, // Paima tik pirmus 5 LootTable'o elementus
                                5
                        )
                );
                processTrade(screen, p, treasureLJ.getCost(), treasureLJ.getGoods(), treasureLJ.getItem(), treasureLJ.getItem());

                break;
            case APPLE:
                Material apple = Material.APPLE;
                treasureLJ = LumberjackLootTable.fromInt(
                        random_int(
                                1,
                                2
                        )
                );
                processTrade(screen, p, 10, 64, apple, treasureLJ.getItem());
                break;
            case OAK_SAPLING:
                treasureLJ = LumberjackLootTable.fromInt(
                        random_int(
                                5, // Paima likusius 5 LootTable'o elementus
                                10
                        )
                );
                processTrade(screen, p, treasureLJ.getCost(), treasureLJ.getGoods(), treasureLJ.getItem(), treasureLJ.getItem());
                break;
            //Miner
            case STONE_PICKAXE:
                MinerLootTable treasureM = MinerLootTable.fromInt(0);
                MinerLootTable stone = MinerLootTable.fromInt(4);
                processTrade(screen, p, treasureM.getCost(), treasureM.getGoods(), treasureM.getItem(), stone.getItem());
                break;
            case IRON_PICKAXE:
                treasureM = MinerLootTable.fromInt(1);
                processTrade(screen, p, treasureM.getCost(), treasureM.getGoods(), treasureM.getItem(), treasureM.getItem());
                break;
            case WOODEN_PICKAXE:
                MinerLootTable coal = MinerLootTable.fromInt(3);
                treasureM = MinerLootTable.fromInt(2);
                processTrade(screen, p, treasureM.getCost(), treasureM.getGoods(), treasureM.getItem(), coal.getItem());
                break;
            //Fisher
            case FISHING_ROD:
                FisherLootTable fisher = FisherLootTable.fromInt(0);
                FisherLootTable treasureF = FisherLootTable.fromInt(
                        random_int(
                                1,
                                4
                        )
                );
                processTrade(screen, p, treasureF.getCost(), treasureF.getGoods(), treasureF.getItem(), fisher.getItem());
                break;
            case ENCHANTED_BOOK:
                fisher = FisherLootTable.fromInt(0);
                treasureF = FisherLootTable.fromInt(
                        random_int(
                                1,
                                11
                        )
                );
                processTrade(screen, p, 5, treasureF.getGoods(), treasureF.getItem(), fisher.getItem());
                break;
            case FILLED_MAP:
                temp = random_int(1, 10);
                if (temp >= 3) {
                    fisher = FisherLootTable.fromInt(0);
                    Material treasureFail = Material.GOLD_NUGGET;
                    processTrade(screen, p, 1, 7, treasureFail, fisher.getItem());
                } else {
                    fisher = FisherLootTable.fromInt(0);
                    treasureF = FisherLootTable.fromInt(
                            random_int(
                                    13,
                                    FisherLootTable.values().length
                            )
                    );
                    processTrade(screen, p, treasureF.getCost(), treasureF.getGoods(), treasureF.getItem(), fisher.getItem());
                }
                break;
            //Close
            case BARRIER:
                p.sendMessage(Component.text("Inventory closed!"));
                p.closeInventory();
                break;
            default:
                p.sendMessage(Component.text("Plugin ERROR: OnClick")
                        .color(NamedTextColor.RED));
                break;
        }
    }

    @EventHandler
    public void onNPCDeath(NPCDeathEvent event) {
        if (event.getReason().equals(RemovalReason.KILLED)) {
            npcManager.deleteNPC(event.getNpc());
            dataManager.getConfig().set("data." + event.getNpc().getId(), null);
            dataManager.saveConfig();
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof SelectionScreen) {
            SelectionScreen screen = (SelectionScreen) event.getInventory().getHolder();
            screen.getNPC().stopTrade();
            screen.getNPC().setKillable();
        }
    }

    private static void processTrade(SelectionScreen screen, Player p, int cost, int goods, Material material, Material goTo){
        ItemStack itemReceived = new ItemStack(getItem(material));
        if (p.getInventory().contains(Material.GOLD_INGOT, cost)) {
            int failureChance = 5; //future functionality for failure
            int timeElapsed = 500; //future functionality for time elapsed while gathering

            //personality check
            switch (screen.getPersonality()) {
                case HARDWORKING -> timeElapsed -= random_int(0, 240);
                case LAZY        -> timeElapsed += random_int(0, 240);
                case RELIABLE    -> failureChance -= random_int(0, 5);
                case CLUMSY      -> failureChance += random_int(0, 15);
                case GENEROUS    -> cost *= random_double(0.5, 0.9);
                case GREEDY      -> cost *= random_double(1, 2);
                default          -> p.sendMessage(Component.text("Plugin ERROR: processTrade")
                        .color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
            }
            //payment
            Location loc = p.getLocation();
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playNote(loc, Instrument.BANJO, Note.sharp(2, Note.Tone.F));
            }
            removeItems(p.getInventory(), Material.GOLD_INGOT, cost);
            p.updateInventory();
            p.sendMessage(Component.text("You have bought villagers services!").color(NamedTextColor.GREEN));

                timeElapsed = 50; //Delete this after testing
                //Double dist = screen.getNPC().distanceTo(goTo);
                screen.getNPC().moveTo(timeElapsed, goTo);

            final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            int finalTimeElapsed = timeElapsed;
            final Runnable runnable = new Runnable() {
                int countdownStarter = finalTimeElapsed + 20;

                public void run() {

                    Bukkit.broadcast(Component.text(countdownStarter));
                    countdownStarter--;

                    if (screen.getNPC().getEntity().isDead()) {
                        p.sendMessage(Component.text("The NPC has died..."));
                        scheduler.shutdown();
                    }

                    if (countdownStarter < 0) {
                        new ReceiveGoods(screen.getNPC(), loc, p, material, itemReceived, goods).runTaskLaterAsynchronously(Main.getInstance(), 20);
                        scheduler.shutdown();
                    }
                }
            };
            scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);

            //failure check
            if(random_int(0, 100) < failureChance) {
                new Failure(p, scheduler, loc, screen.getNPC()).runTaskLater(Main.getInstance(), timeElapsed * 20L);
            }
        } else {
            p.sendMessage(Component.text("You lack the required resources.").color(NamedTextColor.RED));
        }
        p.closeInventory();
    }

    public static int receiveItems(Inventory inventory, Material type, int amount) {
        if(type == null || inventory == null)
            return -1;

        HashMap<Integer, org.bukkit.inventory.ItemStack> retVal = inventory.addItem(new org.bukkit.inventory.ItemStack(type,amount));

        int granted = 0;
        for(org.bukkit.inventory.ItemStack item: retVal.values()) {
            granted+=item.getAmount();
        }
        return granted;
    }

    public static int removeItems(Inventory inventory, Material type, int amount) {
        if(type == null || inventory == null)
            return -1;
        if (amount <= 0 )
            return -1;

        if (amount == Integer.MAX_VALUE) {
            inventory.remove(type);
            return 0;
        }

        HashMap<Integer, org.bukkit.inventory.ItemStack> retVal = inventory.removeItem(new org.bukkit.inventory.ItemStack(type,amount));

        int notRemoved = 0;
        for(org.bukkit.inventory.ItemStack item: retVal.values()) {
            notRemoved+=item.getAmount();
        }
        return notRemoved;
    }

    public static double random_double(double Min, double Max) {
        return (ThreadLocalRandom.current().nextDouble() * (Max - Min)) + Min;
    }

    public static int random_int(int Min, int Max) {
        return (int) (Math.random()*(Max-Min))+Min;
    }
}
