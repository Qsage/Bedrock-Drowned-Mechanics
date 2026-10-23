package com.qsage;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedrockDrownedMechanics implements ModInitializer {
	public static final String MOD_ID = "bedrock-drowned-mechanics";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		DrownedMechanicsConfig.load();
		DrownedGameRules.register();
		DrownedDeathDrops.register();

		LOGGER.info("Mod BedrockDrownedMechanics initialized");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
