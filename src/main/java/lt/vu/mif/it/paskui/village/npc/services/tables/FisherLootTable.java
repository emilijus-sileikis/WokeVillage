package lt.vu.mif.it.paskui.village.npc.services.tables;

import org.bukkit.Material;

public enum FisherLootTable {
    WATER(Material.WATER,0),
    //
    SALMON(Material.SALMON,64),
    TROPICAL_FISH(Material.TROPICAL_FISH,64),
    PUFFERFISH(Material.PUFFERFISH,64),
    COD(Material.COD,64),
    //
    SUGAR_CANE(Material.SUGAR_CANE,1),
    KELP(Material.KELP,1),
    LILY_PAD(Material.LILY_PAD,1),
    LEATHER_BOOTS(Material.LEATHER_BOOTS,1),
    LEATHER_HELMET(Material.LEATHER_HELMET,1),
    LEATHER_CHESTPLATE(Material.LEATHER_CHESTPLATE,1),
    LEATHER_LEGGINGS(Material.LEATHER_LEGGINGS,1),
    //
    DIAMOND(Material.DIAMOND,1),
    SPYGLASS(Material.SPYGLASS,1),
    EXPERIENCE_BOTTLE(Material.EXPERIENCE_BOTTLE,16),
    IRON_HORSE_ARMOR(Material.IRON_HORSE_ARMOR,1),
    DIAMOND_HORSE_ARMOR(Material.DIAMOND_HORSE_ARMOR,1),
    AMETHYST_SHARD(Material.AMETHYST_SHARD,4),
    NAUTILUS_SHELL(Material.NAUTILUS_SHELL,1),
    HEART_OF_THE_SEA(Material.HEART_OF_THE_SEA,1),
    SADDLE(Material.SADDLE,1),
    MUSIC_DISC_13 (Material.MUSIC_DISC_13,1),
    MUSIC_DISC_WAIT (Material.MUSIC_DISC_WAIT,1 ),
    MUSIC_DISC_PIGSTEP(Material.MUSIC_DISC_PIGSTEP,1),
    MUSIC_DISC_CAT(Material.MUSIC_DISC_CAT,1),
    MUSIC_DISC_CHIRP(Material.MUSIC_DISC_CHIRP,1),
    MUSIC_DISC_MELLOHI(Material.MUSIC_DISC_MELLOHI,1),
    GOLDEN_APPLE (Material.GOLDEN_APPLE,4),
    COMPASS (Material.COMPASS,1),
    LEAD (Material.LEAD,1),
    RAW_GOLD (Material.RAW_GOLD,16),
    EMERALD (Material.EMERALD,1),
    BLAZE_ROD (Material.BLAZE_ROD,4),
    FIREWORK_ROCKET (Material.FIREWORK_ROCKET,16),
    BOOK (Material.BOOK,32),
    SLIME_BALL (Material.SLIME_BALL,16),
    HONEY_BOTTLE (Material.HONEY_BOTTLE,4),
    CLOCK (Material.CLOCK,1),
    NAME_TAG (Material.NAME_TAG,1),
    CHEST_MINECART (Material.CHEST_MINECART,1),
    ELYTRA (Material.ELYTRA,1);

    private final Material item;
    private final int goods;

    FisherLootTable(Material item, int goods)
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

    public static FisherLootTable fromInt(int itemId)
    {
        if(itemId < FisherLootTable.values().length)
            return FisherLootTable.values()[itemId];
        return FisherLootTable.SADDLE;
    }

}
