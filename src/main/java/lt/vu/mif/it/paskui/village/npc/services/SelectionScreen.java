package lt.vu.mif.it.paskui.village.npc.services;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

public class SelectionScreen implements InventoryHolder {

    private final NPC npc;
    protected final Inventory inv;

    public SelectionScreen(NPC npc) {
        this.npc = npc;
        inv = Bukkit.createInventory(this,
                InventoryType.HOPPER,
                Component.text(String.format("%s %s", getRole(), getPersonality()))
                        .decorate(TextDecoration.BOLD)
                        .color(NamedTextColor.RED)
        );
        init(npc.getRole(), npc.getPersonality());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    // getters
    public final Role getRole() {
        return npc.getRole();
    }

    public final Personality getPersonality() {
        return npc.getPersonality();
    }

    public final NPC getNPC() {
        return npc;
    }

    // finals
    protected final ItemStack createItem(Component name, Material mat, List<Component> lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    protected final void createAddItem(Component name, Material mat, List<Component> lore) {
        ItemStack item = createItem(name, mat, lore);
        inv.setItem(inv.firstEmpty(), item);
    }

    protected final void addHelpOption() {
        ItemStack item = createItem(
                Component.text("Help").color(NamedTextColor.GREEN),
                Material.BOOK,
                Collections.singletonList(Component.text("Click here for help"))
        );
        inv.setItem(0, item);
    }

    protected final void addCloseOption() {
        ItemStack item = createItem(
                Component.text("Close").color(NamedTextColor.RED),
                Material.BARRIER,
                Collections.singletonList(Component.text("Click to close the menu"))
        );
        inv.setItem(inv.getSize() - 1, item);
    }

    // other
    protected void init(Role role, Personality personality) {
        this.addHelpOption();
        this.addCloseOption();
    }

    // static
    public static SelectionScreen createScreen(NPC npc) {
        try {
            Constructor<? extends SelectionScreen> cns = npc.getRole().getClazz().getConstructor(npc.getClass());
            return cns.newInstance(npc);
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e
        ) {
            e.printStackTrace();
        }

        return new SelectionScreen(npc);
    }
}
