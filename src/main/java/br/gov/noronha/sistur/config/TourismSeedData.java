package br.gov.noronha.sistur.config;

import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import br.gov.noronha.sistur.modules.tourism.model.Event;
import br.gov.noronha.sistur.modules.tourism.model.Tour;
import br.gov.noronha.sistur.modules.tourism.model.TouristPoint;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentRepository;
import br.gov.noronha.sistur.modules.tourism.repository.EventRepository;
import br.gov.noronha.sistur.modules.tourism.repository.TourRepository;
import br.gov.noronha.sistur.modules.tourism.repository.TouristPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "sistur.seed.enabled", havingValue = "true", matchIfMissing = true)
public class TourismSeedData implements ApplicationRunner {

    private static final String BEACH_PHOTO = null;
    private static final String FOOD_PHOTO = null;
    private static final String HOTEL_PHOTO = null;
    private static final String TOUR_PHOTO = null;
    private static final String SERVICE_PHOTO = null;
    private static final LocalDate EDITORIAL_VERIFICATION_DATE = LocalDate.of(2026, 7, 21);
    private static final Map<String, EstablishmentEditorialSeed> ESTABLISHMENT_DETAILS = Map.ofEntries(
        Map.entry("Restaurante do Vale", new EstablishmentEditorialSeed(
            "https://restaurantedovale.com/",
            "https://www.pousadadovale.com/img/Cardaipio_RESTAURANTE_VALE.pdf",
            "+55 81 97341-4288",
            "Café 7h-10h; almoço 12h-15h; jantar 19h-22h",
            "Pratos principais de R$ 145 a R$ 210",
            "Peixe Delegado - R$ 145|Cachorro - R$ 170|Cacimba - R$ 195",
            "Almoço para uma visita mais tranquila; no jantar, reserve com antecedência.",
            "Ambiente ao ar livre: noites de tempo firme valorizam a experiência.",
            "Vegetariano, Vegano, Opções sem glúten, Cardápio infantil, Reservas",
            "https://restaurantedovale.com/"
        )),
        Map.entry("O Pico", new EstablishmentEditorialSeed(
            "https://opiconoronha.com.br/",
            null,
            "+55 81 3619-1377",
            "Seg, ter e qui-dom: 12h-22h; quarta: fechado",
            "Cerca de R$ 200 por pessoa; confirme no cardápio",
            "Cozinha regional contemporânea|Drinks autorais|Pratos brasileiros reinterpretados",
            "Almoço ou início da noite; aos domingos, confirme a programação musical.",
            "Boa opção para qualquer clima; prefira a área interna em dias de chuva.",
            "Reservas, Área externa, Wi-Fi, Drinks",
            "https://restaurantguru.com.br/O-Pico-Brazil-2"
        )),
        Map.entry("Xica da Silva", new EstablishmentEditorialSeed(
            null,
            null,
            "+55 81 3619-0437",
            "Todos os dias: 12h-23h",
            null,
            "Peixe mestiço|Frutos do mar|Cozinha brasileira contemporânea",
            "Funciona para almoço e jantar; confirme reserva nos períodos de maior movimento.",
            "Opção prática também em dias nublados ou chuvosos.",
            "Almoço, Jantar, Bar, Área externa, Opções vegetarianas",
            "https://www.noronha.pe.gov.br/gastronomia/restaurantes/"
        )),
        Map.entry("Varanda Noronha", new EstablishmentEditorialSeed(
            null,
            null,
            "+55 81 99824-4382",
            "Qui-ter: 12h-21h30; quarta: fechado",
            null,
            "Culinária regional contemporânea|Peixes e frutos do mar|Pratos para compartilhar",
            "Almoço com mais calma ou jantar mediante reserva.",
            "Ambiente adequado para qualquer clima; confirme a disponibilidade da área externa.",
            "Reservas, Almoço, Jantar, Pratos para compartilhar",
            "https://www.noronha.pe.gov.br/gastronomia/restaurantes/"
        )),
        Map.entry("Mergulhão", new EstablishmentEditorialSeed(
            null,
            null,
            null,
            null,
            "Faixa alta; confirme o cardápio atual",
            "Siri cremoso na manga|Peixe na crosta de amêndoas|Peixe com purê de banana e coco",
            "Chegue antes do pôr do sol para aproveitar a vista do Porto.",
            "Tempo aberto favorece a vista; em dias de vento, confirme a mesa e a área disponível.",
            "Vista para o mar, Almoço, Jantar, Reservas",
            "https://www.tripadvisor.com.br/Restaurant_Review-g616328-d2002558-Reviews-Mergulhao-Fernando_de_Noronha_State_of_Pernambuco.html"
        )),
        Map.entry("Benedita Cozinha Afetiva", new EstablishmentEditorialSeed(
            null,
            null,
            null,
            null,
            "Referência publicada: pratos de R$ 112 a R$ 198; confirme o menu sazonal",
            "Méqui Fish - R$ 112|Peixe com banana - R$ 194|Tuna Wellington - R$ 188|Nhoque de mandioca com camarão - R$ 194",
            "Ideal para jantar; reserve e confirme o menu sazonal do dia.",
            "Boa escolha em noite chuvosa; a cozinha trabalha com ingredientes sazonais.",
            "Menu sazonal, Forno a lenha, Brasa, Opções vegetarianas, Reservas",
            "https://www.cnnbrasil.com.br/viagemegastronomia/gastronomia/dario-costa-e-seu-benedita-cozinha-transformam-o-cenario-gastronomico-de-noronha/"
        )),
        Map.entry("Márcio Sushi", new EstablishmentEditorialSeed(
            null,
            null,
            null,
            null,
            "Em geral, R$ 60 a R$ 120 por pessoa",
            "Combinados de sushi|Sashimi de peixe fresco|Temakis",
            "Prefira o início da noite para encontrar mais opções de peixe fresco.",
            "Boa opção para qualquer clima.",
            "Sushi, Peixe fresco, Jantar",
            "https://restaurantguru.com.br/Marcio-Sushi-Brazil"
        )),
        Map.entry("Cigana do Cajueiro", new EstablishmentEditorialSeed(
            null,
            null,
            null,
            null,
            "Pratos principais em torno de R$ 98 a R$ 125; confirme valores",
            "Risoto da ilha - R$ 125|Atum selado - R$ 98|Croquetas polvorosas - R$ 50",
            "Jantar tranquilo; reserve para datas especiais e fins de semana.",
            "Ambiente acolhedor para qualquer clima.",
            "Jantar, Frutos do mar, Reservas, Drinks",
            "https://www.tripadvisor.com.br/Restaurant_Review-g616328-d23391455-Reviews-Restaurante_Cigana_Do_Cajueiro_Noronha-Fernando_de_Noronha_State_of_Pernambuco.html"
        )),
        Map.entry("Bar do Meio", new EstablishmentEditorialSeed(
            "https://www.bardomeionoronha.com/",
            "https://www.bardomeionoronha.com/cardapio/",
            null,
            null,
            "Pratos principais de R$ 140 a R$ 220; porções a partir de R$ 80",
            "Peixe na bananeira - R$ 180|Atum Noronha - R$ 195|Risoto de polvo - R$ 210|Ceviche capim-santo - R$ 140",
            "Chegue antes do pôr do sol; confirme reserva, acesso e programação do dia.",
            "Tempo aberto valoriza o pôr do sol e a área externa.",
            "Pôr do sol, Área externa, Drinks, Opções vegetarianas, Reservas",
            "https://www.bardomeionoronha.com/cardapio/"
        ))
    );

