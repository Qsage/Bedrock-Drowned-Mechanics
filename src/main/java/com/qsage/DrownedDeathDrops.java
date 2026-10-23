
package com.qsage;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public final class DrownedDeathDrops {

    public static final String CONVERTED_TAG = "bdm_converted_zombie";

    private DrownedDeathDrops() {}

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register(
                DrownedDeathDrops::onDeath
        );
    }

    private static void onDeath(
            LivingEntity entity,
            DamageSource damageSource
    ) {
        if (!(entity instanceof Drowned drowned)) {
            return;
        }

        if (!(drowned.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        int lootingLevel = 0;

        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            lootingLevel = attacker.level()
                    .registryAccess()
                    .lookupOrThrow(Enchantments.LOOTING.registryKey())
                    .get(Enchantments.LOOTING)
                    .map(holder -> EnchantmentHelper.getEnchantmentLevel(holder, attacker))
                    .orElse(0);
        }

        dropIfSuccessful(
                drowned,
                serverLevel,
                EquipmentSlot.MAINHAND,
                Items.TRIDENT,
                DrownedGameRules.TRIDENT_DROP_CHANCE,
                lootingLevel
        );

        dropIfSuccessful(
                drowned,
                serverLevel,
                EquipmentSlot.OFFHAND,
                Items.NAUTILUS_SHELL,
                DrownedGameRules.NAUTILUS_SHELL_DROP_CHANCE,
                lootingLevel
        );
    }

    private static void dropIfSuccessful(
            Drowned drowned,
            ServerLevel serverLevel,
            EquipmentSlot slot,
            Item item,
            net.minecraft.world.level.gamerules.GameRule<Double> gameRule,
            int lootingLevel
    ) {
        ItemStack equipped = drowned.getItemBySlot(slot);

        if (!equipped.is(item)) {
            return;
        }

        if (drowned.getDropChances().byEquipment(slot) != 0.0F) {
            return;
        }

        double baseChance = serverLevel.getGameRules()
                .get(gameRule)
                .doubleValue();

        double finalChance = Math.min(
                100.0,
                Math.max(0.0, baseChance) + Math.max(0, lootingLevel) * 2.5
        );

        if (serverLevel.getRandom().nextFloat() * 100.0 >= finalChance) {
            return;
        }

        ItemEntity drop = new ItemEntity(
                serverLevel,
                drowned.getX(),
                drowned.getY(),
                drowned.getZ(),
                equipped.copy()
        );

        serverLevel.addFreshEntity(drop);
    }
}
