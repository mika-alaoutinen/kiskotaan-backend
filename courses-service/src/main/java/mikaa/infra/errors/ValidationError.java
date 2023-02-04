package mikaa.infra.errors;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
record ValidationError(String field, String message) {
}
