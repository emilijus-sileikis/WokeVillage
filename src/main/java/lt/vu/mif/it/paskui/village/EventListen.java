package lt.vu.mif.it.paskui.village;

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

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

        ServerPlayer npc = NPCManager.npcs.get(event.getEntityId());

        createInv(Main.inv);
        for (ItemStack item : npc.getInventory().getContents()) {
            Main.inv.addItem(CraftItemStack.asBukkitCopy(item));
        }

        event.getPlayer().openInventory(Main.inv);
    }

    public static void createInv(Inventory inv) {
        Main.inv = Bukkit.createInventory(null, InventoryType.HOPPER,
                Component.text("Configuration Menu")
                        .decorate(TextDecoration.BOLD)
                        .color(NamedTextColor.AQUA)
        );

        org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(
                Component.text("Hello!").color(NamedTextColor.DARK_GREEN)
                        .decoration(TextDecoration.ITALIC, false)
        );
        List<Component> Lore = new ArrayList<>();
        Lore.add( Component.text("Click to select").color(NamedTextColor.GRAY) );
        meta.lore(Lore);
        item.setItemMeta(meta);
        Main.inv.setItem(1, item);

        //WoodChopping Button
        item.setType(Material.STONE_AXE);
        meta.displayName(Component.text("Wood Gathering")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false)
        );
        Lore.clear();
        meta.lore(Lore);
        item.setItemMeta(meta);
        Main.inv.setItem(2, item);

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
        Main.inv.setItem(3, item);

        item.setType(Material.WHITE_STAINED_GLASS_PANE);
        meta.displayName(Component.text(""));
        Lore.clear();
        meta.lore();
        item.setItemMeta(meta);
        Main.inv.setItem(4, item);
        Main.inv.setItem(0, item);
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

            if (event.getSlot() == 1) {

                p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Hello!");
                p.closeInventory();
            }

            if (event.getSlot() == 2) {
                ItemStack item = new ItemStack(getItem(Material.GOLD_INGOT));
                ItemMeta meta = item.asBukkitCopy().getItemMeta();
                item.asBukkitCopy().setItemMeta(meta);         // <--- needs checking kurie iš šitų lines būtini
                if (p.getInventory().contains(Material.GOLD_INGOT))
                {
                    //p.getInventory().remove(Material.GOLD_INGOT);
                    //p.getInventory().removeItem(item);
                    removeItems(p.getInventory(), Material.GOLD_INGOT, 20);
                    p.updateInventory();
                    p.sendMessage(ChatColor.GREEN + "You have bought lumberjack services!");
                }
                p.closeInventory();
            }

            if (event.getSlot() == 3) {

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

    @EventHandler
    public static int removeItems(Inventory inventory, Material type, int amount) {

        if(type == null || inventory == null)
            return -1;
        if (amount <= 0)
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
