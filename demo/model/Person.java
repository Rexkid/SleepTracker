package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Person{
    private  final  UUID id;
    @NotBlank
    private final String name;

    private final List<String> myClockTime = new ArrayList<>();
    private final List<UUID> myFriendList = new ArrayList<>();

    public Person(@JsonProperty("id") UUID id,
                  @JsonProperty("name") String name){
        this.id = id;
        this.name = name;
    }

    //Get ID of person
    public UUID getId() {
        return id;
    }

    //Get name of person
    public String getName() {
        return name;
    }

    public List<String> getMyClockTime() { return myClockTime;  }

    public List<UUID> getMyFriendList(){ return myFriendList; }

}
