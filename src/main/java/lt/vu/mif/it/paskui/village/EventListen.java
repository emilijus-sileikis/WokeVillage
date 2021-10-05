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
import java.util.List;

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
        Main.inv = Bukkit.createInventory(null, InventoryType.BARREL,
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
        Main.inv.setItem(0, item);

        //Copy paste kiek reik

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
        Main.inv.setItem(8, item);
    }

    @EventHandler
    public static void onClick(InventoryClickEvent event) {

        if (!event.getInventory().equals(Main.inv)) {return;}
        if (event.getCurrentItem() == null) {return;}
        if (event.getCurrentItem().getItemMeta() == null) {return;}
        if (event.getCurrentItem().getItemMeta().displayName() == null) {return;}

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == 0) {

            player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Hello! Welcome to Paskui Plugin");
            player.closeInventory();
        }
        if (event.getSlot() == 8) {

            player.closeInventory();
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

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        //PacketReader reader = new PacketReader();
        //reader.uninject(event.getPlayer());

    }
}
