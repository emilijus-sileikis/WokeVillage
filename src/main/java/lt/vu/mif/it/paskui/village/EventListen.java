package lt.vu.mif.it.paskui.village;

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers.getItem;

public class EventListen implements Listener {

    /** PlayerUseUnknownEntityEvent call counter */
    private int pCount = 0;

    @EventHandler
    public void onInteract(PlayerUseUnknownEntityEvent event) {
        pCount += (event.getHand() == EquipmentSlot.OFF_HAND) ? 1 : 0;

        if (pCount < 2) return;

        pCount = 0;

        ServerPlayer npc = NPCManager.npcs.get(event.getEntityId());

        createInv(Main.inv);
        for (ItemStack item : npc.getInventory().getContents()) {
            Main.inv.addItem(CraftItemStack.asBukkitCopy(item));
        }

        event.getPlayer().openInventory(Main.inv);
    }

    public static void createInv(Inventory inv) {
        Main.inv = Bukkit.createInventory(null, InventoryType.HOPPER,
                Component.text("Trading Menu")
                        .decorate(TextDecoration.BOLD)
                        .color(NamedTextColor.AQUA)
        );

        //Greeting Button
        org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(
                Component.text("Hello!").color(NamedTextColor.DARK_GREEN)
                        .decoration(TextDecoration.ITALIC, false)
        );
        //description for Amethyst Shard
        List<Component> Lore = new ArrayList<>();
        Lore.add( Component.text("Click to select").color(NamedTextColor.GRAY) );
        meta.lore(Lore);
        item.setItemMeta(meta);
        Main.inv.setItem(0, item);

        //WoodChopping Button
        item.setType(Material.STONE_AXE);
        ItemMeta metaAxe = item.getItemMeta();
        metaAxe.displayName(Component.text(ChatColor.GOLD + "Wood Gathering")
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false)
        );
        //description for Axe
        List<Component> loreLumberjack = new ArrayList<>();
        loreLumberjack.add( Component.text("Task: 128 Spruce Logs.").color(NamedTextColor.YELLOW) );
        loreLumberjack.add( Component.text("Price: 20 Gold Ingots.").color(NamedTextColor.YELLOW) );
        metaAxe.lore(loreLumberjack);
        item.setItemMeta(metaAxe);
        Main.inv.setItem(1, item);

