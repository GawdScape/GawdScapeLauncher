package com.gawdscape.json.modpacks;

/**
 *
 * @author Vinnie
 */
public enum ModType {

    FORGE, // Standard Forge .jar mod
    COREMOD, // Forge Core Mod
    VERSIONMOD, // Version specific Forge mod
    FORGEZIP, // Forge .zip mod (can be extracted)
    LITEMOD, // LiteLoader .litemod
    VERSIONLITEMOD, // Version specific LiteLoader mod
    JARMOD // Modifications to Minecraft.jar file
}
