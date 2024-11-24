package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.user.api.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingProvider {

    /**
     * Retrieves a training based on its ID.
     *
     * @param trainingId id of the training to be searched
     * @return An {@link Optional} containing the located Training, or {@link Optional#empty()} if not found
     */
    Optional<User> getTraining(Long trainingId);

    /**
     * Retrieves all trainings.
     *
     * @return List of all trainings
     */
    List<Training> getAllTrainings();

    /**
     * Retrieves all trainings for a specific user.
     *
     * @param userId the ID of the user
     * @return List of trainings for the user
     */
    List<Training> getTrainingsByUserId(Long userId);

    /**
     * Retrieves all trainings that ended after the specified date.
     *
     * @param date the date after which trainings are to be retrieved
     * @return List of trainings
     */
    List<Training> getCompletedTrainingsAfter(Date date);

    /**
     * Retrieves all trainings for a specific activity type.
     *
     * @param activityType the type of activity
     * @return List of trainings for the specified activity type
     */
    List<Training> getTrainingsByActivityType(ActivityType activityType);
}
