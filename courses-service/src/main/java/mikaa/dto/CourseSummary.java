package mikaa.dto;

import lombok.Value;

/*
 * Use value class over record due to ModelMapper compatibility.
 * ModelMapper expects fields to have traditional getters.
 */
@Value
public class CourseSummary {
  private Long id;
  private String name;
  private int holes;
  private int par;
}
