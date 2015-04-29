# Solution
- Simple (may not be optimal) algorithm, based on [First-fit algorithm](http://en.wikipedia.org/wiki/Bin_packing_problem) with First Fit Decreasing strategy has been chosen.
- In DDD terms, "breakthrough" didn't happen during a number of design iterations.

# Tooling
- Spock was not used for testing because Groovy was not included in allowed language list
- Gradle Wrapper was not included because executables are prohibited according to requirements
- Since Java 8 lambdas/streams offer only basic functional capabilities and libraries are prohibited (https://github.com/poetix/protonpack), I had to write PairwiseForEach utility

# Implementation details
- Explicit (poka-yoke-style) argument null-checks have been omitted. JSR-305 annotation is great alternative, but fall into 3rd party library group
- (Im)mutability is not communicated well because JSR-305 annotation fall into 3rd party library group
- Requirement "No talk title has numbers in it." was important for Regexps. No additional validation took place in entities.
- I didn't implement complete Equals/HashCode and Comparator, only parts that are needed for app. Normally it can be done well using Guava + Guava TestUtils.
- I made solution (pretty) generic since often we're doing conference and meetup planning :)

# Testing
- Code coverage is close to 100% because of TDD
- Output ordering is different due to specifics of algorithm
- Output event times are different due to better compaction algorithm (e.g. in this implementation Networking Event on Track No2 would start at 4:05PM vs. 5:00PM in reference test output).
- `com.thoughtworks.contraman.AppTest` can prove that application works "according to specification"

# Running
- Application requires JDK 8 and Gradle 2.3+ installed
- To run application from Gradle: `gradle run -Dinput=<inputFilePath>`
- To run application from IDE: run `com.thoughtworks.contraman.App` and supply input file location as the first argument
