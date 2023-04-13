# ITV Studios Coding Exercise

Thank you for investing time in our coding exercise, good luck and remember to
commit often.

## Requirements

ITVX displays thumbnail images to give viewers a taste of what each show
is about.

In this exercise, you are going to build a service that generates a thumbnail for
a piece of content. The service should:

1. Verify that the downloaded video has not been corrupted
2. Create a thumbnail from the downloaded video file

The point in the video from which to capture the thumbnail should be provided as
an argument to the service along with the path to the downloaded video itself.

Sample content (video assets) can be downloaded from the following endpoint:

`https://cdfr062ui5.execute-api.eu-west-1.amazonaws.com/playground/${assetId}`

An associated metadata endpoint provides the expected checksum for
each asset:

`https://cdfr062ui5.execute-api.eu-west-1.amazonaws.com/playground/${assetId}/metadata`

Verification involves checking that the checksum of the file matches the
expected checksum exposed by the `/metadata` endpoint above.

In order to test your solution, some example content and metadata have been
uploaded. You can access this by replacing `assetId` with `valid` and `invalid`,
for testing success and error conditions respectively.

There is no need to build a sophisticated UI for this service; a command-line
application is fine. Details for invoking the service should be provided along
with your solution.

## Hints

There is no need for the service to download files automatically. Providing a
path to a local video file is fine.  You can use any tool you like to generate
thumbnails, but we would recommend FFmpeg, an open source tool that can be
called as follows:

`ffmpeg -ss 00:00:5 -i video.mp4 -vframes 1 thumbnail.png`

## Assessment Criteria

We are looking for evidence of functional programming and good engineering practice.

## Submitting your solution

Once you are happy with your efforts please zip it up and send it for review either via your agent or direct to the ITV recruiter.

Your solution should include a README.md file that contains any assumptions, design decisions or instructions for running the code.

If you have any questions about this exercise, please get in touch via your agent or ITV recruiter.
We will aim to respond to these within business hours and consider all questions to be valid.
