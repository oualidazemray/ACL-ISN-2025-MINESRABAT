package com.lo3ba.gameobjects;

/**
 * Represents a playable avatar/character in the game.
 * Each avatar has a unique sprite and optional special ability (visual or functional).
 */
public class Avatar {
    private final String id;
    private final String name;
    private final String spriteFile;
    private final String description;
    private final AvatarAbility ability;
    
    public enum AvatarAbility {
        NONE("Standard", "No special ability"),
        DOUBLE_JUMP("Double Jump", "Can jump twice in mid-air"), // Future: actual implementation
        SPEED_BOOST("Speed Runner", "Moves 20% faster"),
        GLOW_EFFECT("Glowing Hero", "Emits a soft glow"),
        TRAIL_EFFECT("Speed Trail", "Leaves a trail behind");
        
        private final String displayName;
        private final String description;
        
        AvatarAbility(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public Avatar(String id, String name, String spriteFile, String description, AvatarAbility ability) {
        this.id = id;
        this.name = name;
        this.spriteFile = spriteFile;
        this.description = description;
        this.ability = ability;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpriteFile() { return spriteFile; }
    public String getDescription() { return description; }
    public AvatarAbility getAbility() { return ability; }
    
    // Pre-defined avatars
    public static Avatar[] getAvailableAvatars() {
        return new Avatar[] {
            new Avatar("hero", "Classic Hero", "avatar1_idle.png", 
                "The original adventurer", AvatarAbility.NONE),
            new Avatar("ninja", "Swift Ninja", "avatar2_idle.png", 
                "Fast and agile warrior", AvatarAbility.SPEED_BOOST),
            new Avatar("mage", "Mystic Mage", "avatar3_idle.png", 
                "Glows with magical energy", AvatarAbility.GLOW_EFFECT),
            new Avatar("knight", "Royal Knight", "avatar4_idle.png", 
                "Leaves a trail of honor", AvatarAbility.TRAIL_EFFECT)
        };
    }
    
    public static Avatar getDefault() {
        return getAvailableAvatars()[0];
    }
    
    @Override
    public String toString() {
        return name + " (" + ability.getDisplayName() + ")";
    }
}