    private final EstablishmentRepository establishmentRepository;
    private final TouristPointRepository touristPointRepository;
    private final TourRepository tourRepository;
    private final EventRepository eventRepository;

    @Override
    public void run(ApplicationArguments args) {
        seedTouristPoints();
        seedEstablishments();
        seedTours();
        seedEvents();
    }

    private void seedTouristPoints() {
        List<PointSeed> points = List.of(
            new PointSeed("Baía do Sancho", "Praia símbolo de Noronha, com falésias, mirantes e mar transparente para banho e snorkel.", "Praia", "Parque Nacional Marinho", "Parque Nacional", true, false, "Manhã e maré baixa", "-3.85470", "-32.44060"),
            new PointSeed("Baía dos Porcos", "Enseada pequena, rochosa e muito fotogênica, com vista direta para o Morro Dois Irmãos.", "Praia", "Entre Sancho e Cacimba do Padre", "Parque Nacional", true, false, "Maré baixa", "-3.85330", "-32.43580"),
            new PointSeed("Cacimba do Padre", "Praia ampla, conhecida pelo surf e pelo visual do Morro Dois Irmãos.", "Praia", "Costa oeste", "Livre acesso", false, false, "Manhã ou pôr do sol", "-3.85080", "-32.43170"),
            new PointSeed("Praia do Bode", "Praia de acesso simples e clima mais tranquilo, boa para caminhar e ver o pôr do sol.", "Praia", "Costa oeste", "Livre acesso", false, false, "Fim da tarde", "-3.84930", "-32.42660"),
            new PointSeed("Praia do Americano", "Faixa de areia reservada entre Boldró e Bode, boa para quem procura menos movimento.", "Praia", "Costa oeste", "Livre acesso", false, false, "Manhã", "-3.84840", "-32.42410"),
            new PointSeed("Praia do Boldró", "Praia de ondas e piscinas na maré baixa, próxima a um dos mirantes mais procurados da ilha.", "Praia", "Boldró", "Livre acesso", false, false, "Maré baixa", "-3.84740", "-32.42170"),
            new PointSeed("Praia da Conceição", "Praia acessível, com vista para o Morro do Pico e um dos melhores fins de tarde da ilha.", "Praia", "Vila dos Remédios", "Livre acesso", false, false, "Pôr do sol", "-3.84220", "-32.41730"),
            new PointSeed("Praia do Meio", "Praia central entre Conceição e Cachorro, boa para banho quando o mar está calmo.", "Praia", "Centro histórico", "Livre acesso", false, false, "Manhã", "-3.84090", "-32.41530"),
            new PointSeed("Praia do Cachorro", "Praia urbana, perto da Vila dos Remédios, com acesso fácil e movimento no fim do dia.", "Praia", "Vila dos Remédios", "Livre acesso", false, false, "Fim da tarde", "-3.84000", "-32.41380"),
            new PointSeed("Praia da Biboca", "Trecho rochoso aos pés da Fortaleza dos Remédios, bom para fotos e leitura histórica da costa.", "Praia", "Centro histórico", "Livre acesso", false, false, "Manhã", "-3.83920", "-32.41220"),
            new PointSeed("Praia do Porto de Santo Antônio", "Área de chegada dos barcos, com naufrágio próximo e águas geralmente boas para snorkel.", "Praia", "Porto", "Livre acesso", false, false, "Manhã", "-3.83340", "-32.40460"),
            new PointSeed("Praia da Caieira", "Praia de natureza mais selvagem, próxima ao Buraco da Raquel e ao Museu do Tubarão.", "Praia", "Região do Porto", "Livre acesso", false, false, "Manhã", "-3.83210", "-32.39910"),
            new PointSeed("Praia do Atalaia", "Piscina natural sensível, com visita controlada e agendamento pelo Parque Nacional.", "Praia", "Atalaia", "Parque Nacional", true, true, "Maré baixa", "-3.84380", "-32.39580"),
            new PointSeed("Praia do Sueste", "Baía protegida, procurada para snorkel e observação de tartarugas e vida marinha.", "Praia", "Litoral sudeste", "Parque Nacional", true, false, "Maré baixa", "-3.86500", "-32.42250"),
            new PointSeed("Praia do Leão", "Praia extensa e preservada, com forte presença de tartarugas e visual selvagem.", "Praia", "Litoral sul", "Parque Nacional", true, false, "Nascer do sol", "-3.87320", "-32.42480"),
            new PointSeed("Enseada dos Abreus", "Piscinas naturais acessadas por trilha agendada, indicada para maré baixa e guia credenciado.", "Praia", "Sueste", "Parque Nacional", true, true, "Maré baixa", "-3.86670", "-32.41460"),
            new PointSeed("Mirante dos Dois Irmãos", "Mirante clássico para fotografar a Baía dos Porcos e os morros mais famosos de Noronha.", "Mirante", "PIC Sancho", "Parque Nacional", true, false, "Manhã", "-3.85200", "-32.43680"),
            new PointSeed("Mirante do Boldró", "Ponto tradicional para pôr do sol com vista da costa oeste e do Morro Dois Irmãos.", "Mirante", "Boldró", "Livre acesso", false, false, "Pôr do sol", "-3.84780", "-32.42290"),
            new PointSeed("Mirante da Baía dos Golfinhos", "Ponto de observação dos golfinhos rotadores, especialmente nas primeiras horas do dia.", "Mirante", "Parque Nacional Marinho", "Parque Nacional", true, false, "Amanhecer", "-3.85240", "-32.44460"),
            new PointSeed("Ponta das Caracas", "Mirante com vista para formações vulcânicas, ilhas secundárias e mar aberto.", "Mirante", "Sueste", "Parque Nacional", true, false, "Manhã", "-3.87210", "-32.41920"),
            new PointSeed("Buraco da Raquel", "Formação rochosa histórica e ponto contemplativo próximo ao Porto.", "Mirante", "Região do Porto", "Livre acesso", false, false, "Fim da tarde", "-3.83110", "-32.39780"),
            new PointSeed("Ponta da Sapata", "Extremo de paisagem dramática, visto em passeios de barco e trilhas autorizadas.", "Mirante", "Extremo oeste", "Parque Nacional", true, true, "Manhã", "-3.85770", "-32.45520"),
            new PointSeed("Fortaleza Nossa Senhora dos Remédios", "Fortificação histórica com vista para o mar e para o centro da ilha.", "Histórico", "Vila dos Remédios", "Livre acesso", false, false, "Fim da tarde", "-3.83940", "-32.41240"),
            new PointSeed("Igreja Nossa Senhora dos Remédios", "Igreja histórica no coração da Vila dos Remédios, próxima ao casario e às praças.", "Histórico", "Vila dos Remédios", "Livre acesso", false, false, "Fim da tarde", "-3.84090", "-32.41080"),
            new PointSeed("Palácio São Miguel", "Sede administrativa histórica da ilha, em área central e fácil de combinar com a Vila.", "Histórico", "Vila dos Remédios", "Livre acesso", false, false, "Fim da tarde", "-3.84110", "-32.41020"),
            new PointSeed("Memorial Noronhense", "Espaço cultural para entender a formação histórica e social de Fernando de Noronha.", "Cultura", "Vila dos Remédios", "Centro histórico", false, false, "Tarde", "-3.84080", "-32.41100"),
            new PointSeed("Museu do Tubarão", "Ponto de visita sobre vida marinha, com restaurante próximo e vista da região do Porto.", "Cultura", "Porto", "Livre acesso", false, false, "Tarde", "-3.83240", "-32.39920"),
            new PointSeed("Capela de São Pedro dos Pescadores", "Capela pequena e simbólica, ligada à tradição dos pescadores e à paisagem do Porto.", "Cultura", "Porto", "Livre acesso", false, false, "Entardecer", "-3.83320", "-32.40120"),
            new PointSeed("Centro de Visitantes ICMBio", "Base para informações, ingressos e agendamentos de atrativos do Parque Nacional.", "Serviço turístico", "Boldró", "Serviço turístico", false, false, "Qualquer horário", "-3.84900", "-32.41900"),
            new PointSeed("Projeto Tamar Noronha", "Centro de educação ambiental com programação sobre tartarugas e conservação marinha.", "Educação ambiental", "Boldró", "Livre acesso", false, false, "Fim da tarde", "-3.84960", "-32.41980"),
            new PointSeed("Trilha Capim-Açu", "Trilha longa e exigente por vegetação, costões e paisagens menos urbanas da ilha.", "Trilha", "Parque Nacional Marinho", "Parque Nacional", true, true, "Manhã cedo", "-3.85700", "-32.42020"),
            new PointSeed("Trilha Costa Esmeralda", "Caminhada que conecta praias do mar de dentro, mirantes e paradas para banho.", "Trilha", "Costa oeste", "Livre acesso", false, false, "Manhã", "-3.84860", "-32.42600"),
            new PointSeed("Trilha Costa Azul", "Roteiro costeiro de visual aberto, com trechos de banho e observação da vida marinha.", "Trilha", "Mar de fora", "Parque Nacional", true, true, "Manhã", "-3.84490", "-32.39720")
        );

        points.forEach(this::upsertTouristPoint);
    }

