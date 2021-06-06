package com.example.demo.dao;

import com.example.demo.model.Person;
import org.jetbrains.annotations.NotNull;
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
    public int insertPerson(Person person) {
        return PersonDao.super.insertPerson(person);
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

    @Override
    public int addToMyFriendList(UUID myId, @NotNull Person myNewFriend){//THIS NEEDS TO CONFIRM THE ID IS VALID

        Optional<Person> personMaybe = selectPersonByID(myId);
        UUID myFriendMaybe = myNewFriend.getId();
        List<UUID> myFriendList = personMaybe.get().getMyFriendList();

        for(int counter = 0; counter < myFriendList.size(); counter++)
        {
            if(myFriendMaybe.equals(myFriendList.get(counter))){
                myFriendList.remove(counter);
                return 1;
            }
        }

        personMaybe.get().getMyFriendList().add(myNewFriend.getId());

        return 0;
    }

    @Override
    public List<String> selectMyClockTimeByID(UUID id){
        Optional<Person> personMaybe = selectPersonByID(id);
        return personMaybe.get().getMyClockTime();
    }

    @Override
    public List<UUID> getMyFriendList(UUID id) {
        Optional<Person> personMaybe = selectPersonByID(id);
        List<UUID> tempListOfFriendsUUID = personMaybe.get().getMyFriendList();

        //THIS IS WHERE THE FRIEND LIST WILL GET ORGANIZED TO HAVE FRIENDS IN ORDER.

        return tempListOfFriendsUUID;
    }

    @Override
    public String getMySleepTimeTotal(UUID id) { //THE NAME OF THIS IS BEST AS "getAllClockedTimes(UUID id)"
        Optional<Person> personMaybe = selectPersonByID(id);
        if (personMaybe.get().getMyClockTime().size() > 1) {
            String temp;
            long sleptTime = 0;

            for (int i = 0; i < personMaybe.get().getMyClockTime().size()-1; i++) {
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

    public List<String> getSortMyFriendListByIdAndLongestSleeper(List<UUID> myCurrentFriendList) {
        List<UUID> tempList = myCurrentFriendList;
        List<String> myListOfFriendTimes = new ArrayList<>();
        List<Long> TotalTimes = new ArrayList<>();

        String temp = "none.";

        for(int listCursor =0; listCursor < tempList.size (); listCursor++)
        {
            List<String> selectedFriendClockTimes = selectPersonByID(tempList.get(listCursor)).get().getMyClockTime();
            long sleptTime = 0;
            if( selectedFriendClockTimes.size() > 1) {
                for (int clockInTimesOfFriend = 0; clockInTimesOfFriend < selectedFriendClockTimes.size() - 1; clockInTimesOfFriend++) {
                    if (clockInTimesOfFriend % 2 == 0) {
                        Date date1 = null;
                        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        try {
                            date1 = timeFormat.parse(selectedFriendClockTimes.get(clockInTimesOfFriend));
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        Date date2 = null;
                        try {
                            date2 = timeFormat.parse(selectedFriendClockTimes.get(clockInTimesOfFriend + 1));
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        sleptTime += date2.getTime() - date1.getTime();
                    }
                    else {
                        continue;
                    }
                }
            }
            temp = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(sleptTime),
                    TimeUnit.MILLISECONDS.toMinutes(sleptTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sleptTime)),
                    TimeUnit.MILLISECONDS.toSeconds(sleptTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sleptTime)));
            TotalTimes.add(sleptTime);
            myListOfFriendTimes.add("Name: " + selectPersonByID(tempList.get(listCursor)).get().getName() + " - Slept Hours: " + temp );
        }

        //PUTS FRIEND LIST IN THE ORDER FROM LONGEST SLEEPER TO LEAST*/
        for(int i=0;i<TotalTimes.size();i++)
        {
            for(int j=i;j<TotalTimes.size();j++)
            {
                String tempFriendTimeHolder;
                //Gets the times from milliseconds
                if(TotalTimes.get(i)<TotalTimes.get(j))
                {
                    long tempLong=0;
                    tempFriendTimeHolder = myListOfFriendTimes.get(i);
                    myListOfFriendTimes.set(i,myListOfFriendTimes.get(j));
                    myListOfFriendTimes.set(j, tempFriendTimeHolder);

                    tempLong=TotalTimes.get(i);
                    TotalTimes.set(i,TotalTimes.get(j));
                    TotalTimes.set(j,tempLong);
                }
            }
        }
        return myListOfFriendTimes;
    }

    String sleptTimesWithinAPeriod(List<UUID> myFriendSleptTimes, int daysWithin){

        String sleptTimeWithinAPeriodByDays = null;
        //List<String> tempList = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        List<UUID> tempTotal = new ArrayList<>();
        int counter = 0;

        for(UUID id: myFriendSleptTimes)
        {
           // tempTotal.get(counter) = selectMyClockTimeByID(myFriendSleptTimes.get(counter));
           // counter++;
        }
        //THIS WILL CALCULATE TIME SLEPT WITHIN X AMOUNT OF DAYS.
        return sleptTimeWithinAPeriodByDays;
    }

}
