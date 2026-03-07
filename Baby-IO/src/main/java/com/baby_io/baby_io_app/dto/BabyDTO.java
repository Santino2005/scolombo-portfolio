package com.baby_io.baby_io_app.dto;

public class BabyDTO {
  private Long id;
  private String name;
  private Integer ageInMonths;
  private Double weightInKilograms;
  private boolean isBioVulnerable;
  private String gender;
  private String medicalNotes;
  private boolean isSelected;

  public BabyDTO() {}

  public BabyDTO(Long id, String name,
                 Integer ageInMonths,
                 Double weightInKilograms,
                 boolean isBioVulnerable,
                 String gender,
                 String medicalNotes,
                 boolean isSelected){
    this.id = id;
    this.name = name;
    this.ageInMonths = ageInMonths;
    this.weightInKilograms = weightInKilograms;
    this.isBioVulnerable = isBioVulnerable;
    this.gender = gender;
    this.medicalNotes = medicalNotes;
    this.isSelected = isSelected;
  }

  public Long getId() {
    return id;
  }
  public String getName() {return name;}
  public void setName(String name) {this.name = name;}
  public Double getWeightInKilograms() {return weightInKilograms;}
  public boolean getIsBioVulnerable() {return isBioVulnerable;}
  public String getGender() {return gender;}
  public Integer getAgeInMonths() {return ageInMonths;}
  public String getMedicalNotes() {return medicalNotes;}
  public boolean getIsSelected() {return isSelected;}

}