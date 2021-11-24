package gg.moonflower.starter.core;

import gg.moonflower.pollen.api.platform.Platform;

public class Starter {
    public static final String MOD_ID = "starter";
    public static final Platform PLATFORM = Platform.builder(MOD_ID)
            .clientInit(Starter::onClientInit)
            .clientPostInit(Starter::onClientPostInit)
            .commonInit(Starter::onCommonInit)
            .commonPostInit(Starter::onCommonPostInit)
            .build();

    public static void onClientInit() {
    }

    public static void onClientPostInit(Platform.ModSetupContext ctx) {
    }

    public static void onCommonInit() {
    }

    public static void onCommonPostInit(Platform.ModSetupContext ctx) {
    }
}
