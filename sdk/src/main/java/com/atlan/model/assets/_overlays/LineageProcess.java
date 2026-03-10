// IMPORT: import com.atlan.model.enums.AIDatasetType;
// IMPORT: import java.math.BigInteger;
// IMPORT: import java.nio.charset.StandardCharsets;
// IMPORT: import java.security.MessageDigest;
// IMPORT: import java.security.NoSuchAlgorithmException;

/**
     * Builds the minimal object necessary to create a process.
     *
     * @param name of the process to use for display purposes
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param id (optional) unique ID of this process within the software / system that ran it (if not provided, it will be generated)
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @param parent (optional) parent process in which this sub-process ran
     * @return the minimal object necessary to create the process, as a builder
     */
    public static LineageProcessBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String id,
            List<ICatalog> inputs,
            List<ICatalog> outputs,
            LineageProcess parent) {
        return LineageProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName, id, inputs, outputs, parent))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs);
    }