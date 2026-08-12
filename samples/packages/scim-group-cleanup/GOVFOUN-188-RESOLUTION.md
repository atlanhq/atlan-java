# Resolution Guide for GOVFOUN-188

## Issue Summary

**Customer**: Flo Barot Jr on apex.atlan.com  
**Problem**: Okta Push Groups fails for `grpAtlanProdWorkflowAdmin` with stale `externalId` error  
**Error**: `Unable to update group with externalId: 2ea7c8f7-7506-4b71-a53c-f307aedb647d`  
**Root Cause**: Orphaned SCIM mapping in Keycloak backend

## Solution Overview

The stale `externalId` mapping needs to be cleared from the SCIM/Keycloak backend. This is accomplished by:
1. Deleting the affected group (which removes all SCIM mappings)
2. Recreating the group with the same name and members
3. Allowing Okta to create a fresh SCIM mapping with a new `externalId`

## Prerequisites

Before proceeding, ensure you have:

1. **Admin access** to apex.atlan.com
2. **API token** with admin privileges
3. **Group member backup** (optional but recommended)
4. **Okta admin access** to re-push the group after cleanup

## Resolution Steps

### Step 1: Set Environment Variables

```bash
export ATLAN_BASE_URL="https://apex.atlan.com"
export ATLAN_API_KEY="your-admin-api-token"
```

### Step 2: Run Diagnostic (Optional but Recommended)

First, inspect the group to understand its current state:

```bash
cd /workspace
./samples/packages/scim-group-cleanup/cleanup-apex-group.sh
```

This runs in DIAGNOSTIC mode by default and will show:
- Group ID and metadata
- Current member count
- Creation and update timestamps
- Sample member list

### Step 3: Run Cleanup

To fix the stale SCIM mapping, run the cleanup:

```bash
export OPERATION_MODE=CLEANUP
./samples/packages/scim-group-cleanup/cleanup-apex-group.sh
```

This will:
1. ✓ Capture group details and member list
2. ✓ Delete the group (clearing stale SCIM mapping)
3. ✓ Recreate the group with the same name
4. ✓ Restore all group members

### Step 4: Wait for Propagation

Wait 1-2 minutes for the backend changes to propagate through the system.

### Step 5: Re-push from Okta

1. Log into Okta Admin Console
2. Navigate to **Directory** → **Groups**
3. Find `grpAtlanProdWorkflowAdmin`
4. Go to the **Push Groups** tab
5. Click **Push** or **Retry**

The group should now push successfully with a fresh `externalId` mapping.

## Alternative: Manual Execution via Gradle

If the shell script doesn't work, you can run the utility directly:

### Diagnostic:
```bash
cd /workspace
./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=DIAGNOSTIC'
```

### Cleanup:
```bash
./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=CLEANUP recreate_group=true'
```

## Verification

After cleanup and re-push, verify:

1. **In Okta**:
   - Group shows as "Active" in Push Groups
   - No error messages
   - Members are synced

2. **In Atlan**:
   - Group exists with correct name
   - Members are present
   - Persona/Purpose assignments are intact

3. **Test Login**:
   - Have a group member log in
   - Verify they have correct permissions

## Troubleshooting

### Issue: Group Not Found During Diagnostic

**Solution**: 
- Verify the group name is exactly `grpAtlanProdWorkflowAdmin`
- Check if the group was already deleted
- The stale mapping may exist without a corresponding Atlan group

### Issue: Member Restoration Fails

**Solution**:
- Check the logs for specific user IDs that failed
- Manually re-add users to the group
- Verify user accounts are active

### Issue: Okta Push Still Fails After Cleanup

**Solutions**:
1. Wait 5 minutes for full backend propagation
2. In Okta, unlink and relink the group:
   - Go to Push Groups tab
   - Click "Unlink pushed group"
   - Wait 1 minute
   - Click "Push" again
3. Check if Okta is using a different `externalId`
4. Verify network connectivity between Okta and Atlan
5. Contact Atlan support if issue persists

### Issue: Permissions Lost After Cleanup

**Solution**:
- Check Persona and Purpose assignments
- Re-assign the group to appropriate Personas/Purposes
- Verify role assignments are correct

## Technical Details

### Why This Works

When a group is deleted in Atlan:
1. The Keycloak backend removes all associated records
2. SCIM mappings (including `externalId`) are cleared
3. Group membership tables are purged

When the group is recreated:
1. A fresh group entity is created
2. Members are restored from the snapshot
3. No SCIM mappings exist yet

When Okta pushes the group again:
1. Okta creates a new SCIM mapping
2. A new `externalId` is generated
3. The group is properly linked

### Stale Mapping Root Causes

This issue typically occurs when:
- Group was renamed in Atlan but not Okta
- Group was deleted and recreated manually
- SCIM sync was interrupted mid-operation
- Backend cleanup job failed to remove old mappings

## Rollback Plan

If something goes wrong:

1. **Group members lost**:
   - Check diagnostic logs for member IDs
   - Manually re-add users from backup
   - Or restore from Atlan audit logs

2. **Group recreation failed**:
   - Manually create the group via UI
   - Add members manually
   - Push from Okta with a fresh mapping

3. **Permissions broken**:
   - Re-assign Personas and Purposes
   - Verify role assignments
   - Check access policies

## Post-Resolution

After successful resolution:

1. **Document the fix** in the Linear issue
2. **Verify with customer** that group push works
3. **Monitor** for similar issues with other groups
4. **Update runbook** if process changes

## Prevention

To prevent similar issues in the future:

1. **Avoid manual group deletion** when using SCIM
2. **Always unlink in Okta** before deleting in Atlan
3. **Use consistent naming** between Okta and Atlan
4. **Monitor SCIM sync logs** regularly
5. **Document group renames** and coordinate with Okta admins

## Support Escalation

If this resolution doesn't work:

1. Collect logs from diagnostic and cleanup runs
2. Capture Okta error screenshots
3. Note exact error messages
4. Escalate to Atlan Support with:
   - Tenant: apex.atlan.com
   - Group: grpAtlanProdWorkflowAdmin
   - External ID: 2ea7c8f7-7506-4b71-a53c-f307aedb647d
   - This resolution guide and logs

## Related Issues

Similar stale SCIM mapping issues:
- `atlan_roleguest` group
- `JIT-Atlan-Admin` group
- Various customer-specific groups with rename history

## Success Criteria

Resolution is complete when:
- ✓ Group pushes successfully from Okta
- ✓ No `externalId` errors
- ✓ All members are synced
- ✓ Permissions are intact
- ✓ Customer confirms resolution
