/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

/**
 * Interface for syncing files between cloud object storage and local file systems.
 */
interface ObjectStorageSyncer {
    /**
     * Copy all files from the provided prefix to the specified local directory.
     * Note: files that already exist in the local directory will be replaced by files from object storage,
     * whenever the object's last modification timestamp is greater than the local file's.
     *
     * @param prefix from which to copy the files
     * @param localDirectory into which to copy the files
     * @return list of files that were copied (as a local target path)
     */
    fun copyFrom(
        prefix: String,
        localDirectory: String,
    ): List<String>

    /**
     * Copy the latest file we can find in the specified prefix, with the specified extension, to
     * the provided local directory location.
     * Note: this relies on the keys of the objects in the prefix being sortable. (They will be
     * sorted in descending order, and the first one chosen as the latest.)
     *
     * @param prefix from which to copy the file
     * @param extension unique extension the file must have to be copied
     * @param localDirectory into which to copy the file
     * @return path to the file that was downloaded (locally), or blank if no file was downloaded
     */
    fun copyLatestFrom(
        prefix: String,
        extension: String,
        localDirectory: String,
    ): String

    /**
     * Download a single file from the provided object key to the specified local file.
     *
     * @param remoteKey from which to download the file
     * @param localFile into which to download the file
     */
    fun downloadFrom(
        remoteKey: String,
        localFile: String,
    )

    /**
     * Copy all files from the provided local directory to the specified object storage prefix.
     * Note: files that already exist in object store will be replaced by files from the local directory,
     * whenever the local file's last modification timestamp is greater than object's.
     *
     * @param localDirectory from which to copy the files
     * @param prefix into which to copy the files
     * @return true if any files were copied, otherwise false
     */
    fun copyTo(
        localDirectory: String,
        prefix: String,
    ): Boolean

    /**
     * Upload a single file from the specified local file to the provided object key.
     *
     * @param localFile from which to upload the file
     * @param remoteKey into which to upload the file
     */
    fun uploadTo(
        localFile: String,
        remoteKey: String,
    )
}
