package org.example.oops;

import org.example.oops.repository.OpeningExceptionRepository;
import org.example.oops.repository.OpeningHourRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HoursService {
    private final OpeningHourRepository hoursRepo;
    private final OpeningExceptionRepository exceptionsRepo;

    public HoursService(OpeningHourRepository h, OpeningExceptionRepository e) {
        this.hoursRepo = h;
        this.exceptionsRepo = e;
    }

    public List<OpeningHour> getAllHours() {
        return hoursRepo.findAll();
    }

    public List<OpeningException> getAllExceptions() {
        return exceptionsRepo.findAll();
    }

    public OpeningHour saveHour(OpeningHour hour) {
        return hoursRepo.save(hour);
    }

    public OpeningException saveException(OpeningException ex) {
        return exceptionsRepo.save(ex);
    }

    public void deleteException(Integer id) {
        exceptionsRepo.deleteById(id);
    }
}
