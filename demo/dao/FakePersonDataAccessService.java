package com.example.demo.dao;

import com.example.demo.model.Person;
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
        Optional<Person> personMaybe = selectPersonByID(id);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        personMaybe.get().getMyClockTime().add(dateFormat.format(Calendar.getInstance().getTime()));
    }

    /*public void setSleepTimeTotal(UUID id) {
        Optional<Person> personMaybe = selectPersonByID(id);//.map(person ->{
        String temp = null;
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        if(personMaybe.get().getMyClockTime().size() > 1)
        {
            /*long sleptTime=0;

            for(int i =0; i< personMaybe.get().getMyClockTime().size();i++){
                //temp.add("2021-06-02T04:36:23");
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
                sleptTime += date2.getTime() - date1.getTime();
            }
            temp = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(sleptTime),
                    TimeUnit.MILLISECONDS.toMinutes(sleptTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sleptTime)),
                    TimeUnit.MILLISECONDS.toSeconds(sleptTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sleptTime)));

                   String it = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(1111515156),
                    TimeUnit.MILLISECONDS.toMinutes(1111115156) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(1111515156)),
                    TimeUnit.MILLISECONDS.toSeconds(1111515156) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(1111515156)));

        }
    }*/


    @Override
    public List<String> selectMyClockTimeByID(UUID id){
        Optional<Person> personMaybe = selectPersonByID(id);
        return personMaybe.get().getMyClockTime();
    }

    @Override
    public String getMySleepTimeTotal(UUID id) {//List<String> getMySleepTimeTotal(UUID id) {
//THERE'S AN ERROR WHEN ATTEMPTING TO GET AN ODD NUMBER OF CLOCKins.  i.e. 5 clock-ins.
        Optional<Person> personMaybe = selectPersonByID(id);
        if (personMaybe.get().getMyClockTime().size() > 1) {
            String temp;
            long sleptTime = 0;

            for (int i = 0; i < personMaybe.get().getMyClockTime().size(); i++) {
                if (i % 2 == 0) {
                    Date date1 = null;
                    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    try {
                        date1 = timeFormat.parse(personMaybe.get().getMyClockTime().get(i));
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    Date date2 = null;
                    try {
                        date2 = timeFormat.parse(personMaybe.get().getMyClockTime().get(i + 1));
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    sleptTime += date2.getTime() - date1.getTime();
                }
                else{
                    continue;
                }
            }
            temp = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(sleptTime),
                    TimeUnit.MILLISECONDS.toMinutes(sleptTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sleptTime)),
                    TimeUnit.MILLISECONDS.toSeconds(sleptTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sleptTime)));
            return temp;

        } else if (personMaybe.get().getMyClockTime().size() == 1) {
            String notClockedOut = "Error: Please clock-out to get a sleep total.";
            return notClockedOut;
        } else if (personMaybe.get().getMyClockTime().size() == 0) {
            String notClockedOut = "Error: No clock-in times logged. ";
            return notClockedOut;
        } else {
            return "";
        }
    }

}