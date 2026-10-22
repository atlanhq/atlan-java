# Quick Start: Fixing SCIM Group Mapping for grpAtlanProdWorkflowAdmin

## For apex.atlan.com Customer (Flo Barot Jr)

### The Problem
Okta Push Groups fails with:
```
Unable to update group with externalId: 2ea7c8f7-7506-4b71-a53c-f307aedb647d
```

### The Fix (3 Steps)

#### Step 1: Set Credentials
```bash
export ATLAN_BASE_URL="https://apex.atlan.com"
export ATLAN_API_KEY="<your-admin-api-key>"
```

#### Step 2: Run Cleanup
```bash
cd /workspace
export OPERATION_MODE=CLEANUP
./samples/packages/scim-group-cleanup/cleanup-apex-group.sh
```

**This will**:
- ✓ Back up all group members
- ✓ Delete the group (clears stale SCIM mapping)
- ✓ Recreate the group with the same name
- ✓ Restore all members

#### Step 3: Re-push from Okta
1. Wait 2 minutes
2. Log into Okta Admin Console
3. Go to `grpAtlanProdWorkflowAdmin` → Push Groups
4. Click "Push"
5. ✓ Should succeed!

---

## Alternative: Manual Gradle Execution

### Diagnostic First (Recommended)
```bash
cd /workspace
export ATLAN_BASE_URL="https://apex.atlan.com"
export ATLAN_API_KEY="<your-api-key>"
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=DIAGNOSTIC'
```

### Then Cleanup
```bash
./gradlew :samples:packages:scim-group-cleanup:run \
  --args='group_name=grpAtlanProdWorkflowAdmin operation_mode=CLEANUP recreate_group=true'
```

---

## What This Does

**Root Cause**: Okta is trying to update a group using a stale `externalId` that doesn't exist in Atlan's backend anymore.

**Solution**: Delete and recreate the group, which clears all SCIM mappings. Okta can then create a fresh mapping with a new `externalId`.

**Safe**: Group members are automatically backed up and restored.

---

## Verification

After cleanup:
1. Check Atlan UI → Admin → Groups → grpAtlanProdWorkflowAdmin
2. Verify member count matches (check logs for original count)
3. Test Okta push - should succeed
4. Have a member log in to verify permissions

---

## Troubleshooting

**"Group not found"**:
- Group may already be deleted
- Check group name spelling
- Check you have admin access

**"Member restoration failed"**:
- Check logs for member IDs
- Manually re-add members from logs
- Contact support if needed

**"Okta push still fails"**:
- Wait 5 more minutes
- Try unlinking/relinking in Okta
- Check for different error message
- Contact Atlan support

---

## Need Help?

Full documentation:
- `/workspace/samples/packages/scim-group-cleanup/README.md`
- `/workspace/samples/packages/scim-group-cleanup/GOVFOUN-188-RESOLUTION.md`
- `/workspace/SOLUTION_SUMMARY.md`

Logs are in: `/tmp/debug.log`