    private void seedEstablishments() {
        List<EstablishmentSeed> establishments = List.of(
            new EstablishmentSeed("Restaurante do Vale", "Cozinha brasileira contemporânea em uma das regiões mais procuradas da ilha.", EstablishmentType.RESTAURANT, "Brasileira", "Vila dos Remédios", FOOD_PHOTO, "180", "4.8", "-3.84080", "-32.41120"),
            new EstablishmentSeed("O Pico", "Restaurante e bar central com clima descontraído, drinks e pratos autorais.", EstablishmentType.RESTAURANT, "Bar e restaurante", "Vila dos Remédios", FOOD_PHOTO, "150", "4.6", "-3.84130", "-32.41080"),
            new EstablishmentSeed("Xica da Silva", "Restaurante conhecido por pratos regionais, frutos do mar e ambiente acolhedor.", EstablishmentType.RESTAURANT, "Regional", "Floresta Nova", FOOD_PHOTO, "170", "4.5", "-3.84430", "-32.41400"),
            new EstablishmentSeed("Varanda Noronha", "Cozinha brasileira com peixes, frutos do mar e atendimento voltado ao jantar.", EstablishmentType.RESTAURANT, "Brasileira", "Vila do Trinta", FOOD_PHOTO, "160", "4.5", "-3.84460", "-32.41070"),
            new EstablishmentSeed("Mergulhão", "Restaurante na região do Porto, famoso pelo visual e por pratos de frutos do mar.", EstablishmentType.RESTAURANT, "Frutos do mar", "Porto de Santo Antônio", FOOD_PHOTO, "190", "4.4", "-3.83300", "-32.40260"),
            new EstablishmentSeed("Cacimba Bistrô", "Bistrô no centro histórico, próximo à Igreja dos Remédios, com pratos autorais.", EstablishmentType.RESTAURANT, "Bistrô", "Vila dos Remédios", FOOD_PHOTO, "170", "4.5", "-3.84070", "-32.41140"),
            new EstablishmentSeed("Benedita Cozinha Afetiva", "Cozinha afetiva brasileira com proposta intimista para jantar.", EstablishmentType.RESTAURANT, "Brasileira", "Vila dos Remédios", FOOD_PHOTO, "180", "4.5", "-3.84140", "-32.41170"),
            new EstablishmentSeed("Mesa da Ana", "Experiência gastronômica intimista com menu autoral e reservas disputadas.", EstablishmentType.RESTAURANT, "Menu degustação", "Floresta Velha", FOOD_PHOTO, "260", "4.6", "-3.84540", "-32.41310"),
            new EstablishmentSeed("Márcio Sushi", "Sushi e frutos do mar com foco em peixe fresco e ambiente casual.", EstablishmentType.RESTAURANT, "Japonesa", "Vila dos Remédios", FOOD_PHOTO, "150", "4.7", "-3.84190", "-32.41130"),
            new EstablishmentSeed("Acqua Noronha", "Restaurante com menu variado, boas opções de peixes e pratos para compartilhar.", EstablishmentType.RESTAURANT, "Contemporânea", "Vila dos Remédios", FOOD_PHOTO, "150", "4.6", "-3.84250", "-32.41160"),
            new EstablishmentSeed("Cigana do Cajueiro", "Cozinha regional com personalidade local e pratos de frutos do mar.", EstablishmentType.RESTAURANT, "Regional", "Vila dos Remédios", FOOD_PHOTO, "140", "4.6", "-3.84180", "-32.41210"),
            new EstablishmentSeed("Bar do Meio", "Bar de praia para pôr do sol, petiscos e drinks entre Conceição e Meio.", EstablishmentType.BAR, "Bar de praia", "Praia do Meio", FOOD_PHOTO, "120", "4.4", "-3.84110", "-32.41610"),
            new EstablishmentSeed("Duda Rei Bar", "Bar casual muito procurado para bebidas, petiscos e clima de praia.", EstablishmentType.BAR, "Bar", "Praia da Conceição", FOOD_PHOTO, "100", "4.3", "-3.84200", "-32.41730"),
            new EstablishmentSeed("Empório São Miguel", "Opção prática no centro para lanches, refeições rápidas e apoio ao passeio.", EstablishmentType.RESTAURANT, "Lanches", "Vila dos Remédios", FOOD_PHOTO, "80", "4.2", "-3.84100", "-32.41040"),
            new EstablishmentSeed("Flamboyant", "Restaurante tradicional para almoço e pratos brasileiros no centro da ilha.", EstablishmentType.RESTAURANT, "Brasileira", "Vila dos Remédios", FOOD_PHOTO, "110", "4.2", "-3.84160", "-32.41090"),
            new EstablishmentSeed("Pousada Maravilha", "Hospedagem de alto padrão próxima ao Sueste, com vista marcante e serviço completo.", EstablishmentType.POUSADA, "Luxo", "Sueste", HOTEL_PHOTO, "1800", "4.8", "-3.86290", "-32.42170"),
            new EstablishmentSeed("NANNAI Noronha", "Hospedagem premium com restaurante e experiência voltada ao descanso.", EstablishmentType.RESORT, "Luxo", "Sueste", HOTEL_PHOTO, "2200", "4.8", "-3.86330", "-32.42230"),
            new EstablishmentSeed("Teju-Açu Eco Pousada", "Pousada de charme com proposta integrada à natureza e gastronomia reconhecida.", EstablishmentType.POUSADA, "Charme", "Floresta Velha", HOTEL_PHOTO, "1600", "4.7", "-3.84690", "-32.41730"),
            new EstablishmentSeed("Dolphin Hotel", "Hotel com estrutura de lazer e localização prática para circular pela ilha.", EstablishmentType.HOTEL, "Hotel", "Floresta Nova", HOTEL_PHOTO, "900", "4.4", "-3.84600", "-32.41500"),
            new EstablishmentSeed("Pousada do Vale", "Pousada central, arborizada e próxima à Vila dos Remédios.", EstablishmentType.POUSADA, "Charme", "Vila dos Remédios", HOTEL_PHOTO, "1100", "4.6", "-3.84060", "-32.41170"),
            new EstablishmentSeed("Pousada Morena", "Hospedagem de charme com vista para o Morro do Pico e boa estrutura.", EstablishmentType.POUSADA, "Charme", "Floresta Velha", HOTEL_PHOTO, "1300", "4.6", "-3.84490", "-32.41470"),
            new EstablishmentSeed("Pousada Zé Maria", "Pousada tradicional da ilha, conhecida também por experiências gastronômicas.", EstablishmentType.POUSADA, "Tradicional", "Floresta Velha", HOTEL_PHOTO, "1400", "4.5", "-3.84580", "-32.41620"),
            new EstablishmentSeed("Pousada Triboju", "Hospedagem de charme perto do centro histórico e das praias urbanas.", EstablishmentType.POUSADA, "Charme", "Vila dos Remédios", HOTEL_PHOTO, "1200", "4.5", "-3.84020", "-32.41210"),
            new EstablishmentSeed("Solar dos Ventos", "Pousada com visual privilegiado na região do Sueste.", EstablishmentType.POUSADA, "Vista mar", "Sueste", HOTEL_PHOTO, "1500", "4.6", "-3.86400", "-32.42300"),
            new EstablishmentSeed("Maria Bonita Noronha", "Pousada confortável e bem localizada para roteiros de poucos dias.", EstablishmentType.POUSADA, "Charme", "Floresta Nova", HOTEL_PHOTO, "1200", "4.5", "-3.84410", "-32.41380"),
            new EstablishmentSeed("Posto de Combustível BR Noronha", "Ponto de abastecimento essencial para buggy, carros e motos alugadas.", EstablishmentType.GAS_STATION, "Posto", "BR-363", SERVICE_PHOTO, "0", "4.0", "-3.84550", "-32.41550"),
            new EstablishmentSeed("Noronhão Supermercado", "Mercado amplo para compras de água, snacks, itens de praia e conveniência.", EstablishmentType.MARKET, "Mercado", "Vila do Trinta", SERVICE_PHOTO, "0", "4.1", "-3.84500", "-32.41090"),
            new EstablishmentSeed("Supermercado Breakfast", "Mercado de apoio para café da manhã, bebidas e produtos rápidos.", EstablishmentType.MARKET, "Mercado", "Floresta Nova", SERVICE_PHOTO, "0", "4.0", "-3.84470", "-32.41390"),
            new EstablishmentSeed("Mercadinho Poty", "Mercadinho local para compras rápidas durante o roteiro.", EstablishmentType.MARKET, "Mercadinho", "Vila dos Remédios", SERVICE_PHOTO, "0", "4.0", "-3.84150", "-32.41030"),
            new EstablishmentSeed("Farmácia Noronha", "Farmácia de apoio para itens básicos, protetor, medicamentos e emergências leves.", EstablishmentType.PHARMACY, "Farmácia", "Vila dos Remédios", SERVICE_PHOTO, "0", "4.0", "-3.84220", "-32.41100"),
            new EstablishmentSeed("Farmácia Ilha Farma", "Serviço útil para compras de saúde e higiene durante a estadia.", EstablishmentType.PHARMACY, "Farmácia", "Vila do Trinta", SERVICE_PHOTO, "0", "4.0", "-3.84430", "-32.41080"),
            new EstablishmentSeed("Feirinha da Vila dos Remédios", "Pequena feira e ponto de compras locais, lembranças e apoio ao visitante.", EstablishmentType.FAIR, "Feira", "Vila dos Remédios", SERVICE_PHOTO, "0", "4.0", "-3.84100", "-32.41070"),
            new EstablishmentSeed("Banco 24 Horas Noronha", "Ponto de autoatendimento útil para saque e emergência financeira.", EstablishmentType.CONVENIENCE, "Serviço", "Vila dos Remédios", SERVICE_PHOTO, "0", "3.8", "-3.84120", "-32.41060"),
            new EstablishmentSeed("Aeroporto de Fernando de Noronha", "Principal chegada aérea da ilha, referência para transfers e aluguel de veículos.", EstablishmentType.CONVENIENCE, "Transporte", "BR-363", SERVICE_PHOTO, "0", "4.1", "-3.85490", "-32.42330")
        );

        establishments.forEach(this::upsertEstablishment);
    }

