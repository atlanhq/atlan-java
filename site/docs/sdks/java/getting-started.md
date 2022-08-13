---
icon: fontawesome/brands-java
---

# Getting started with the Atlan Java SDK

## Obtain the SDK

The SDK is available on Maven Central, ready to be included in your project:

=== "Gradle"

	```gradle title="build.gradle"
	dependencies {
	  implementation group: "com.atlan.clients", name: "atlan-java", version: "0.0.1"
	}
	```

=== "Maven"

	```xml title="pom.xml"
	<dependency>
	  <groupId>com.atlan.clients</groupId>
	  <artifactId>atlan-java</artifactId>
	  <version>0.0.1</version>
	</dependency>
	```

## Configure the SDK

Set two values on the static `Atlan` class:

- `Atlan.setApiToken()` should be given your Atlan [API token](/concepts/api-tokens), for authentication
- `Atlan.setBaseUrl()` should be given your Atlan URL (for example, `https://tenant.atlan.com`)

That's it — once these are set, you can start using your SDK to make live calls against your Atlan instance! 🎉

Here's an example of setting these based on environment variables:

```java title="AtlanLiveTest.java"
import com.atlan.Atlan;

public class AtlanLiveTest {
    static {
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
    }
}
```
