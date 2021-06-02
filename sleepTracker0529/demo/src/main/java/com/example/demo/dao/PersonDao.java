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

    int updatePersonById(UUID id, Person update);

    List<String> selectMyClockTimeByID(UUID id);//, String targetDate);//List<List<String>> clockDate);
    List<String> getMySleepTimeTotal(UUID id);

    void addClockInTime(UUID id, boolean clockIn);
    void setSleepTimeTotal(UUID id);



}