    private void seedTours() {
        List<TourSeed> tours = List.of(
            new TourSeed("Ingresso Parque Nacional Marinho", "Ingresso oficial do Parque Nacional Marinho de Fernando de Noronha. Brasileiros pagam R$ 192,00 e o publico geral paga R$ 384,00; a validade informada pelo parque e de 10 dias.", "Ingresso", "Centro de Visitantes ICMBio / Parnanoronha", "192", "-3.84900", "-32.41900"),
            new TourSeed("Ilha Tour 360", "Passeio terrestre de dia inteiro para conhecer praias, mirantes e pontos clássicos da ilha.", "Ilha Tour", "Agências locais", "342", "-3.84150", "-32.41160"),
            new TourSeed("Passeio de barco Mar de Dentro", "Navegação pelo mar de dentro com vista para ilhas secundárias, Sancho e Dois Irmãos.", "Barco", "Porto de Santo Antônio", "323", "-3.83320", "-32.40420"),
            new TourSeed("Canoa havaiana ao amanhecer", "Experiência em canoa para ver a ilha de outro ângulo e aproveitar o mar cedo.", "Canoa", "Praia do Porto", null, "-3.83340", "-32.40460"),
            new TourSeed("Mergulho de batismo", "Mergulho acompanhado para iniciantes em pontos protegidos de Noronha.", "Mergulho", "Porto de Santo Antônio", null, "-3.83300", "-32.40360"),
            new TourSeed("Mergulho credenciado", "Saídas para mergulhadores certificados em pontos avançados do arquipélago.", "Mergulho", "Porto de Santo Antônio", null, "-3.83300", "-32.40360"),
            new TourSeed("Prancha submarina", "Passeio de barco com prancha rebocada para observar a vida marinha em movimento.", "Snorkel", "Porto de Santo Antônio", "215", "-3.83320", "-32.40410"),
            new TourSeed("Snorkel no naufrágio do Porto", "Flutuação na região do Porto com foco no naufrágio e vida marinha.", "Snorkel", "Praia do Porto", null, "-3.83340", "-32.40460"),
            new TourSeed("Trilha Atalaia curta", "Trilha agendada até a piscina natural da Atalaia, com regras de conservação.", "Trilha", "Atalaia", null, "-3.84380", "-32.39580"),
            new TourSeed("Trilha Atalaia longa", "Roteiro guiado com piscinas naturais e caminhada por trecho sensível do Parque.", "Trilha", "Atalaia", null, "-3.84380", "-32.39580"),
            new TourSeed("Trilha Capim-Açu guiada", "Trilha longa para viajantes com preparo físico e interesse em paisagens menos óbvias.", "Trilha", "Parque Nacional", null, "-3.85700", "-32.42020"),
            new TourSeed("Trilha Costa Esmeralda", "Caminhada pela sequência de praias do mar de dentro, com paradas para banho.", "Trilha", "Costa oeste", null, "-3.84860", "-32.42600"),
            new TourSeed("Trilha Costa Azul", "Experiência guiada no mar de fora com visual costeiro e orientação ambiental.", "Trilha", "Mar de fora", null, "-3.84490", "-32.39720"),
            new TourSeed("Lancha privativa", "Passeio personalizado de lancha para grupos, com paradas para banho e contemplação.", "Barco", "Porto de Santo Antônio", null, "-3.83320", "-32.40420"),
            new TourSeed("Entardecer no barco", "Navegação curta no fim do dia com vista do pôr do sol a partir do mar.", "Barco", "Porto de Santo Antônio", "362", "-3.83320", "-32.40420"),
            new TourSeed("Bike aquática", "Atividade leve no mar para fotos e contemplação perto das praias urbanas.", "Aventura", "Praia da Conceição", null, "-3.84220", "-32.41730"),
            new TourSeed("Observação de golfinhos", "Saída cedo para observar golfinhos e entender o comportamento da espécie.", "Natureza", "Mirante dos Golfinhos", null, "-3.85240", "-32.44460"),
            new TourSeed("Tour histórico Vila dos Remédios", "Caminhada guiada pela igreja, fortaleza, palácio e casario histórico.", "Cultura", "Vila dos Remédios", null, "-3.84090", "-32.41080"),
            new TourSeed("Experiência Projeto Tamar", "Visita orientada e palestra para entender conservação de tartarugas marinhas.", "Educação ambiental", "Boldró", null, "-3.84960", "-32.41980"),
            new TourSeed("Transfer aeroporto e praias", "Serviço de deslocamento para chegada, saída e praias com acesso por estrada.", "Transporte", "Aeroporto", null, "-3.85490", "-32.42330"),
            new TourSeed("Aluguel de buggy", "Locação para circular pela ilha com autonomia entre praias, mirantes e restaurantes.", "Transporte", "BR-363", null, "-3.84530", "-32.41450")
        );

        tours.forEach(this::upsertTour);
    }

