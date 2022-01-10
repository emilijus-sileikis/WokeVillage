package lt.vu.mif.it.paskui.village.npc.services.tables;

import org.bukkit.Material;

public enum MinerLootTable {
    COBBLESTONE(Material.COBBLESTONE,96),
    IRON_ORE(Material.IRON_ORE,32),
    COAL(Material.COAL,64),
    //
    COAL_ORE(Material.COAL_ORE,0),
    STONE(Material.STONE,0);

    private final Material item;
    private final int goods;

    MinerLootTable(Material item, int goods)
    {
        this.item = item;
        this.goods = goods;
    }

    public Material getItem() {
        return item;
    }
    public int getGoods() {
        return goods;
    }

    public static MinerLootTable fromInt(int itemId)
    {
        if(itemId < FisherLootTable.values().length)
            return MinerLootTable.values()[itemId];
        return MinerLootTable.COBBLESTONE;
    }

}
