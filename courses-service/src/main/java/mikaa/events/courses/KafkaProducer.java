package mikaa.events.courses;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
class KafkaProducer implements CourseProducer {

  @Inject
  @Channel("course-added")
  Emitter<CourseAdded> addEmitter;

  @Inject
  @Channel("course-deleted")
  Emitter<Long> deleteEmitter;

  @Inject
  @Channel("course-updated")
  Emitter<CourseUpdated> updateEmitter;

  @Override
  public void courseAdded(CourseAdded payload) {
    addEmitter.send(payload);
  }

  @Override
  public void courseUpdated(CourseUpdated payload) {
    updateEmitter.send(payload);
  }

  @Override
  public void courseDeleted(long id) {
    deleteEmitter.send(id);
  }

}
