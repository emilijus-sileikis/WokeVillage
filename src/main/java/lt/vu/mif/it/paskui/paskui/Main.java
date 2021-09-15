package lt.vu.mif.it.paskui.paskui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {
    public Inventory inv;

    @Override
    public void onEnable() {
        super.onEnable();
        this.getServer().getPluginManager().registerEvents(this,this);
        createInv();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, String label, String[] args) {
	
		if (label.equalsIgnoreCase("gui")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.openInventory(inv);
                return true;
            }
            else {
                sender.sendMessage("GUI does not work on the console");
            }
        }

        return false;
    }

    @EventHandler
    public  void onClick(InventoryClickEvent event) {

        if (!event.getInventory().equals(inv))
            return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        if (event.getCurrentItem().getItemMeta().displayName() == null) return;

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

    public void createInv() {

		inv = Bukkit.createInventory(null, InventoryType.BARREL, Component.text(ChatColor.AQUA + "" + ChatColor.BOLD + "Configuration Menu"));
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(ChatColor.DARK_GREEN + "Hello!"));
        List<Component> Lore = new ArrayList<>();
        Lore.add(Component.text(ChatColor.GRAY + "Click to select"));
        meta.lore(Lore);
        item.setItemMeta(meta);
        inv.setItem(0, item);

        //Copy paste kiek reik

        //close button
        item.setType(Material.BARRIER);
        meta.displayName(Component.text(ChatColor.RED + "" + ChatColor.BOLD + "Close"));
        Lore.clear();
        meta.lore(Lore);
        item.setItemMeta(meta);
        inv.setItem(8, item);

    }
}
