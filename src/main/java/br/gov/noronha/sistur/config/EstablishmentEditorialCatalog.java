package br.gov.noronha.sistur.config;

import java.util.Map;

/**
 * Verified editorial fallbacks. Volatile Google data (photos, ratings, reviews and live hours)
 * is intentionally loaded by the frontend instead of being persisted here.
 */
final class EstablishmentEditorialCatalog {

    private static final String NORONHA_RESTAURANTS =
        "https://www.noronha.pe.gov.br/gastronomia/restaurantes/";

    private static final Map<String, Details> DETAILS = Map.ofEntries(
        Map.entry("Restaurante do Vale", d(
            "https://restaurantedovale.com/",
            "https://www.pousadadovale.com/img/Cardaipio_RESTAURANTE_VALE.pdf",
            "+55 81 97341-4288",
            "Todos os dias: café 7h-10h; almoço 12h-15h; jantar 19h-22h",
            "Pratos principais de R$ 145 a R$ 210",
            "Peixe Delegado - R$ 145|Cachorro - R$ 170|Cacimba - R$ 195",
            "Almoço para uma visita mais tranquila; no jantar, reserve com antecedência.",
            "O ambiente ao ar livre fica ainda melhor em noites de tempo firme.",
            "Vegetariano, Vegano, Opções sem glúten, Cardápio infantil, Reservas",
            "https://restaurantedovale.com/"
        )),
        Map.entry("O Pico", d(
            "https://opiconoronha.com.br/", null, "+55 81 3619-1377",
            "Qua-seg: 12h-23h; terça: fechado",
            "Cerca de R$ 200 por pessoa; confirme no cardápio atual",
            "Cozinha regional contemporânea|Drinks autorais|Pratos brasileiros reinterpretados",
            "Almoço ou início da noite; aos domingos, confirme a programação musical.",
            "Boa opção para qualquer clima; prefira a área interna em dias de chuva.",
            "Reservas, Área externa, Wi-Fi, Drinks", NORONHA_RESTAURANTS
        )),
        Map.entry("Xica da Silva", d(
            null, null, "+55 81 98491-7725", "Todos os dias: 12h-24h", null,
            "Peixe mestiço|Frutos do mar|Cozinha brasileira contemporânea",
            "Funciona para almoço e jantar; reserve nos períodos de maior movimento.",
            "Opção prática também em dias nublados ou chuvosos.",
            "Almoço, Jantar, Bar, Área externa, Opções vegetarianas", NORONHA_RESTAURANTS
        )),
        Map.entry("Varanda Noronha", d(
            null, null, "+55 81 99824-4382", "Qui-ter: 12h-21h30; quarta: fechado",
            "Linguine de frutos do mar a partir de R$ 110; pratos para duas pessoas a partir de R$ 230",
            "Linguine de frutos do mar - R$ 110|Risoto de camarão - R$ 120|Paella para duas pessoas - R$ 230",
            "Almoço com mais calma ou jantar mediante reserva.",
            "Ambiente adequado para qualquer clima; confirme a disponibilidade da área externa.",
            "Reservas, Almoço, Jantar, Pratos para compartilhar", NORONHA_RESTAURANTS
        )),
        Map.entry("Mergulhão", d(
            "https://www.mergulhaonoronha.com.br/", null, "+55 81 99601-0203",
            "Seg-sáb: 12h-22h; domingo: confirme no Google",
            "Faixa alta; confirme o cardápio atual",
            "Siri cremoso na manga|Peixe na crosta de amêndoas|Peixe com purê de banana e coco",
            "Chegue antes do pôr do sol para aproveitar a vista do Porto.",
            "Tempo aberto favorece a vista; em dias de vento, confirme a área disponível.",
            "Vista para o mar, Almoço, Jantar, Reservas", NORONHA_RESTAURANTS
        )),
        Map.entry("Cacimba Bistrô", d(
            null, null, "+55 81 3619-1200", "Todos os dias: 12h-23h30",
            "Pratos principais de R$ 147 a R$ 299; opções para compartilhar",
            "Risoto de polvo - R$ 147|Espaguete de coco com lagosta - R$ 157|Bobó de lagosta para duas pessoas - R$ 299",
            "Almoço tardio ou jantar; reserve para as noites de maior movimento.",
            "A área interna funciona bem em dias de chuva.",
            "Almoço, Jantar, Reservas, Cozinha regional contemporânea", NORONHA_RESTAURANTS
        )),
        Map.entry("Benedita Cozinha Afetiva", d(
            null, null, "+55 81 99232-6820", "Todos os dias: 12h-23h",
            "Pratos de R$ 112 a R$ 198; confirme o menu sazonal",
            "Méqui Fish - R$ 112|Tuna Wellington - R$ 188|Nhoque de mandioca com camarão - R$ 194",
            "Ideal para jantar; reserve e confirme o menu sazonal do dia.",
            "Boa escolha em noite chuvosa; a cozinha trabalha com ingredientes sazonais.",
            "Menu sazonal, Forno a lenha, Brasa, Opções vegetarianas, Reservas",
            "https://restaurantguru.com.br/Benedita-Restaurant-Brazil"
        )),
        Map.entry("Mesa da Ana", d(
            null, null, null, "Atendimento somente mediante reserva; confirme o horário", null,
            "Menu degustação surpresa|Cozinha autoral|Ingredientes locais",
            "Reserve com antecedência; a experiência segue horário e menu definidos pela casa.",
            "Experiência em ambiente interno, adequada também para noites chuvosas.",
            "Menu degustação, Somente com reserva, Experiência intimista",
            "https://www.tripadvisor.com.br/Restaurant_Review-g616328-d6523386-Reviews-Mesa_da_Ana-Fernando_de_Noronha_State_of_Pernambuco.html"
        )),
        Map.entry("Márcio Sushi", d(
            null, null, "+55 81 98650-2826", "Ter-dom: 19h-23h; segunda: fechado",
            "Em geral, R$ 60 a R$ 120 por pessoa",
            "Combinados de sushi|Sashimi de peixe fresco|Temakis",
            "Prefira o início da noite para encontrar mais opções de peixe fresco.",
            "Boa opção para qualquer clima.",
            "Sushi, Peixe fresco, Jantar", "https://restaurantguru.com.br/Marcio-Sushi-Brazil"
        )),
        Map.entry("Acqua Noronha", d(
            "https://dolphinnoronha.com.br/gastronomia/", null, "+55 81 3619-1100",
            "Todos os dias: 12h-22h", null,
            "Peixe fresco|Lagosta|Massas com frutos do mar|Camarão",
            "Almoço ou jantar; confirme reserva quando a ilha estiver mais cheia.",
            "Salão coberto e boa alternativa para dias chuvosos.",
            "Frutos do mar, Cozinha mediterrânea, Almoço, Jantar", NORONHA_RESTAURANTS
        )),
        Map.entry("Cigana do Cajueiro", d(
            null, null, "+55 81 99848-9396", "Seg-sáb: 12h-15h e 19h-23h; domingo: fechado",
            "Pratos principais em torno de R$ 98 a R$ 125; confirme valores",
            "Risoto da ilha - R$ 125|Atum selado - R$ 98|Croquetas polvorosas - R$ 50",
            "Jantar tranquilo; reserve para datas especiais e fins de semana.",
            "Ambiente acolhedor para qualquer clima.",
            "Jantar, Frutos do mar, Reservas, Drinks", NORONHA_RESTAURANTS
        )),
        Map.entry("Bar do Meio", d(
            "https://www.bardomeionoronha.com/", "https://www.bardomeionoronha.com/cardapio/",
            "+55 81 99605-5344", "Todos os dias: 12h-21h; cozinha até 20h",
            "Pratos principais de R$ 140 a R$ 220; porções a partir de R$ 80",
            "Peixe na bananeira - R$ 180|Atum Noronha - R$ 195|Risoto de polvo - R$ 210",
            "Chegue antes do pôr do sol; confirme reserva e programação do dia.",
            "Tempo aberto valoriza o pôr do sol e a área externa.",
            "Pôr do sol, Área externa, Drinks, Opções vegetarianas, Reservas",
            "https://www.bardomeionoronha.com/duvidas-frequentes/"
        )),
        Map.entry("Duda Rei Bar", d(
            null, null, "+55 81 3619-1679", "Todos os dias: 10h ao pôr do sol",
            "Em geral, R$ 51 a R$ 130 por pessoa",
            "Peixe grelhado|Petiscos|Drinks",
            "Fim da tarde, quando a Praia da Conceição ganha luz de pôr do sol.",
            "Prefira tempo firme; a experiência é principalmente ao ar livre.",
            "Praia, Pôr do sol, Petiscos, Drinks", "https://restaurantguru.com.br/Bar-Duda-Rei-Brazil"
        )),
        Map.entry("Empório São Miguel", d(
            null, null, "+55 81 3619-1859",
            "Seg-sáb: 11h-23h30; domingo: 17h-23h30", null,
            "Self-service no almoço|Pizzas|Frutos do mar",
            "Útil para almoço rápido; à noite, funciona com serviço à la carte.",
            "Opção central e prática também em dias de chuva.",
            "Self-service, À la carte, Pizza, Almoço, Jantar", NORONHA_RESTAURANTS
        )),
        Map.entry("Flamboyant", d(
            null, null, "+55 81 3619-0232", "Todos os dias: 12h-23h", null,
            "Culinária brasileira|Pratos variados|Almoço",
            "Boa parada no almoço ou no começo da noite, perto da praça.",
            "Salão coberto e localização central.",
            "Almoço, Jantar, Culinária variada", NORONHA_RESTAURANTS
        )),

        Map.entry("Pousada Maravilha", d(
            "https://www.pousadamaravilha.com.br/", null, "+55 81 99836-0001",
            "Recepção: +55 81 99902-0805; confirme check-in e check-out na reserva",
            "Diárias a partir de R$ 4.264 em consulta oficial; variam por data, suíte e hóspedes",
            null,
            "Reserve com antecedência e confirme transfer, horários e políticas antes do voo.",
            "Em chuva forte, confirme transfers e condições de deslocamento para o Sueste.",
            "Piscina, Restaurante, Vista para o mar, Concierge, Transfer",
            "https://www.pousadamaravilha.com.br/fale-conosco"
        )),
        Map.entry("NANNAI Noronha", d(
            "https://www.nannai.com.br/", null, "+55 81 3552-0100",
            "Check-in a partir de 14h; check-out até 12h", null, null,
            "Confirme a disponibilidade por data; café da manhã e transfer in/out estão incluídos na diária.",
            "Consulte a recepção sobre transfers e atividades em dias de chuva forte.",
            "Café da manhã, Transfer, Restaurante, Piscina, Hospedagem premium",
            "https://www.nannai.com.br/politicas-nn"
        )),
        Map.entry("Teju-Açu Eco Pousada", d(
            null, null, "+55 81 99979-5485",
            "Restaurante: todos os dias, 12h-15h e 19h-21h30; confirme a recepção", null, null,
            "Confirme check-in, transfer e disponibilidade diretamente com a pousada.",
            "A proposta integrada à natureza pede atenção extra em períodos de chuva.",
            "Eco pousada, Restaurante, Natureza, Hospedagem de charme", NORONHA_RESTAURANTS
        )),
        Map.entry("Dolphin Hotel", d(
            "https://dolphinnoronha.com.br/", null, "+55 81 99718-0438",
            "Recepção: 7h-22h; confirme check-in e transfer na reserva", null, null,
            "Informe o voo com antecedência para alinhar o transfer de chegada.",
            "Consulte a recepção sobre mudanças de transfer em dias de chuva ou vento forte.",
            "Piscina, Restaurante, Transfer, Concierge, Café da manhã",
            "https://dolphinnoronha.com.br/"
        )),
        Map.entry("Pousada do Vale", d(
            "https://pousadadovale.com/", null, "+55 81 99613-1001",
            "Recepção 24h; gastronomia todos os dias", null, null,
            "Reserve por data e confirme as políticas da tarifa escolhida.",
            "A recepção 24h pode orientar deslocamentos quando o clima mudar.",
            "Recepção 24h, Restaurante, Spa, Piscina, Concierge",
            "https://pousadadovale.com/contato/"
        )),
        Map.entry("Pousada Morena", d(
            "https://pousadamorena.com.br/", null, "+55 81 3038-5008",
            "Recepção: +55 81 3619-1142; confirme check-in e check-out", null, null,
            "Consulte disponibilidade por data e alinhe transfer antes da chegada.",
            "Em dias de chuva, confirme atividades e deslocamentos com a concierge.",
            "Piscina, Spa, Restaurante, Concierge, Academia",
            "https://pousadamorena.com.br/"
        )),
        Map.entry("Pousada Zé Maria", d(
            "https://pousadazemaria.com.br/", null, "+55 81 98829-9749",
            "Recepção: +55 81 3619-1258; confirme check-in e check-out", null, null,
            "Reserve a hospedagem e o festival gastronômico separadamente quando necessário.",
            "Consulte a recepção sobre a programação em noites de chuva.",
            "Restaurante, Festival gastronômico, Piscina, Café da manhã",
            "https://pousadazemaria.com.br/localizacao-e-contato/"
        )),
        Map.entry("Pousada Triboju", d(
            "https://www.pousadatriboju.com.br/triboju/", null, "+55 81 99200-0464",
            "Recepção 24h; confirme check-in e transfer na reserva", null, null,
            "Informe o voo para organizar o transfer e confirme as políticas da tarifa.",
            "A recepção 24h pode reorganizar serviços quando o clima mudar.",
            "Recepção 24h, Transfer, Café da manhã, Restaurante, Piscina aquecida",
            "https://www.pousadatriboju.com.br/triboju/"
        )),
        Map.entry("Solar dos Ventos", d(
            null, null, null, null, null, null,
            "Consulte no Google a disponibilidade, os canais atuais e os horários de recepção.",
            "A região do Sueste exige atenção a deslocamentos em chuva forte.",
            "Vista para o mar, Região do Sueste",
            "https://www.ilhadenoronha.com.br/ailha/hoteis_pousadas_solar_dos_ventos.php"
        )),
        Map.entry("Maria Bonita Noronha", d(
            "https://www.mbonitanoronha.com.br/", null, "+55 81 98829-9751",
            "Check-in a partir de 14h; check-out até 12h; recepção 7h-22h", null, null,
            "Confirme disponibilidade por data e informe o horário do voo.",
            "Consulte a concierge sobre passeios e deslocamentos em dias de chuva.",
            "Café da manhã, Piscina, Concierge, Wi-Fi Starlink",
            "https://www.mbonitanoronha.com.br/"
        )),

        Map.entry("Posto de Combustível BR Noronha", d(
            null, null, null, null, null, null,
            "Abasteça antes de deslocamentos longos e confira o funcionamento no Google.",
            "Em chuva forte, dirija devagar: trechos da ilha podem ficar escorregadios.",
            "Combustível, Apoio a carros, motos e buggies",
            "https://www.google.com/maps/search/?api=1&query=posto+combustivel+Fernando+de+Noronha"
        )),
        Map.entry("Noronhão Supermercado", d(
            null, null, "+55 81 3619-1106", "Seg-sáb: 9h-20h; domingo: 10h-18h",
            null, null,
            "Compre água e itens essenciais antes de seguir para praias mais afastadas.",
            "Em alertas de chuva, antecipe compras básicas e evite deslocamentos desnecessários.",
            "Mercado, Água, Bebidas, Itens de higiene",
            "https://www.apontador.com.br/local/pe/fernando_de_noronha/supermercados/C40729280D483S4831/supermercado_noronhao.html"
        )),
        Map.entry("Supermercado Breakfast", d(
            null, null, null, null, null, null,
            "Consulte no Google o horário do dia antes de sair.",
            "Em alertas de chuva, antecipe compras básicas.",
            "Mercado, Café da manhã, Bebidas, Conveniência",
            "https://www.google.com/maps/search/?api=1&query=Supermercado+Breakfast+Fernando+de+Noronha"
        )),
        Map.entry("Mercadinho Poty", d(
            null, null, "+55 81 3619-1310",
            "Seg-sex: 5h-21h; domingo: 5h-13h e 17h-20h; sábado: confirme no Google",
            null, null,
            "Útil para compras rápidas; confirme o horário atualizado antes de sair.",
            "Em alertas de chuva, antecipe água e itens essenciais.",
            "Mercadinho, Bebidas, Itens básicos",
            "https://br.todosnegocios.com/pt/supermercado-poty_4h-81-3619-1310"
        )),
        Map.entry("Farmácia Fernando de Noronha", d(
            null, null, "+55 81 3619-0925", "Seg-sex: 8h-12h e 14h-18h", null, null,
            "Confirme estoque e atendimento por telefone antes do deslocamento.",
            "Mantenha protetor solar, hidratação e medicamentos de uso contínuo à mão.",
            "Farmácia pública, Medicamentos, Orientação básica",
            "https://cnes2.datasus.gov.br/Mod_Ambulatorial.asp?VCo_Unidade=2605457883099"
        )),
        Map.entry("Farmácia Nativa", d(
            null, null, "+55 81 3619-1428", null, null, null,
            "Consulte o horário e o estoque no Google antes de sair.",
            "Procure atendimento médico em emergências; a farmácia atende necessidades básicas.",
            "Farmácia, Higiene, Protetor solar, Medicamentos",
            "https://www.apontador.com.br/em/fernando-de-noronha-pe/farmacias-e-drogarias"
        )),
        Map.entry("Feira Orgânica de Noronha", d(
            null, null, null,
            "Evento periódico; datas e horários são divulgados pela Administração da Ilha",
            null, null,
            "Confira a próxima edição antes de incluir a feira no roteiro.",
            "Atividade ao ar livre, sujeita a mudanças por chuva forte.",
            "Produtos locais, Agricultura orgânica, Evento periódico",
            "https://www.noronha.pe.gov.br/fernando-de-noronha-realiza-feira-organica-com-produtos-cultivados-na-ilha/"
        )),
        Map.entry("Banco24Horas - Aeroporto de Noronha", d(
            "https://www.banco24horas.com.br/", null, null,
            "Conforme o funcionamento do aeroporto e a disponibilidade do terminal", null, null,
            "Não dependa de um único caixa: leve cartão e uma pequena reserva em espécie.",
            "O serviço pode ficar indisponível; planeje saques com antecedência.",
            "Caixa eletrônico, Saque, Aeroporto",
            "https://pousadadovale.com/dicas"
        )),
        Map.entry("Aeroporto de Fernando de Noronha", d(
            null, null, null, "Conforme a malha aérea do dia", null, null,
            "Chegue com a antecedência indicada pela companhia aérea e acompanhe o voo.",
            "Vento e chuva podem alterar pousos e decolagens; acompanhe a companhia aérea.",
            "Voos, Transfers, Locação de veículos, Caixa eletrônico",
            "https://www.noronha.pe.gov.br/quer-conhecer-fernando-de-noronha/"
        ))
    );

