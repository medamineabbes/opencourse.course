package com.opencourse.course.apis;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    //user must be teacher/admin
    @PostMapping
    public ResponseEntity<Long> addCourse(@Valid @RequestBody(required = true) CourseDto courseDto){
        //get user id from token
        Long userId=15L;
        return ResponseEntity.ok(courseService.addCourse(courseDto,userId));
    }

    //user can be anyone
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable(required = true) Long id){
        //get user id from token
        Long userId=15L;
        return ResponseEntity.ok(courseService.getCourseByIdAsClient(id, userId));
    }

    //user must be teacher
    @PutMapping
    public ResponseEntity<Object> updateCourse(@Valid @RequestBody(required = true) CourseDto courseDto){
        //get user id from token
        Long userId=15L;
        courseService.updateCourse(courseDto, userId);
        return ResponseEntity.ok().build();
    }

    //user can be anyone
    @GetMapping
    public ResponseEntity<Page<CourseDto>> searchForCourse(@RequestParam(required = true) String title,@RequestParam(defaultValue = "0") int page){
        //get user id from token
        Long userId=15L;
        //page size is 20 for now
        int pageSize=20;
        Pageable pageable=PageRequest.of(page, pageSize);
        return ResponseEntity.ok(courseService.searchForCourseByTitleAsClient(title, userId, pageable));
    }

    //user must be admin
    @GetMapping("/paid/{id}")
    public ResponseEntity<Object> setCourseAsPaid(@PathVariable(required = true) Long id){
        
        //user must be admin
        courseService.setCourseAsPaid(id);
        return ResponseEntity.ok().build();
    }

    //user must be admin
    @GetMapping("/free/{id}")
    public ResponseEntity<Object> setCourseAsFree(@PathVariable(required = true) Long id){

        courseService.setCourseAsFree(id);
        return ResponseEntity.ok().build();
    }

    //user must be teacher
    @GetMapping("/active/{id}")
    public ResponseEntity<Object> activateCourse(@PathVariable(required = true) Long id){
        //get user  id from token
        Long userId=15L;
        courseService.activateCourse(id, userId);

        return ResponseEntity.ok().build();
    }

    // only authenticated users
    @GetMapping("/certificate/{courseId}")
    public ResponseEntity<?> getCourseSertificate(@PathVariable(required = true) Long courseId) throws IOException, WriterException{

        //get user id from token
        Long userId=15L;
        
        return ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_PDF)
        .body(courseService.getCertificate(courseId, userId));
    }

}