        //Miner Button
        item.setType(Material.STONE_PICKAXE);
        ItemMeta metaPickaxe = item.getItemMeta();
        metaPickaxe.displayName(Component.text(ChatColor.GOLD + "Mining")
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false)
        );
        //description for Pickaxe
        List<Component> loreMiner = new ArrayList<>();
        loreMiner.add( Component.text("Task: 96 Cobblestone").color(NamedTextColor.YELLOW) );
        loreMiner.add( Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW) );
        metaPickaxe.lore(loreMiner);
        item.setItemMeta(metaPickaxe);
        Main.inv.setItem(2, item);

        //Fisher Button
        item.setType(Material.FISHING_ROD);
        ItemMeta metaFish = item.getItemMeta();
        metaFish.displayName(Component.text(ChatColor.GOLD + "Fishing")
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false)
        );
        //description for Fishing Rod
        List<Component> loreFish = new ArrayList<>();
        loreFish.add( Component.text("Task: 64 Fish").color(NamedTextColor.YELLOW) );
        loreFish.add( Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW) );
        metaFish.lore(loreFish);
        item.setItemMeta(metaFish);
        Main.inv.setItem(3, item);

        //close button
        item.setType(Material.BARRIER);
        meta.displayName(Component.text("Close")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false)
        );
        Lore.clear();
        meta.lore(Lore);
        item.setItemMeta(meta);
        Main.inv.setItem(4, item);

        //placeholders
        item.setType(Material.WHITE_STAINED_GLASS_PANE);
        meta.displayName(Component.text(""));
        Lore.clear();
        meta.lore();
        item.setItemMeta(meta);
        //Main.inv.setItem(4, item);
        //Main.inv.setItem(0, item);
    }

    @EventHandler
    public static void onClick(InventoryClickEvent event) {

        if (!event.getInventory().equals(Main.inv)) {return;}
        if (event.getCurrentItem() == null) {return;}
        if (event.getCurrentItem().getItemMeta() == null) {return;}
        if (event.getCurrentItem().getItemMeta().displayName() == null) {return;}

        if (event.getWhoClicked() instanceof Player){

            Player p = (Player) event.getWhoClicked();
            org.bukkit.inventory.@Nullable ItemStack is = event.getCurrentItem();

            if (is == null || is.getType() == Material.AIR || is.getType() == null) { event.setCancelled(true); }

            if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) { event.setCancelled(true); }

            if (event.getClick().isKeyboardClick()) { event.setCancelled(true); }

            //Amethyst Shart
            if (event.getSlot() == 0) {

                p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Welcome to WokeVillage!");
                p.closeInventory();
            }

            //Axe
            if (event.getSlot() == 1) {
                ItemStack item = new ItemStack(getItem(Material.GOLD_INGOT));
                ItemMeta meta = item.asBukkitCopy().getItemMeta();
                item.asBukkitCopy().setItemMeta(meta);
                if (p.getInventory().contains(Material.GOLD_INGOT, 20))
                {
                    //payment
                    Location loc = p.getLocation();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playNote(loc,  Instrument.BANJO, Note.sharp(2, Note.Tone.F));
                    }
                    removeItems(p.getInventory(), Material.GOLD_INGOT, 20);
                    p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "You have bought lumberjack services!");
                    //receiving goods
                    for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spawnParticle(Particle.CRIT_MAGIC, loc,100);
                    }
                    receiveItems(p.getInventory(), Material.SPRUCE_LOG, 128);
                    p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "Your Spruce Logs have been delivered!");

                }
                else {
                    p.sendMessage(ChatColor.RED + "You lack the required resources.");
                }
                p.closeInventory();
            }

            //Pickaxe
            if (event.getSlot() == 2) {
                ItemStack item = new ItemStack(getItem(Material.GOLD_INGOT));
                ItemMeta meta = item.asBukkitCopy().getItemMeta();
                item.asBukkitCopy().setItemMeta(meta);
                if (p.getInventory().contains(Material.GOLD_INGOT, 10))
                {
                    //payment
                    Location loc = p.getLocation();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playNote(loc, Instrument.BANJO, Note.sharp(2, Note.Tone.F));
                    }
                    removeItems(p.getInventory(), Material.GOLD_INGOT, 10);
                    p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "You have bought mining services!");
                    //receiving goods
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
                    }
                    receiveItems(p.getInventory(), Material.COBBLESTONE, 96);
                    p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "Your Stone have been delivered!");
                }
                else {
                    p.sendMessage(ChatColor.RED + "You lack the required resources.");
                }
                p.closeInventory();
            }

            //Fishing Rod
            if (event.getSlot() == 3) {
                ItemStack item = new ItemStack(getItem(Material.GOLD_INGOT));
                ItemMeta meta = item.asBukkitCopy().getItemMeta();
                item.asBukkitCopy().setItemMeta(meta);
                if (p.getInventory().contains(Material.GOLD_INGOT, 10))
                {
                    //payment
                    Location loc = p.getLocation();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playNote(loc, Instrument.BANJO, Note.sharp(2, Note.Tone.F));
                    }
                    removeItems(p.getInventory(), Material.GOLD_INGOT, 10);
                    p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "You have bought fishing services!");
                    //receiving goods
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
                    }
                    receiveItems(p.getInventory(), Material.COD, 64);
                    p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "Your Fish have been delivered!");
                }
                else {
                    p.sendMessage(ChatColor.RED + "You lack the required resources.");
                }
                p.closeInventory();
            }

            if (event.getSlot() == 4) {

                p.closeInventory();
            }

            event.setCancelled(true);
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        PacketReader reader = new PacketReader(event.getPlayer());
        reader.inject();

        if (NPCManager.getNPCs() == null)
            return;
        if (NPCManager.getNPCs().isEmpty())
            return;

        NPCManager.addJoinPacket(event.getPlayer());
    }

    private static int receiveItems(Inventory inventory, Material type, int amount) {

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

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        //PacketReader reader = new PacketReader();
        //reader.uninject(event.getPlayer());

    }
}
