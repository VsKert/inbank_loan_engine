# inbank_loan_engine
Inbank Intern Test Task

## General decisions, tech stack
 - Project uses Java 21 + SpringBoot for backend and Vue as frontend framework based on personal preference.
 - PostgreSQL + Liquibase to simulate querying user data from real database.
 - Due to the smaller and solo nature of the task, decided not to use branches and pull requests for this repository.
 - Thought process is shared via comments and this readme file.

## Assumptions
 - When calculating maximum amount of loan that would be allowed, loan period is not modified and method will return the maximum allowed loan in user specified loan period.
 - As per task description, if the scoring algorithm would return a negative decision and no suitable loan amount is found in the user specified period, engine will try to find the closest period (maximal amount in that period) that would be allowed as a loan. Task description is ambiguous in this regard, as it is not specified if this case should return closest suitable period (which seems to be implied) or the maximum allowed loan like the rest of the task.

## How to run project
Prerequisites: Java 21 installed, Docker Engine running, gradle installed.

### Step 1 - build backend:
in ìnbank_loan_engine/backend run `./gradlew clean build`

### Step 2a - running prod profile:
in inbank_loan_engine/backend run `docker compose --profile prod up`

### Step 2b - running dev profile:
In inbank_loan_engine/backend run `docker compose --profile dev up`

### Step 3:
Depending on profile, navigate to:
 - DEV: http://localhost:5173/
 - PROD: http://localhost:80/

 If page doesn't resolve, attempt 127.0.0.1 instead of localhost.
