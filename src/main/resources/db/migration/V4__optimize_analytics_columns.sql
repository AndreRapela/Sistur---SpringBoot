DO $$
BEGIN
    IF to_regclass('public.access_logs') IS NOT NULL THEN
        ALTER TABLE access_logs ALTER COLUMN target_type TYPE VARCHAR(64);
        ALTER TABLE access_logs ALTER COLUMN action_type TYPE VARCHAR(64);
        ALTER TABLE access_logs ALTER COLUMN target_label TYPE VARCHAR(255);
        ALTER TABLE access_logs ALTER COLUMN page_path TYPE VARCHAR(512);
        ALTER TABLE access_logs ALTER COLUMN referrer TYPE VARCHAR(1024);
        ALTER TABLE access_logs ALTER COLUMN ip_address TYPE VARCHAR(64);

        CREATE INDEX IF NOT EXISTS idx_access_logs_timestamp ON access_logs (timestamp);
        CREATE INDEX IF NOT EXISTS idx_access_logs_action_timestamp ON access_logs (action_type, timestamp);
        CREATE INDEX IF NOT EXISTS idx_access_logs_target_action ON access_logs (target_type, target_id, action_type);
        CREATE INDEX IF NOT EXISTS idx_access_logs_user_timestamp ON access_logs (user_id, timestamp);
    END IF;
END $$;
