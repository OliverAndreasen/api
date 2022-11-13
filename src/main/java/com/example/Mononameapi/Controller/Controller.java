package com.example.Mononameapi.Controller;

import com.example.Mononameapi.DTO.Age;
import com.example.Mononameapi.DTO.Gender;
import com.example.Mononameapi.DTO.Nationality;
import com.example.Mononameapi.DTO.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class Controller {

    @GetMapping("/name-info")
    public Person getPerson(@RequestParam String name) {
        Mono<Age> age = getAgeFromName(name);
        Mono<Gender> gender = getGenderForName(name);
        Mono<Nationality> nationality = getNationalityFromName(name);

        var resMono = Mono.zip(age, gender, nationality).map(t -> {
            Person res = new Person();
            res.setAge(t.getT1().getAge());
            res.setAgeCount(t.getT1().getCount());

            res.setGender(t.getT2().getGender());
            res.setGenderProbability(t.getT2().getProbability());

            res.setCountry(t.getT3().getCountry().get(0).getCountry_id());
            res.setCountryProbability(t.getT3().getCountry().get(0).getProbability());

            return res;
        });
        Person res = resMono.block();
        res.setName(name);
        return res;
    }

    Mono<Gender> getGenderForName(String name) {
        WebClient client = WebClient.create();
        Mono<Gender> gender = client.get()
                .uri("https://api.genderize.io?name=" + name)
                .retrieve()
                .bodyToMono(Gender.class);
        return gender;
    }

    Mono<Age> getAgeFromName(String name) {
        WebClient client = WebClient.create();
        Mono<Age> age = client.get()
                .uri("https://api.agify.io?name=" + name)
                .retrieve()
                .bodyToMono(Age.class);
        return age;
    }

    Mono<Nationality> getNationalityFromName(String name){
        WebClient client = WebClient.create();
        Mono<Nationality> nationality = client.get()
                .uri("https://api.nationalize.io?name="+name)
                .retrieve()
                .bodyToMono(Nationality.class);
        return nationality;
    }

}
