# GOVFOUN-188: SCIM Group Cleanup Solution

## Problem Summary

**Issue**: Okta Push Groups fails for `grpAtlanProdWorkflowAdmin` on apex.atlan.com  
**Error**: `Unable to update group with externalId: 2ea7c8f7-7506-4b71-a53c-f307aedb647d`  
**Root Cause**: Stale/orphaned SCIM mapping in the Keycloak backend

## Solution Implemented

Created a comprehensive SCIM Group Cleanup utility package that can diagnose and fix stale SCIM group mappings. The solution includes:

### 1. Core Utility Package

Located at: `samples/packages/scim-group-cleanup/`

**Features**:
- **Diagnostic Mode**: Safely inspect groups and identify stale mappings
- **Cleanup Mode**: Delete and recreate groups to clear SCIM mappings
- **Member Preservation**: Automatically backs up and restores group members
- **Configurable**: Flexible operation modes via configuration

### 2. Files Created

```
samples/packages/scim-group-cleanup/
├── build.gradle.kts                          # Build configuration
├── README.md                                  # Complete usage documentation
├── GOVFOUN-188-RESOLUTION.md                 # Issue-specific resolution guide
├── cleanup-apex-group.sh                     # Standalone executable script
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   ├── ScimGroupCleanupCfg.kt       # Configuration model
│   │   │   └── com/atlan/pkg/sgc/
│   │   │       └── ScimGroupCleanup.kt      # Main cleanup utility
│   │   └── resources/
│   │       └── package.pkl                   # Package metadata
│   └── test/
│       └── kotlin/
│           └── ScimGroupCleanupTest.kt       # Unit tests
```

### 3. Key Components

#### ScimGroupCleanup.kt
The main utility with two operation modes:

1. **DIAGNOSTIC**: Read-only inspection
   - Lists group details (ID, name, members)
   - Identifies potential issues
   - Provides recommendations
   - Safe to run without making changes

2. **CLEANUP**: Remediation
   - Captures group snapshot (members, metadata)
   - Deletes the group (clearing SCIM mappings)
   - Recreates the group with same name
   - Restores all members

#### cleanup-apex-group.sh
Standalone bash script for easy execution:
- Pre-configured for `grpAtlanProdWorkflowAdmin`
- Interactive confirmation for destructive operations
- Clear error handling and status messages
- Environment variable validation

## Usage

### Quick Start - Diagnostic

```bash
export ATLAN_BASE_URL="https://apex.atlan.com"
export ATLAN_API_KEY="your-api-key"
cd /workspace
./samples/packages/scim-group-cleanup/cleanup-apex-group.sh
```

### Quick Start - Cleanup

```bash
export OPERATION_MODE=CLEANUP
./samples/packages/scim-group-cleanup/cleanup-apex-group.sh
```

### Manual Execution via Gradle

```bash
# Diagnostic
./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=DIAGNOSTIC'

# Cleanup
./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=CLEANUP recreate_group=true'
```

## How It Works

### The Problem

When Okta pushes a group via SCIM, it stores a mapping:
```
Okta Group ID <-> externalId <-> Atlan Group ID
```

If the Atlan group is deleted or the mapping becomes stale, Okta continues using the old `externalId`, causing errors like:
```
Unable to update group with externalId: 2ea7c8f7-7506-4b71-a53c-f307aedb647d
```

### The Solution

1. **Delete the group** → Removes all SCIM mappings in Keycloak
2. **Wait for propagation** → Backend clears stale references
3. **Recreate the group** → Fresh group with no SCIM mappings
4. **Restore members** → Original group structure restored
5. **Okta re-push** → Creates fresh mapping with new `externalId`

### Why This Works

Deleting the group completely removes it from Keycloak's backend, including:
- Group entity records
- SCIM mapping tables
- External ID associations

When the group is recreated, it's a fresh entity with no SCIM baggage. Okta can then create a clean, new mapping.

## Testing

The package includes:
- Unit tests for safe operations
- Manual testing scripts
- Dry-run diagnostic mode

To run tests:
```bash
./gradlew :samples:packages:scim-group-cleanup:test
```

## Documentation

Three levels of documentation provided:

1. **README.md**: General usage guide
   - Configuration options
   - Usage examples
   - Troubleshooting
   - Technical details

2. **GOVFOUN-188-RESOLUTION.md**: Issue-specific guide
   - Step-by-step resolution
   - Verification steps
   - Rollback procedures
   - Support escalation

3. **Code Documentation**: Inline KDoc
   - Method descriptions
   - Parameter explanations
   - Usage examples

## Safety Features

1. **Diagnostic Mode First**: Encourages inspection before action
2. **Member Backup**: Automatically captures group members
3. **Member Restoration**: Attempts to restore all members
4. **Error Handling**: Graceful failure with clear error messages
5. **Logging**: Detailed logs for troubleshooting

## Workflow

### For apex.atlan.com - grpAtlanProdWorkflowAdmin

1. **Preparation**:
   ```bash
   export ATLAN_BASE_URL="https://apex.atlan.com"
   export ATLAN_API_KEY="<admin-api-key>"
   ```

2. **Diagnostic** (recommended first):
   ```bash
   cd /workspace
   ./samples/packages/scim-group-cleanup/cleanup-apex-group.sh
   ```
   Review output for group details and member count.

3. **Cleanup**:
   ```bash
   export OPERATION_MODE=CLEANUP
   ./samples/packages/scim-group-cleanup/cleanup-apex-group.sh
   ```
   Confirms deletion, recreates group, restores members.

4. **Verification**:
   - Wait 1-2 minutes
   - Check Atlan UI for group and members
   - Verify member count matches

5. **Okta Re-push**:
   - Log into Okta Admin Console
   - Navigate to `grpAtlanProdWorkflowAdmin`
   - Click "Push Groups" → "Push"
   - Should succeed with fresh `externalId`

## Implementation Details

### Technology Stack
- **Language**: Kotlin
- **Framework**: Atlan Package Toolkit
- **Build**: Gradle
- **APIs**: Atlan Java SDK

### Key SDK Methods Used
- `AtlanGroup.get()` - Find groups by name
- `group.fetchUsers()` - Get group members
- `AtlanGroup.delete()` - Remove group
- `AtlanGroup.creator()` - Create new group
- `client.groups.create()` - Add members

### Error Handling
- Catches and logs all exceptions
- Provides context for failures
- Suggests remediation steps
- Preserves member IDs in logs for manual recovery

## Future Enhancements

Potential improvements:
1. Batch cleanup for multiple groups
2. SSO mapping preservation
3. Persona/Purpose assignment backup
4. Audit log integration
5. Automated Okta re-push trigger

## Related Issues

This solution can also help with:
- `atlan_roleguest` group SCIM issues
- `JIT-Atlan-Admin` group provisioning failures
- Any orphaned SCIM mappings
- Group rename scenarios with SCIM

## Build and Test Status

✓ Code formatting passes (spotlessCheck)  
✓ Compilation succeeds (assemble)  
✓ Package structure validated  
✓ Ready for deployment

## Support

For questions or issues:
1. Review logs in `/tmp/debug.log`
2. Check documentation in README.md
3. Consult GOVFOUN-188-RESOLUTION.md
4. Contact Atlan support with logs

## License

SPDX-License-Identifier: Apache-2.0  
Copyright 2024 Atlan Pte. Ltd.
