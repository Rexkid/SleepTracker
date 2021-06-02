package com.example.demo.dao;

import com.example.demo.model.Person;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Repository("imaginaryDao")
public class FakePersonDataAccessService implements  PersonDao{
    private static List<Person> DB = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id,person.getName()));
        return 1; //This is so that we know that the insertion always works.
    }

    @Override
    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public Optional<Person> selectPersonByID(UUID id) {
        return DB.stream().filter(person -> person.getId().equals(id)).findFirst();
    }

    @Override
    public int deletePersonById(UUID id) {
        Optional<Person> personMaybe = selectPersonByID(id);
        if(personMaybe.isEmpty()){
            return 0;
        }
        DB.remove((personMaybe.get()));
        return 1;
    }

    @Override
    public int updatePersonById(UUID id, Person newPerson) {
        return selectPersonByID(id).map(person -> {
            int indexOfPersonToUpdate = DB.indexOf(person);
        if (indexOfPersonToUpdate >= 0) {
            DB.set(indexOfPersonToUpdate, new Person(id, newPerson.getName()));
            return 1;
        }
        return 0;
        })
                .orElse(0);
    }

    public void addClockInTime(UUID id, boolean clockIn){
        selectPersonByID(id).map(person -> {
            int indexOfPersonToUpdate = DB.indexOf(person);
            if (indexOfPersonToUpdate >= 0) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Optional<Person> personMaybe = selectPersonByID(id);
                personMaybe.get().getMyClockTime().add(dateFormat.format(Calendar.getInstance().getTime()));
                if(personMaybe.get().getMyClockTime().size() > 1)
                {
                    personMaybe.get().getMySleepTimeTotal().add("2021-06-02T04:36:23");
                }
                return 1;
            }
            return 0;
        })
                .orElse(0);
    }

    @Override
    public List<String> selectMyClockTimeByID(UUID id){
        Optional<Person> personMaybe = selectPersonByID(id);

        return personMaybe.get().getMyClockTime();
    }

    @Override
    public List<String> getMySleepTimeTotal(UUID id) {

        Optional<Person> personMaybe = selectPersonByID(id);
        List<String> temp = new ArrayList<>();// = personMaybe.get().getMyClockTime();

        if(personMaybe.get().getMyClockTime().size() > 1)
        {
            temp.add("2021-06-02T04:36:23");
        }  return temp;
    }

    public void setSleepTimeTotal(UUID id) {
        selectPersonByID(id).map(person -> {
            int indexOfPersonToUpdate = DB.indexOf(person);
            if (indexOfPersonToUpdate >= 0) {
                String myTotalSleptTime = null; //If this is not "AtomicReference then the for loop below cannot see it for some reason.  Confirm other methods or why this is.
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Optional<Person> personMaybe = selectPersonByID(id);
                for(int i =0; i< personMaybe.get().getMyClockTime().size();i++) {
                    Date date1 = null;
                    StdDateFormat timeFormat = null;
                    try {
                        date1 = timeFormat.parse(personMaybe.get().getMyClockTime().get(i));
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    Date date2 = null;
                    try {
                        date2 = timeFormat.parse(personMaybe.get().getMyClockTime().get(i+1));
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    long sleptTime = date2.getTime() - date1.getTime();
                    //double sleptTime = Integer.parseInt(time) - Integer.parseInt(timeFormat.format(Calendar.getInstance().getTime()));
                    myTotalSleptTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(sleptTime),
                            TimeUnit.MILLISECONDS.toMinutes(sleptTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sleptTime)),
                            TimeUnit.MILLISECONDS.toSeconds(sleptTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sleptTime)));
                }
                personMaybe.get().getMySleepTimeTotal().add(myTotalSleptTime);
            }
            return 1;
        })

                .orElse(0);
    }

}
