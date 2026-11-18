#!/usr/bin/env bash
# SPDX-License-Identifier: Apache-2.0
# Copyright 2024 Atlan Pte. Ltd.

#
# Standalone script to clean up the stale SCIM mapping for grpAtlanProdWorkflowAdmin on apex.atlan.com
#
# This script addresses the specific issue: GOVFOUN-188
# Error: Okta Push Groups fails for grpAtlanProdWorkflowAdmin with stale externalId 2ea7c8f7-7506-4b71-a53c-f307aedb647d
#

set -euo pipefail

# Configuration
GROUP_NAME="${GROUP_NAME:-grpAtlanProdWorkflowAdmin}"
OPERATION_MODE="${OPERATION_MODE:-DIAGNOSTIC}"
RECREATE_GROUP="${RECREATE_GROUP:-true}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "================================================================"
echo "SCIM Group Cleanup for apex.atlan.com"
echo "================================================================"
echo ""
echo "Target group: ${GROUP_NAME}"
echo "Operation mode: ${OPERATION_MODE}"
echo "Recreate after deletion: ${RECREATE_GROUP}"
echo ""

# Check for required environment variables
if [ -z "${ATLAN_BASE_URL:-}" ]; then
    echo -e "${RED}ERROR: ATLAN_BASE_URL environment variable not set${NC}"
    echo "Please set: export ATLAN_BASE_URL=https://apex.atlan.com"
    exit 1
fi

if [ -z "${ATLAN_API_KEY:-}" ]; then
    echo -e "${RED}ERROR: ATLAN_API_KEY environment variable not set${NC}"
    echo "Please set: export ATLAN_API_KEY=your-api-key"
    exit 1
fi

echo -e "${GREEN}Environment variables configured${NC}"
echo "Base URL: ${ATLAN_BASE_URL}"
echo ""

# Confirm if running in cleanup mode
if [ "${OPERATION_MODE}" = "CLEANUP" ]; then
    echo -e "${YELLOW}WARNING: CLEANUP mode will DELETE the group!${NC}"
    echo "This will:"
    echo "  1. Delete the group '${GROUP_NAME}'"
    echo "  2. Remove all group member assignments"
    echo "  3. Clear stale SCIM mappings"
    if [ "${RECREATE_GROUP}" = "true" ]; then
        echo "  4. Recreate the group with the same name"
        echo "  5. Restore group members"
    fi
    echo ""
    read -p "Are you sure you want to proceed? (yes/no): " -r
    echo
    if [[ ! $REPLY =~ ^[Yy][Ee][Ss]$ ]]; then
        echo "Operation cancelled."
        exit 0
    fi
fi

# Change to the script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${SCRIPT_DIR}"

# Navigate to workspace root
cd "../../.."

echo "Building the utility..."
./gradlew :samples:packages:scim-group-cleanup:assemble

echo ""
echo "Running SCIM Group Cleanup utility..."
echo "================================================================"

# Run the utility
./gradlew :samples:packages:scim-group-cleanup:run \
  --args="group_name=${GROUP_NAME} operation_mode=${OPERATION_MODE} recreate_group=${RECREATE_GROUP}"

EXIT_CODE=$?

echo "================================================================"
echo ""

if [ ${EXIT_CODE} -eq 0 ]; then
    echo -e "${GREEN}✓ Cleanup completed successfully${NC}"
    echo ""
    
    if [ "${OPERATION_MODE}" = "CLEANUP" ]; then
        echo "Next steps:"
        echo "  1. Wait 1-2 minutes for backend propagation"
        echo "  2. Go to Okta Admin Console"
        echo "  3. Navigate to the ${GROUP_NAME} group"
        echo "  4. Try pushing the group to Atlan again via Push Groups"
        echo "  5. The stale externalId error should now be resolved"
    else
        echo "Diagnostic complete. Review the output above."
        echo ""
        echo "To fix the stale SCIM mapping, run:"
        echo "  OPERATION_MODE=CLEANUP ./cleanup-apex-group.sh"
    fi
else
    echo -e "${RED}✗ Cleanup failed with exit code ${EXIT_CODE}${NC}"
    echo "Please review the error messages above."
    exit ${EXIT_CODE}
fi
