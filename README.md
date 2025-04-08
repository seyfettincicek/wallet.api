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

The api will start 8090 port s