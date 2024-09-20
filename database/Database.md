# Web Store Database Schema Documentation

## Table of Contents

- [Users Table](#users-table)
- [Categories Table](#categories-table)
- [Items Table](#items-table)
- [Discounts Table](#discounts-table)
- [Discount Items Table](#discount-items-table)
- [Orders Table](#orders-table)
- [Order Items Table](#order-items-table)
- [Cart Table](#cart-table)
- [Reviews Table](#reviews-table)
- [Ads Table](#ads-table)
- [Sales Table](#sales-table)
- [Best Sellers View](#best-sellers-view)
- [Enums](#enums)
    - [AdStatus](#adstatus)
    - [Gender](#gender)
    - [OrderStatus](#orderstatus)
    - [Role](#role)
- [Docker Commands](#docker-commands)

## Users Table

The `users` table stores information about the users of the web store.

| Column        | Type              | Description                                                                 |
|---------------|-------------------|-----------------------------------------------------------------------------|
| `user_id`     | `SERIAL PRIMARY KEY` | Unique identifier for each user.                                             |
| `username`    | `VARCHAR(50)`     | Unique username for the user.                                               |
| `email`       | `VARCHAR(100)`    | Unique email address of the user.                                           |
| `password`    | `VARCHAR(255)`    | Hashed password for the user.                                               |
| `first_name`  | `VARCHAR(50)`     | User's first name.                                                          |
| `last_name`   | `VARCHAR(50)`     | User's last name.                                                           |
| `phone_number`| `VARCHAR(20)`     | User's phone number.                                                        |
| `address`     | `VARCHAR(255)`    | User's address.                                                             |
| `role`        | `VARCHAR(10)`     | Role of the user (`USER`, `ADMIN`, `OWNER`).                                |
| `image`       | `BYTEA`           | User's profile image.                                                       |
| `gender`      | `VARCHAR(10)`     | User's gender (`MALE`, `FEMALE`, `DEFAULT`).                                |
| `created_at`  | `TIMESTAMP`       | Timestamp when the user was created.                                        |
| `updated_at`  | `TIMESTAMP`       | Timestamp when the user was last updated.                                   |
| `birth_date`  | `DATE`            | User's date of birth.                                                       |

## Categories Table

The `categories` table stores product categories in the web store.

| Column        | Type              | Description                                                                 |
|---------------|-------------------|-----------------------------------------------------------------------------|
| `category_id` | `SERIAL PRIMARY KEY` | Unique identifier for each category.                                        |
| `name`        | `VARCHAR(100)`    | Unique name of the category.                                                |
| `description` | `TEXT`            | Description of the category.                                                |
| `image`       | `BYTEA`           | Image representing the category.                                            |
| `main`        | `BOOLEAN`         | Indicates if the category is a main category (`TRUE` or `FALSE`).           |

## Discounts Table

The `discounts` table stores information about discounts available in the web store.

| Column               | Type                | Description                                                           |
|----------------------|---------------------|-----------------------------------------------------------------------|
| `discount_id`        | `SERIAL PRIMARY KEY` | Unique identifier for each discount.                                  |
| `code`               | `VARCHAR(50)`       | Unique code for the discount.                                         |
| `description`        | `TEXT`              | Description of the discount.                                          |
| `discount_percentage`| `DECIMAL(5, 2)`     | Percentage discount offered.                                          |
| `valid_from`         | `DATE`              | Date from which the discount is valid.                                |
| `valid_until`        | `DATE`              | Date until which the discount is valid.                               |

## Discount Items Table

The `discount_items` table maps discounts to specific items.

| Column            | Type                | Description                                                             |
|-------------------|---------------------|-------------------------------------------------------------------------|
| `discount_item_id`| `SERIAL PRIMARY KEY` | Unique identifier for each discount-item relationship.                  |
| `discount_id`     | `INT`               | Identifier for the discount.                                            |
| `item_id`         | `INT`               | Identifier for the item.                                                |

- **Unique Constraint**: Each item can only have one unique discount.

## Orders Table

The `orders` table stores information about customer orders.

| Column        | Type                | Description                                                              |
|---------------|---------------------|--------------------------------------------------------------------------|
| `order_id`    | `SERIAL PRIMARY KEY` | Unique identifier for each order.                                        |
| `user_id`     | `INT`               | Identifier for the user who placed the order.                            |
| `order_date`  | `TIMESTAMP`         | Timestamp when the order was placed.                                     |
| `status`      | `VARCHAR(50)`       | Status of the order (e.g., `PENDING`, `SHIPPED`, `DELIVERED`).           |
| `total_amount`| `DECIMAL(10, 2)`    | Total amount of the order.                                               |
| `discount_id` | `INT`               | Identifier for any discount applied to the order.                        |

## Order Items Table

The `order_items` table stores information about the items included in each order.

| Column         | Type                | Description                                                             |
|----------------|---------------------|-------------------------------------------------------------------------|
| `order_item_id`| `SERIAL PRIMARY KEY` | Unique identifier for each order-item relationship.                     |
| `order_id`     | `INT`               | Identifier for the order.                                               |
| `item_id`      | `INT`               | Identifier for the item.                                                |
| `quantity`     | `INT`               | Quantity of the item ordered.                                           |
| `price`        | `DECIMAL(10, 2)`    | Price of the item at the time of the order.                             |

## Cart Table

The `cart` table stores items added to a user's shopping cart.

| Column     | Type                | Description                                                              |
|------------|---------------------|--------------------------------------------------------------------------|
| `cart_id`  | `SERIAL PRIMARY KEY` | Unique identifier for each cart entry.                                   |
| `user_id`  | `INT`               | Identifier for the user who owns the cart.                               |
| `item_id`  | `INT`               | Identifier for the item added to the cart.                               |
| `quantity` | `INT`               | Quantity of the item added to the cart.                                  |

## Reviews Table

The `reviews` table stores reviews for items in the web store.

| Column       | Type                | Description                                                              |
|--------------|---------------------|--------------------------------------------------------------------------|
| `review_id`  | `SERIAL PRIMARY KEY` | Unique identifier for each review.                                       |
| `user_id`    | `INT`               | Identifier for the user who wrote the review.                            |
| `item_id`    | `INT`               | Identifier for the item being reviewed.                                  |
| `description`| `VARCHAR(1500)`     | Text of the review.                                                      |
| `created_at` | `TIMESTAMP`         | Timestamp when the review was created.                                   |
| `updated_at` | `TIMESTAMP`         | Timestamp when the review was last updated.                              |
| `star_rating`| `INT`               | Star rating given by the user (e.g., 1-5).                               |

- **Unique Constraint**: A user can only review an item once.

## Ads Table

The `ads` table stores advertisements for items in the web store.

| Column       | Type                | Description                                                              |
|--------------|---------------------|--------------------------------------------------------------------------|
| `ad_id`      | `SERIAL PRIMARY KEY` | Unique identifier for each ad.                                           |
| `item_id`    | `INT`               | Identifier for the item being advertised.                                |
| `user_id`    | `INT`               | Identifier for the user who created the ad.                              |
| `description`| `TEXT`              | Description of the ad.                                                   |
| `start_date` | `TIMESTAMP`         | Start date of the ad campaign.                                           |
| `end_date`   | `TIMESTAMP`         | End date of the ad campaign.                                             |
| `status`     | `VARCHAR(20)`       | Status of the ad (`ACTIVE`, `INACTIVE`, `DELETED`).                      |
| `image`      | `BYTEA`             | Image associated with the ad.                                            |
| `created_at` | `TIMESTAMP`         | Timestamp when the ad was created.                                       |
| `updated_at` | `TIMESTAMP`         | Timestamp when the ad was last updated.                                  |

## Sales Table

The `sales` table stores information about the sale of items in the web store.

| Column   | Type                | Description                                                               |
|----------|---------------------|---------------------------------------------------------------------------|
| `id`     | `SERIAL PRIMARY KEY` | Unique identifier for each sale entry.                                    |
| `item_id`| `INT`               | Identifier for the item sold.                                             |
| `quantity`| `INT`              | Quantity of the item sold.                                                |
| `sale_date`| `TIMESTAMP`       | Timestamp when the sale was made.                                         |

## Best Sellers View

The `best_sellers` view displays the top 10 best-selling items in the last 30 days.

| Column       | Type  | Description                                    |
|--------------|-------|------------------------------------------------|
| `item_id`    | `INT` | Identifier for the item.                       |
| `total_sales`| `INT` | Total quantity of the item sold in the last 30 days. |

## Enums

### AdStatus

The `AdStatus` enum represents the possible statuses of an ad.

- `ACTIVE`: The ad is currently active.
- `INACTIVE`: The ad is inactive but still exists.
- `DELETED`: The ad has been deleted.

### Gender

The `Gender` enum represents the gender of a user.

- `MALE`: Male gender.
- `FEMALE`: Female gender.
- `DEFAULT`: Default value, used when gender is not specified.

### OrderStatus

The `OrderStatus` enum represents the possible statuses of an order.

- `UNPAID`: The order has not been paid for.
- `PENDING`: The order is pending processing.
- `CONFIRMED`: The order has been confirmed.
- `PROCESSING`: The order is being processed.
- `SHIPPED`: The order has been shipped.
- `DELIVERED`: The order has been delivered.
- `CANCELED`: The order has been canceled.
- `RETURNED`: The order has been returned.
- `REFUNDED`: The order has been refunded.
- `ON_HOLD`: The order is on hold.
- `FAILED`: The order has failed.

### Role

The `Role` enum represents the role of a user in the system.

- `USER`: Regular user with basic privileges - 0
- `ADMIN`: Administrator with elevated privileges - 1
- `OWNER`: Owner of the web store with the highest level of access - 2

Each role has an associated integer value, and a static map is used to retrieve roles by their name, specified above.

## Docker Commands

### Backup the Database

To create a backup of the PostgreSQL database running in your Docker container, use the following command:
```bash
docker exec -t merchStoreDB pg_dumpall -c -U ToJestTrudnyLogin > backup_dd_mm_yyyy
```

- **merchStoreDB**: The name or container ID of the PostgreSQL container (e.g., `3d0ae38bc64e`).
- **ToJestTrudnyLogin**: The PostgreSQL login username.
- **backup_dd_mm_yyyy**: The name of the backup file.

### Automatic Database Backup

```bash
$wslPath = (Get-Location).Path -replace '\\', '/' -replace 'C:', '/mnt/c'
wsl "$wslPath/backup.sh"
```

Perform the backup from the terminal in the `\database` directory.

### Update the Docker Application

To update your Dockerized web store application, follow these steps:

1. **Clean and Package the Maven Project**:
```bash
   mvn clean package
```

2. **Build the Docker Image**:
```bash
    docker build -t merch-store-app .

```

3. **Rebuild and Restart the Docker Containers**:
```bash
   docker-compose up -d --build springboot-app
```

This will rebuild the Docker image and update the running Spring Boot application container. <br><br>
Currently, the application is not dockerized, so the above commands are not applicable.
