### Design notes

The main goal is to build a very easy to understand design and easily extendable, it's based on few uses cases and the
design would change based on new input.

****Main guidelines****

* Full TDD (Outside-In development)
* DD Inspired / Hexagonal architecture
* Try to use all domain words
* To be precise with amount, with should use BigDecimal, but to make the code quicker/simpler I used double (for 2
  decimal precision it should be enough)* could
* could wrap ID into object (UserId for example to ensure format, readability)
* all abstraction are easily interchangeable and extendable (for database impl for example)
* all the domain only use abstractions