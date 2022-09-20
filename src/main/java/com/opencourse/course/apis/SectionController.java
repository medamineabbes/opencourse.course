package com.opencourse.course.apis;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/v1/section")
@AllArgsConstructor
public class SectionController {
    private final SectionService service;

    //only teacher
    @PostMapping
    public ResponseEntity<Long> addSection(@RequestBody(required = true) SectionDto sectionDto){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.addSection(sectionDto, userId));
    }

    //authentic users
    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionDto> getSectionById(@PathVariable(required = true) Long sectionId) {
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.getSectionById(sectionId, userId));
    }

    // authentic users
    @GetMapping
    public ResponseEntity<List<SectionDto>> getSectionByCourseId(@RequestParam(required = true) Long courseId){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.getSectionByCourse(courseId, userId));
    }

    //teachers
    @PutMapping
    public ResponseEntity<Object> updateSection(@RequestBody(required = true) SectionDto sectionDto) {
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        service.updateSection(sectionDto, userId);
        return ResponseEntity.ok().build();
    }

    //all users
    @PostMapping("/creator")
    public ResponseEntity<Boolean> userCreatedSection(@RequestParam(required = true) Long userId,@RequestParam(required = true) Long sectionId){
        return ResponseEntity.ok(service.userCreatedSection(sectionId, userId));
    }

}
