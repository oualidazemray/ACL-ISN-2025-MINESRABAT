package com.lo3ba.core;

/**
 * Simple data holder for victory screen statistics.
 */
public class VictoryData {
    private final int collectedStars;
    private final int totalStars;
    private final int deathCount;
    private final String timeElapsed;
    
    public VictoryData(int collectedStars, int totalStars, int deathCount, String timeElapsed) {
        this.collectedStars = collectedStars;
        this.totalStars = totalStars;
        this.deathCount = deathCount;
        this.timeElapsed = timeElapsed;
    }
    
    public int getCollectedStars() {
        return collectedStars;
    }
    
    public int getTotalStars() {
        return totalStars;
    }
    
    public int getDeathCount() {
        return deathCount;
    }
    
    public String getTimeElapsed() {
        return timeElapsed;
    }
}
