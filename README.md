# Task Drones

This is a practical test, you can find all information and description of the problem [here](./drones.md)

## Generated code

This application was generated using JHipster 7.9.3, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.9.3](https://www.jhipster.tech/documentation-archive/v7.9.3).

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, JHipster generates configuration files for tools like git, prettier, eslint, husky, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.

- `.yo-rc.json` - Yeoman configuration file
  JHipster configuration is stored in this file at `generator-jhipster` key. You may find `generator-jhipster-*` for specific blueprints configuration.
- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if ommited) or force. Lines starting with `#` are considered comments and are ignored.
- `.jhipster/*.json` - JHipster entity configuration files
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

# Development

To start your application in the dev profile, run:

```
./mvnw
```

## Building for production

### Packaging as jar

To build the final jar and optimize the drones application for production, run:

- Keep in mind that for production you need to set up a postgres database.

```
./mvnw -Pprod clean verify
```

To ensure everything worked, run:

```
java -jar target/*.jar
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

## Using Docker to simplify deployment (optional)

You can use Docker too!

First build a docker image of the app by running:

```
npm run java:docker
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

# Practical Task Explanation

### Some assumptions I defined.

1. All new drone `(POST)` are created in IDLE state and without medication items.
2. To update a drone `(PATCH)` its only permited to change battery capacity and state.
3. Image in medication item are persisted to database as two separated field `image-bytes` and `image-content-type`.
4. The application in secured with Basic Auth, use user=`admin` and password=`admin` to get access.
5. There is a limit of 10MB to image in medication items.

### Endpoints

- POST /api/drones : Create new drone
- GET /api/drones : Get all drones
- GET /api/drones/count : Count number of drones
- GET /api/drones/{id} : Get a drone by "id"
- DELETE /api/drones/{id} : Delete a drone by "id" and its medication items
- POST /api/drones/{id}/bulk-load : Bulk load medication items to a drone
- POST /api/drones/{id}/load : Load one medication item to a drone
- GET /api/drones/{id}/medications : Get all medication items of a drone
- GET /api/drones/available : Get all available drone for loading
- GET /api/drones/available?weight=10 : Get all available drone for loading with available weights
- GET /api/drones/{id}/battery : Get battery level of a drone
- PATCH /api/drones/{id} : Update battery and state of a drone

For more detail you can check this [postman collection](./Drones.postman_collection.json)

### How to parse image

There are two endpoint that allow to load medication item to a drone, `/bulk-load` and `/load`:

- `BULK-LOAD`: you need to write in the body `(application/json)` an array of medication where each medication might have an `image` and `imageContentType`, the `image` is loaded in `bytes[]` format, for testing propose you can use the next script in python, this paste in your clipboard so you only need to paste `(Ctrl+V)` in the request body, lastly `imageContentType` is the content-type of the image, e.g., `image/jpg`, `image/png`.
- `LOAD`: its a `(multipart/form-data)` request, use field `image` to load the image and field `medication` to load the medication item entity as `(application/json)`.

```py
import base64
import pyperclip

path_image = "./image.jpg"

with open(im, "rb") as image:
  read = image.read()
  image_bytes = [str(i) for i in read]
  pyperclip.copy("[%s]" % ",".join(image_bytes))
```

- To show the image in the `html` page simply use the following line code:

```html
<img src="data:[imageContentType];base64,[image]" />
```

### Periodical task to check all drone battery level

There are set a schedule task that run every minute and save to the log `battery-drone.{date}.log` all asked information.
