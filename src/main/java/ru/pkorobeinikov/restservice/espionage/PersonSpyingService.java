package ru.pkorobeinikov.restservice.espionage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.pkorobeinikov.restservice.entity.*;
import ru.pkorobeinikov.restservice.repository.PersonRepository;

import java.util.concurrent.*;

@Service
final public class PersonSpyingService {
    final private PersonRepository personRepository;

    @Autowired
    public PersonSpyingService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Async
    // def spyOnThePerson(identificator:String, coordinates:(Double,Double), R:Int, timeOutM:Int, callback: () => String)
    public String spyOnThePerson(String personId, Point point, int radius, int timeoutSec)
            throws ExecutionException, InterruptedException {
        Person person = personRepository.findById(personId);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new PersonSpyingTask(person, point, radius));

        String callbackOut;
        try {
            callbackOut = future.get(timeoutSec, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            callbackOut = "nothing.";
        }

        executor.shutdownNow();

        return callbackOut;
    }
}
