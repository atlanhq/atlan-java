# SCIM Group Cleanup Utility

## Overview

This utility diagnoses and fixes stale SCIM group mappings that cause Okta Push Groups to fail with errors like:

```
Error: Unable to update group with externalId: 2ea7c8f7-7506-4b71-a53c-f307aedb647d
```

## Problem Statement

When Okta attempts to push a group to Atlan via SCIM, it may fail if there's an orphaned mapping in the SCIM/Keycloak backend. This happens when:

1. A group was previously provisioned via SCIM with a specific `externalId`
2. The group was deleted or the mapping became stale
3. Okta still tries to update using the old `externalId`
4. Atlan's backend cannot resolve this `externalId` to a valid group

## Solution

This utility provides two modes:

### 1. Diagnostic Mode (Safe, Read-Only)

Run this first to inspect the group and understand the issue:

```bash
./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=DIAGNOSTIC'
```

This will:
- Find the group by name
- Display group details (ID, members, metadata)
- Identify potential issues
- Provide recommendations

### 2. Cleanup Mode (Destructive)

Run this to fix the stale SCIM mapping:

```bash
./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=CLEANUP recreate_group=true'
```

This will:
1. Capture group details and member list
2. Delete the group (clearing stale SCIM mappings)
3. Optionally recreate the group with the same name
4. Restore group members

## Usage Workflow

### Step 1: Diagnostic

First, run in diagnostic mode to understand the current state:

```bash
# Set your environment variables
export ATLAN_BASE_URL="https://apex.atlan.com"
export ATLAN_API_KEY="your-api-key"

# Run diagnostic
./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=DIAGNOSTIC'
```

### Step 2: Backup (Optional but Recommended)

Export the group members or take a snapshot of important assignments.

### Step 3: Cleanup

Run the cleanup to fix the stale mapping:

```bash
./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=CLEANUP recreate_group=true'
```

### Step 4: Re-push from Okta

After cleanup, go back to Okta and attempt to push the group again via SCIM Push Groups. The stale `externalId` mapping should now be cleared, allowing Okta to create a fresh mapping.

## Configuration Options

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `group_name` | String | Yes | Name of the group with stale SCIM mappings |
| `operation_mode` | String | Yes | Either `DIAGNOSTIC` or `CLEANUP` |
| `recreate_group` | Boolean | No | Whether to recreate the group after deletion (default: `true`) |

## Important Notes

1. **Backup First**: Always run in DIAGNOSTIC mode first and consider backing up group memberships
2. **Destructive Operation**: CLEANUP mode deletes the group, which may affect permissions and access
3. **Member Restoration**: The utility attempts to restore group members, but verify after cleanup
4. **SSO Mappings**: This does NOT handle SSO group mappings - those need to be managed separately
5. **SCIM-Specific**: This addresses SCIM provisioning issues, not general group management

## Troubleshooting

### Group Not Found

If the group isn't found during diagnostic:
- Verify the group name is correct
- Check if the group was already deleted
- The stale mapping may exist without a corresponding Atlan group

### Member Restoration Fails

If member restoration fails during cleanup:
- Check the logs for specific user IDs
- Manually re-add users to the group
- Verify user permissions

### Okta Push Still Fails

If Okta push still fails after cleanup:
1. Wait a few minutes for backend propagation
2. Try unlinking and relinking the group in Okta
3. Check Okta logs for different error messages
4. Contact Atlan support if the issue persists

## Technical Details

### How It Works

1. **Diagnostic Mode**:
   - Queries Atlan's group API to find the target group
   - Retrieves group metadata and member list
   - Reports findings without modifications

2. **Cleanup Mode**:
   - Captures group snapshot (members, metadata)
   - Deletes the group via Atlan's admin API
   - Waits for backend propagation (2 seconds)
   - Recreates the group with the same name (if requested)
   - Restores group members

### Why This Fixes the Issue

Deleting the group removes all associated SCIM mappings in the Keycloak backend. When Okta next pushes the group, it will create a fresh mapping with a new `externalId`, avoiding the stale reference error.

## Related Issues

This utility addresses similar issues documented in:
- `atlan_roleguest` group with stale mappings
- `JIT-Atlan-Admin` group SCIM provisioning failures
- Orphaned SCIM mappings after group renames or deletions

## Support

For additional help:
- Check Atlan documentation: https://docs.atlan.com
- Contact Atlan support with error logs
- Review SCIM provisioning logs in Okta
