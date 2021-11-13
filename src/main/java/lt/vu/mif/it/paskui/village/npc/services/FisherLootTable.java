package lt.vu.mif.it.paskui.village.npc.services;

import org.bukkit.Material;

public enum FisherLootTable {
    SALMON(Material.SALMON,10,64),
    TROPICAL_FISH(Material.TROPICAL_FISH,10,64),
    PUFFERFISH(Material.PUFFERFISH,10,64),
    COD(Material.COD,10,64),
    //
    SUGAR_CANE(Material.SUGAR_CANE,5,1),
    KELP(Material.KELP,5,1),
    LILY_PAD(Material.LILY_PAD,5,1),
    LEATHER_BOOTS(Material.LEATHER_BOOTS,5,1),
    LEATHER_HELMET(Material.LEATHER_HELMET,5,1),
    LEATHER_CHESTPLATE(Material.LEATHER_CHESTPLATE,5,1),
    LEATHER_LEGGINGS(Material.LEATHER_LEGGINGS,5,1),
    //
    DIAMOND(Material.DIAMOND,10,1),
    SPYGLASS(Material.SPYGLASS, 10,1),
    EXPERIENCE_BOTTLE(Material.EXPERIENCE_BOTTLE,10,16),
    IRON_HORSE_ARMOR(Material.IRON_HORSE_ARMOR,10,1),
    DIAMOND_HORSE_ARMOR(Material.DIAMOND_HORSE_ARMOR,10,1),
    AMETHYST_SHARD(Material.AMETHYST_SHARD,10,4),
    NAUTILUS_SHELL(Material.NAUTILUS_SHELL,10,1),
    HEART_OF_THE_SEA(Material.HEART_OF_THE_SEA,10,1),
    SADDLE(Material.SADDLE,10,1),
    MUSIC_DISC_13 (Material.MUSIC_DISC_13,10,1),
    MUSIC_DISC_WAIT (Material.MUSIC_DISC_WAIT,10,1 ),
    MUSIC_DISC_PIGSTEP(Material.MUSIC_DISC_PIGSTEP,10,1),
    MUSIC_DISC_CAT(Material.MUSIC_DISC_CAT,10,1),
    MUSIC_DISC_CHIRP(Material.MUSIC_DISC_CHIRP,10,1),
    MUSIC_DISC_MELLOHI(Material.MUSIC_DISC_MELLOHI,10,1),
    GOLDEN_APPLE (Material.GOLDEN_APPLE,10,4),
    COMPASS (Material.COMPASS, 10, 1),
    LEAD (Material.LEAD, 10, 1),
    RAW_GOLD (Material.RAW_GOLD, 10, 16),
    EMERALD (Material.EMERALD, 10, 1),
    BLAZE_ROD (Material.BLAZE_ROD, 10, 4),
    FIREWORK_ROCKET (Material.FIREWORK_ROCKET, 10, 16),
    BOOK (Material.BOOK, 10, 32),
    SLIME_BALL (Material.SLIME_BALL, 10, 16),
    HONEY_BOTTLE (Material.HONEY_BOTTLE, 10, 4),
    CLOCK (Material.CLOCK, 10, 1),
    NAME_TAG (Material.NAME_TAG, 10, 1),
    CHEST_MINECART (Material.CHEST_MINECART, 10, 1),
    ELYTRA (Material.ELYTRA, 10, 1);

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