    private void seedEvents() {
        List.of(
            "Palestra ambiental do Projeto Tamar",
            "Feirinha cultural da Vila",
            "Música ao vivo no centro histórico",
            "Mutirão de limpeza de praia",
            "Roda de conversa sobre turismo sustentável",
            "Noite cultural noronhense"
        ).forEach(title -> eventRepository.findByTitleIgnoreCase(title).ifPresent(eventRepository::delete));
    }

    private void upsertTouristPoint(PointSeed seed) {
        TouristPoint point = touristPointRepository.findByNameIgnoreCase(seed.name())
            .orElseGet(() -> toTouristPoint(seed));

        point.setName(seed.name());
        point.setDescription(seed.description());
        point.setCategory(seed.category());
        point.setLocation(seed.location());
        setTrustedPhotoUrl(point, BEACH_PHOTO);
        point.setAccessType(seed.accessType());
        point.setRequiresTicket(seed.requiresTicket());
        point.setRequiresGuide(seed.requiresGuide());
        point.setBestTime(seed.bestTime());
        point.setLatitude(bd(seed.latitude()));
        point.setLongitude(bd(seed.longitude()));

        touristPointRepository.save(point);
    }

    private void upsertEstablishment(EstablishmentSeed seed) {
        Establishment establishment = establishmentRepository.findByNameIgnoreCase(seed.name())
            .orElseGet(() -> toEstablishment(seed));

        establishment.setName(seed.name());
        establishment.setType(seed.type());
        establishment.setFoodType(seed.category());
        establishment.setLocation(seed.location());
        establishment.setLatitude(bd(seed.latitude()));
        establishment.setLongitude(bd(seed.longitude()));

        if (isBlank(establishment.getDescription())) {
            establishment.setDescription(seed.description());
        }
        if (isSeededAveragePrice(establishment.getAveragePrice(), seed.averagePrice())) {
            establishment.setAveragePrice(null);
        }
        if (isSeededRating(establishment.getRating(), seed.rating())) {
            establishment.setRating(null);
        }
        setTrustedPhotoUrl(establishment, seed.photoUrl());
        if (isBlank(establishment.getOpeningHours())) {
            establishment.setOpeningHours("Consulte horarios antes de sair");
        }
        if (isBlank(establishment.getAmenities())) {
            establishment.setAmenities(seed.category());
        }
        if (establishment.getIsPremiumExclusive() == null) {
            establishment.setIsPremiumExclusive(false);
        }

        applyEditorialDetails(establishment);

        establishmentRepository.save(establishment);
    }

