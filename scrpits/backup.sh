#!/bin/bash

# Define backup directory and filename
BACKUP_DIR="/c/Projects/MerchStore/backup"
BACKUP_FILE="$BACKUP_DIR/backup_$(date +%Y%m%d_%H%M%S).sql"

# Create backup directory if it doesn't exist
mkdir -p "$BACKUP_DIR"

# Perform the backup
docker exec -t postgres pg_dump -U ToJestTrudnyLogin merchDatabase > "$BACKUP_FILE"
echo BACKUP_FILE
# Optional: Remove old backups (keep only the last 7 backups)
ls -1tr "$BACKUP_DIR/backup_*.sql" | head -n -7 | xargs -d '\n' rm -f --
