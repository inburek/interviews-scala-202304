# API

The application will host the API on port `8080`.

## `/metadatas`

### `GET`
Returns a list of all movie metadatas as they are stored in the backend.
Useful for debugging.

The format is undocumented, as this is a debugging endpoint tied to the underlying data structure.

## `/recommendations?movieId=<int>`

### `GET`
Returns a recommended movie for the given movie id.

Also returns the metadata for the given movie and the shared tag for debugging.

Response format for successful recommendation:
```json
{
   "maybeMetadata":{
      "id":"5",
      "length":129,
      "tags":[
         "Comedy",
         "Drama"
      ],
      "title":"Mr. Smith Goes to Washington"
   },
   "maybeRecommendation":{
      "movieId":"66",
      "sharedTag":"Drama"
   }
}
```

Response format for nonexistent movie ID:
```json
{
}
```

Response format for failed recommendation given a valid movie ID:
```json
{
   "maybeMetadata":{
      "id":"5",
      "length":129,
      "tags":[
         "Comedy",
         "Drama"
      ],
      "title":"Mr. Smith Goes to Washington"
   }
}
```

#### Behaviour
The query parameter `movieId` is required.

The number of recommendations is either 0 or 1.
The recommendation is based solely on the existence of a shared movie tag.
The movie ID supplied in the request is never recommended.
