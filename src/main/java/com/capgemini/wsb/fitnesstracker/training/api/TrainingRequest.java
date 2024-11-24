package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * Represents a request for creating or updating a training session.
 * This class contains the necessary details for defining a training, such as the user's ID,
 * the activity type, the start and end times, as well as optional parameters like distance and average speed.
 */
@Data
public class TrainingRequest {

    /**
     * The ID of the user associated with the training.
     * This field is required.
     */
    @NotNull
    private Long userId;

    /**
     * The start time of the training session.
     * This field is required.
     */
    @NotNull
    private Date startTime;

    /**
     * The end time of the training session.
     * This field is required.
     */
    @NotNull
    private Date endTime;

    /**
     * The type of activity performed during the training session.
     * This field is required.
     */
    @NotNull
    private ActivityType activityType;

    /**
     * The distance covered during the training session, in kilometers or miles, depending on the context.
     * This field is optional.
     */
    private double distance;

    /**
     * The average speed during the training session, in km/h or mph, depending on the context.
     * This field is optional.
     */
    private double averageSpeed;
}
