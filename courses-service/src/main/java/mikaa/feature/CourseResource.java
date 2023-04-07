package mikaa.feature;

import java.util.List;

import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import mikaa.api.CoursesApi;
import mikaa.model.CourseDTO;
import mikaa.model.CourseNameDTO;
import mikaa.model.CourseSummaryDTO;
import mikaa.model.HoleDTO;
import mikaa.model.NewCourseDTO;
import mikaa.model.NewCourseNameDTO;
import mikaa.model.NewHoleDTO;

@ApplicationScoped
@RequiredArgsConstructor
public class CourseResource implements CoursesApi {

  private static final ModelMapper MAPPER = new ModelMapper();
  private final CourseService service;
  private final HoleService holeService;

  @Override
  @Transactional
  public CourseDTO addCourse(@Valid @NotNull NewCourseDTO newCourseDTO) {
    var course = service.add(MAPPER.map(newCourseDTO, CourseEntity.class));
    return MAPPER.map(course, CourseDTO.class);
  }

  @Override
  @Transactional
  public HoleDTO addHole(Integer id, @Valid @NotNull NewHoleDTO newHoleDTO) {
    var hole = holeService.add(id, MAPPER.map(newHoleDTO, HoleEntity.class));
    return MAPPER.map(hole, HoleDTO.class);
  }

  @Override
  @Transactional
  public void deleteCourse(Integer id) {
    service.delete(id);
  }

  @Override
  public CourseDTO getCourse(Integer id) {
    return MAPPER.map(service.findOne(id), CourseDTO.class);
  }

  @Override
  public List<CourseSummaryDTO> getCourses(
      String name,
      Integer holesMin,
      Integer holesMax,
      Integer parMin,
      Integer parMax) {
    return service.findAll()
        .stream()
        .map(courseSummary -> MAPPER.map(courseSummary, CourseSummaryDTO.class))
        .toList();
  }

  @Override
  @Transactional
  public CourseNameDTO updateCourseName(Integer id, @Valid @NotNull NewCourseNameDTO courseName) {
    var course = service.updateCourseName(id, courseName.getName());
    return MAPPER.map(course, CourseNameDTO.class);
  }

}
