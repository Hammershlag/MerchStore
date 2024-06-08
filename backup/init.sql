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
                       birth_date DATE
);


-- Create categories table
CREATE TABLE categories (
                            category_id SERIAL PRIMARY KEY,
                            name VARCHAR(100) UNIQUE NOT NULL,
                            description TEXT
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
                       FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- Create discounts table
CREATE TABLE discounts (
                           discount_id SERIAL PRIMARY KEY,
                           code VARCHAR(50) UNIQUE NOT NULL,
                           description TEXT,
                           discount_percentage DECIMAL(5, 2) NOT NULL,
                           valid_from DATE NOT NULL,
                           valid_until DATE NOT NULL
);

-- Create orders table
CREATE TABLE orders (
                        order_id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        status VARCHAR(50) DEFAULT 'pending' NOT NULL,
                        total_amount DECIMAL(10, 2) NOT NULL,
                        discount_id INT,
                        FOREIGN KEY (user_id) REFERENCES users(user_id),
                        FOREIGN KEY (discount_id) REFERENCES discounts(discount_id)
);

-- Create order_items table
CREATE TABLE order_items (
                             order_item_id SERIAL PRIMARY KEY,
                             order_id INT NOT NULL,
                             item_id INT NOT NULL,
                             quantity INT NOT NULL,
                             price DECIMAL(10, 2) NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(order_id),
                             FOREIGN KEY (item_id) REFERENCES items(item_id)
);

-- Create invoices table
CREATE TABLE invoices (
                          invoice_id SERIAL PRIMARY KEY,
                          order_id INT NOT NULL,
                          invoice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          amount DECIMAL(10, 2) NOT NULL,
                          status VARCHAR(50) DEFAULT 'unpaid' NOT NULL,
                          FOREIGN KEY (order_id) REFERENCES orders(order_id)
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