package com.opencourse.course.apis;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencourse.course.dtos.SectionDto;
import com.opencourse.course.services.SectionService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v1/course/section")
@AllArgsConstructor
public class SectionController {
    private final SectionService service;

    //only teacher
    @PostMapping
    public ResponseEntity<Long> addSection(@RequestBody(required = true) SectionDto sectionDto){
        Long userId=15L;
        return ResponseEntity.ok(service.addSection(sectionDto, userId));
    }

    //all users
    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionDto> getSectionById(@PathVariable(required = true) Long sectionId) {
        Long userId=15L;
        return ResponseEntity.ok(service.getSectionById(sectionId, userId));
    }

    // all users
    @GetMapping
    public ResponseEntity<List<SectionDto>> getSectionByCourseId(@RequestParam(required = true) Long courseId){
        Long userId=15L;
        return ResponseEntity.ok(service.getSectionByCourse(courseId, userId));
    }

    //teachers
    @PutMapping
    public ResponseEntity<Object> updateSection(@RequestBody(required = true) SectionDto sectionDto) {
        Long userId=15L;
        service.updateSection(sectionDto, userId);
        return ResponseEntity.ok().build();
    }

    //only other services
    @PostMapping("/creator")
    public ResponseEntity<Boolean> userCreatedSection(@RequestBody(required = true) Long userId,Long sectionId){
        return ResponseEntity.ok(service.userCreatedSection(sectionId, userId));
    }

}