package lt.vu.mif.it.paskui.village.npc.services;

import org.bukkit.Material;

public enum MinerLootTable {
    COBBLESTONE(Material.COBBLESTONE,10,96),
    IRON_ORE(Material.IRON_ORE,16,32),
    COAL(Material.COAL,10,64);



    private final Material item;
    private final int cost;
    private final int goods;

    MinerLootTable(Material item, int cost, int goods)
    {
        this.item = item;
        this.cost = cost;
        this.goods = goods;
    }

    public Material getItem() {
        return item;
    }
    public int getCost() {
        return cost;
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
