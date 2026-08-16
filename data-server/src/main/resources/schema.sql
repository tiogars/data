ALTER TABLE IF EXISTS gtin
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE IF EXISTS gtin
    ADD COLUMN IF NOT EXISTS owner_username VARCHAR(255);

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'gtin') THEN
        UPDATE gtin SET updated_at = TIMESTAMPTZ '2000-01-01T00:00:00Z' WHERE updated_at IS NULL;
        UPDATE gtin SET owner_username = 'system' WHERE owner_username IS NULL OR owner_username = '';
    END IF;
END $$;

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

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'car') THEN
        UPDATE car SET updated_at = TIMESTAMPTZ '2000-01-01T00:00:00Z' WHERE updated_at IS NULL;
        UPDATE car SET owner_username = 'system' WHERE owner_username IS NULL OR owner_username = '';
    END IF;
END $$;

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

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'car_mileage') THEN
        UPDATE car_mileage SET updated_at = TIMESTAMPTZ '2000-01-01T00:00:00Z' WHERE updated_at IS NULL;
        UPDATE car_mileage SET owner_username = 'system' WHERE owner_username IS NULL OR owner_username = '';
    END IF;
END $$;

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

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'android_app') THEN
        UPDATE android_app SET updated_at = TIMESTAMPTZ '2000-01-01T00:00:00Z' WHERE updated_at IS NULL;
        UPDATE android_app SET owner_username = 'system' WHERE owner_username IS NULL OR owner_username = '';
    END IF;
END $$;

ALTER TABLE IF EXISTS android_app
    ALTER COLUMN updated_at SET NOT NULL;
ALTER TABLE IF EXISTS android_app
    ALTER COLUMN owner_username SET DEFAULT 'system';
ALTER TABLE IF EXISTS android_app
    ALTER COLUMN owner_username SET NOT NULL;

ALTER TABLE IF EXISTS section
    ADD COLUMN IF NOT EXISTS display_order INTEGER;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'section') THEN
        UPDATE section SET display_order = 0 WHERE display_order IS NULL;
    END IF;
END $$;

ALTER TABLE IF EXISTS section
    ALTER COLUMN display_order SET DEFAULT 0;
ALTER TABLE IF EXISTS section
    ALTER COLUMN display_order SET NOT NULL;

CREATE TABLE IF NOT EXISTS section_document (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    storage_path VARCHAR(1000) NOT NULL
);

INSERT INTO section_document (id, name, storage_path)
SELECT UUID '00000000-0000-0000-0000-000000000001', 'Document par défaut', 'default'
WHERE NOT EXISTS (SELECT 1 FROM section_document WHERE id = UUID '00000000-0000-0000-0000-000000000001');

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'section_document'
          AND column_name = 'id'
          AND data_type <> 'uuid'
    ) THEN
        ALTER TABLE section_document
            ALTER COLUMN id TYPE UUID
            USING CASE
                WHEN id ~* '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN id::UUID
                ELSE UUID '00000000-0000-0000-0000-000000000001'
            END;
    END IF;
END $$;

ALTER TABLE IF EXISTS section
    ADD COLUMN IF NOT EXISTS document_id UUID;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'section'
          AND column_name = 'document_id'
          AND data_type <> 'uuid'
    ) THEN
        ALTER TABLE section
            ALTER COLUMN document_id TYPE UUID
            USING CASE
                WHEN document_id IS NULL OR document_id = '' THEN UUID '00000000-0000-0000-0000-000000000001'
                WHEN document_id ~* '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN document_id::UUID
                ELSE UUID '00000000-0000-0000-0000-000000000001'
            END;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'section') THEN
        UPDATE section SET document_id = UUID '00000000-0000-0000-0000-000000000001' WHERE document_id IS NULL;
    END IF;
END $$;

ALTER TABLE IF EXISTS section
    ALTER COLUMN document_id SET NOT NULL;

ALTER TABLE IF EXISTS section
    DROP CONSTRAINT IF EXISTS fk_section_document_id;

ALTER TABLE IF EXISTS section
    ADD CONSTRAINT fk_section_document_id
    FOREIGN KEY (document_id)
    REFERENCES section_document(id);

CREATE TABLE IF NOT EXISTS section_docs_setting (
    id TEXT PRIMARY KEY,
    section_id TEXT NOT NULL UNIQUE,
    storage_path VARCHAR(1000) NOT NULL
);

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
