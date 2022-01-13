package lt.vu.mif.it.paskui.village.npc;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;

public class Book {
    //TODO: Add required info, add some colors.
    public void createBook(Player player) {
        String nl = "\n";
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookmeta = (BookMeta) book.getItemMeta();
        bookmeta.setAuthor("Paskui Solutions");
        bookmeta.setTitle("Plugin guide");
        ArrayList<Component> pages = new ArrayList<>();
        pages.add(Component.text("Welcome to WokeVillage plugin guide book!" + nl + nl + "Our traders are here to help you whenever you need them" + nl + nl + "There are 3 variations of roles and 6 types of personalities")); //1st page
        pages.add(Component.text("Our NPC’s can be spawn as Lumberjack, Miner or Fisher" + nl + nl + "They will have their own personality as well, for instance" + nl + nl + "Hardworking, lazy, reliable, clumsy generous or even greedy")); //2nd page
        pages.add(Component.text("They are indeed rare mobs. So, if you are attempting to find one, you will need:" + nl + "1 splash potion of weakness" + nl + "1 golden apple" + nl + nl + "Step 1: Find the zombie villager")); //3rd page
        pages.add(Component.text("Step 2: Throw a potion of weakness and right click on it with golden apple" + nl + nl + "Step 3: Wait for few minutes ant there is 25% chance that you will get a random NPC of ours")); //4th page
        pages.add(Component.text("If you are server admin, things are much easier for you" + nl + nl + "To create a random npc, simply type command /npc create" + nl + nl + "To remove that NPC you must type /npc remove")); //5th page
        pages.add(Component.text("To remove all the NPCs that are in your current world just type /npc removeAll" + nl + nl + "To create a specific one, add flags to your command -p (personality), -r (role) (e.g /npc create -r fisher -p greedy")); //6th page
        pages.add(Component.text("To spawn an NPC with the custom name you must type /npc create -n John" + nl + nl + "To spawn an NPC in specific location you can use flag -l and enter preferred coordinates (e.g /npc create -l  X Y Z)")); //7th page
        pages.add(Component.text("Lastly, there are few more interesting facts about our traders that you’ve got to know:" + nl + nl + "NPCs are usable only in the normal world (They will not be interactable in nether or the end)")); //8th page
        pages.add(Component.text("Reliable traders have the lowest chance to lose your order" + nl + nl + "Clumsy traders are more likely to lose your order" + nl + nl + "Greedy traders have higher prices")); //9th page
        pages.add(Component.text("Generous traders have lower prices" + nl + nl + "Hardworking traders are the fastest at completing their jobs" + nl + nl + "Lazy traders are the slowest at completing their jobs")); //10th page
        pages.add(Component.text("You can only interact with the NPC 5 times per Minecraft day (Right click on it counts as interaction)")); //11th page

        bookmeta.pages(pages);
        book.setItemMeta(bookmeta);
        player.getInventory().addItem(book);
    }
}