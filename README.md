# ITV Studios Coding Exercise

Please read the [exercise instructions](instructions.md).

## Running the code

### 1. Install `ffmpeg` if you don't have it already
Tested on [this version](https://github.com/BtbN/FFmpeg-Builds/releases/tag/autobuild-2023-04-13-12-48). \
Its `bin` folder just needs to be on the `PATH`.

A couple of tests use `ffmpeg` too. See [Design decisions](#design-decisions).

### 2. Ensure you're connected to the internet
The code downloads the video and the metadata.

A couple of tests use the internet too. See [Design decisions](#design-decisions).

### 3. Run the unit tests with `sbt test`
This will validate that all the features are working as intended.

### 4. Run the app
Ensure that the thumbnail path is in an existing directory.
```
sbt "run valid 00:00:05 ./thumbnail-valid.png"
```
```
sbt "run invalid 00:00:05 ./thumbnail-invalid.png"
```

## Assumptions

- We're using Java 11. No idea if the code works with any other version.
- The `ffmpeg` command must be available on the path.
- Some "unit tests" require an internet connection.
- I am assuming that the video files can comfortably fit into memory.
  - That's a reckless assumption by all accounts, but how long can cute cat videos get?
- The `ffmpeg` command works on any video file even if the file extension is `.mov`. (Or the type is always `video/quicktime`)
  - I'm not sure how to get the correct file extension from the API, so I'm assuming it's `video/quicktime`.
  - If `ffmpeg` can't cope with this assumption, 
    one *could* create a utility that returns the appropriate file extension for each supported MIME type.

## Design decisions

- I unignored the IntelliJ files from Git.
  - I wanted them in case my IDE setup broke. (It did.)

- Many of the locations of implicits are *un*decided.
  - e.g. what's a class implicit vs what's a method implicit.

- The style of mocking is also undecided.
  - I've never used ScalaMock before, but for a small project it's the most concise solution I could think of.
  - Some of the mocking syntax for methods with implicits is quite horrendous.
  - There are probably some ScalaMock gotchas I didn't notice.

- I didn't use `override` anywhere, and I wasn't planning to. That meant some extra traits to make the mocking easier.

- Many edge cases weren't tested, manually or automatically.
  - e.g. timestamps beyond the video limit, strange HTTP statuses, different video types, Linux/MacOS.
  - If they mattered, I would recommend automated tests. 
    There are too many of these edge cases for manual tests to be properly recorded.

- I chose to download the video file even though the instructions said that it was optional.
  - I was going to have to implement all the HTTP stuff for the metadata anyway.
  - Coming up with another way to read data didn't seem worth the effort.

- Akka HTTP was abstracted into a trait.
  - The ability to 'mock the network' is invaluable when testing APIs and higher-level logic.
  - The abstraction encourages the user to always download the HTTP entity, but it makes it clear in the method name.
    - Guranteeing the downloading of the entity has some benefits:
      - Lower risk of hanging HTTP requests when people hand-craft unmarshallers.
      - More reliable logging of responses in case of errors, e.g. 400 Bad Request.
      - To most developers, the streaming nature of Akka HTTP is surprising, but also dangerous.
    - Other kinds of HTTP calls could be added if needed. e.g. explicit streaming of HTTP entities.
      - Video downloading is a good use case for HTTP streaming. Taking a `File` parameter or a path would be my choice.

- I chose to make `ffmpeg` an external dependency instead of bundling it in the build, or using another library.
  - The executable is massive, so it would bloat my GitHub repo.
  - It's not ideal because more external dependencies means more things that need to be maintained/migrated when the infrastructure changes.
  - StackOverflow strongly recommended not to use Java library wrappers for ffmpeg due to security patch issues.
  - I can't bundle an executable if I don't know what system it'll run on. I'm on Windows right now, but the app will probably run on Linux.
  - As a result, this service can (probably) only be deployed in a container due to this decision, 
    but maybe that's how all the ITV apps are deployed. 
    In some departments using containers for applications is forbidden, so it's not unprecedented to avoid containers.

- Some "unit tests" have external dependencies, e.g. internet connection & access to `ffmpeg`.
  - Those features need to be automatically tested, so I wrote the tests.
  - These tests should be placed in a separate module so that developers aren't forced to run them all the time.
    - The tests should still run on a pipeline somewhere.
    - I didn't want to reorganise the exercise's SBT structure to fit my own ideals.
  - The real HTTP test should be tied to a more robust HTTP server instead of `example.com`, but that's what I had available.
    - A good example would be to start our own, but we'd need to be careful about port clashes. (Maybe permissions too? 🤷‍♂️)
  - The real HTTP test exists because HTTP can go subtly wrong in ways that might be hidden by other reliability features, like timeouts.
