# EzPic
Simple server with a RESTful API for processing, uploading, and downloading images from AWS S3. Works using a small number of presets.

## Compiling & Running

Clone this repo, and then modify the `application.properties` file located in the project's and set the relevant `AWS_REGION` value to your AWS region. 
Alternatively, you can use a custom AWS S3 endpoint by changing the `CUSTOM_ENDPOINT` property. This is useful for local development with an AWS S3 API clone such as localstack.
These values can also be set as environment variables.

**COMING SOON: A Docker image to get started quicker without the need to compile the code yourself..**

## API Usage

### Uploading an Image

Make a POST request to `/api/v1/{bucket}/image` where `{bucket}` is the name of your S3 bucket. 

In the body, include the image as `form-data` with the key `file`. Only `.png`, `.jpg`, and `.jpeg` files are supported.

Specify an image processing preset when making the request to have the image processed in a certain way. The following presets are valid:

* `/api/v1/{bucket}/image?preset=square` - crops the image to be square and generates 3 resolutions: 1024x1024, 512x512, 256x256
* `/api/v1/{bucket}/image?preset=wide` - crops the image to have an aspect ratio of 16:9 and generates 3 resolutions: 1920x1080, 960x540, 480x270
* `/api/v1/{bucket}/image?preset=tall` - crops the image to have an aspect ratio of 9:16 and generates 3 resolutions: 1080x1920, 540x960, 270x480
* `/api/v1/{bucket}/image` (no preset) - Stores the original resolution and 3 thumbnails with the same aspect ratios scaled down by half, a quarter, and an eighth

### Downloading an Image

Make a GET request to `/api/v1/{bucket}/image/{filename}` where `{bucket}` is the name of your S3 bucket, and `{filename}` is the name of your file returned when it was uploaded.
Always returns the image as a `.jpeg` file.

## License
