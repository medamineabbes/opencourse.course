package com.opencourse.course.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;
import com.opencourse.course.dtos.CourseDto;
import com.opencourse.course.dtos.UserInfoDto;
import com.opencourse.course.entities.Certificate;
import com.opencourse.course.entities.Course;
import com.opencourse.course.entities.CourseTopic;
import com.opencourse.course.entities.Difficulty;
import com.opencourse.course.exceptions.CourseNotFoundException;
import com.opencourse.course.exceptions.CourseNotValidException;
import com.opencourse.course.exceptions.TopicNotFoundException;
import com.opencourse.course.exceptions.UnAuthorizedActionException;
import com.opencourse.course.externalservices.ChapterService;
import com.opencourse.course.externalservices.QuizService;
import com.opencourse.course.externalservices.UserService;
import com.opencourse.course.repos.CertificateRepo;
import com.opencourse.course.repos.CourseRepo;
import com.opencourse.course.repos.CourseTopicRepo;

import lombok.AllArgsConstructor;



@Service
@AllArgsConstructor
public class CourseService {
    private final  CourseRepo courseRepo;
    private final CourseTopicRepo courseTopicRepo;
    private final CertificateRepo certfifcateRepo;
    private final ChapterService chapterService;
    private final QuizService quizService;
    private final CertificateGenerationService certififcateService;
    private final UserService userService;
    // CRUD COURSE
    //only teacher or admin 
    public Long addCourse(CourseDto courseDto,Long creatorId) {
        Course course=CourseDto.toCourse(courseDto);
        course.setActive(false);
        CourseTopic topic=courseTopicRepo
        .findById(courseDto.getTopicId())
        .orElseThrow(()->new TopicNotFoundException(courseDto.getTopicId())
        );
        course.setTopic(topic);
        courseRepo.save(course);
        return course.getId();
    }
    
    //all users
    public CourseDto getCourseByIdAsClient(Long id,Long userId) {
        //findCourse
        Course course=courseRepo
        .findById(id)
        .orElseThrow(()->new CourseNotFoundException(id));

        //only active courses
        if(!course.isActive() && !course.getCreatorId().equals(userId))
        throw new CourseNotFoundException(id);

        CourseDto courseDto=CourseDto.toDto(course);

        //see if course is finished by user
        boolean finished=certfifcateRepo
        .findByCourseIdAndUserId(id, userId)
        .isPresent();
        courseDto.setFinished(finished);
        return courseDto;
    }

    //(teacher and course creator)
    public void updateCourse(CourseDto courseDto,Long userId){
        //make sure course exists
        Course course=courseRepo
        .findById(courseDto.getId())
        .orElseThrow(()->new CourseNotFoundException(courseDto.getId()));

        //make sure topic exists
        CourseTopic topic=courseTopicRepo.findById(courseDto.getTopicId())
        .orElseThrow(()->new TopicNotFoundException(courseDto.getTopicId()));
        
        //make sure user is course creator
        if(!course.getCreatorId().equals(userId))
        throw new UnAuthorizedActionException();


        course.setActive(courseDto.isActive());
        course.setAvgTimeInHours(courseDto.getAvgTimeInHours());
        //add exception handling for string value 
        course.setDiffiulty(Difficulty.valueOf(courseDto.getDiffiulty()));
        course.setFree(courseDto.isFree());
        course.setImageUrl(courseDto.getImageUrl());
        course.setIntroVideoUrl(courseDto.getIntroVideoUrl());
        course.setTitle(courseDto.getTitle());
        course.setTopic(topic);
        courseRepo.flush();
    }

    //OTHER OPERATIONS
    //all users
    public Page<CourseDto> searchForCourseByTitleAsClient(String title,Long userId,Pageable pageable){
        //saearch for active courses
        Page<CourseDto> courses=courseRepo
        .findByActiveAndTitleContaining(true,title,pageable)
        .map((course)->CourseDto.toDto(course));

        //se finished courses as finished
        courses.forEach((course)->{
            boolean finished=certfifcateRepo
            .findByCourseIdAndUserId(course.getId(), userId)
            .isPresent();
            course.setFinished(finished);
        });
        
        return courses;
    }

