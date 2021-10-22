package lt.vu.mif.it.paskui.village;

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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

        Player player = event.getPlayer();
        SelectionScreen gui = new SelectionScreen();
        player.openInventory(gui.getInventory());
    }

    @EventHandler
    public static void onClick(InventoryClickEvent event) {

        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getHolder() instanceof SelectionScreen) {

            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();

            if (event.getCurrentItem() == null) {
                return;
            }

            if (event.getCurrentItem().getType() == Material.BOOK) {
                p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Welcome to WokeVillage plugin helper!");
                p.sendMessage(ChatColor.GOLD + "Woke Villagers are here to trade and help you gather large amounts of resources in a relatively short time. In the NPC trading menu, you can see various gathering tools, which when hovered over, display trade offers and details. ");
                p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "TASK - " + ChatColor.RESET +"displays offered items.");
                p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "PRICE - " + ChatColor.RESET +"displays resources needed to pay for the service.");
                p.closeInventory();
            }

            //Lumberjack
            else if (event.getCurrentItem().getType() == Material.STONE_AXE) {
                ItemStack item = new ItemStack(getItem(Material.GOLD_INGOT));
                ItemStack sprucelogs_item = new ItemStack(getItem(Material.SPRUCE_LOG));
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
                    for(int spruce_logs=0; spruce_logs<128; spruce_logs++) {
                        switch(p.getInventory().firstEmpty()) {
                            case -1:
                                p.getWorld().dropItemNaturally(loc, sprucelogs_item.asBukkitCopy());
                                break;
                            default:
                                //items are added 1 by 1 to avoid duping
                                receiveItems(p.getInventory(), Material.SPRUCE_LOG, 1);
                                p.updateInventory();
                                break;
                        }
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.spawnParticle(Particle.CRIT_MAGIC, loc,100);
                    }
                    //receiveItems(p.getInventory(), Material.SPRUCE_LOG, 128);
                    //p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "Your Spruce Logs have been delivered!");

                }
                else {
                    p.sendMessage(ChatColor.RED + "You lack the required resources.");
                }
                p.closeInventory();
            }

            //Miner
            else if (event.getCurrentItem().getType() == Material.STONE_PICKAXE) {
                ItemStack item = new ItemStack(getItem(Material.GOLD_INGOT));
                ItemStack cobblestone_item = new ItemStack(getItem(Material.COBBLESTONE));
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
                    for(int cobblestone=0; cobblestone<96; cobblestone++) {
                        switch(p.getInventory().firstEmpty()) {
                            case -1:
                                p.getWorld().dropItemNaturally(loc, cobblestone_item.asBukkitCopy());
                                break;
                            default:
                                //items are added 1 by 1 to avoid duping
                                receiveItems(p.getInventory(), Material.COBBLESTONE, 1);
                                p.updateInventory();
                                break;
                        }
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
                    }
                    //receiveItems(p.getInventory(), Material.COBBLESTONE, 96);
                    //p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "Your Stone have been delivered!");
                }
                else {
                    p.sendMessage(ChatColor.RED + "You lack the required resources.");
                }
                p.closeInventory();
            }

            //Fisher
            else if (event.getCurrentItem().getType() == Material.FISHING_ROD) {
                ItemStack item = new ItemStack(getItem(Material.GOLD_INGOT));
                ItemStack cod_item = new ItemStack(getItem(Material.COD));
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
                    for(int cod=0; cod<64; cod++) {
                        switch(p.getInventory().firstEmpty()) {
                            case -1:
                                p.getWorld().dropItemNaturally(loc, cod_item.asBukkitCopy());
                                break;
                            default:
                                //items are added 1 by 1 to avoid duping
                                receiveItems(p.getInventory(), Material.COD, 1);
                                p.updateInventory();
                                break;
                        }
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
                    }
                    //receiveItems(p.getInventory(), Material.COD, 64);
                    //p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "Your Fish have been delivered!");
                }
                else {
                    p.sendMessage(ChatColor.RED + "You lack the required resources.");
                }
                p.closeInventory();
            }

            else if (event.getCurrentItem().getType() == Material.BARRIER) {
                p.sendMessage("Inventory closed!");
                p.closeInventory();
            }
        }
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
