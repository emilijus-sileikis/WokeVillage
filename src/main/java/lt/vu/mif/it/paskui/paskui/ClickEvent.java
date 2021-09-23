package lt.vu.mif.it.paskui.paskui;

import net.kyori.adventure.text.Component;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClickEvent implements Listener {

    @EventHandler
    public void onInteract(NPCEvent event) {
        //if (event.getNpc().getInventory().isEmpty())
        //return;

        createInv(Main.inv);
        for (ItemStack item : event.getNpc().getInventory().getContents())
            Main.inv.addItem(CraftItemStack.asBukkitCopy(item));

        event.getPlayer().openInventory(Main.inv);

        /*for (Player on : Bukkit.getOnlinePlayers()) {
            PlayerConnection p = ((CraftPlayer)on).getHandle().b;
            p.sendPacket(new PacketPlayOutEntityDestroy(event.getNpc().getId()));
        }*/
        //NPCManager.npcs.remove(event.getNpc().getId());
    }

    public static void createInv(Inventory inv) {

        Main.inv = Bukkit.createInventory(null, InventoryType.BARREL, Component.text(ChatColor.AQUA + "" + ChatColor.BOLD + "Configuration Menu"));
        org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(ChatColor.DARK_GREEN + "Hello!"));
        List<Component> Lore = new ArrayList<>();
        Lore.add(Component.text(ChatColor.GRAY + "Click to select"));
        meta.lore(Lore);
        item.setItemMeta(meta);
        Main.inv.setItem(0, item);

        //Copy paste kiek reik

        //close button
        item.setType(Material.BARRIER);
        meta.displayName(Component.text(ChatColor.RED + "" + ChatColor.BOLD + "Close"));
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
}
