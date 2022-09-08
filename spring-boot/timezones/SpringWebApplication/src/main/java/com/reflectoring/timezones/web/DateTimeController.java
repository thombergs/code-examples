package com.reflectoring.timezones.web;

import com.reflectoring.timezones.mapper.DateTimeMapper;
import com.reflectoring.timezones.model.DateTime;
import com.reflectoring.timezones.model.DateTimeEntity;
import com.reflectoring.timezones.service.DateTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("app/v1/timezones")
public class DateTimeController {

    private static final Logger log = LoggerFactory.getLogger(DateTimeController.class);

    private final DateTimeService dateTimeService;


    public DateTimeController(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @PostMapping("/default")
    public ResponseEntity<DateTime> save() {
        List<DateTimeEntity> fromDb = dateTimeService.saveDateTime();
        DateTime obj = DateTimeMapper.mapToDateTime(fromDb.get(fromDb.size() - 1), null);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/dst")
    public ResponseEntity<DateTime> saveNonDst() {
        List<DateTimeEntity> fromDb = dateTimeService.saveCustomDateTime();
        DateTime obj = DateTimeMapper.mapToDateTime(fromDb.get(fromDb.size() - 1), null);
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DateTime> getDateTime(@PathVariable Integer id, @RequestParam String timezone) {
        DateTimeEntity entity = dateTimeService.fetchDateTime(id);
        DateTime dateTime = DateTimeMapper.mapToDateTime(entity, timezone);
        return ResponseEntity.ok(dateTime);

    }


}
