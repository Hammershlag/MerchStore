-- Create users table
CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       phone_number VARCHAR(20),
                       address VARCHAR(255),
                       role VARCHAR(10) NOT NULL,
                       image BYTEA,
                       gender VARCHAR(10) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       birth_date DATE,
                       oauth_provider VARCHAR(20),
                       oauth_user_id VARCHAR(100) UNIQUE,
                       oauth_user_picture_url TEXT
);


-- Create categories table
CREATE TABLE categories (
            category_id SERIAL PRIMARY KEY,
            name VARCHAR(100) UNIQUE NOT NULL,
            description TEXT,
            image BYTEA,
            main BOOLEAN DEFAULT FALSE NOT NULL,
            parent_category_id INT,
            should_display BOOLEAN DEFAULT TRUE NOT NULL,
            language VARCHAR(50) NOT NULL DEFAULT 'POLISH',
            FOREIGN KEY (parent_category_id) REFERENCES categories(category_id)
);

-- Create items table
CREATE TABLE items (
                       item_id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,
                       price DECIMAL(10, 2) NOT NULL,
                       stock_quantity INT NOT NULL,
                       category_id INT NOT NULL,
                       image BYTEA,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       currency_id INT NOT NULL DEFAULT 1,
                       language VARCHAR(50) NOT NULL DEFAULT 'POLISH',
                       FOREIGN KEY (category_id) REFERENCES categories(category_id),
                       FOREIGN KEY (currency_id) REFERENCES currencies(id)
);

-- Create index on category_id in items table
CREATE INDEX ON items (category_id);

-- Create discounts table
CREATE TABLE discounts (
                           discount_id SERIAL PRIMARY KEY,
                           code VARCHAR(50) UNIQUE NOT NULL,
                           description TEXT,
                           discount_percentage DECIMAL(5, 2) NOT NULL,
                           valid_from DATE NOT NULL,
                           valid_until DATE NOT NULL,
                           language VARCHAR(50) NOT NULL DEFAULT 'POLISH'

);

-- Create discount_items table
CREATE TABLE discount_items (
                                discount_item_id SERIAL PRIMARY KEY,
                                discount_id INT NOT NULL,
                                item_id INT NOT NULL,
                                FOREIGN KEY (discount_id) REFERENCES discounts(discount_id),
                                FOREIGN KEY (item_id) REFERENCES items(item_id),
                                UNIQUE (discount_id, item_id) -- Ensure a specific item has only one unique discount
);


-- Create orders table
CREATE TABLE orders (
                        order_id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        status VARCHAR(50) DEFAULT 'pending' NOT NULL,
                        total_amount DECIMAL(10, 2) NOT NULL,
                        total_after_discount DECIMAL(10, 2) NOT NULL,
                        discount_id INT,
                        currency_id INT NOT NULL DEFAULT 1,
                        exchange_rate_id INT NOT NULL DEFAULT 1,
                        service_fee DECIMAL(10, 2) DEFAULT 0.00 NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(user_id),
                        FOREIGN KEY (discount_id) REFERENCES discounts(discount_id),
                        FOREIGN KEY (currency_id) REFERENCES currencies(id),
                        FOREIGN KEY (exchange_rate_id) REFERENCES exchange_rates(id)
);

-- Create order_items table
CREATE TABLE order_items (
                             order_item_id SERIAL PRIMARY KEY,
                             order_id INT NOT NULL,
                             item_id INT NOT NULL,
                             quantity INT NOT NULL,
                             price DECIMAL(10, 2) NOT NULL,
                             price_after_discount DECIMAL(10, 2) NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(order_id),
                             FOREIGN KEY (item_id) REFERENCES items(item_id)
);

-- Create cart table
CREATE TABLE cart (
                      cart_id SERIAL PRIMARY KEY,
                      user_id INT NOT NULL,
                      item_id INT NOT NULL,
                      quantity INT NOT NULL,
                      FOREIGN KEY (user_id) REFERENCES users(user_id),
                      FOREIGN KEY (item_id) REFERENCES items(item_id)
);

-- Create index on user_id in cart table
CREATE INDEX ON cart (user_id);

-- Create reviews table
CREATE TABLE reviews (
                         review_id SERIAL PRIMARY KEY,
                         user_id INT NOT NULL,
                         item_id INT NOT NULL,
                         description VARCHAR(1500) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users(user_id),
                         FOREIGN KEY (item_id) REFERENCES items(item_id),
                         star_rating INT NOT NULL,
                         language VARCHAR(50) NOT NULL DEFAULT 'POLISH',
                         UNIQUE (user_id, item_id) -- Ensure a user can only review an item once

);

-- Create index on item_id in reviews table
CREATE INDEX ON reviews (item_id);

