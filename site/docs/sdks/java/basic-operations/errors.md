
# Error-handling in the Java SDK

## Exceptions

The SDK defines checked exceptions for the following categories of error:

| Exception | Description |
|---|---|
| `ApiConnectionException` | Errors when the SDK is unable to connect to the API, for example due to a lack of network access or timeouts. |
| `AuthenticationException` | Errors when the API token configured for the SDK is invalid or expired. |
| `ConflictException` | Errors when there is some conflict with an existing asset and the operation cannot be completed as a result. |
| `InvalidRequestException` | Errors when the request sent to Atlan does not match its expectations. If you are using the built-in methods like `toCreate()` and `toUpdate()` this exception should be treated as a bug in the SDK. (These operations take responsibility for avoiding this error.) |
| `LogicException` | Errors where some assumption made in the SDK's code is proven incorrect. If ever raised, they should be reported as bugs against the SDK. |
| `NotFoundException` | Errors when the requested resource or asset does not exist in Atlan. |
| `PermissionException` | Errors when the API token used by the SDK does not have permission to access a resource or carry out an operation on a specific asset. |
| `RateLimitException` | Errors when the Atlan server is being overwhelmed by requests. |

A given API call could fail due to all of the errors above. So these all extend a generic `AtlanException` checked exception, and all API operations throw `AtlanException`.

## An example

For example, when creating a connection there is an asynchronous process that grants permissions to the admins of that connection. So there can be a slight delay between creating the connection and being permitted to do any operations with the connection. During that delay, any attempt to interact with the connection will result in a `PermissionException`, even if your API token is one of the connection admins.

To work around this, you can implement a wait-loop along these lines:

```java linenums="1" title="Wait loop for permissions to be granted asynchronously"
Entity minimal = null;
do { // (1)
    try {
        minimal = Entity.retrieveMinimal(connectionGuid); // (2)
    } catch (PermissionException pe) { // (3)
        log.warn("Connection not yet accessible, will wait and retry...");
        Thread.sleep(5000); // (4)
    } // (5)
} while (minimal == null); // (6)
```

1. Run your operation through a loop, allowing you to retry. (You should also be able to integrate into more flexible frameworks for resilience, but we'll leave that to your preferences.)
2. Attempt your operation within the loop.
3. Catch any exception(s) you can handle through retrying.
4. Add a delay to the retry, so you don't overwhelm the server with requests. (Again, use your own favorite framework for more advanced resilience including circuit-breaker patterns, exponential back-off, jitter, etc.)
5. Note that you do not catch any other `AtlanException` exceptions that you are not able to handle through retrying. These will still be thrown as errors that you are unable to handle.
6. (Yes, this is an infinite loop if the API token is never granted permission — we're only trying to keep the example extremely concise!)
