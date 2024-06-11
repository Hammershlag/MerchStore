current_date=$(date +"%d_%m_%Y_%H_%M")

docker exec -t merchstore_merchStore_1 pg_dumpall -c -U ToJestTrudnyLogin > backups/backup_$current_date

echo "Backup zrobiony"