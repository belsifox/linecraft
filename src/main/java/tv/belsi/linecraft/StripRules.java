package tv.belsi.linecraft;

public final class StripRules {
    private StripRules() {
    }

    /**
     * Must be odd. 3 means z in [-1, 1].
     */
    public static final int STRIP_WIDTH = 3;

    public static final int BIOME_SCALE = 4; // 2 = ~half-size biomes, 4 = ~quarter-size

    public static final float YAW = -90.0f;

    public static final int HALF = (STRIP_WIDTH - 1) / 2;

    public static boolean inStripZ(int worldZ) {
        return worldZ >= -HALF && worldZ <= HALF;
    }
}
