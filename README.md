# Solution
- Simple (may not be optimal) algorithm, based on ![First-fit algorithm](http://en.wikipedia.org/wiki/Bin_packing_problem) with First Fit

# Tooling
- Spock is not used for testing since Groovy was not included in allowed language list
- Gradle Wrapper is not included since executables are prohibited according to requirements
- Since Java 8 lambdas/streams offer only basic functional capabilities and libraries are prohibited (https://github.com/poetix/protonpack), PairwiseForEach is created

# Implementation details
- Explicit argument null-checks a-ala Objects.checkNotNull(...) have been omitted. JSR-305 are great, but fall into 3rd party library group
- (Im)mutability is not communicated well because JSR-305 annotation fall into 3rd party library group
- Requirement "No talk title has numbers in it." was important for Regexps. No argument validation is implemented in entities
- Equals/HashCode and Comparators was not tested for awkward null-cases. Normally it can be done well using Guava + Guava TestUtils.
- Solution is (pretty) generic and can be used with other schedules
- Beware many classes (in honour of SRP and Micro-Services :)
- Classes like TalkConsumer and Timeslot became separate units during refactoring and were initially part of Timetable, indirectly covered by "parent" unit tests. Since they are not designed for reuse and should be hidden from the outside world (e.g. with VeriPacks), they don't deserve separate unit tests. Probably I could get better test encapsulation with Mockist-style TDD and Role Interfaces.

# Testing
- Code coverage is close to 100% because of TDD (majority of tests are not Mocking-style). Some unit tests read files form disk. 
- Output ordering is different due to specifics of algorithm
- Output events have different starting dates due to better compaction algorithm (e.g. in this implementation Networking Event on Track No2 would start at 4:05PM vs. reference 5:00PM)
- `com.thoughtworks.contraman.AppTest` can prove that application works "according to specification"

# Running
- Application requires JDK 8 and Gradle 2.3+ installed (that's why Gradle Wrappers are cool)
- To run application from Gradle: `gradle run -Dinput=<inputFilePath>`
- To run application from IDE: run `com.thoughtworks.contraman.App` and supply input file location as the first argument
