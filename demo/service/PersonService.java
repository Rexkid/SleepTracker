package com.example.demo.service;

import com.example.demo.dao.PersonDao;
import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    private final PersonDao personDao;
    private UUID id;

    @Autowired
    public PersonService(@Qualifier("imaginaryDao")PersonDao personDao){
        this.personDao = personDao;
    }

    public int addPerson(Person person) {
        return personDao.insertPerson(person);
    }

    //POST FUNCTIONS
    public List<Person> getAllPeople(){
        return personDao.selectAllPeople();
    }

    public Optional<Person> getPersonById(UUID id){
        return personDao.selectPersonByID(id);
    }

    public List<String> selectMyClockTimeByID(UUID id){
        this.id = id;
        return personDao.selectMyClockTimeByID(id);
    }
    public List<UUID> getMyFriendList(UUID id){
        this.id = id;
        return personDao.getMyFriendList(id);
    }
    public String getMySleepTimeTotal(UUID id){
        this.id = id;
        return personDao.getMySleepTimeTotal(id);
    }

        //DELETE FUNCTIONS
    public int deletePerson(UUID id){
        return personDao.deletePersonById(id);
    }
    public int updateFriendByIdFromMyFriendList(UUID id, Person myOldFriend) {
        return personDao.updateFriendByIdFromMyFriendList(id,myOldFriend);
    }


        //PUT FUNCTIONS
    public int updatePerson(UUID id, Person newPerson){
        return personDao.updatePersonById(id, newPerson);
    }
    public void addClockInTime(UUID id, boolean clockIn) {
         this.id =id;
        personDao.addClockInTime(id,clockIn);
    }
    public int addToMyFriendList(UUID myId, Person myNewFriend){ personDao.addToMyFriendList(myId,myNewFriend);
    return 0;}


}
