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
    //private String totalSleptTime;
    // private boolean clockIn = false;
    //private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    //private UUID myFriendId;
    public Person(@JsonProperty("id") UUID id,
                  @JsonProperty("name") String name){
        this.id = id;
        this.name = name;
        //this.clockIn = true;
        //this.totalSleptTime = null;
        //Originally there is no time that is being set.  REMOVE UNTIL START AND STOP HAS BEEN USED.
        //myClockTime.add(dateFormat.format(Calendar.getInstance().getTime())+5);
        //myClockDate.add(myClockTime);
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

    //Get myClockTimes of person NOTE: It may be easier to just pass the address of the array
    //public String getMySleepTimeTotal() { return totalSleptTime;  }
}