    //only admin
    public void setCourseAsPaid(Long courseId){
        //make sure exists
        Course c=courseRepo.findById(courseId)
        .orElseThrow(()->new CourseNotFoundException(courseId));

        c.setFree(false);
        courseRepo.flush();
    }

    //only admin
    public void setCourseAsFree(Long courseId){
        //make sure course exists
       Course c=courseRepo.findById(courseId)
       .orElseThrow(()->new CourseNotFoundException(courseId));
       
       c.setFree(true);
       courseRepo.flush();
    }

    //only course creator
    public void activateCourse(Long courseId,Long userId){
        //make sure course exists
        Course course=courseRepo
        .findById(courseId)
        .orElseThrow(()->new CourseNotFoundException(courseId));
        
        //make sure user is course creator
        if(!course.getCreatorId().equals(userId))
        throw new UnAuthorizedActionException();

        //make sure course hase sections
        if(
            course.getSections() == null ||
            course.getSections().size()==0
        )
        throw new CourseNotValidException(courseId);

        List<Long> sectionIds=course.getSections()
        .stream()
        .map((section) -> section.getId())
        .collect(Collectors.toList());

        //make sure course quizs and chapters are valid
        if(!chapterService.validSections(sectionIds) || !quizService.validSections(sectionIds))
        throw new CourseNotValidException(courseId);
        
        course.setActive(true);
        courseRepo.flush();
    }

    //needs testing
    //only authenticated users
    public byte[] getCertificate(Long courseId,Long userId) throws IOException, WriterException{

        //fetch sertificate from database
        Optional<Certificate> opCert=certfifcateRepo.findByCourseIdAndUserId(courseId, userId);
        
        //make sure course exists
        Course course=courseRepo.findById(courseId)
        .orElseThrow(()->new CourseNotFoundException(courseId));
        
        Map<String,Object> data=new HashMap<>();
        
        String templateName="Certificate";

        //get user info from userService
        UserInfoDto userInfoDto=userService.getUserInfo(userId);
           
        data.put("firstname", userInfoDto.getFirstname());          
        data.put("lastname", userInfoDto.getLastname());           
        data.put("yearofbirth", userInfoDto.getDateOfBirth().getYear());           
        data.put("monthofbirth", userInfoDto.getDateOfBirth().getMonthValue());           
        data.put("dayofbirth",userInfoDto.getDateOfBirth().getDayOfMonth());
        data.put("coursetitle",course.getTitle());
        

        //if certificate is not in the database
        if(opCert.isEmpty()){
            List<Long> sectionIds=course.getSections().stream()
            .map((section)->section.getId())
            .collect(Collectors.toList());
            boolean finished=quizService.finishedSections(sectionIds,userId);

            if(!finished) //if user didnt finish the course
            templateName="CertificateExample";
            else{ // if user finished the course

                //add certificate to datbase
                Certificate certificate=new Certificate();
                certificate.setCourse(course);
                certificate.setDate(LocalDateTime.now());
                certificate.setUserId(userId);
                certfifcateRepo.save(certificate);
                data.put("yearofcertification", certificate.getDate().getYear());
                data.put("monthofcertification", certificate.getDate().getMonthValue());
                data.put("dayofcertification", certificate.getDate().getDayOfMonth());
            }
        }else{//certificate exists in database
            data.put("yearofcertification", "----");
            data.put("monthofcertification", "--");
            data.put("dayofcertification", "--");
        }

        //generate pdf
        ByteArrayOutputStream htmlCertificate= certififcateService.generateCertificate(userId,courseId,data,templateName);
        return htmlCertificate.toByteArray();
    }

}
