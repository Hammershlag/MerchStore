current_date=$(date +"%d_%m_%Y_%H_%M")

docker exec -t merchStoreDB pg_dumpall -c -U ToJestTrudnyLogin > backups/backup_$current_date

echo "Backup zrobiony"