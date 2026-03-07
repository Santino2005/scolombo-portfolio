package com.baby_io.baby_io_app.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateBabyDTO {
  @NotNull
  @NotBlank
  private String name;

  @NotNull
  private int ageInMonths;

  @NotNull
  private Double weightInKilograms;

  @NotNull
  private boolean isBioVulnerable;

  @Nullable
  private String medicalNotes;

  @Pattern(regexp = "male|female", flags = Pattern.Flag.CASE_INSENSITIVE, message =
      "Gender must be male or female")
  private String gender;

  public UpdateBabyDTO() {}

  public UpdateBabyDTO(String name,
                       int ageInMonths,
                       Double weightInKilograms,
                       boolean isBioVulnerable,
                       String gender,
                       String medicalNotes){
    this.name = name;
    this.ageInMonths = ageInMonths;
    this.weightInKilograms = weightInKilograms;
    this.isBioVulnerable = isBioVulnerable;
    this.medicalNotes = medicalNotes;
    this.gender = gender;
  }

  public String getName() {return name;}
  public int getAgeInMonths() {return ageInMonths;}
  public Double getWeightInKilograms() {return weightInKilograms;}
  public boolean getIsBioVulnerable() {return isBioVulnerable;}
  public String getGender() {return gender;}
  public String getMedicalNotes() {return medicalNotes;}

}