    private void applyEditorialDetails(Establishment establishment) {
        if (isBlank(establishment.getGoogleMapsUrl())) {
            establishment.setGoogleMapsUrl(googleSearchUrl(establishment.getName()));
        }

        EstablishmentEditorialSeed details = ESTABLISHMENT_DETAILS.get(establishment.getName());
        if (details == null) {
            if (isBlank(establishment.getBestVisitTime())) {
                establishment.setBestVisitTime(defaultBestVisitTime(establishment.getType()));
            }
            return;
        }

        setIfBlank(establishment.getWebsiteUrl(), details.websiteUrl(), establishment::setWebsiteUrl);
        setIfBlank(establishment.getMenuUrl(), details.menuUrl(), establishment::setMenuUrl);
        setIfBlank(establishment.getContactNumber(), details.contactNumber(), establishment::setContactNumber);
        if (isBlank(establishment.getOpeningHours()) || establishment.getOpeningHours().startsWith("Consulte")) {
            setIfBlank(null, details.openingHours(), establishment::setOpeningHours);
        }
        setIfBlank(establishment.getPriceRange(), details.priceRange(), establishment::setPriceRange);
        setIfBlank(establishment.getPopularDishes(), details.popularDishes(), establishment::setPopularDishes);
        setIfBlank(establishment.getBestVisitTime(), details.bestVisitTime(), establishment::setBestVisitTime);
        setIfBlank(establishment.getWeatherAdvice(), details.weatherAdvice(), establishment::setWeatherAdvice);
        if (!isBlank(details.amenities()) &&
                (isBlank(establishment.getAmenities()) || establishment.getAmenities().equals(establishment.getFoodType()))) {
            establishment.setAmenities(details.amenities());
        }
        setIfBlank(establishment.getDataSourceUrl(), details.sourceUrl(), establishment::setDataSourceUrl);
        if (establishment.getDataVerifiedAt() == null) {
            establishment.setDataVerifiedAt(EDITORIAL_VERIFICATION_DATE);
        }
    }

