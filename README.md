# wallet.api
**Digital Wallet API**

`wallet.api` is a backend API for a digital wallet application, built with Spring Boot. It provides features for handling wallet operations such as deposits, withdrawals, transactions, and more.

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Running the Application](#running-the-application)
- [Importan Notes](#important)
- [Building the Project](#building-the-project)
- [Tests](#tests)
- [License](#license)

## Requirements

- Java 21 or higher
- Maven 3.8 or higher

## Installation

To set up the project on your local machine:

1. **Clone the repository**:

Open your terminal and run belowed commands
 

	git clone https://github.com/seyfettincicek/wallet.api.git
	cd wallet.api



2. **Ensure `spotless` is applied**:
    
    mvn spotless:apply
   

3. **Install dependencies and build the project**:
   
    mvn clean install
   

## Usage

### Running the Application

You can run the Spring Boot application with a specific profile. For example, to run with the test or prod profile: There are only difference for logging level

    mvn spring-boot:run -Dspring-boot.run.profiles=test


## important

The api will start 8090 port.

### Initialize data

When application start there are customer and wallet data so there is a public URL for generation sampler data feel free to use this service. 
 when you call this service it will delete all customer , wallet and transactions and it will create 3 Employee , 3 Customer and wallets for them.
 
 GET http://localhost:8090/api/public/initialize
 
 response :
 ...
     {
        "id": "id",
        "name": "EMPLOYEE 3",
        "surname": "HnmhN",
        "tckn": "0359800151",
        "role": "EMPLOYEE"
    },
    {
        "id": "id",
        "name": "CUSTOMER 1",
        "surname": "HIJUV",
        "tckn": "4508186723",
        "role": "CUSTOMER"
    },
 ....
### Authenticaiton

All end-points are secured instead of /api/public/** so you need to add header that name is "authentication-token" and value is custemer Id . You can find custermer id in Initialize service


### Open API
You can find more details for requesting service below link.

http://localhost:8090/api/public/swagger-ui/index.html	

