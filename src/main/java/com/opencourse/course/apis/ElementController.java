package com.opencourse.course.apis;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opencourse.course.dtos.OutlineElementDto;
import com.opencourse.course.services.OutlineElementService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/course/element")
@AllArgsConstructor
public class ElementController {
    
    private final OutlineElementService service;

    //only teacher
    @PostMapping
    public ResponseEntity<Long> addOutlineElement(@RequestBody(required = true) OutlineElementDto element){
        Long userId=15L;
        return ResponseEntity.ok(service.addOutlineElement(element, userId));
    }

    // al users
    @GetMapping("/{elementId}")
    public ResponseEntity<OutlineElementDto> getElementById(@PathVariable(required = true) Long elementId) {
        Long userId=15L;
        return ResponseEntity.ok(service.getOutlineElementById(elementId, userId));
    }
    
    //all users
    @GetMapping
    public ResponseEntity<List<OutlineElementDto>> getCourseElements(@RequestParam(required = true) Long courseId){
        Long userId=15L;
        return ResponseEntity.ok(service.getOutlineElementByCourse(courseId, userId));
    }

    //only teacher
    @PutMapping
    public ResponseEntity<Object> updateElement(@RequestBody(required = true) OutlineElementDto outlineElementDto){
        Long userId=15L;
        service.updateOutlineElement(outlineElementDto, userId);
        return ResponseEntity.ok().build();
    }

    //only teacher
    @DeleteMapping("/{elementId}")
    public ResponseEntity<Object> deleteElementById(@PathVariable(required = true) Long elementId){
        Long userId=15L;
        service.deleteOutlineElementById(elementId, userId);
        return ResponseEntity.ok().build();
    }

}
