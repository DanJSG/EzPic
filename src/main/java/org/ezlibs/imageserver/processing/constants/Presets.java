package org.ezlibs.imageserver.processing.constants;

import org.ezlibs.imageserver.processing.types.Dimensions;
import org.ezlibs.imageserver.processing.types.Preset;
import org.ezlibs.imageserver.processing.types.Ratio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Presets {

    private static final String DEFAULT_KEY = "default";

    // Square image aspect ratio and sizes
    private static final Ratio SQUARE_RATIO = new Ratio(1f, 1f);
    private static final Dimensions SM_SQ_DIMS = new Dimensions(256, 256, "sm");
    private static final Dimensions MD_SQ_DIMS = new Dimensions(512, 512, "md");
    private static final Dimensions LG_SQ_DIMS = new Dimensions(1024, 1024, "lg");

    // Wide image aspect ratio and sizes
    private static final Ratio WIDE_RATIO = new Ratio(16f, 9f);
    private static final Dimensions SM_WIDE_DIMS = new Dimensions(480, 270, "sm");
    private static final Dimensions MD_WIDE_DIMS = new Dimensions(960, 540, "md");
    private static final Dimensions LG_WIDE_DIMS = new Dimensions(1920, 1080, "lg");

    // Tall image aspect ratio and sizes
    private static final Ratio TALL_RATIO = new Ratio(9f, 16f);
    private static final Dimensions SM_TALL_DIMS = new Dimensions(270, 480, "sm");
    private static final Dimensions MD_TALL_DIMS = new Dimensions(540, 960, "md");
    private static final Dimensions LG_TALL_DIMS = new Dimensions(1080, 1920, "lg");

    // Square image ratios and dimensions
    private static final List<Dimensions> SQUARE_DIMS = List.of(SM_SQ_DIMS, MD_SQ_DIMS, LG_SQ_DIMS);
    private static final List<Ratio> SQUARE_RATIOS = List.of(SQUARE_RATIO, SQUARE_RATIO, SQUARE_RATIO);

    // Wide image ratios and dimensions
    private static final List<Dimensions> WIDE_DIMS = List.of(SM_WIDE_DIMS, MD_WIDE_DIMS, LG_WIDE_DIMS);
    private static final List<Ratio> WIDE_RATIOS = List.of(WIDE_RATIO, WIDE_RATIO, WIDE_RATIO);

    private static final List<Dimensions> TALL_DIMS = List.of(SM_TALL_DIMS, MD_TALL_DIMS, LG_TALL_DIMS);
    private static final List<Ratio> TALL_RATIOS = List.of(TALL_RATIO, TALL_RATIO, TALL_RATIO);

    // Presets
    private static final Preset SQUARE = new Preset(true, true, 0.75f, SQUARE_DIMS, SQUARE_RATIOS);
    private static final Preset WIDE = new Preset(true, true, 0.75f, WIDE_DIMS, WIDE_RATIOS);
    private static final Preset TALL = new Preset(true, true, 0.75f, TALL_DIMS, TALL_RATIOS);
    private static final Preset DEFAULT = new Preset(false, false, 0.75f, null, null);

    private static final Map<String, Preset> PRESETS = new HashMap<>();

    static {
        PRESETS.put("square", SQUARE);
        PRESETS.put("tall", TALL);
        PRESETS.put("wide", WIDE);
        PRESETS.put("default", DEFAULT);
    }

    public static Preset getPreset(String presetName) {
        return PRESETS.containsKey(presetName) ? PRESETS.get(presetName) : PRESETS.get(DEFAULT_KEY);
    }

}
