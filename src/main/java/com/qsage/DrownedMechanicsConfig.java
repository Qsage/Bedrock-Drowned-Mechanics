package com.qsage;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class DrownedMechanicsConfig {
    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("bedrock-drowned-mechanics.properties");

    public static double nautilusShellSpawnChance = 3.0;
    public static double nautilusShellDropChance = 8.5;
    public static double tridentSpawnChance = 6.25;
    public static double tridentDropChance = 8.5;


    private DrownedMechanicsConfig() {}

    public static void load() {
        Properties properties = new Properties();

        try {
            Files.createDirectories(FILE.getParent());

            if (Files.exists(FILE)) {
                try (InputStream input = Files.newInputStream(FILE)) {
                    properties.load(input);
                }
            }

            nautilusShellSpawnChance = read(properties,
                    "nautilusShellSpawnChance", nautilusShellSpawnChance);
            nautilusShellDropChance = read(properties,
                    "nautilusShellDropChance", nautilusShellDropChance);
            tridentSpawnChance = read(properties,
                    "tridentSpawnChance", tridentSpawnChance);
            tridentDropChance = read(properties,
                    "tridentDropChance", tridentDropChance);

            save();
        } catch (IOException exception) {
            BedrockDrownedMechanics.LOGGER.error(
                    "Failed to load drowned mechanics config", exception);
        }
    }

    private static double read(
            Properties properties, String key, double defaultValue) {
        try {
            double value = Double.parseDouble(
                    properties.getProperty(key, Double.toString(defaultValue)));

            if (!Double.isFinite(value)) {
                return defaultValue;
            }

            return Math.max(0.0, Math.min(100.0, value));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    private static void save() throws IOException {
        Properties properties = new Properties();

        properties.setProperty("nautilusShellSpawnChance",
                Double.toString(nautilusShellSpawnChance));
        properties.setProperty("nautilusShellDropChance",
                Double.toString(nautilusShellDropChance));
        properties.setProperty("tridentSpawnChance",
                Double.toString(tridentSpawnChance));
        properties.setProperty("tridentDropChance",
                Double.toString(tridentDropChance));

        try (OutputStream output = Files.newOutputStream(FILE)) {
            properties.store(output,
                    "Bedrock Drowned Mechanics - chances in percent (0-100)");
        }
    }
}