
package com.qsage;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.item.ItemStack;

public final class DrownedConversionHandler {

    private DrownedConversionHandler() {
    }

    public static void register() {
        ServerLivingEntityEvents.MOB_CONVERSION.register(
                (entity, convertedEntity, keepEquipment) -> {
                    if (!(entity instanceof Zombie zombie)) {
                        return;
                    }

                    if (!(convertedEntity instanceof Drowned drowned)) {
                        return;
                    }

                    handleConversion(zombie, drowned);
                }
        );
    }

    private static void handleConversion(Zombie zombie, Drowned drowned) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) {
                continue;
            }

            ItemStack stack = zombie.getItemBySlot(slot);

            if (stack.isEmpty()) {
                continue;
            }

            ItemStack droppedStack = stack.copy();


            zombie.setItemSlot(slot, ItemStack.EMPTY);
            drowned.setItemSlot(slot, ItemStack.EMPTY);

            droppedStack.setDamageValue(0);

            ItemEntity itemEntity = new ItemEntity(
                    drowned.level(),
                    drowned.getX(),
                    drowned.getY(),
                    drowned.getZ(),
                    droppedStack
            );

            drowned.level().addFreshEntity(itemEntity);
        }
    }
}