-- Create ads table with additional fields and user reference
CREATE TABLE ads (
                     ad_id SERIAL PRIMARY KEY,
                     item_id INT NOT NULL,
                     user_id INT NOT NULL, -- Column to reference the user who added the ad
                     description TEXT NOT NULL,
                     start_date TIMESTAMP NOT NULL,
                     end_date TIMESTAMP NOT NULL,
                     status VARCHAR(20) DEFAULT 'active', -- Status of the ad
                     image BYTEA, -- Optional: Image for the ad
                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                     language VARCHAR(50) NOT NULL DEFAULT 'POLISH',
                     FOREIGN KEY (item_id) REFERENCES items(item_id),
                     FOREIGN KEY (user_id) REFERENCES users(user_id) -- Foreign key reference to users table
);

-- Create sales table
CREATE TABLE sales (
                       id SERIAL PRIMARY KEY,
                       item_id INT NOT NULL,
                       quantity INT NOT NULL,
                       sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       FOREIGN KEY (item_id) REFERENCES items(item_id)
);

-- Create index on item_id in sales table
CREATE INDEX ON sales (item_id);

-- Create best_sellers view (last 30 days)
CREATE VIEW best_sellers AS
SELECT item_id, SUM(quantity) as total_sales
FROM sales
WHERE sale_date >= current_date - interval '30 days'
GROUP BY item_id
ORDER BY total_sales DESC
LIMIT 10;

-- Create user item browse history table
CREATE TABLE user_item_history (
                                  id SERIAL PRIMARY KEY,
                                  user_id INT NOT NULL,
                                  item_id INT NOT NULL,
                                  last_browsed TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                  FOREIGN KEY (user_id) REFERENCES users(user_id),
                                  FOREIGN KEY (item_id) REFERENCES items(item_id)
);

-- Create index on user_id in user_item_history table
CREATE INDEX ON user_item_history (user_id);

-- Statement for getting 10 newest browsed items for a user
PREPARE get_newest_browsed_items_for_user_test AS
    WITH RankedItems AS (
        SELECT
            user_id,
            item_id,
            last_browsed,
            ROW_NUMBER() OVER (ORDER BY last_browsed DESC) AS rn
        FROM (
                 SELECT
                     user_id,
                     item_id,
                     MAX(last_browsed) AS last_browsed
                 FROM
                     user_item_history
                 WHERE
                     user_id = $1
                 GROUP BY
                     user_id, item_id
             ) AS UniqueItems
    )
    SELECT
        user_id,
        item_id,
        last_browsed
    FROM
        RankedItems
    WHERE
        rn <= 10;

CREATE TABLE currencies (
                            id SERIAL PRIMARY KEY,
                            short VARCHAR(10) NOT NULL UNIQUE,
                            symbol VARCHAR(10) NOT NULL,
                            name VARCHAR(50)
);

-- Create an index on short in currencies table
CREATE INDEX ON currencies (short);

-- Create a new exchange_rates table
CREATE TABLE exchange_rates (
                                id SERIAL PRIMARY KEY,
                                currency_id INT NOT NULL,
                                exchange_rate DECIMAL(10, 5),
                                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                FOREIGN KEY (currency_id) REFERENCES currencies(id)
);

-- Create an index on currency_id in exchange_rates table
CREATE INDEX ON exchange_rates (currency_id);

-- Create a view to get the latest exchange rates for each currency
CREATE VIEW latest_exchange_rates AS
SELECT er1.id, er1.currency_id, er1.exchange_rate, er1.last_updated
FROM exchange_rates er1
         JOIN (
    SELECT currency_id, MAX(last_updated) AS max_last_updated
    FROM exchange_rates
    GROUP BY currency_id
) er2 ON er1.currency_id = er2.currency_id AND er1.last_updated = er2.max_last_updated;

-- Create a table to store items wish-listed by users
CREATE TABLE wishlist (
                          wishlist_id SERIAL PRIMARY KEY,
                          user_id INT,
                          item_id INT,
                          FOREIGN KEY (user_id) REFERENCES users(user_id),
                          FOREIGN KEY (item_id) REFERENCES items(item_id)
);

CREATE TABLE attribute_types (
    attribute_type_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    language VARCHAR(50) NOT NULL DEFAULT 'POLISH'
);

CREATE TABLE attributes (
    attribute_id SERIAL PRIMARY KEY,
    value VARCHAR(50) NOT NULL,
    attribute_type_id INT NOT NULL,
    item_id INT NOT NULL,
    language VARCHAR(50) NOT NULL DEFAULT 'POLISH',
    FOREIGN KEY (attribute_type_id) REFERENCES attribute_types(attribute_type_id),
    FOREIGN KEY (item_id) REFERENCES items(item_id),
    UNIQUE (attribute_type_id, item_id)
);

CREATE TABLE pre_translated_texts (
                                      id SERIAL PRIMARY KEY,
                                      entity_id BIGINT NOT NULL,
                                      class_name VARCHAR(255) NOT NULL,
                                      field_name VARCHAR(255) NOT NULL,
                                      language VARCHAR(50) NOT NULL,  -- Assuming Language is stored as a string (Enum)
                                      text TEXT NOT NULL,
                                      last_update TIMESTAMP NOT NULL,
                                      UNIQUE (class_name, field_name, language, entity_id)
);