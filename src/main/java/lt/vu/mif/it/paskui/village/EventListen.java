package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import lt.vu.mif.it.paskui.village.npc.events.NPCInteractEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.world.item.ItemStack;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers.getItem;

public class EventListen implements Listener {

    private final NPCManager npcManager;

    public EventListen(NPCManager npcManager) {
        this.npcManager = npcManager;
    }

    // EventHandlers
    @EventHandler
    public void onInteract(NPCInteractEvent event) {

        Player player = event.getPlayer();
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
                //TODO: or add something here to reset the speed (and everywhere where the inventory is closed i guess)
            }

            //Lumberjack
            else if (event.getCurrentItem().getType() == Material.STONE_AXE) {
                Material spruce = Material.SPRUCE_LOG;
                processTrade(event, p, 20, 128, spruce);
            }

            //Miner
            else if (event.getCurrentItem().getType() == Material.STONE_PICKAXE) {
                Material cobble = Material.COBBLESTONE;
                processTrade(event, p, 15, 96, cobble);
            }

            //Fisher
            else if (event.getCurrentItem().getType() == Material.FISHING_ROD) {
                Material cod = Material.COD;
                processTrade(event, p, 10, 64, cod);
            }

            else if (event.getCurrentItem().getType() == Material.BARRIER) {
                p.sendMessage("Inventory closed!");
                p.closeInventory();
            }
        }
    }
    //TODO: gal kazkas tokio kad istrint data, kai npc mirsta?
/*    @EventHandler
    public void onEDeath(EntityDeathEvent event) {
        //NPC npc = new NPC("", npcManager.getNPCs().get(0).getLoc());
        if (event.getEntity().getKiller() != null && event.getEntity() instanceof NPC) {
            Player player = event.getEntity().getKiller();
            Bukkit.broadcast(Component.text("NPC was killed by " + player.getName()));
        }
    }

 */
    @EventHandler
    public static void onMove(InventoryMoveItemEvent event) {
        SelectionScreen gui = new SelectionScreen(null, null);
        if (event.getInitiator().equals(gui)) {
            event.setCancelled(true);
        }
    }

    private static void processTrade(InventoryClickEvent event, Player p, int cost, int goods, Material material){

        ItemStack itemReceived = new ItemStack(getItem(material));
        if (p.getInventory().contains(Material.GOLD_INGOT, cost))
        {
            int failureChance = 5; //future functionality for failure
            int timeElapsed = 500; //future functionality for time elapsed while gathering

            Personality personality = Personality.GENEROUS;
            //personality check
            switch(personality)
            {
                case HARDWORKING:
                    timeElapsed -= random_int(0, 240);
                    break;
                case LAZY:
                    timeElapsed += random_int(0, 240);
                    break;
                case RELIABLE:
                    failureChance -= random_int(0, 5);
                    break;
                case CLUMSY:
                    failureChance += random_int(0, 15);
                    break;
                case GENEROUS:
                    cost *= random_double(0.5, 0.9);
                    break;
                case GREEDY:
                    cost *= random_double(1, 2);
                    break;
                default:
                    p.sendMessage(ChatColor.RED + "Plugin ERROR: processTrade");
                    break;
            }
            //payment
            Location loc = p.getLocation();
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playNote(loc, Instrument.BANJO, Note.sharp(2, Note.Tone.F));
            }
            removeItems(p.getInventory(), Material.GOLD_INGOT, cost);
            p.updateInventory();
            p.sendMessage(ChatColor.GREEN + "You have bought villagers services!");


            //TODO: insert here init to pathfind the resources(possible through CustomVillager)
            // but, use 'timeElapsed' to force NPC to comeback to player


            //failure check
            if(random_int(0, 100) < failureChance)
            {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
                }
                p.sendMessage(ChatColor.RED + "Your items have been lost! The trader suffered an accident...");
            }
            else
            {
                //receiving goods
                for(int i=0; i<goods; i++) {
                    switch(p.getInventory().firstEmpty()) {
                        case -1:
                            p.getWorld().dropItemNaturally(loc, itemReceived.asBukkitCopy());
                            break;
                        default:
                            //items are added 1 by 1 to avoid duping
                            receiveItems(p.getInventory(), material, 1);
                            p.updateInventory();
                            break;
                    }
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
                }
                p.sendMessage(ChatColor.GREEN + "Your items have been delivered!");
            }

        }
        else {
            p.sendMessage(ChatColor.RED + "You lack the required resources.");
        }
        p.closeInventory();

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

    public static double random_double(double Min, double Max) {
        return (ThreadLocalRandom.current().nextDouble() * (Max - Min)) + Min;
    }
    public static int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }


}
