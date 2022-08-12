
# Introduction to the Atlan Java SDK

Our Java SDK is written with the following principles in mind:

- Define each Atlan entity as an object
- Surface a builder pattern for chaining the creation of object (in particular, for building up requests)
- Each object provides its own methods to carry out CRUD operations against Atlan

The resulting SDK is released as a pre-packaged `.jar` file on standard repositories like Maven Central. From there it can be bundled in as a dependency for any other software development.