    private void setIfBlank(String current, String value, java.util.function.Consumer<String> setter) {
        if (isBlank(current) && !isBlank(value)) {
            setter.accept(value);
        }
    }

    private String defaultBestVisitTime(EstablishmentType type) {
        if (type == EstablishmentType.BAR) return "Fim da tarde e início da noite.";
        if (type == EstablishmentType.RESTAURANT) return "Confirme o horário e reserve nos períodos de maior movimento.";
        if (type == EstablishmentType.HOTEL || type == EstablishmentType.POUSADA || type == EstablishmentType.RESORT) {
            return "Consulte disponibilidade e condições de check-in antes da chegada.";
        }
        return "Consulte o funcionamento no Google antes de sair.";
    }

    private String googleSearchUrl(String name) {
        String query = URLEncoder.encode(name + " Fernando de Noronha", StandardCharsets.UTF_8);
        return "https://www.google.com/maps/search/?api=1&query=" + query;
    }

    private void upsertTour(TourSeed seed) {
        Tour tour = tourRepository.findByNameIgnoreCase(seed.name())
            .orElseGet(() -> toTour(seed));

        tour.setName(seed.name());
        tour.setDescription(seed.description());
        tour.setCategory(seed.category());
        tour.setPrice(bdOrNull(seed.price()));
        tour.setPartnership(seed.location());
        tour.setLatitude(bd(seed.latitude()));
        tour.setLongitude(bd(seed.longitude()));
        setTrustedPhotoUrl(tour, TOUR_PHOTO);

        tourRepository.save(tour);
    }

