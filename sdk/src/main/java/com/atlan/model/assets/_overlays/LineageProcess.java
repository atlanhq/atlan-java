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

    /**
     * Generate a unique qualifiedName for a LineageProcess.
     *
     * @param name of the process to use for display purposes
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param id (optional) unique ID of this process within the software / system that ran it (if not provided, it will be generated)
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @param parent (optional) parent process in which this sub-process ran
     * @return unique qualifiedName for the LineageProcess
     */
    public static String generateQualifiedName(
            String name,
            String connectionQualifiedName,
            String id,
            List<ICatalog> inputs,
            List<ICatalog> outputs,
            LineageProcess parent) {
        if (id != null && id.length() > 0) {
            return connectionQualifiedName + "/" + id;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(connectionQualifiedName);
            if (parent != null) {
                appendRelationship(sb, parent);
            }
            appendRelationships(sb, inputs);
            appendRelationships(sb, outputs);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(sb.toString().getBytes(StandardCharsets.UTF_8));
                String hashed = String.format("%032x", new BigInteger(1, md.digest()));
                return connectionQualifiedName + "/" + hashed;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(
                        "Unable to generate the qualifiedName for the process: MD5 algorithm does not exist on your platform!");
            }
        }
    }

    private static void appendRelationships(StringBuilder sb, List<ICatalog> relationships) {
        for (ICatalog relationship : relationships) {
            appendRelationship(sb, (IAsset) relationship);
        }
    }

    private static void appendRelationship(StringBuilder sb, IAsset relationship) {
        if (relationship.getGuid() != null) {
            sb.append(relationship.getGuid());
        } else if (relationship.getUniqueAttributes() != null
                && relationship.getUniqueAttributes().getQualifiedName() != null) {
            sb.append(relationship.getUniqueAttributes().getQualifiedName());
        }
    }