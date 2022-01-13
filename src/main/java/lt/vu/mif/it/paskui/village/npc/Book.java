package lt.vu.mif.it.paskui.village.npc;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;

public class Book {
    public void createBook(Player player) {
        String nl = "\n";
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookmeta = (BookMeta) book.getItemMeta();
        bookmeta.setAuthor("Paskui Solutions");
        bookmeta.setTitle("Plugin Guide");
        ArrayList<Component> pages = new ArrayList<>();
        pages.add(Component.text("Welcome to WokeVillage plugin guide book!" + nl + nl + "Our NPCs are here to help you whenever you need them." + nl + nl + "There are 3 variations of roles and 6 types of personalities.")); //1st page
        pages.add(Component.text("Our NPCs can be spawned as Lumberjack, Miner or Fisher." + nl + nl + "They will have their own personality as well, for instance: " + nl + nl + "hardworking, lazy, reliable, clumsy, generous or even greedy."));
        pages.add(Component.text("They are quite rare mobs, so if you are attempting to find one, you will need:" + nl + "1 splash potion of weakness." + nl + "1 golden apple." + nl + nl + "Step 1: Find a zombie villager."));
        pages.add(Component.text("Step 2: Throw a potion of weakness at it and right click it with a golden apple." + nl + nl + "Step 3: Wait for a few minutes and you will have a 25% chance of getting our NPC (if the zombie villager is not nitwit type)."));
        pages.add(Component.text("For those zombie villagers who are nitwit type, the percentage of becoming our NPC is 100% (since they are mostly useless for a player)."));
        pages.add(Component.text("If you are a server admin, things are much easier for you." + nl + nl + "To create a random npc, simply type in a command /npc create." + nl + nl + "To remove that NPC you must type /npc remove."));
        pages.add(Component.text("To remove all the NPCs that are in your current world just type /npc removeAll." + nl + nl + "To create a specific one, add flags to your command -p (personality), -r (role) (e.g /npc create -r fisher -p greedy)."));
        pages.add(Component.text("To spawn an NPC with a custom name you must type /npc create -n John." + nl + nl + "To spawn an NPC in a specific location you can use a flag -l and enter preferred coordinates (e.g /npc create -l  X Y Z)."));
        pages.add(Component.text("Lastly, there are few more interesting facts about our traders that you must know:" + nl + nl + "NPCs are usable only in the normal world (you can not spawn them in nether or the end)."));
        pages.add(Component.text("Reliable traders have the lowest chance to lose your order." + nl + nl + "Clumsy traders are more likely to lose your order." + nl + nl + "Greedy traders have higher prices."));
        pages.add(Component.text("Generous traders have lower prices." + nl + nl + "Hardworking traders are the fastest at completing their job." + nl + nl + "Lazy traders are the slowest at completing their job."));
        pages.add(Component.text("You can only interact with an NPC 5 times per Minecraft day (Right clicking on it counts as an interaction and one Minecraft day is about 20 minutes)." + nl + nl + "Keep in mind that sleeping will not affect the NPC cooldown!"));
        pages.add(Component.text("For more information and more in - depth stuff, please visit our git and read through our documentation or contact us.")); //13th page

        bookmeta.pages(pages);
        book.setItemMeta(bookmeta);
        if (!(player.getInventory().contains(book))) {
            player.getInventory().addItem(book);
        }
        else player.sendMessage("You can only have one information book!");
    }
}