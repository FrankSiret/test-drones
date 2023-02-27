# Task Drones

This is a practical test, you can find all information and description of the problem [here](./drones.md)

## Generated code

This application was generated using JHipster 7.9.3, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.9.3](https://www.jhipster.tech/documentation-archive/v7.9.3).

# Development

To start the application in the dev profile, run:

```
./mvnw
```

## Building for production

### Packaging as jar

To build the final jar and optimize the drones application for production, run:

- Keep in mind for production you need to set up a postgres database.

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

1. Drone and Medication have a primary key `id` integer, use it to retrieve the entity.
2. Drone `serial number` allowed only numbers.
3. All new drone `(POST)` are created in IDLE state and without medication items.
4. To update a drone `(PATCH)` its only permited to change battery capacity and state.
5. Image in medication item are persisted to database as two separated field `image-bytes` and `image-content-type`.
6. The application in secured with Basic Auth, use user=`admin` and password=`admin` to get access.
7. There is a limit of 10MB to image size in Medication items.

### Endpoints

- `POST /api/drones` : Create new drone
- `GET /api/drones` : Get all drones
- `GET /api/drones/count` : Count number of drones
- `GET /api/drones/{id}` : Get a drone by "id"
- `DELETE /api/drones/{id}` : Delete a drone by "id" and its medication items
- `POST /api/drones/{id}/bulk-load` : Bulk load medication items to a drone
- `POST /api/drones/{id}/load` : Load one medication item to a drone
- `GET /api/drones/{id}/medications` : Get all medication items of a drone
- `GET /api/drones/available` : Get all available drone for loading
- `GET /api/drones/available?weight=10` : Get all available drone for loading with available weights
- `GET /api/drones/{id}/battery` : Get battery level of a drone
- `PATCH /api/drones/{id}` : Update battery and state of a drone

For more detail you can check this [postman collection](./Drones.postman_collection.json)

### How to parse image

There are two endpoint that allow to load medication item to a drone, `/bulk-load` and `/load`:

- `BULK-LOAD`: you need to write in the body `(application/json)` an array of medication where each medication might have an `image` and `imageContentType`, the `image` is loaded in `bytes[]` format, for testing propose you can use the next script in python to get the bytes array of an image, lastly `imageContentType` is the content-type of the image, e.g., `image/jpg`, `image/png`.
- `LOAD`: its a `(multipart/form-data)` request, use field `image` to load the image and field `medication` to load the medication item entity as `(application/json)` format.

```py
import base64
import pyperclip

path_image = "./image.jpg"

with open(im, "rb") as image:
  read = image.read()
  image_bytes = [str(i) for i in read]
  # copy to clipboard the bytes array
  pyperclip.copy("[%s]" % ",".join(image_bytes))
  # use the paste control (Ctrl+V) to paste the array of byte of the image
```

- To show the image in the `html` page simply use the following line code:

```html
<img src="data:[imageContentType];base64,[image]" />
```

### Periodical task to check all drone battery level

There are set a schedule task that run every minute and save to the log `battery-drone.{date}.log` all asked information.

### API

You can get the api documentation on `/v3/docs` in json format. There is not swagger-ui predefined, but you can imported in postman collection.
