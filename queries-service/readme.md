# Kiskotaan Queries service

## Technology considerations
- Implement REST endpoints using a reactive programming style.
- Try out MongoDB just for the heck of it.

## Tehnical issues
A note on technical issues.

### No code generation with reactive applications
- There doesn't seem to be a sensible way to generate reactive interfaces from an OpenAPI document. At best it's possible to generate interfaces that wrap return values in `CompletionStage`, but I definitely want to take advantage of Mutiny with Quarkus. I could generate the model classes and do the API interfaces by hand, but I'd rather just ditch code generation entirely.
