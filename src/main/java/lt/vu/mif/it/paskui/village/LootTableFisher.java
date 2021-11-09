package lt.vu.mif.it.paskui.village;

import org.bukkit.Material;

public enum LootTableFisher {

    DIAMOND (Material.DIAMOND,10,1),
    SPYGLASS (Material.SPYGLASS, 10,1),
    EXPERIENCE_BOTTLE (Material.EXPERIENCE_BOTTLE,10,16),
    IRON_HORSE_ARMOR (Material.IRON_HORSE_ARMOR,10,1),
    DIAMOND_HORSE_ARMOR (Material.DIAMOND_HORSE_ARMOR,10,1),
    AMETHYST_SHARD (Material.AMETHYST_SHARD,10,1),
    NAUTILUS_SHELL (Material.NAUTILUS_SHELL,10,1),
    HEART_OF_THE_SEA (Material.HEART_OF_THE_SEA,10,1),
    SADLLE (Material.SADDLE,10,1),
    MUSIC_DISC_13 (Material.MUSIC_DISC_13,10,1),
    MUSIC_DISC_WAIT (Material.MUSIC_DISC_WAIT,10,1 ),
    MUSIC_DISC_PIGSTEP (Material.MUSIC_DISC_PIGSTEP,10,1),
    GOLDEN_APPLE (Material.GOLDEN_APPLE,10,4),
    COMPASS (Material.COMPASS, 10, 1),
    LEAD (Material.LEAD, 10, 1);

    private Material item;
    private int cost;
    private int goods;

    public Material getItem() {
        return item;
    }
    public int getCost() {
        return cost;
    }
    public int getGoods() {
        return goods;
    }

    LootTableFisher(Material item, int cost, int goods)
    {
        this.item = item;
        this.cost = cost;
        this.goods = goods;
    }

    public static LootTableFisher fromInt(int itemId)
    {
        if(itemId < LootTableFisher.values().length)
            return LootTableFisher.values()[itemId];
       return LootTableFisher.SADLLE;
    }

}
