package com.baby_io.baby_io_app.service;

import com.baby_io.baby_io_app.dto.BabyDTO;
import com.baby_io.baby_io_app.dto.SleepHistoryDTO;
import com.baby_io.baby_io_app.dto.SleepRoutineDTO;
import com.baby_io.baby_io_app.dto.UpdateBabyDTO;
import com.baby_io.baby_io_app.entity.*;
import com.baby_io.baby_io_app.repository.BabyRepository;
import com.baby_io.baby_io_app.repository.SleepHistoryRepository;
import com.baby_io.baby_io_app.repository.UserRepository;
import com.baby_io.baby_io_app.repository.SleepRoutineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BabyService {

  private final BabyRepository babyRepository;
  private final UserRepository userRepository;
  private final SleepRoutineRepository sleepRoutineRepository;
  private final SleepHistoryRepository sleepHistoryRepository;

  @Autowired
  public BabyService(BabyRepository babyRepository, UserRepository userRepository, SleepRoutineRepository sleepRoutineRepository, SleepHistoryRepository sleepHistoryRepository) {
    this.babyRepository = babyRepository;
    this.userRepository = userRepository;
    this.sleepRoutineRepository = sleepRoutineRepository;
    this.sleepHistoryRepository = sleepHistoryRepository;
  }

  public Optional<BabyDTO> findBabyById(Long userId, Long babyId) {
    return userRepository.findById(userId)
        .flatMap(user -> babyRepository.findById(babyId)
            .filter(baby -> baby.getUser().getId().equals(userId))
            .map(baby -> new BabyDTO(
                baby.getId(),
                baby.getName(),
                baby.getAgeInMonths(),
                baby.getWeightInKilograms(),
                baby.getIsBioVulnerable(),
                baby.getGender(),
                baby.getMedicalNotes(),
                baby.getIsSelected())));
  }

  public Optional<BabyDTO> createBaby(Long userId, UpdateBabyDTO updateBabyDTO) {
    return userRepository.findById(userId)
        .map(user -> {

          Baby baby = new Baby();
          baby.setName(updateBabyDTO.getName());
          baby.setAgeInMonths(updateBabyDTO.getAgeInMonths());
          baby.setWeightInKilograms(updateBabyDTO.getWeightInKilograms());
          baby.setIsBioVulnerable(updateBabyDTO.getIsBioVulnerable());
          baby.setGender(updateBabyDTO.getGender());
          baby.setMedicalNotes(updateBabyDTO.getMedicalNotes());
          baby.setUser(user);
          baby.setSelected(false);

          Baby savedBaby = babyRepository.save(baby);


          SleepHistory sleepHistory = new SleepHistory();
          sleepHistory.setBaby(savedBaby);
          sleepHistory = sleepHistoryRepository.save(sleepHistory);

          savedBaby.setSleepHistory(sleepHistory);
          babyRepository.save(savedBaby);

          return new BabyDTO(
              savedBaby.getId(),
              savedBaby.getName(),
              savedBaby.getAgeInMonths(),
              savedBaby.getWeightInKilograms(),
              savedBaby.getIsBioVulnerable(),
              savedBaby.getGender(),
              savedBaby.getMedicalNotes(),
              savedBaby.getIsSelected());
        });
  }

  public Collection<BabyDTO> getAllBabiesByUserId(Long userId) {
    Collection<Baby> babies = babyRepository.findAllByUserId(userId);
    return babies.stream()
        .map(baby -> new BabyDTO(
            baby.getId(),
            baby.getName(),
            baby.getAgeInMonths(),
            baby.getWeightInKilograms(),
            baby.getIsBioVulnerable(),
            baby.getGender(),
            baby.getMedicalNotes(),
            baby.getIsSelected()))
        .collect(Collectors.toList());
  }

  public boolean deleteBaby(Long userId, Long babyId) {
    return babyRepository.findById(babyId)
        .filter(baby -> baby.getUser().getId().equals(userId))
        .map(baby -> {
          babyRepository.deleteById(babyId);
          return true;
        })
        .orElse(false);
  }

  public Optional<BabyDTO> updateBaby(Long userId, Long babyId, UpdateBabyDTO babyDTO) {
    return userRepository.findById(userId)
            .flatMap(user -> babyRepository.findById(babyId)
                    .filter(baby -> baby.getUser().getId().equals(userId))
                    .map(baby -> {
                      // First, remove this baby from all sleep routines that have it assigned
                      Set<SleepRoutine> currentRoutines = new HashSet<>(baby.getSleepRoutines());
                      for (SleepRoutine routine : currentRoutines) {
                        routine.getBabies().remove(baby);
                        sleepRoutineRepository.save(routine);
                      }

                      // Update baby details
                      baby.setName(babyDTO.getName());
                      baby.setAgeInMonths(babyDTO.getAgeInMonths());
                      baby.setWeightInKilograms(babyDTO.getWeightInKilograms());
                      baby.setIsBioVulnerable(babyDTO.getIsBioVulnerable());
                      baby.setGender(babyDTO.getGender());
                      baby.setUser(user);
                      baby.setMedicalNotes(babyDTO.getMedicalNotes());

                      // Clear the baby's sleep routines
                      baby.setSleepRoutines(new HashSet<>());

                      Baby savedBaby = babyRepository.save(baby);
                      return new BabyDTO(
                              savedBaby.getId(),
                              savedBaby.getName(),
                              savedBaby.getAgeInMonths(),
                              savedBaby.getWeightInKilograms(),
                              savedBaby.getIsBioVulnerable(),
                              savedBaby.getGender(),
                              savedBaby.getMedicalNotes(),
                              savedBaby.getIsSelected()
                      );
                    }));
  }

  public Optional<Collection<SleepRoutineDTO>> getSleepRoutinesByBabyId(Long babyId, Long userId) {
    Optional<Baby> babyOpt = babyRepository.findById(babyId);
    Optional<User> userOpt = userRepository.findById(userId);

    if (babyOpt.isPresent() && userOpt.isPresent()) {
      Baby baby = babyOpt.get();
      if (baby.getUser().getId().equals(userId)) {
        List<SleepRoutineDTO> routineDTOs = new ArrayList<>();
        for (SleepRoutine routine : baby.getSleepRoutines()) {
          SleepRoutineDTO dto = new SleepRoutineDTO();
          dto.setId(routine.getId());
          dto.setName(routine.getName());
          dto.setDescription(routine.getDescription());
          dto.setDefaultDurationMinutes(routine.getPlannedDurationMinutes());
          dto.setEnableAlerts(routine.getEnableAlerts());
          dto.setMediumAlertTimeoutSeconds(routine.getMediumAlertTimeoutSeconds());
          dto.setHighAlertTimeoutSeconds(routine.getHighAlertTimeoutSeconds());
          routineDTOs.add(dto);
        }
        return Optional.of(routineDTOs);
      }
    }
    return Optional.empty();
  }

    public Optional<SleepRoutineDTO> setSleepRoutine(Long userId, Long babyId, Long sleepRoutineId) {
        Optional<Baby> babyOpt = babyRepository.findById(babyId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (babyOpt.isPresent() && userOpt.isPresent()) {
            Baby baby = babyOpt.get();
            if (baby.getUser().getId().equals(userId)) {

                Optional<SleepRoutine> sleepRoutineOptional = sleepRoutineRepository.findById(sleepRoutineId);
                if (sleepRoutineOptional.isEmpty()) {
                    return Optional.empty();
                }

                SleepRoutine sleepRoutine = sleepRoutineOptional.get();

                // Check if routine is already assigned to this baby
                if (sleepRoutine.getBabies().contains(baby)) {
                    SleepRoutineDTO resultDTO = new SleepRoutineDTO();
                    resultDTO.setId(sleepRoutine.getId());
                    resultDTO.setName(sleepRoutine.getName());
                    resultDTO.setDescription(sleepRoutine.getDescription());
                    resultDTO.setDefaultDurationMinutes(sleepRoutine.getPlannedDurationMinutes());
                    resultDTO.setEnableAlerts(sleepRoutine.getEnableAlerts());
                    resultDTO.setMediumAlertTimeoutSeconds(sleepRoutine.getMediumAlertTimeoutSeconds());
                    resultDTO.setHighAlertTimeoutSeconds(sleepRoutine.getHighAlertTimeoutSeconds());
                    return Optional.of(resultDTO);
                }

                // ONLY enforce strict rules for bio-vulnerable babies
                if (baby.getIsBioVulnerable()) {
                    if (!sleepRoutine.getEnableAlerts()) {
                        return Optional.empty(); // Alerts must be enabled
                    }

                    if (sleepRoutine.getSensorConfigurations() == null ||
                            sleepRoutine.getSensorConfigurations().isEmpty()) {
                        return Optional.empty(); // Sensors must be configured
                    }

                    boolean allSensorsEnabled = sleepRoutine.getSensorConfigurations().stream()
                            .allMatch(SensorConfiguration::getEnabled);

                    if (!allSensorsEnabled) {
                        return Optional.empty(); // All sensors must be enabled
                    }
                }

                // For non-bio-vulnerable babies, allow assignment regardless of sensors/alerts
                sleepRoutine.getBabies().add(baby);
                SleepRoutine savedRoutine = sleepRoutineRepository.save(sleepRoutine);

                baby.getSleepRoutines().add(savedRoutine);
                babyRepository.save(baby);

                SleepRoutineDTO resultDTO = new SleepRoutineDTO();
                resultDTO.setId(savedRoutine.getId());
                resultDTO.setName(savedRoutine.getName());
                resultDTO.setDescription(savedRoutine.getDescription());
                resultDTO.setDefaultDurationMinutes(savedRoutine.getPlannedDurationMinutes());
                resultDTO.setEnableAlerts(savedRoutine.getEnableAlerts());
                resultDTO.setMediumAlertTimeoutSeconds(savedRoutine.getMediumAlertTimeoutSeconds());
                resultDTO.setHighAlertTimeoutSeconds(savedRoutine.getHighAlertTimeoutSeconds());

                return Optional.of(resultDTO);
            }
        }
        return Optional.empty();
    }

  public boolean removeSleepRoutine(Long userId, Long babyId, Long sleepRoutineId) {
    Optional<Baby> babyOpt = babyRepository.findById(babyId);
    Optional<User> userOpt = userRepository.findById(userId);
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(sleepRoutineId);

    if (babyOpt.isPresent() && userOpt.isPresent() && routineOpt.isPresent()) {
      Baby baby = babyOpt.get();
      SleepRoutine routine = routineOpt.get();

      if (baby.getUser().getId().equals(userId)) {

        baby.getSleepRoutines().remove(routine);
        babyRepository.save(baby);

        routine.getBabies().remove(baby);
        sleepRoutineRepository.save(routine);

        return true;
      }
    }
    return false;
  }

  public Optional<SleepHistoryDTO> getSleepHistoryByBabyId(Long babyId, Long userId) {
    Optional<Baby> babyOpt = babyRepository.findById(babyId);
    Optional<User> userOpt = userRepository.findById(userId);

    if (babyOpt.isPresent() && userOpt.isPresent()) {
      Baby baby = babyOpt.get();
      if (baby.getUser().getId().equals(userId)) {
        SleepHistory sleepHistory = baby.getSleepHistory();
        if (sleepHistory != null) {
          return Optional.of(new SleepHistoryDTO(
              sleepHistory.getId(),
              sleepHistory.getTrackingStartDate(),
              sleepHistory.getTotalSessions(),
              sleepHistory.getTotalSleepMinutes(),
              sleepHistory.getTotalWakeUps(),
              sleepHistory.getLongestSleepMinutes(),
              sleepHistory.getAverageTemperature(),
              sleepHistory.getAverageHumidity(),
              sleepHistory.getAverageSound(),
              sleepHistory.getAverageMotion(),
              sleepHistory.getRecommendations()
          ));
        }
      }
    }
    return Optional.empty();
  }

  public Optional<BabyDTO> getSelectedBaby(Long userId) {
    return babyRepository.findByUserIdAndIsSelectedTrue(userId)
        .map(baby -> new BabyDTO(
            baby.getId(),
            baby.getName(),
            baby.getAgeInMonths(),
            baby.getWeightInKilograms(),
            baby.getIsBioVulnerable(),
            baby.getGender(),
            baby.getMedicalNotes(),
            true));
  }

  @Transactional
  public Optional<BabyDTO> selectBaby(Long userId, Long babyId) {
    Optional<Baby> babyToSelect = babyRepository.findById(babyId)
        .filter(baby -> baby.getUser().getId().equals(userId));

    if (babyToSelect.isEmpty()) {
      return Optional.empty();
    }

    Baby baby = babyToSelect.get();

    babyRepository.findByUserIdAndIsSelectedTrue(userId)
        .ifPresent(currentSelected -> {
          currentSelected.setSelected(false);
          babyRepository.save(currentSelected);
        });

    baby.setSelected(true);
    Baby selectedBaby = babyRepository.save(baby);

    return Optional.of(new BabyDTO(
        selectedBaby.getId(),
        selectedBaby.getName(),
        selectedBaby.getAgeInMonths(),
        selectedBaby.getWeightInKilograms(),
        selectedBaby.getIsBioVulnerable(),
        selectedBaby.getGender(),
        selectedBaby.getMedicalNotes(),
        true));
  }

}