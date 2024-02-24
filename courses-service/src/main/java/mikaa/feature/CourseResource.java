package mikaa.feature;

import java.math.BigDecimal;
import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import mikaa.api.CoursesApi;
import mikaa.domain.Course;
import mikaa.domain.NewCourse;
import mikaa.domain.NewHole;
import mikaa.model.CourseDTO;
import mikaa.model.CourseNameDTO;
import mikaa.model.CourseSummaryDTO;
import mikaa.model.HoleDTO;
import mikaa.model.NewCourseDTO;
import mikaa.model.NewCourseNameDTO;
import mikaa.util.StringFilter;
import mikaa.util.RangeFilter;

@RequiredArgsConstructor
class CourseResource implements CoursesApi {

  private final CourseService service;

  @Override
  @Transactional
  public CourseDTO addCourse(@Valid @NotNull NewCourseDTO newCourseDTO) {
    var holes = newCourseDTO.getHoles()
        .stream()
        .map(hole -> new NewHole(hole.getNumber(), hole.getPar(), hole.getDistance()))
        .toList();

    var newCourse = new NewCourse(newCourseDTO.getName(), holes);
    var course = service.add(newCourse);

    return toDto(course);
  }

  @Override
  @Transactional
  public void deleteCourse(Integer id) {
    service.delete(id);
  }

  @Override
  public CourseDTO getCourse(Integer id) {
    return toDto(service.findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public List<CourseSummaryDTO> getCourses(
      String name,
      Integer holesMin,
      Integer holesMax,
      Integer parMin,
      Integer parMax) {
    var filters = new QueryFilters(
        new StringFilter(name),
        new RangeFilter<Integer>(holesMin, holesMax),
        new RangeFilter<Integer>(parMin, parMax));

    return service.findAll(filters)
        .stream()
        .map(summary -> {
          var dto = new CourseSummaryDTO();
          dto.setId(BigDecimal.valueOf(summary.id()));
          dto.setName(summary.name());
          dto.setHoles(BigDecimal.valueOf(summary.holes()));
          dto.setPar(BigDecimal.valueOf(summary.par()));
          return dto;
        })
        .toList();
  }

  @Override
  @Transactional
  public CourseNameDTO updateCourseName(Integer id, @Valid @NotNull NewCourseNameDTO courseName) {
    var course = service.updateCourseName(id, courseName.getName());
    var dto = new CourseNameDTO();
    dto.setId(BigDecimal.valueOf(course.id()));
    dto.setName(course.name());
    return dto;
  }

  private static CourseDTO toDto(Course course) {
    var holes = course.holes()
        .stream()
        .map(hole -> {
          var dto = new HoleDTO();
          dto.setNumber(hole.number());
          dto.setPar(hole.par());
          dto.setDistance(hole.distance());
          return dto;
        })
        .toList();

    var dto = new CourseDTO();
    dto.setId(BigDecimal.valueOf(course.id()));
    dto.setName(course.name());
    dto.setHoles(holes);
    return dto;
  }

}
