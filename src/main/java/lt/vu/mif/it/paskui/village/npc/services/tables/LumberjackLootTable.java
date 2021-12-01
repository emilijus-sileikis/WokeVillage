package lt.vu.mif.it.paskui.village.npc.services.tables;

import org.bukkit.Material;

public enum LumberjackLootTable {
    //
    SPRUCE_LOG(Material.SPRUCE_LOG,20,128),
    OAK_LOG(Material.OAK_LOG,20,128),
    BIRCH_LOG(Material.DARK_OAK_LOG,20,128),
    ACACIA_LOG(Material.ACACIA_LOG,20,128),
    JUNGLE_LOG(Material.JUNGLE_LOG,20,128),

    SPRUCE_SAPLING(Material.SPRUCE_SAPLING,2,16),
    OAK_SAPLING(Material.OAK_SAPLING,2,16),
    ACACIA_SAPLING(Material.ACACIA_SAPLING,2,16),
    DARK_OAK_SAPLING(Material.DARK_OAK_SAPLING,2,16),
    JUNGLE_SAPLING(Material.JUNGLE_SAPLING,2,16);


    private final Material item;
    private final int cost;
    private final int goods;

    LumberjackLootTable(Material item, int cost, int goods)
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

    public static LumberjackLootTable fromInt(int itemId)
    {
        if(itemId < FisherLootTable.values().length)
            return LumberjackLootTable.values()[itemId];
        return LumberjackLootTable.SPRUCE_LOG;
    }

}


