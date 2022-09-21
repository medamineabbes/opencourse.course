package com.opencourse.course.apis;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.opencourse.course.dtos.CourseDto;
import com.opencourse.course.services.CourseService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/course")
@AllArgsConstructor
public class CourseController {
    private final CourseService courseService;
    //user must be teacher
    @PostMapping
    public ResponseEntity<Long> addCourse(@Valid @RequestBody(required = true) CourseDto courseDto){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(courseService.addCourse(courseDto,userId));
    }

    //user can be anyone
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable(required = true) Long id){

        Long userId;
        try{
            userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        }catch(Exception e){
            userId=null;
        }
        return ResponseEntity.ok(courseService.getCourseByIdAsClient(id, userId));
    }

    //user must be teacher
    @PutMapping
    public ResponseEntity<Object> updateCourse(@Valid @RequestBody(required = true) CourseDto courseDto){

        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        courseService.updateCourse(courseDto, userId);
        return ResponseEntity.ok().build();
    }

    //user can be anyone
    @GetMapping
    public ResponseEntity<Page<CourseDto>> searchForCourse(@RequestParam(required = true) String title,@RequestParam(defaultValue = "0") int page){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        //page size is 20 for now
        int pageSize=20;
        Pageable pageable=PageRequest.of(page, pageSize);
        return ResponseEntity.ok(courseService.searchForCourseByTitleAsClient(title, userId, pageable));
    }

    //user must be admin
    @PutMapping("/paid/{id}")
    public ResponseEntity<Object> setCourseAsPaid(@PathVariable(required = true) Long id){
        
        //user must be admin
        courseService.setCourseAsPaid(id);
        return ResponseEntity.ok().build();
    }

    //user must be admin
    @PutMapping("/free/{id}")
    public ResponseEntity<Object> setCourseAsFree(@PathVariable(required = true) Long id){

        courseService.setCourseAsFree(id);
        return ResponseEntity.ok().build();
    }

    //user must be teacher
    @PutMapping("/active/{id}")
    public ResponseEntity<Object> activateCourse(@PathVariable(required = true) Long id){

        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        courseService.activateCourse(id, userId);

        return ResponseEntity.ok().build();
    }

    // only authenticated users
    @GetMapping("/certificate/{courseId}")
    public ResponseEntity<?> getCourseSertificate(@PathVariable(required = true) Long courseId) throws IOException, WriterException{

        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        
        return ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_PDF)
        .body(courseService.getCertificate(courseId, userId));
    }

}
