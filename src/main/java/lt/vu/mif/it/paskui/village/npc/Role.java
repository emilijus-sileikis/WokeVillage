package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.npc.services.FisherSelectionScreen;
import lt.vu.mif.it.paskui.village.npc.services.LumberJackSelectionScreen;
import lt.vu.mif.it.paskui.village.npc.services.MinerSelectionScreen;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import org.bukkit.Material;

import java.util.Random;

public enum Role {
    LUMBERJACK(LumberJackSelectionScreen.class, Material.BARREL, Material.IRON_AXE),
    MINER(MinerSelectionScreen.class, Material.CHEST, Material.IRON_PICKAXE),
    FISHER(FisherSelectionScreen.class, Material.TROPICAL_FISH_BUCKET, Material.FISHING_ROD);

    public final Class<? extends SelectionScreen> clazz;
    public final Material goodsCosmetic;
    public final Material workCosmetic;

    Role(final Class<? extends SelectionScreen> clazz, Material goodsCosmetic, Material workCosmetic) {
        this.clazz = clazz;
        this.goodsCosmetic = goodsCosmetic;
        this.workCosmetic = workCosmetic;
    }

    public static Role fromString(String val) {
        for (Role role : Role.values()) {
            if ( val.toLowerCase().equals(role.toString()) )
                return role;
        }

        return Role.LUMBERJACK;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public String toStringWithCapInitial() {
        return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1);
    }

    public static Role getRandomRole() {
        int id = new Random().nextInt(Role.values().length);
        return Role.values()[id];
    }
}
