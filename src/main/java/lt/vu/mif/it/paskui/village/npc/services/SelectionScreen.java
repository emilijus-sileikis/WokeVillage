package lt.vu.mif.it.paskui.village.npc.services;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.states.Failure;
import lt.vu.mif.it.paskui.village.npc.states.ReceiveGoods;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
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
import java.util.concurrent.ThreadLocalRandom;

public class SelectionScreen implements InventoryHolder {

    protected static final Material REQUIRED_RESOURCE = Material.GOLD_INGOT;
    protected static final String REQUIRED_RESOURCE_STR = "Gold ingots";

    public final NPC npc;
    public final Inventory inv;
    protected int[] prices;

    public SelectionScreen(final @NotNull NPC npc, final int[] prices) {
        this.npc = npc;
        this.inv = Bukkit.createInventory(this,
                InventoryType.HOPPER,
                Component.text(
                        String.format("%s %s",
                                getRole().toStringWithCapInitial(),
                                getPersonality()
                        )).decorate(TextDecoration.BOLD)
                        .color(NamedTextColor.RED)
        );
        this.prices = prices;

        init(getRole(), getPersonality());
    }

    private SelectionScreen(final @NotNull NPC npc) {
        this(npc, new int[]{0, 0, 0});
    }

    // getters
    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public final Role getRole() {
        return npc.role;
    }

    public final Personality getPersonality() {
        return npc.personality;
    }

    // public
    /**
     * Modifies service prices based on NPC personality.
     */
    protected void modifyPrices() {
        switch (getPersonality()) {
            case GREEDY -> {
                for(int i = 0; i < prices.length; i++) {
                    prices[i] *= randomDouble(1, 2);
                }
            }
            case GENEROUS -> {
                for(int i = 0; i < prices.length; i++) {
                    prices[i] *= randomDouble(0.5, 0.9);
                }
            }
        }
    }

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
        if (!p.getInventory().contains(REQUIRED_RESOURCE, cost)) {
            p.sendMessage(Component.text("You lack the required resources.").color(NamedTextColor.RED));
            p.closeInventory();
            return;
        }

        int failChance = 5 + getPersonality().getFailChanceMod(); //future functionality for failure
        int workDuration = 500 + getPersonality().getWorkDurationMod(); //future functionality for time elapsed while gathering

        //payment
        Location loc = p.getLocation();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playNote(loc, Instrument.BANJO, Note.sharp(2, Note.Tone.F));
        }
        removeItems(p.getInventory(), REQUIRED_RESOURCE, cost);
        p.updateInventory();
        p.sendMessage(Component.text("You have bought villagers services!").color(NamedTextColor.GREEN));

        workDuration = 40; //Delete this after testing
        //Double dist = this.npc.distanceTo(material);
        this.npc.moveTo(workDuration, material);

        //failure check
        //long delay = (timeElapsed * 20L) + (dist.longValue() * 40);
        if(randomInt(0, 100) < failChance) {
            new Failure(npc, loc, p).runTaskLater(workDuration * 20L);
        } else {
            new ReceiveGoods(this.npc, loc, p, material, goods).runTaskTimer((workDuration * 20L) + 80L, 20L);
        }
        p.closeInventory();
    }

    public static int receiveItems(Inventory inventory, Material type, int amount) {
        if(type == null || inventory == null)
            return -1;

        HashMap<Integer, ItemStack> retVal = inventory.addItem(new ItemStack(type,amount));

        int granted = 0;
        for(ItemStack item: retVal.values()) {
            granted+=item.getAmount();
        }
        return granted;
    }

    public static int removeItems(final @NotNull Inventory inventory,
                                  final @NotNull Material type,
                                  final int amount) {
        HashMap<Integer, ItemStack> retVal = inventory.removeItem(new ItemStack(type, amount));

        int notRemoved = 0;
        for(ItemStack item: retVal.values()) {
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
            Constructor<? extends SelectionScreen> cns = npc.role.clazz.getConstructor(npc.getClass());
            return cns.newInstance(npc);
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e
        ) {
            e.printStackTrace();
        }

        return new SelectionScreen(npc);
    }
}
