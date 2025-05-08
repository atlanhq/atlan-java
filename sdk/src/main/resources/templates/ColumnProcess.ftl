<#macro all>
    /**
     * Builds the minimal object necessary to create a column-level process.
     *
     * @param name of the column-level process
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param id (optional) unique ID of this process within the software / system that ran it (if not provided, it will be generated)
     * @param inputs columns of data the process reads from
     * @param outputs columns of data the process writes to
     * @param parent parent process in which this column-level process ran
     * @return the minimal object necessary to create the column-level process, as a builder
     */
    public static ColumnProcessBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String id,
            List<ICatalog> inputs,
            List<ICatalog> outputs,
            LineageProcess parent) {
        String connectionName = StringUtils.getNameFromQualifiedName(connectionQualifiedName);
        ColumnProcessBuilder<?, ?> builder = ColumnProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(LineageProcess.generateQualifiedName(name, connectionQualifiedName, id, inputs, outputs, parent))
                .name(name)
                .connectionName(connectionName)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs);
        if (parent != null) {
            builder.process(parent);
        }
        return builder;
    }

    /**
     * Builds the minimal object necessary to update a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the minimal request necessary to update the ColumnProcess, as a builder
     */
    public static ColumnProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return ColumnProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ColumnProcess, from a potentially
     * more-complete ColumnProcess object.
     *
     * @return the minimal object necessary to update the ColumnProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ColumnProcess are not found in the initial object
     */
    @Override
    public ColumnProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
