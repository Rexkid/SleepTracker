package com.example.demo.api;

import com.example.demo.model.Person;
import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/person")
@RestController
public class PersonController {

    private final PersonService personService;
    private UUID id;
    private @Valid boolean clockIn;

    /****************** AUTO CONTROLS ***************** */

    @Autowired
    public PersonController(PersonService personService)
    {
        this.personService = personService;
    }


    /****************** POST CONTROLS ***************** */

    @PostMapping
    public void addPerson(@Valid @NonNull @RequestBody Person person){
        personService.addPerson(person);
    }


    /****************** PUT CONTROLS ***************** */

    @GetMapping
    public List<Person> getAllPeople(){
        return personService.getAllPeople();
    }

    @GetMapping(path = "{id}")
    public Person getPersonById(@PathVariable("id") UUID id){
        return personService.getPersonById(id).orElse(null);
    }

    @GetMapping(path = "{id}"+"/myClockTime")
    public List<String> selectMyClockTimeByID(@PathVariable("id") UUID id){
        return personService.selectMyClockTimeByID(id);
    }

    @GetMapping(path = "{id}"+"/sleepTimeTotal")
    public String getMySleepTimeTotal(@PathVariable("id") UUID id){
        return personService.getMySleepTimeTotal(id);
    }

    @GetMapping(path = "{id}"+"/myFriendListById")
    public List<UUID> getMyFriendList(@PathVariable("id") UUID id){
       return personService.getMyFriendList(id);
    }

    @GetMapping(path = "{id}"+"/viewFriendListBySleepTimes")
    public List<String> getSortMyFriendListByIdAndLongestSleeper(@PathVariable("id") UUID id){
        this.id = id;
        List<UUID> myCurrentFriendList = this.getMyFriendList(id);
        return personService.getSortMyFriendListByIdAndLongestSleeper(myCurrentFriendList);
    }

    /****************** DELETE CONTROLS ***************** */
    @DeleteMapping(path = "{id}")
    public void deletePersonById(@PathVariable("id") UUID id){
        personService.deletePerson(id);
    }

    /*@DeleteMapping(path="{id}" + "/myFriendList")
    public void updateFriendByIdFromMyFriendList(@PathVariable("id") UUID id, @Valid @NonNull @RequestBody Person myOldFriend) {
        personService.updateFriendByIdFromMyFriendList(id,myOldFriend);
    }*/

    /****************** PUT CONTROLS ***************** */

    @PutMapping(path = "{id}")
    public int updatePersonById(@PathVariable("id") UUID id, @NonNull @RequestBody Person personToUpdate){
        return personService.updatePersonById(id, personToUpdate);
    }

    @PutMapping(path = "{id}"+"/myClockTime")
    public void addClockInTime(@PathVariable("id") UUID id, boolean clockIn){
        this.id = id;
        this.clockIn = clockIn;
        personService.addClockInTime(id,clockIn);
    }

    @PutMapping(path = "{id}"+"/myFriendListById")
    public int addToMyFriendList(@PathVariable("id") UUID myId, @RequestBody Person myNewFriend){
        this.id = myId;
        personService.addToMyFriendList(myId,myNewFriend);
        return 0;
    }


}
