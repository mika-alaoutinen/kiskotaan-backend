package mikaa.errors;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ValidationError(String field, String message) {
}
