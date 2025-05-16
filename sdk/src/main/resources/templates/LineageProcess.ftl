<#macro all>
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
     * Builds the minimal object necessary to update a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the minimal request necessary to update the LineageProcess, as a builder
     */
    public static LineageProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return LineageProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LineageProcess, from a potentially
     * more-complete LineageProcess object.
     *
     * @return the minimal object necessary to update the LineageProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LineageProcess are not found in the initial object
     */
    @Override
    public LineageProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique qualifiedName for a process.
     *
     * @param name of the process
     * @param connectionQualifiedName unique name of the specific instance of the software / system that ran the process
     * @param id (optional) unique ID of this process within the software / system that ran it (if not provided, it will be generated)
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @param parent (optional) parent process in which this sub-process ran
     * @return unique name for the process
     */
    public static String generateQualifiedName(
            String name,
            String connectionQualifiedName,
            String id,
            List<ICatalog> inputs,
            List<ICatalog> outputs,
            LineageProcess parent) {
        // If an ID was provided, use that as the unique name for the process
        if (id != null && id.length() > 0) {
            return connectionQualifiedName + "/" + id;
        } else {
            // Otherwise, hash all the relationships to arrive at a consistent
            // generated qualifiedName
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

    /**
     * Append all the relationships into the provided string builder.
     * @param sb into which to append
     * @param relationships to append
     */
    private static void appendRelationships(StringBuilder sb, List<ICatalog> relationships) {
        for (ICatalog relationship : relationships) {
            appendRelationship(sb, (IAsset) relationship);
        }
    }

    /**
     * Append a single relationship into the provided string builder.
     * @param sb into which to append
     * @param relationship to append
     */
    private static void appendRelationship(StringBuilder sb, IAsset relationship) {
        // TODO: if two calls are made for the same process, but one uses GUIDs for
        //  its references and the other uses qualifiedName, we'll end up with different
        //  hashes (duplicate processes)
        if (relationship.getGuid() != null) {
            sb.append(relationship.getGuid());
        } else if (relationship.getUniqueAttributes() != null
                && relationship.getUniqueAttributes().getQualifiedName() != null) {
            sb.append(relationship.getUniqueAttributes().getQualifiedName());
        }
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
