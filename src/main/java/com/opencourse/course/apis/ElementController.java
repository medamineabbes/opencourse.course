package com.opencourse.course.apis;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opencourse.course.dtos.OutlineElementDto;
import com.opencourse.course.services.OutlineElementService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/element")
@AllArgsConstructor
@Slf4j
public class ElementController {
    
    private final OutlineElementService service;

    //only teacher
    @PostMapping
    public ResponseEntity<Long> addOutlineElement(@RequestBody(required = true) OutlineElementDto element){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.addOutlineElement(element, userId));
    }

    // authentic users
    @GetMapping("/{elementId}")
    public ResponseEntity<OutlineElementDto> getElementById(@PathVariable(required = true) Long elementId) {
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.getOutlineElementById(elementId, userId));
    }
    
    //authentic users
    @GetMapping
    public ResponseEntity<List<OutlineElementDto>> getCourseElements(@RequestParam(required = true) Long courseId){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.getOutlineElementByCourse(courseId, userId));
    }

    //only teacher
    @PutMapping
    public ResponseEntity<Object> updateElement(@RequestBody(required = true) OutlineElementDto outlineElementDto){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        service.updateOutlineElement(outlineElementDto, userId);
        return ResponseEntity.ok().build();
    }

    //only teacher
    @DeleteMapping("/{elementId}")
    public ResponseEntity<Object> deleteElementById(@PathVariable(required = true) Long elementId){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("userId is " + userId);
        service.deleteOutlineElementById(elementId, userId);
        return ResponseEntity.ok().build();
    }

}