    private void upsertEvent(EventSeed seed) {
        Event event = eventRepository.findByTitleIgnoreCase(seed.title())
            .orElseGet(() -> toEvent(seed));

        event.setTitle(seed.title());
        event.setDescription(seed.description());
        event.setCategory(seed.category());
        event.setDate(seed.date());
        event.setLocation(seed.location());
        event.setLatitude(bd(seed.latitude()));
        event.setLongitude(bd(seed.longitude()));
        setTrustedPhotoUrl(event, TOUR_PHOTO);
        if (isBlank(event.getExternalBookingUrl())) {
            event.setExternalBookingUrl("https://www.google.com/maps/search/?api=1&query=" + seed.title().replace(" ", "+") + "+Fernando+de+Noronha");
        }

        eventRepository.save(event);
    }

    private TouristPoint toTouristPoint(PointSeed seed) {
        return TouristPoint.builder()
            .name(seed.name())
            .description(seed.description())
            .category(seed.category())
            .location(seed.location())
            .photoUrl(BEACH_PHOTO)
            .accessType(seed.accessType())
            .requiresTicket(seed.requiresTicket())
            .requiresGuide(seed.requiresGuide())
            .bestTime(seed.bestTime())
            .latitude(bd(seed.latitude()))
            .longitude(bd(seed.longitude()))
            .build();
    }

    private Establishment toEstablishment(EstablishmentSeed seed) {
        return Establishment.builder()
            .name(seed.name())
            .description(seed.description())
            .type(seed.type())
            .foodType(seed.category())
            .averagePrice(null)
            .rating(null)
            .location(seed.location())
            .photoUrl(seed.photoUrl())
            .openingHours("Consulte horários antes de sair")
            .amenities(seed.category())
            .isPremiumExclusive(false)
            .latitude(bd(seed.latitude()))
            .longitude(bd(seed.longitude()))
            .build();
    }

    private Tour toTour(TourSeed seed) {
        return Tour.builder()
            .name(seed.name())
            .description(seed.description())
            .category(seed.category())
            .photoUrl(TOUR_PHOTO)
            .price(bdOrNull(seed.price()))
            .partnership(seed.location())
            .latitude(bd(seed.latitude()))
            .longitude(bd(seed.longitude()))
            .build();
    }

    private Event toEvent(EventSeed seed) {
        return Event.builder()
            .title(seed.title())
            .description(seed.description())
            .category(seed.category())
            .date(seed.date())
            .location(seed.location())
            .photoUrl(TOUR_PHOTO)
            .externalBookingUrl("https://www.google.com/maps/search/?api=1&query=" + seed.title().replace(" ", "+") + "+Fernando+de+Noronha")
            .latitude(bd(seed.latitude()))
            .longitude(bd(seed.longitude()))
            .build();
    }

    private BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    private BigDecimal bdOrNull(String value) {
        return isBlank(value) ? null : new BigDecimal(value);
    }

    private void setTrustedPhotoUrl(TouristPoint point, String photoUrl) {
        if (isBlank(point.getPhotoUrl()) || isDemoImage(point.getPhotoUrl())) {
            point.setPhotoUrl(photoUrl);
        }
    }

    private void setTrustedPhotoUrl(Establishment establishment, String photoUrl) {
        if (isBlank(establishment.getPhotoUrl()) || isDemoImage(establishment.getPhotoUrl())) {
            establishment.setPhotoUrl(photoUrl);
        }
    }

    private void setTrustedPhotoUrl(Tour tour, String photoUrl) {
        if (isBlank(tour.getPhotoUrl()) || isDemoImage(tour.getPhotoUrl())) {
            tour.setPhotoUrl(photoUrl);
        }
    }

    private void setTrustedPhotoUrl(Event event, String photoUrl) {
        if (isBlank(event.getPhotoUrl()) || isDemoImage(event.getPhotoUrl())) {
            event.setPhotoUrl(photoUrl);
        }
    }

    private boolean isDemoImage(String value) {
        return value != null && value.contains("images.unsplash.com");
    }

    private boolean isSeededAveragePrice(BigDecimal currentValue, String seedValue) {
        return currentValue != null && !isBlank(seedValue) && currentValue.compareTo(new BigDecimal(seedValue)) == 0;
    }

    private boolean isSeededRating(Double currentValue, String seedValue) {
        return currentValue != null && !isBlank(seedValue) && Math.abs(currentValue - Double.valueOf(seedValue)) < 0.0001;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private record PointSeed(String name, String description, String category, String location, String accessType, boolean requiresTicket, boolean requiresGuide, String bestTime, String latitude, String longitude) {}
    private record EstablishmentSeed(String name, String description, EstablishmentType type, String category, String location, String photoUrl, String averagePrice, String rating, String latitude, String longitude) {}
    private record EstablishmentEditorialSeed(String websiteUrl, String menuUrl, String contactNumber, String openingHours, String priceRange, String popularDishes, String bestVisitTime, String weatherAdvice, String amenities, String sourceUrl) {}
    private record TourSeed(String name, String description, String category, String location, String price, String latitude, String longitude) {}
    private record EventSeed(String title, String description, String category, String location, LocalDateTime date, String latitude, String longitude) {}
}
