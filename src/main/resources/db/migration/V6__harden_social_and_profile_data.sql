DO $$
BEGIN
    IF to_regclass('public.itinerary_items') IS NOT NULL THEN
        ALTER TABLE itinerary_items
            ALTER COLUMN reference_id TYPE VARCHAR(100) USING reference_id::text,
            ALTER COLUMN type TYPE VARCHAR(40),
            ALTER COLUMN name TYPE VARCHAR(255),
            ALTER COLUMN image TYPE VARCHAR(2000),
            ALTER COLUMN location TYPE VARCHAR(255);

        ALTER TABLE itinerary_items
            ADD COLUMN IF NOT EXISTS category VARCHAR(100),
            ADD COLUMN IF NOT EXISTS latitude NUMERIC(10, 7),
            ADD COLUMN IF NOT EXISTS longitude NUMERIC(10, 7),
            ADD COLUMN IF NOT EXISTS scheduled_time VARCHAR(20);
    END IF;

    IF to_regclass('public.users') IS NOT NULL THEN
        ALTER TABLE users ALTER COLUMN photo_url TYPE TEXT;
    END IF;

    IF to_regclass('public.establishment_reviews') IS NOT NULL THEN
        DELETE FROM establishment_reviews older
        USING establishment_reviews newer
        WHERE older.establishment_id = newer.establishment_id
          AND older.user_id = newer.user_id
          AND older.id < newer.id;

        CREATE UNIQUE INDEX IF NOT EXISTS uk_establishment_reviews_establishment_user
            ON establishment_reviews (establishment_id, user_id);
    END IF;

    IF to_regclass('public.itinerary_likes') IS NOT NULL THEN
        DELETE FROM itinerary_likes older
        USING itinerary_likes newer
        WHERE older.itinerary_id = newer.itinerary_id
          AND older.user_id = newer.user_id
          AND older.id < newer.id;

        CREATE UNIQUE INDEX IF NOT EXISTS uk_itinerary_likes_itinerary_user
            ON itinerary_likes (itinerary_id, user_id);
    END IF;
END $$;
