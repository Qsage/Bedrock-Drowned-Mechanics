
package com.qsage.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.qsage.DrownedGameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.ConversionType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class ConversionTypeMixin {

    @WrapOperation(
            method = "convertTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ConversionType;convert(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/entity/ConversionParams;)V"
            )
    )
    private void bedrockDrownedMechanics$processEquipment(
            ConversionType type,
            Mob from,
            Mob to,
            ConversionParams params,
            Operation<Void> original
    ) {
        original.call(type, from, to, params);

        if (!(to instanceof Drowned drowned)) {
            return;
        }

        if (!(drowned.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack equipped = drowned.getItemBySlot(slot);

            if (equipped.isEmpty()) {
                continue;
            }

            ItemStack dropped = equipped.copy();

            if ((slot == EquipmentSlot.HEAD
                    || slot == EquipmentSlot.CHEST
                    || slot == EquipmentSlot.LEGS
                    || slot == EquipmentSlot.FEET)
                    && serverLevel.getGameRules()
                    .get(DrownedGameRules.RANDOM_ARMOR_DURABILITY)) {

                if (dropped.isDamageableItem()) {
                    dropped.setDamageValue(
                            serverLevel.getRandom().nextInt(dropped.getMaxDamage())
                    );
                }
            }

            drowned.setItemSlot(slot, ItemStack.EMPTY);

            ItemEntity itemEntity = new ItemEntity(
                    serverLevel,
                    drowned.getX(),
                    drowned.getY(),
                    drowned.getZ(),
                    dropped
            );

            serverLevel.addFreshEntity(itemEntity);
        }

        if (!(from instanceof Zombie)) {
            return;
        }

        drowned.addTag("bdm_converted_zombie");

        double tridentChance = serverLevel.getGameRules()
                .get(DrownedGameRules.TRIDENT_SPAWN_CHANCE)
                .doubleValue();

        double shellChance = serverLevel.getGameRules()
                .get(DrownedGameRules.NAUTILUS_SHELL_SPAWN_CHANCE)
                .doubleValue();

        if (serverLevel.getRandom().nextFloat() * 100.0 < tridentChance) {
            ItemStack trident = new ItemStack(Items.TRIDENT);

            boolean randomDurability = serverLevel.getGameRules()
                    .get(DrownedGameRules.RANDOM_TRIDENT_DURABILITY);

            if (randomDurability) {
                trident.setDamageValue(
                        serverLevel.getRandom().nextInt(trident.getMaxDamage())
                );
            }

            drowned.setItemSlot(EquipmentSlot.MAINHAND, trident);
            drowned.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        }

        if (serverLevel.getRandom().nextFloat() * 100.0 < shellChance) {
            drowned.setItemSlot(
                    EquipmentSlot.OFFHAND,
                    new ItemStack(Items.NAUTILUS_SHELL)
            );
            drowned.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
        }
    }
}
