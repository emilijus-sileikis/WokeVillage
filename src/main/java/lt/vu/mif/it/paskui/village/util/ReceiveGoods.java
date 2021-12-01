package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.EventListen;
import lt.vu.mif.it.paskui.village.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ReceiveGoods extends BukkitRunnable {
    NPC npc;
    Location loc;
    Player p;
    Material material;
    ItemStack itemReceived;
    int goods;

    public ReceiveGoods(NPC npc, Location loc, Player p, Material material, ItemStack itemReceived, int goods) {
        this.npc = npc;
        this.loc = loc;
        this.p = p;
        this.material = material;
        this.itemReceived = itemReceived;
        this.goods = goods;
    }

    @Override
    public void run() {
        for(int i=0; i<goods; i++) {
            if (p.getInventory().firstEmpty() == -1) {
                p.getWorld().dropItemNaturally(loc, itemReceived.asBukkitCopy());
            } else {//items are added 1 by 1 to avoid duping
                EventListen.receiveItems(p.getInventory(), material, 1);
                p.updateInventory();
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
        }
        p.sendMessage(Component.text("Your items have been delivered!").color(NamedTextColor.GREEN));
        npc.refreshBrain();
    }
}
