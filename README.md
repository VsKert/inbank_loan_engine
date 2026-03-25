# inbank_loan_engine
Inbank Intern Test Task

## General decisions, tech stack
 - Project uses Java 25 for backend and Vue as frontend framework based on personal preference.
 - PostgreSQL + Liquibase to simulate querying user data from real database.
 - Due to the smaller and solo nature of the task, decided not to use branches and pull requests for this repository.

## Assumptions
 - When calculating maximum amount of loan that would be allowed, loan period is not modified and method will return the maximum allowed loan in user specified loan period.
 - As per task description, if the scoring algorithm would return a negative decision and no suitable loan amount is found in the user specified period, engine will try to find the closest period (minimal amount) that would be allowed as a loan. Task description is ambiguous in this regard, as it is not specified if this case should return closest suitable period (which seems to be implied) or the maximum allowed loan like the rest of the task.
