package lt.vu.mif.it.paskui.village.npc.services;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.util.ReceiveGoods;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static lt.vu.mif.it.paskui.village.npc.Personality.CLUMSY;
import static lt.vu.mif.it.paskui.village.npc.Personality.GENEROUS;
import static lt.vu.mif.it.paskui.village.npc.Personality.GREEDY;
import static lt.vu.mif.it.paskui.village.npc.Personality.HARDWORKING;
import static lt.vu.mif.it.paskui.village.npc.Personality.LAZY;
import static lt.vu.mif.it.paskui.village.npc.Personality.RELIABLE;
import static org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers.getItem;

public class SelectionScreen implements InventoryHolder {

    private final NPC npc;
    protected final Inventory inv;
    protected int pricesFisher[];
    protected int pricesMiner[];
    protected int pricesLumberjack[];

    public SelectionScreen(NPC npc) {
        this.npc = npc;
        inv = Bukkit.createInventory(this,
                InventoryType.HOPPER,
                Component.text(String.format("%s %s", getRole().toString().substring(0,1).toUpperCase() + getRole().toString().substring(1), getPersonality()))
                        .decorate(TextDecoration.BOLD)
                        .color(NamedTextColor.RED)
        );
        pricesFisher = new int[3];
        pricesMiner = new int[3];
        pricesLumberjack = new int[3];

        init(npc.getRole(), npc.getPersonality());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    // getters
    public final Role getRole() {
        return npc.getRole();
    }

    public final Personality getPersonality() {
        return npc.getPersonality();
    }

    public final NPC getNPC() {
        return npc;
    }

    // public
    public void processService(Material item, Player player) {
        switch (item) {
            case BOOK -> {
                player.sendMessage(Component.text("Welcome to WokeVillage plugin helper!")
                        .color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
                player.sendMessage(Component.text("Woke Villagers are here to trade and " +
                        "help you gather large amounts of resources in a relatively " +
                        "short time. In the NPC trading menu, you can see various " +
                        "gathering tools, which when hovered over, display trade " +
                        "offers and details. ").color(NamedTextColor.GOLD)
                );
                player.sendMessage(Component.text("TASK - ")
                        .color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD)
                        .append(Component.text("displays offered items."))
                );
                player.sendMessage(Component.text("PRICE - ")
                        .color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD)
                        .append(Component.text("displays resources needed to pay for the service."))
                );
                player.closeInventory();
            }
            case BARRIER -> {
                player.sendMessage(Component.text("Inventory closed!"));
                player.closeInventory();
            }
        }
    }

    // finals
    protected final ItemStack createItem(Component name, Material mat, List<Component> lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    protected final void createAddItem(Component name, Material mat, List<Component> lore) {
        ItemStack item = createItem(name, mat, lore);
        inv.setItem(inv.firstEmpty(), item);
    }

    protected final void addHelpOption() {
        ItemStack item = createItem(
                Component.text("Help").color(NamedTextColor.GREEN),
                Material.BOOK,
                Collections.singletonList(Component.text("Click here for help"))
        );
        inv.setItem(0, item);
    }

    protected final void addCloseOption() {
        ItemStack item = createItem(
                Component.text("Close").color(NamedTextColor.RED),
                Material.BARRIER,
                Collections.singletonList(Component.text("Click to close the menu"))
        );
        inv.setItem(inv.getSize() - 1, item);
    }


    protected final void processTrade(Player p, int cost, int goods, Material material){
        net.minecraft.world.item.ItemStack itemReceived = new net.minecraft.world.item.ItemStack(getItem(material));
        if (p.getInventory().contains(Material.GOLD_INGOT, cost)) {
            int failureChance = 5; //future functionality for failure
            int timeElapsed = 500; //future functionality for time elapsed while gathering

            //personality check
            switch (this.npc.getPersonality()) {
                case HARDWORKING -> timeElapsed -= randomInt(0, 240);
                case LAZY        -> timeElapsed += randomInt(0, 240);
                case RELIABLE    -> failureChance -= randomInt(0, 5);
                case CLUMSY      -> failureChance += randomInt(0, 15);
                case GENEROUS    -> cost *= randomDouble(0.5, 0.9);
                case GREEDY      -> cost *= randomDouble(1, 2);
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

            timeElapsed = 20; //Delete this after testing
            Double dist = this.npc.distanceTo(material);
            this.npc.moveTo(timeElapsed, material);

            //failure check
            if(randomInt(0, 100) < failureChance) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
                }
                p.sendMessage(Component.text("Your items have been lost! The trader suffered an accident...") //vis tiek duoda items
                        .color(NamedTextColor.RED));
            } else {
                new ReceiveGoods(this.npc, loc, p, material, itemReceived, goods)
                        .runTaskLater(
                                Main.getInstance(),
                                (timeElapsed * 20L) + (dist.longValue() * 40)
                        );
            }
        } else {
            p.sendMessage(Component.text("You lack the required resources.").color(NamedTextColor.RED));
        }
        p.closeInventory();
    }

    public static int receiveItems(Inventory inventory, Material type, int amount) {
        if(type == null || inventory == null)
            return -1;

        HashMap<Integer, ItemStack> retVal = inventory.addItem(new org.bukkit.inventory.ItemStack(type,amount));

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

    // other
    protected void init(Role role, Personality personality) {
        this.addHelpOption();
        this.addCloseOption();
    }

    // static
    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public static double randomDouble(double min, double max) {
        return (ThreadLocalRandom.current().nextDouble() * (max - min)) + min;
    }

    public static SelectionScreen createScreen(NPC npc) {
        try {
            Constructor<? extends SelectionScreen> cns = npc.getRole().getClazz().getConstructor(npc.getClass());
            return cns.newInstance(npc);
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e
        ) {
            e.printStackTrace();
        }

        return new SelectionScreen(npc);
    }
}