    private static final Map<String, String> LEGACY_NAMES = Map.of(
        "Varanda de Noronha", "Varanda Noronha",
        "Farmácia Noronha", "Farmácia Fernando de Noronha",
        "Farmácia Ilha Farma", "Farmácia Nativa",
        "Feirinha da Vila dos Remédios", "Feira Orgânica de Noronha",
        "Banco 24 Horas Noronha", "Banco24Horas - Aeroporto de Noronha"
    );

    private EstablishmentEditorialCatalog() {
    }

    static Map<String, Details> details() {
        return DETAILS;
    }

    static Map<String, String> legacyNames() {
        return LEGACY_NAMES;
    }

    static String canonicalName(String name) {
        return LEGACY_NAMES.getOrDefault(name, name);
    }

    private static Details d(
        String websiteUrl,
        String menuUrl,
        String contactNumber,
        String openingHours,
        String priceRange,
        String popularDishes,
        String bestVisitTime,
        String weatherAdvice,
        String amenities,
        String sourceUrl
    ) {
        return new Details(
            websiteUrl,
            menuUrl,
            contactNumber,
            openingHours,
            priceRange,
            popularDishes,
            bestVisitTime,
            weatherAdvice,
            amenities,
            sourceUrl
        );
    }

    record Details(
        String websiteUrl,
        String menuUrl,
        String contactNumber,
        String openingHours,
        String priceRange,
        String popularDishes,
        String bestVisitTime,
        String weatherAdvice,
        String amenities,
        String sourceUrl
    ) {
    }
}
