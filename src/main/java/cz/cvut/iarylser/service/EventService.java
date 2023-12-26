package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventService {
    private EventRepository repository;
//
//    public Object method() {
//        var allByCapacity = repository.findAllByCapacity(100);
//    }
}
