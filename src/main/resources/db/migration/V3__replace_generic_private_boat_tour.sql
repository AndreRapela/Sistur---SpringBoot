DO $$
BEGIN
    IF to_regclass('tours') IS NOT NULL THEN
        UPDATE tours
        SET name = 'Passeio de Lancha Noronha',
            description = 'Passeio privativo pela APA de Fernando de Noronha em lancha de 36 pés, com gastronomia a bordo, parada para mergulho e navegação até a Cacimba do Padre.',
            category = 'Barco',
            photo_url = '/assets/places/bubba-noronha-hero.png',
            photo_credit = 'Passeio de Lancha Noronha',
            price = NULL,
            rating = 5.00,
            review_count = 6,
            review_source = 'Tripadvisor',
            review_url = 'https://www.tripadvisor.com.br/Attraction_Review-g616328-d33258023-Reviews-Lancha_First-Fernando_de_Noronha_State_of_Pernambuco.html',
            partnership = 'Passeio de Lancha Noronha / Lancha First',
            contact_number = '558191338538',
            duration = 'Cerca de 4 horas',
            schedule = 'Manhã: 9h às 13h | Entardecer: 14h30 ao pôr do sol',
            meeting_point = 'Píer do Porto de Santo Antônio, com chegada 15 minutos antes',
            itinerary = 'Porto de Santo Antônio|Biboca|Rugido do Leão|Praia do Cachorro|Praia do Meio|Morro de Fora|Praia da Conceição|Praia do Boldró|Praia do Americano|Praia do Bode|Quixabinha|Cacimba do Padre',
            included_items = 'Plana sub|Som Bluetooth|Wi-Fi via satélite|Parada para mergulho livre|Petiscos|Tapete flutuante|Cooler com água e gelo',
            excluded_items = 'Bebidas|Material de mergulho|Transfer até o porto',
            requirements = 'Levar máscara, snorkel e colete para o plana sub|Colete obrigatório para menores de 13 anos|Não levar copos ou taças de vidro|Passeio sujeito às condições do mar',
            booking_url = 'https://passeiodelanchanoronha.com.br/',
            google_maps_url = 'https://www.google.com/maps/search/?api=1&query=Passeio%20de%20Lancha%20Noronha%20Porto%20de%20Santo%20Antonio',
            source_url = 'https://passeiodelanchanoronha.com.br/',
            data_verified_at = DATE '2026-07-21',
            latitude = -3.8332,
            longitude = -32.4042
        WHERE LOWER(name) = LOWER('Lancha privativa');
    END IF;
END $$;
