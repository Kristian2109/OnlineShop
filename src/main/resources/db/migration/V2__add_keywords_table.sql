-- Create keywords table
CREATE TABLE keywords (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Create join table for product_keywords
CREATE TABLE product_keywords (
    product_id BIGINT NOT NULL,
    keyword_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, keyword_id),
    CONSTRAINT fk_product_keywords_product_id FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_product_keywords_keyword_id FOREIGN KEY (keyword_id) REFERENCES keywords (id) ON DELETE CASCADE
);

-- Migrate existing keyword data
-- First, we need to create a temporary function to split string values
DELIMITER //
CREATE FUNCTION split_string(str TEXT, delim VARCHAR(10), pos INT) RETURNS VARCHAR(255)
BEGIN
    RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(str, delim, pos), 
               LENGTH(SUBSTRING_INDEX(str, delim, pos-1)) + 1),
               delim, '');
END //
DELIMITER ;

-- Insert all unique keywords into the keywords table
INSERT INTO keywords (name)
WITH RECURSIVE split_keywords AS (
    -- Use recursive CTE to split comma-separated values
    SELECT 
        p.id AS product_id,
        split_string(CONCAT(p.keywords, ','), ',', 1) AS keyword,
        1 AS pos,
        CONCAT(p.keywords, ',') AS rest
    FROM 
        products p
    WHERE 
        p.keywords IS NOT NULL AND LENGTH(TRIM(p.keywords)) > 0
    
    UNION ALL
    
    SELECT 
        product_id,
        split_string(rest, ',', 1) AS keyword,
        pos + 1,
        SUBSTRING(rest, LOCATE(',', rest) + 1)
    FROM 
        split_keywords
    WHERE 
        LOCATE(',', rest) > 0 AND LENGTH(TRIM(split_string(rest, ',', 1))) > 0
)
SELECT DISTINCT TRIM(keyword) AS name
FROM split_keywords
WHERE LENGTH(TRIM(keyword)) > 0
ORDER BY keyword;

-- Insert product_keyword relationships
INSERT INTO product_keywords (product_id, keyword_id)
WITH RECURSIVE split_keywords AS (
    -- Same split logic as above
    SELECT 
        p.id AS product_id,
        split_string(CONCAT(p.keywords, ','), ',', 1) AS keyword,
        1 AS pos,
        CONCAT(p.keywords, ',') AS rest
    FROM 
        products p
    WHERE 
        p.keywords IS NOT NULL AND LENGTH(TRIM(p.keywords)) > 0
    
    UNION ALL
    
    SELECT 
        product_id,
        split_string(rest, ',', 1) AS keyword,
        pos + 1,
        SUBSTRING(rest, LOCATE(',', rest) + 1)
    FROM 
        split_keywords
    WHERE 
        LOCATE(',', rest) > 0 AND LENGTH(TRIM(split_string(rest, ',', 1))) > 0
)
SELECT 
    sk.product_id,
    k.id AS keyword_id
FROM 
    split_keywords sk
JOIN 
    keywords k ON TRIM(sk.keyword) = k.name
WHERE 
    LENGTH(TRIM(sk.keyword)) > 0;

-- Drop the temporary function
DROP FUNCTION split_string;

-- Remove the old keywords column
ALTER TABLE products DROP COLUMN keywords; 