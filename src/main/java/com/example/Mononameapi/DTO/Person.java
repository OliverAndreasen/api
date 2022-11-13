package com.example.Mononameapi.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {
    String name;
    String gender;
    double genderProbability;
    int age;
    int ageCount;
    String country;
    double countryProbability;


    public void setGenderProbability(double probability){
        genderProbability = probability*100;

    }
    public void setCountryProbability(double probability){
        countryProbability = probability*100;
    }
}
