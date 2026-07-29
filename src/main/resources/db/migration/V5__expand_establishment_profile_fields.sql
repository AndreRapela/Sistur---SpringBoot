DO $$
BEGIN
    IF to_regclass('public.establishments') IS NOT NULL THEN
        ALTER TABLE establishments ALTER COLUMN photo_url TYPE VARCHAR(2000);
        ALTER TABLE establishments ALTER COLUMN instagram_url TYPE VARCHAR(1000);
        ALTER TABLE establishments ALTER COLUMN website_url TYPE VARCHAR(1000);
        ALTER TABLE establishments ALTER COLUMN opening_hours TYPE VARCHAR(1000);
        ALTER TABLE establishments ALTER COLUMN amenities TYPE VARCHAR(2000);
    END IF;
END $$;
