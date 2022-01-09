package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.EventListen;
import lt.vu.mif.it.paskui.village.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ReceiveGoods extends NPCLocState {
    private final Player p;
    private final ItemStack itemReceived;

    /**
     * Bukkit task for transferring collected goods to player that
     * ordered them.
     * @param npc       instance of NPC running this state
     * @param loc       npc position
     * @param p         player to receive goods
     * @param material  type of item to give to player.
     * @param goodCount count of goods
     */
    public ReceiveGoods(NPC npc, Location loc, Player p, Material material, int goodCount) {
        super(npc, loc);
        this.p = p;
        this.itemReceived = new ItemStack(material, goodCount);
    }

    @Override
    public void run() {
        Map<Integer, ItemStack> leftItems = p.getInventory().addItem(itemReceived);
        p.updateInventory();
        for (ItemStack items : leftItems.values()) {
            p.getWorld().dropItemNaturally(loc, items);
        }

        // displays particle effect to all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
        }

        p.sendMessage(Component.text("Your items have been delivered!").color(NamedTextColor.GREEN));
        ServerLevel world = ((CraftWorld) loc.getWorld()).getHandle();
        npc.refreshBrains(world);
        npc.setKillable();
        npc.setCollidable();
        npc.itemReset();
    }
}
