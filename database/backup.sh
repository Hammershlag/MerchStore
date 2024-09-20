#!/bin/bash

# Define the backup directory
backup_dir="database/backups"

# Check if the backup directory exists, if not, create it
[ ! -d "$backup_dir" ] && mkdir -p "$backup_dir"

# Count the number of files in the backup directory
file_count=$(ls -1 $backup_dir | wc -l)

# If there are 5 or more files, delete the oldest one
if [ "$file_count" -ge 6 ]; then
    # Find the oldest file and delete it
    oldest_file=$(ls -t $backup_dir | tail -1)
    rm "$backup_dir/$oldest_file"
    echo "Deleted oldest backup: $oldest_file"
fi

# Create the backup with a timestamp
current_date=$(date +"%d_%m_%Y_%H_%M")
docker exec -t merchStoreDB pg_dumpall -c -U ToJestTrudnyLogin > $backup_dir/backup_$current_date.sql

echo "Backup created: backup_$current_date.sql"
