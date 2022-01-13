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
        bookmeta.setTitle("Title");
        ArrayList<Component> pages = new ArrayList<>();
        pages.add(Component.text("Something Something Something Something Something" + nl + "Something Something Something Something")); //1st page
        pages.add(Component.text("Something2 Something Something Something Something" + nl + "Something Something Something Something")); //2nd page
        bookmeta.pages(pages);
        book.setItemMeta(bookmeta);
        if (!(player.getInventory().contains(book))) {
            player.getInventory().addItem(book);
        }
        else player.sendMessage("You can only have one information book!");
    }
}