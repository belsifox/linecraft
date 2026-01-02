package tv.belsi.linecraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.rule.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Linecraft implements ModInitializer {
    public static final String MOD_ID = "linecraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Hello Fabric world!");
        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.getRegistryKey() != World.OVERWORLD) return;

            int radius = StripRules.HALF;
            world.getGameRules().setValue(GameRules.RESPAWN_RADIUS, radius, server);


            int y = world.getTopY(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, 0, 0);
            world.setSpawnPoint(new WorldProperties.SpawnPoint(new GlobalPos(World.OVERWORLD, new BlockPos(0, y, 0)), StripRules.YAW, 0.0f));
        });
    }
}