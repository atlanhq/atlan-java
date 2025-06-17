# Generating an extension to the SDK

This will guide you through the steps to generate an extension to the SDK.
The objective is to allow you to create your own "mini-SDK" that contains only
the net-new portions of any metadata model extensions, but otherwise continue to
use the base Java SDK as a dependency.

In this way, you do not need to "fork" the SDK in order to include your own new
metadata model. Therefore, you can continue to benefit from any updates to the base
Java SDK simply by bumping the version of the base SDK as one of the dependencies of
your JVM-based project.

(You can continue to manage your own extension independently, including re-generating
its bindings if, for example, the base SDK adds further functionality to its generators).

## Create and deploy your metamodel

Follow the standard steps to create and deploy your net-new metamodel: entities, relationships,
structs, enums, etc. These must be deployed to and available in a tenant in order for the
code generator to run and produce the SDK extension.

Note: be sure to use a specific `serviceType` when creating the typedefs, as this will be used
to uniquely select the typedefs during the SDK extension generation.

## Configure the code generator

To generate the extension, you can use the same code generator that the base Java SDK
uses to generate its object models.

### Create the generator class outline

Start by creating an outline for the generator class, in which you should define:

- The service type used for your metadata model extension (this is like the "namespace" all of your typedefs will be within)
- A unique prefix (name) that you've used, which should be present as the first part of every typedef's name
- The Java package root where you want this extended SDK's object model to be generated

For example:

```java
public class ExtendedModelGenerator {
    protected static final String SERVICE_TYPE = "guacamole";
    protected static final String TYPE_PREFIX = "Guacamole";
    protected static final String PACKAGE_ROOT = "com.probable.guacamole.model";
}
```

### Create executable `main` method

Next, create an executable `main` method. Within this method:

1. Initialize the `AtlanClient` to use your tenant (where the typedefs are deployed), also providing your API token as the second argument. (You can alternatively initialize these via environment variables `ATLAN_BASE_URL` and `ATLAN_API_KEY` and leave the constructor arguments for `AtlanClient` blank.)
2. Create a `GeneratorConfig` instance from your model generator class, also specifying the location for any Freemarker templates (where you can implement `creator()` methods for your new objects), and the Java package location for the generated code.
3. Pass the client and configuration to 3 generators, in order, calling the `.generate()` method on each:
   1. `ModelGenerator`
   2. `TestGenerator`
   3. `DocGenerator`

For example:

```java
    public static void main(String[] args) throws Exception {
        try (AtlanClient client = new AtlanClient("https://your-tenant.atlan.com", "...")) {
            GeneratorConfig cfg = GeneratorConfig.creator(
                            ExtendedModelGenerator.class, new File("resources/templates"), PACKAGE_ROOT)
                    .serviceType(SERVICE_TYPE)
                    .build();
            new ModelGenerator(client, cfg).generate();
            new TestGenerator(client, cfg).generate();
            new DocGenerator(client, cfg).generate();
        } catch (IOException e) {
            log.error("Failed to cleanup client.", e);
        }
    }
```

### Run the `main` method

Now you simply need to run the `main` method to generate all the object model classes for your extended typedefs into the Java package root location provided.

If you encounter errors when doing so about some particular object not being found or introspectable, simply
run the main method again. (There are some dependencies, like enums, that can only be resolved after the initial
code generation.)

Examples of code generated for some imaginary new Table and Column concepts can be found under the `src/main/java/com/probable/guacamole/model` directory.

### Create any extended logic for your model

If you want to create any extended logic for your model, for example define the `creator()` methods that list the bare minimum information required to create one of your new objects,
define this through Freemarker templates.

You can find examples of such templates for the imaginary Guacamole Columns and Tables in this sample under `src/main/resources/templates/*.ftl`.

Note that you only need to define any extra functionality that should be included in your classes, the baseline functionality will still be generated
for you automatically by the code generator.

## Test or use your generated code

You should now be able to test and use your generated code.
Examples of how you might do so can be found under the `src/main/java/com/probable/guacamole/instances` directory.
