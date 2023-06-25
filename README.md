# Oil Extraction System

The Oil Extraction System is a Java-based application for managing oil rigs and tankers. It provides functionality for creating, updating, and deleting rigs, as well as managing associated tankers.

## Features

- Create, update, and delete rigs
- Manage tankers associated with rigs
- Read data from CSV files
- Write data to CSV files
- Retrieve rigs and tankers by ID

## Technologies Used

- Java
- Spring Framework
- Hibernate
- Maven

## Getting Started

To run the Oil Extraction System locally, follow these steps:

1. Clone the repository:

```bash
git clone https://github.com/d3nnyyy/OilExtraction.git
```

2. Open the project in your preferred IDE.

3. Build the project using Maven:

```bash
mvn clean install
```

4. Run the application:

```bash
mvn spring-boot:run
```

5. The application will start running on `http://localhost:8080`.

## Usage

You can interact with the Oil Extraction System using API endpoints. You can test these endpoints using a tool like [Postman](https://www.postman.com/). Here are the available endpoints:

### Rigs

- `GET /rigs`: Retrieve a list of all rigs.
- `GET /rigs/{id}`: Retrieve a specific rig by ID.
- `POST /rigs`: Create a new rig.
- `PUT /rigs/{id}`: Update an existing rig.
- `DELETE /rigs/{id}`: Delete a rig by ID.

### Tankers

- `GET /tankers`: Retrieve a list of all tankers.
- `GET /tankers/free` : Retrieve a list of tankers without rig.
- `GET /tankers/{id}`: Retrieve a specific tanker by ID.
- `POST /tankers`: Create a new tanker.
- `PUT /tankers/{id}`: Update an existing tanker.
- `DELETE /tankers/{id}`: Delete a tanker by ID.

Make sure to include the necessary request parameters and provide the required data in the request body for creating and updating rigs or tankers. Refer to the API documentation for detailed information on the request and response formats.

## Data Persistence

The application stores data in CSV files located in the `src/main/resources/entities/` directory. Each entity (rig or tanker) is stored in a separate file, named with the format `entityName-date.csv`.

## Contributing

Contributions to the Oil Extraction System are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request.

## Contact

For any inquiries or questions, please contact [d.tsebulia@gmail.com].
