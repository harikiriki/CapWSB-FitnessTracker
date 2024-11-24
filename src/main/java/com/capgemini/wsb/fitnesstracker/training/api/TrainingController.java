package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.api.TrainingRequest;
import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/trainings")
public class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final UserService userService;

    public TrainingController(TrainingServiceImpl trainingService, UserService userService) {
        this.trainingService = trainingService;
        this.userService = userService;
    }

    /**
     * Retrieves all trainings.
     *
     * @return a list of all Training objects
     */
    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        List<Training> trainings = trainingService.getAllTrainings();
        return ResponseEntity.ok(trainings); // Zwracamy listę wszystkich treningów
    }

    /**
     * Retrieves all trainings for a specific user by their user ID.
     *
     * @param userId the ID of the user whose trainings are to be retrieved
     * @return a list of Training objects for the specified user
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<Training>> getTrainingsByUserId(@PathVariable Long userId) {
        // Sprawdzamy, czy użytkownik istnieje
        User user = findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Zwracamy 404, jeśli użytkownik nie istnieje
        }

        // Pobieramy treningi dla użytkownika
        List<Training> trainings = trainingService.getTrainingsByUserId(userId);
        return ResponseEntity.ok(trainings);
    }

    /**
     * Retrieves all trainings that were finished after the specified date.
     *
     * @param afterTime a string representing the date (in "yyyy-MM-dd" format) after which the trainings are to be retrieved
     * @return a list of Training objects completed after the specified date
     */
    @GetMapping("/finished/{afterTime}")
    public ResponseEntity<List<Training>> getFinishedTrainingsAfter(@PathVariable String afterTime) {
        try {
            // Konwersja tekstowej daty na obiekt `Date`
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(afterTime);

            // Pobranie treningów zakończonych po podanej dacie
            List<Training> trainings = trainingService.getCompletedTrainingsAfter(date);
            return ResponseEntity.ok(trainings);

        } catch (ParseException e) {
            // W przypadku błędu parsowania daty, zwróć 400 (Bad Request)
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves all trainings that were performed for a specific activity type.
     *
     * @param activityType the type of activity for which trainings are to be retrieved
     * @return a list of Training objects for the specified activity type
     */
    @GetMapping("/activityType")
    public ResponseEntity<List<Training>> getTrainingsByActivityType(@RequestParam String activityType) {
        try {
            ActivityType type = ActivityType.valueOf(activityType.toUpperCase()); // Zamiana na wielkie litery
            List<Training> trainings = trainingService.getTrainingsByActivityType(type);
            return ResponseEntity.ok(trainings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Zwraca 400, jeśli podano nieprawidłowy typ aktywności
        }
    }

    /**
     * Creates a new training based on the provided TrainingRequest.
     * The user associated with the training is determined from the request.
     *
     * @param trainingRequest the request containing the details of the new training
     * @return a ResponseEntity containing the created Training object
     */
    @PostMapping
    public ResponseEntity<Training> createTraining(@Valid @RequestBody TrainingRequest trainingRequest) {
        // Znajdź użytkownika na podstawie userId z ciała żądania
        User user = findUserById(trainingRequest.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Tworzenie treningu
        Training createdTraining = trainingService.createTraining(trainingRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTraining);
    }

    /**
     * Updates an existing training with the provided TrainingRequest.
     * The user associated with the training is determined from the request.
     *
     * @param trainingId the ID of the training to be updated
     * @param trainingRequest the request containing the updated details of the training
     * @return a ResponseEntity containing the updated Training object
     */
    @PutMapping("/{trainingId}")
    public ResponseEntity<Training> updateTraining(
            @PathVariable Long trainingId,
            @Valid @RequestBody TrainingRequest trainingRequest) {

        // Znajdź użytkownika na podstawie userId z TrainingRequest
        User user = findUserById(trainingRequest.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Aktualizacja treningu
        Training updatedTraining = trainingService.updateTraining(trainingId, trainingRequest, user);
        return ResponseEntity.ok(updatedTraining);
    }

    /**
     * Finds a user by their ID using the UserService.
     *
     * @param userId the ID of the user to be found
     * @return the User if found, or null if the user does not exist
     */
    private User findUserById(Long userId) {
        // Używamy UserService do znalezienia użytkownika
        return userService.getUser(userId).orElse(null); // Zwraca null, jeśli użytkownik nie zostanie znaleziony
    }
}
