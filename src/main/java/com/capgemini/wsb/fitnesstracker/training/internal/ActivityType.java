package com.capgemini.wsb.fitnesstracker.training.internal;

/**
 * Enum representing different types of physical activities.
 * Each activity type has a corresponding display name.
 */
public enum ActivityType {

    /**
     * Represents the activity of running.
     */
    RUNNING("Running"),

    /**
     * Represents the activity of cycling.
     */
    CYCLING("Cycling"),

    /**
     * Represents the activity of walking.
     */
    WALKING("Walking"),

    /**
     * Represents the activity of swimming.
     */
    SWIMMING("Swimming"),

    /**
     * Represents the activity of playing tennis.
     */
    TENNIS("Tennis");

    private final String displayName;

    /**
     * Constructs an ActivityType enum with the specified display name.
     *
     * @param displayName the display name of the activity type
     */
    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the activity type.
     *
     * @return the display name of the activity type
     */
    public String getDisplayName() {
        return displayName;
    }
}
