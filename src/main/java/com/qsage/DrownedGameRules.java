package com.qsage;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public final class DrownedGameRules {
    public static GameRule<Double> NAUTILUS_SHELL_SPAWN_CHANCE;
    public static GameRule<Double> NAUTILUS_SHELL_DROP_CHANCE;
    public static GameRule<Double> TRIDENT_SPAWN_CHANCE;
    public static GameRule<Double> TRIDENT_DROP_CHANCE;
    public static GameRule<Boolean> RANDOM_ARMOR_DURABILITY;
    public static GameRule<Boolean> RANDOM_TRIDENT_DURABILITY;

    private DrownedGameRules() {}

    public static void register() {
        NAUTILUS_SHELL_SPAWN_CHANCE = GameRuleBuilder
                .forDouble(DrownedMechanicsConfig.nautilusShellSpawnChance)
                .range(0.0, 100.0)
                .category(GameRuleCategory.MISC)
                .buildAndRegister(BedrockDrownedMechanics.id(
                        "nautilus_shell_spawn_chance"));

        NAUTILUS_SHELL_DROP_CHANCE = GameRuleBuilder
                .forDouble(DrownedMechanicsConfig.nautilusShellDropChance)
                .range(0.0, 100.0)
                .category(GameRuleCategory.MISC)
                .buildAndRegister(BedrockDrownedMechanics.id(
                        "nautilus_shell_drop_chance"));

        TRIDENT_SPAWN_CHANCE = GameRuleBuilder
                .forDouble(DrownedMechanicsConfig.tridentSpawnChance)
                .range(0.0, 100.0)
                .category(GameRuleCategory.MISC)
                .buildAndRegister(BedrockDrownedMechanics.id(
                        "trident_spawn_chance"));

        TRIDENT_DROP_CHANCE = GameRuleBuilder
                .forDouble(DrownedMechanicsConfig.tridentDropChance)
                .range(0.0, 100.0)
                .category(GameRuleCategory.MISC)
                .buildAndRegister(BedrockDrownedMechanics.id(
                        "trident_drop_chance"));

        RANDOM_ARMOR_DURABILITY = GameRuleBuilder
                .forBoolean(false)
                .category(GameRuleCategory.MISC)
                .buildAndRegister(BedrockDrownedMechanics.id(
                        "random_armor_durability"));

        RANDOM_TRIDENT_DURABILITY = GameRuleBuilder
                .forBoolean(true)
                .category(GameRuleCategory.MISC)
                .buildAndRegister(BedrockDrownedMechanics.id(
                        "random_trident_durability"));
    }
}