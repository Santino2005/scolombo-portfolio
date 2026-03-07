package com.baby_io.baby_io_app.service;

import com.baby_io.baby_io_app.dto.LullabyDTO;
import com.baby_io.baby_io_app.entity.Lullaby;
import com.baby_io.baby_io_app.repository.LullabyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LullabyService {

  @Autowired
  private LullabyRepository lullabyRepository;

  @PostConstruct
  public void initializeDefaultLullabies() {
    // Check if lullabies already exist
    if (lullabyRepository.count() == 0) {
      createDefaultLullabies();
    }
  }

  public List<LullabyDTO> getAllLullabies() {
    List<Lullaby> lullabies = lullabyRepository.findAll();
    return lullabies.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public LullabyDTO getLullabyById(Long id) {
    Lullaby lullaby = lullabyRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Lullaby not found with id: " + id));
    return convertToDTO(lullaby);
  }

  public LullabyDTO getLullabyByName(String name) {
    Lullaby lullaby = lullabyRepository.findByName(name)
        .orElseThrow(() -> new RuntimeException("Lullaby not found with name: " + name));
    return convertToDTO(lullaby);
  }

  public LullabyDTO getLullabyBySongNumber(Integer songNumber) {
    Lullaby lullaby = lullabyRepository.findBySongNumber(songNumber)
        .orElseThrow(() -> new RuntimeException("Lullaby not found with song number: " + songNumber));
    return convertToDTO(lullaby);
  }

  private void createDefaultLullabies() {
    List<Lullaby> defaultLullabies = Arrays.asList(
        new Lullaby("Gentle Rain Sounds",
            1,
            "Soft rain sounds to help baby fall asleep",
            60,
            false,
            "falling asleep"),

        new Lullaby("Classical Lullaby",
            2,"Traditional classical lullaby melodies",
            60,
            false,
            "deep sleep"),

        new Lullaby("White Noise",
            3,
            "Pure white noise for consistent sleep environment",
            60, false,
            "preventing wake up"),

        new Lullaby("Ocean Waves",
            4,
            "Calming ocean wave sounds",
            60,
            false,
            "extending sleep"),

        new Lullaby("Forest Sounds",
            5,
            "Peaceful forest ambiance with birds",
            60,
            false,
            "falling asleep"),

        new Lullaby("Soft Piano",
            6,
            "Gentle piano melodies for bedtime",
            60,
            false,
            "deep sleep"),

        new Lullaby("Heartbeat Sounds",
            7,
            "Mother's heartbeat simulation",
            60,
            false,
            "preventing wake up"),

        new Lullaby("Music Box",
            8,
            "Classic music box lullabies",
            60,
            false,
            "falling asleep"),

        new Lullaby("Fan Noise",
            9,
            "Consistent fan noise for white noise effect",
            60,
            false,
            "extending sleep"),

        new Lullaby("Gentle Humming",
            10,
            "Soft maternal humming sounds",
            60,
            false,
            "deep sleep")
    );

    lullabyRepository.saveAll(defaultLullabies);
  }

  private LullabyDTO convertToDTO(Lullaby lullaby) {
    LullabyDTO dto = new LullabyDTO();
    dto.setId(lullaby.getId());
    dto.setName(lullaby.getName());
    dto.setSongNumber(lullaby.getSongNumber());
    dto.setDescription(lullaby.getDescription());
    dto.setDurationSeconds(lullaby.getDurationSeconds());
    dto.setRecommendedFor(lullaby.getRecommendedFor());
    dto.setActive(lullaby.getActive());
    return dto;
  }

}