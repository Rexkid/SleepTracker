package com.example.demo.dao;

import com.example.demo.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonDao {

    int insertPerson(UUID id, Person person);

    default int insertPerson(Person person){
        UUID id = UUID.randomUUID();
        return insertPerson(id,person);
    }
    List<Person> selectAllPeople();

    Optional<Person> selectPersonByID(UUID id);
    int deletePersonById(UUID id);

    //SETTERS
    int updatePersonById(UUID id, Person update);
    void addClockInTime(UUID id, boolean clockIn);//boolean not necessary Maybe
    void addToMyFriendList(UUID myId, Person myFriend);

    //GETTERS
    List<String> selectMyClockTimeByID(UUID id);
    List<UUID>  getMyFriendList(UUID id);
    String getMySleepTimeTotal(UUID id);
}
