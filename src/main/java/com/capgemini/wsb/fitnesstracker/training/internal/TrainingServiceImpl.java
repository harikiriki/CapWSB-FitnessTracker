package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingRequest;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Implementation of the TrainingProvider service for managing trainings.
 * Provides functionality for creating, updating, and retrieving trainings for users.
 * Uses TrainingRepository to persist training data.
 */
@Service
public class TrainingServiceImpl implements TrainingProvider {

    private final TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Optional<User> getTraining(final Long trainingId) {
        throw new UnsupportedOperationException("Not finished yet");
    }

    /**
     * Creates a new training based on the provided TrainingRequest and assigns it to a specific user.
     *
     * @param trainingRequest the request containing the details of the new training
     * @param user the user associated with the training
     * @return Training object
     */
    public Training createTraining(TrainingRequest trainingRequest, User user) {
        Training training = new Training(
                user,
                trainingRequest.getStartTime(),
                trainingRequest.getEndTime(),
                trainingRequest.getActivityType(),
                trainingRequest.getDistance(),
                trainingRequest.getAverageSpeed()
        );
        return trainingRepository.save(training);
    }

    /**
     * Retrieves all trainings from the repository.
     *
     * @return a list of all Training objects
     */
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll(); // Pobieramy wszystkie treningi z bazy danych
    }

    /**
     * Retrieves all trainings for a specific user.
     *
     * @param userId the ID of the user
     * @return List of trainings for the user
     */
    @Override
    public List<Training> getTrainingsByUserId(Long userId) {
        return trainingRepository.findAllByUserId(userId);
    }

    /**
     * Retrieves all trainings that ended after the specified date.
     *
     * @param date the date after which trainings are to be retrieved
     * @return List of trainings
     */
    public List<Training> getCompletedTrainingsAfter(Date date) {
        return trainingRepository.findAllByEndTimeAfter(date);
    }


    /**
     * Retrieves all trainings for a specific activity type.
     *
     * @param activityType the type of activity
     * @return List of trainings for the specified activity type
     */
    public List<Training> getTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findAllByActivityType(activityType);
    }

    /**
     * Updates an existing training with new data from the provided TrainingRequest and reassigns the user.
     *
     * @param trainingId the ID of the training to be updated
     * @param trainingRequest the request containing the updated training details
     * @param user the user associated with the updated training
     * @return the updated Training object
     * @throws IllegalArgumentException if no training is found with the specified ID
     */
    public Training updateTraining(Long trainingId, TrainingRequest trainingRequest, User user) {
        // Znajdź istniejący trening
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + trainingId));

        // Aktualizuj dane treningu
        training.updateStartTime(trainingRequest.getStartTime());
        training.updateEndTime(trainingRequest.getEndTime());
        training.updateActivityType(trainingRequest.getActivityType());
        training.updateDistance(trainingRequest.getDistance());
        training.updateAverageSpeed(trainingRequest.getAverageSpeed());
        training.setUser(user);

        // Zapisz zmiany
        return trainingRepository.save(training);
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

}
