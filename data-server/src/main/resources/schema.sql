ALTER TABLE IF EXISTS gtin
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE IF EXISTS gtin
    ADD COLUMN IF NOT EXISTS owner_username VARCHAR(255);

UPDATE gtin
SET updated_at = TIMESTAMPTZ '2000-01-01T00:00:00Z'
WHERE updated_at IS NULL;
UPDATE gtin
SET owner_username = 'system'
WHERE owner_username IS NULL OR owner_username = '';

ALTER TABLE IF EXISTS gtin
    ALTER COLUMN updated_at SET NOT NULL;
ALTER TABLE IF EXISTS gtin
    ALTER COLUMN owner_username SET DEFAULT 'system';
ALTER TABLE IF EXISTS gtin
    ALTER COLUMN owner_username SET NOT NULL;

ALTER TABLE IF EXISTS car
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE IF EXISTS car
    ADD COLUMN IF NOT EXISTS owner_username VARCHAR(255);

UPDATE car
SET updated_at = TIMESTAMPTZ '2000-01-01T00:00:00Z'
WHERE updated_at IS NULL;
UPDATE car
SET owner_username = 'system'
WHERE owner_username IS NULL OR owner_username = '';

ALTER TABLE IF EXISTS car
    ALTER COLUMN updated_at SET NOT NULL;
ALTER TABLE IF EXISTS car
    ALTER COLUMN owner_username SET DEFAULT 'system';
ALTER TABLE IF EXISTS car
    ALTER COLUMN owner_username SET NOT NULL;

ALTER TABLE IF EXISTS car_mileage
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE IF EXISTS car_mileage
    ADD COLUMN IF NOT EXISTS owner_username VARCHAR(255);

UPDATE car_mileage
SET updated_at = TIMESTAMPTZ '2000-01-01T00:00:00Z'
WHERE updated_at IS NULL;
UPDATE car_mileage
SET owner_username = 'system'
WHERE owner_username IS NULL OR owner_username = '';

ALTER TABLE IF EXISTS car_mileage
    ALTER COLUMN updated_at SET NOT NULL;
ALTER TABLE IF EXISTS car_mileage
    ALTER COLUMN owner_username SET DEFAULT 'system';
ALTER TABLE IF EXISTS car_mileage
    ALTER COLUMN owner_username SET NOT NULL;

ALTER TABLE IF EXISTS android_app
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE IF EXISTS android_app
    ADD COLUMN IF NOT EXISTS owner_username VARCHAR(255);

UPDATE android_app
SET updated_at = TIMESTAMPTZ '2000-01-01T00:00:00Z'
WHERE updated_at IS NULL;
UPDATE android_app
SET owner_username = 'system'
WHERE owner_username IS NULL OR owner_username = '';

ALTER TABLE IF EXISTS android_app
    ALTER COLUMN updated_at SET NOT NULL;
ALTER TABLE IF EXISTS android_app
    ALTER COLUMN owner_username SET DEFAULT 'system';
ALTER TABLE IF EXISTS android_app
    ALTER COLUMN owner_username SET NOT NULL;

CREATE TABLE IF NOT EXISTS user_account (
    id TEXT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS sync_deletion_event (
    id TEXT PRIMARY KEY,
    domain TEXT NOT NULL,
    resource_id TEXT NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE NOT NULL
);
