ALTER TABLE IF EXISTS tours
    ADD COLUMN IF NOT EXISTS photo_credit VARCHAR(255),
    ADD COLUMN IF NOT EXISTS rating NUMERIC(3, 2),
    ADD COLUMN IF NOT EXISTS review_count INTEGER,
    ADD COLUMN IF NOT EXISTS review_source VARCHAR(255),
    ADD COLUMN IF NOT EXISTS review_url VARCHAR(2048),
    ADD COLUMN IF NOT EXISTS duration VARCHAR(255),
    ADD COLUMN IF NOT EXISTS schedule VARCHAR(255),
    ADD COLUMN IF NOT EXISTS meeting_point VARCHAR(255),
    ADD COLUMN IF NOT EXISTS itinerary VARCHAR(3000),
    ADD COLUMN IF NOT EXISTS included_items VARCHAR(2000),
    ADD COLUMN IF NOT EXISTS excluded_items VARCHAR(2000),
    ADD COLUMN IF NOT EXISTS requirements VARCHAR(2000),
    ADD COLUMN IF NOT EXISTS booking_url VARCHAR(2048),
    ADD COLUMN IF NOT EXISTS google_maps_url VARCHAR(2048),
    ADD COLUMN IF NOT EXISTS source_url VARCHAR(2048),
    ADD COLUMN IF NOT EXISTS data_verified_at DATE;

DO $$
BEGIN
    IF to_regclass('tours') IS NOT NULL THEN
        UPDATE tours
        SET name = 'Ilha Tour - Alga Noronha',
            description = 'Passeio de dia inteiro em veículo 4x4 com guia credenciado, praias, mirantes, parada para mergulho livre e encerramento após o pôr do sol.',
            category = 'Ilha Tour',
            photo_url = '/assets/tours/alga-ilha-tour.jpg',
            photo_credit = 'Alga Noronha',
            price = 249.00,
            rating = 5.00,
            review_count = 165,
            review_source = 'Tripadvisor',
            review_url = 'https://www.tripadvisor.com/AttractionProductReview-g616328-d26877667-CITY_TOUR_Complete_Island_Tour_in_Fernando_de_Noronha-Fernando_de_Noronha_State_of.html',
            partnership = 'Alga Tour in Noronha',
            contact_number = '5581992548000',
            duration = 'Aproximadamente 8 horas',
            schedule = '08h30 às 18h30, com término após o pôr do sol',
            meeting_point = 'Transfer de ida e volta na hospedagem',
            itinerary = 'Mirante e Praia do Sancho|Praia do Sueste|Mirante do Leão|Capela de São Pedro|Buraco da Raquel|Praia do Porto|Cacimba do Padre|Baía dos Porcos|Mirante do Boldró',
            included_items = 'Guia credenciado|Veículo 4x4|Transfer de ida e volta na hospedagem|Parada para mergulho livre, conforme condições do mar',
            excluded_items = 'Ingresso do Parque Nacional Marinho|Alimentação e bebidas',
            requirements = 'Ingresso do Parque Nacional Marinho obrigatório|Levar água, proteção solar, chapéu e calçado seguro|Roteiro sujeito às condições climáticas, do mar e de acesso',
            booking_url = 'https://www.alganoronha.com.br/service-page/ilha-tour-em-fernando-de-noronha',
            google_maps_url = 'https://www.google.com/maps/search/?api=1&query=Alga%20Noronha%20Fernando%20de%20Noronha',
            source_url = 'https://www.alganoronha.com.br/service-page/ilha-tour-em-fernando-de-noronha',
            data_verified_at = DATE '2026-07-21',
            latitude = -3.8409,
            longitude = -32.4108
        WHERE LOWER(name) = LOWER('Ilha Tour 360');
    END IF;
END $$;
