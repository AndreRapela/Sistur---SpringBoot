ALTER TABLE IF EXISTS establishments
    DROP CONSTRAINT IF EXISTS establishments_type_check;

ALTER TABLE IF EXISTS establishments
    ADD CONSTRAINT establishments_type_check
    CHECK (type IN (
        'RESTAURANT',
        'BAR',
        'HOTEL',
        'POUSADA',
        'RESORT',
        'CONVENIENCE',
        'GAS_STATION',
        'MARKET',
        'FAIR',
        'PHARMACY'
    ));
