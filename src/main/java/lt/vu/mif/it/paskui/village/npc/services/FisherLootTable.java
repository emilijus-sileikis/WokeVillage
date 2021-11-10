package lt.vu.mif.it.paskui.village.npc.services;

import org.bukkit.Material;

public enum FisherLootTable {
    DIAMOND(Material.DIAMOND,10,1),
    SPYGLASS(Material.SPYGLASS, 10,1),
    EXPERIENCE_BOTTLE(Material.EXPERIENCE_BOTTLE,10,16),
    IRON_HORSE_ARMOR(Material.IRON_HORSE_ARMOR,10,1),
    DIAMOND_HORSE_ARMOR(Material.DIAMOND_HORSE_ARMOR,10,1),
    AMETHYST_SHARD(Material.AMETHYST_SHARD,10,1),
    NAUTILUS_SHELL(Material.NAUTILUS_SHELL,10,1),
    HEART_OF_THE_SEA(Material.HEART_OF_THE_SEA,10,1),
    SADDLE(Material.SADDLE,10,1),
    MUSIC_DISC_13 (Material.MUSIC_DISC_13,10,1),
    MUSIC_DISC_WAIT (Material.MUSIC_DISC_WAIT,10,1 ),
    MUSIC_DISC_PIGSTEP(Material.MUSIC_DISC_PIGSTEP,10,1),
    GOLDEN_APPLE (Material.GOLDEN_APPLE,10,4),
    COMPASS (Material.COMPASS, 10, 1),
    LEAD (Material.LEAD, 10, 1);

    private final Material item;
    private final int cost;
    private final int goods;

    FisherLootTable(Material item, int cost, int goods)
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

    public static FisherLootTable fromInt(int itemId)
    {
        if(itemId < FisherLootTable.values().length)
            return FisherLootTable.values()[itemId];
       return FisherLootTable.SADDLE;
    }

}
