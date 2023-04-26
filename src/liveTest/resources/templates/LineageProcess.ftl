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
            List<Catalog> inputs,
            List<Catalog> outputs,
            LineageProcess parent) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return LineageProcess.builder()
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName, id, inputs, outputs, parent))
                .name(name)
                .connectorType(connectorType)
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
        return LineageProcess.builder().qualifiedName(qualifiedName).name(name);
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LineageProcess", String.join(",", missing));
        }
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
            List<Catalog> inputs,
            List<Catalog> outputs,
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
    private static void appendRelationships(StringBuilder sb, List<Catalog> relationships) {
        for (Catalog relationship : relationships) {
            appendRelationship(sb, relationship);
        }
    }

    /**
     * Append a single relationship into the provided string builder.
     * @param sb into which to append
     * @param relationship to append
     */
    private static void appendRelationship(StringBuilder sb, Asset relationship) {
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
</#macro>
