package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
interface TrainingRepository extends JpaRepository<Training, Long> {
    /**
     * Retrieves all trainings for a specific user.
     *
     * @param userId the ID of the user
     * @return List of trainings for the user
     */
    List<Training> findAllByUserId(Long userId);

    /**
     * Retrieves all trainings that ended after the specified date.
     *
     * @param date the date after which trainings are to be retrieved
     * @return List of trainings
     */
    List<Training> findAllByEndTimeAfter(Date date);

    /**
     * Retrieves all trainings for a specific activity type.
     *
     * @param activityType the type of activity
     * @return List of trainings for the specified activity type
     */
    List<Training> findAllByActivityType(ActivityType activityType);

}
