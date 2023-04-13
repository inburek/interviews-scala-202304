# ITV Studios Coding Exercise

Please read the [exercise instructions](instructions.md).

## Running the code

### 1. Install `ffmpeg` if you don't have it already

### 2. Ensure you're connected to the internet

### 3. Run the unit tests with `sbt test`
This will validate that all the features are working as intended.

### 4. Run the 

## Assumptions

- The `ffmpeg` command must be available on the path.
- Some "unit tests" require an internet connection.

## Design decisions

- I chose to make `ffmpeg` an external dependency instead of bundling it in the build, or using another library.
  - The executable is massive.
  - It's not ideal because more external dependencies means more things that need to be maintained/migrated when the infrastructure changes.
  - StackOverflow strongly recommended not to use Java library wrappers for ffmpeg due to security patch issues.
  - I can't bundle an executable if I don't know what system it'll run on. I'm on Windows right now, but the app will probably run on Linux.
  - As a result, this service can (probably) only be deployed in a container due to this decision, 
    but maybe that's how all the ITV apps are deployed. In some Ocado departments using containers for applications is forbidden.

- Some "unit tests" have external dependencies, e.g. internet, access to `ffmpeg`.
  - Those features need to be automatically tested.
  - The tests should be placed in a separate module so that developers aren't forced to run them all the time.
    - The tests should still run on a pipeline somewhere.
    - I didn't want to reorganise the exercise's SBT structure.
  - The real HTTP test should be tied to a more robust HTTP server instead of `example.com`, but that's what I had available.
    - It exists because HTTP can go subtly wrong in ways that might be hidden by other reliability features, like timeouts.
