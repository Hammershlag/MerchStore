To create backup use:
```bash
docker exec -t merchStoreDB pg_dumpall -c -U ToJestTrudnyLogin > backup_01_09_2024
```

3d0ae38bc64e - container id <br>
ToJestTrudnyLogin - login <br>
backup_11_06_2024 - backup name <br>



To update docker app
mvn clean package
docker build -t merch-store-app .
docker-compose up -d --build springboot-app
