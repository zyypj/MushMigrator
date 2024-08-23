package br.com.pulse.mushmigrator.core;

import java.util.Arrays;

public enum LayoutItem {

    WOOL("blocks-category.category-content.wool", "wool"),
    CLAY("blocks-category.category-content.clay", "clay"),
    GLASS("blocks-category.category-content.glass", "glass"),
    END_STONE("blocks-category.category-content.stone", "end_stone"),
    LADDER("blocks-category.category-content.ladder", "stairs"),
    WOOD("blocks-category.category-content.wood", "wood"),
    OBSIDIAN("blocks-category.category-content.obsidian", "obsidian"),
    STONE_SWORD("melee-category.category-content.stone-sword", "stone_sword"),
    IRON_SWORD("melee-category.category-content.iron-sword", "iron_sword"),
    DIAMOND_SWORD("melee-category.category-content.diamond-sword", "diamond_sword"),
    KNOCKBACK_STICK("melee-category.category-content.stick", "knockback_stick"),
    CHAINMAIL_ARMOR("armor-category.category-content.chainmail", "chainmail_armor"),
    IRON_ARMOR("armor-category.category-content.iron-armor", "iron_armor"),
    DIAMOND_ARMOR("armor-category.category-content.diamond-armor", "diamond_armor"),
    SHEARS("tools-category.category-content.shears", "shears"),
    WOODEN_PICKAXE("tools-category.category-content.pickaxe", "pickaxe"),
    WOODEN_AXE("tools-category.category-content.axe", "axe"),
    ARROW("ranged-category.category-content.arrow", "arrow"),
    BOW_ONE("ranged-category.category-content.bow1", "bow"),
    BOW_TWO("ranged-category.category-content.bow2", "bow_lvl2"),
    BOW_THREE("ranged-category.category-content.bow3", "bow_lvl3"),
    SPEED_POTION("potions-category.category-content.speed-potion", "speed_potion"),
    JUMP_POTION("potions-category.category-content.jump-potion", "jump_potion"),
    INVISIBILITY_POTION("potions-category.category-content.invisibility", "invisibility_potion"),
    GOLDEN_APPLE("utility-category.category-content.golden-apple", "golden_apple"),
    BEDBUG("utility-category.category-content.bedbug", "silverfish"),
    DREAM_DEFENDER("utility-category.category-content.dream-defender", "iron_golem"),
    FIREBALL("utility-category.category-content.fireball", "fireball"),
    TNT("utility-category.category-content.tnt", "tnt"),
    ENDER_PEARL("utility-category.category-content.ender-pearl", "ender_pearl"),
    WATER_BUCKET("utility-category.category-content.water-bucket", "water_bucket"),
    BRIDGE_EGG("utility-category.category-content.bridge-egg", "bridge_egg"),
    MAGIC_MILK("utility-category.category-content.magic-milk", "milk"),
    SPONGE("utility-category.category-content.sponge", "sponge"),
    POPUP_TOWER("utility-category.category-content.tower", "popup_tower"),
    SLINGSHOT(null, "slingshot");

    LayoutItem(String category, String id) {
    this.category = category;
    this.id = id;
    }

    private final String category;

    private final String id;

    public String getCategory() {
        return this.category;
    }

    public String getId() {
        return this.id;
    }

    public static LayoutItem matchItem(String item) {
        return Arrays.<LayoutItem>stream(values()).filter(layoutItem -> layoutItem.getId().equals(item)).findAny().orElse(null);
    }
}
