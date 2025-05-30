-- Add image_url column to tournaments table
ALTER TABLE tournaments ADD COLUMN image_url VARCHAR(500);

-- Add index for better performance
CREATE INDEX idx_tournaments_image_url ON tournaments(image_url);
