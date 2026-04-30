\encoding UTF8

UPDATE public.tourist_points SET
  name = U&'Ba\00EDa do Sancho',
  description = U&'\00CDcone absoluto de Noronha, com mar cristalino, acesso controlado e um cen\00E1rio perfeito para snorkel em dias de mar calmo.',
  category = U&'Praia',
  location = U&'Lado oeste do Parque Nacional Marinho',
  access_type = U&'Parque Nacional',
  best_time = U&'Mar\00E9 baixa e manh\00E3'
WHERE id = 1;

UPDATE public.tourist_points SET
  name = U&'Ba\00EDa dos Porcos',
  description = U&'Uma das paisagens mais fotografadas da ilha, com vista frontal para o Morro Dois Irm\00E3os e \00E1guas transparentes.',
  category = U&'Praia',
  location = U&'Entre o Sancho e a Cacimba do Padre',
  access_type = U&'Parque Nacional',
  best_time = U&'Mar\00E9 baixa'
WHERE id = 2;

UPDATE public.tourist_points SET
  name = U&'Praia do Le\00E3o',
  description = U&'Praia ampla e selvagem, conhecida pelo ninho de tartarugas e pela luz forte do amanhecer.',
  category = U&'Praia',
  location = U&'Litoral sul',
  access_type = U&'Parque Nacional',
  best_time = U&'Nascer do sol'
WHERE id = 3;

UPDATE public.tourist_points SET
  name = U&'Ba\00EDa dos Golfinhos',
  description = U&'Ponto cl\00E1ssico para observar golfinhos rotadores ao amanhecer, em uma enseada protegida e silenciosa.',
  category = U&'Mirante',
  location = U&'Face norte do arquip\00E9lago',
  access_type = U&'Parque Nacional',
  best_time = U&'Primeiras horas da manh\00E3'
WHERE id = 4;

UPDATE public.tourist_points SET
  name = U&'Praia do Sueste',
  description = U&'\00C1gua calma, fundo rico e um dos melhores cen\00E1rios da ilha para observar a vida marinha com snorkel.',
  category = U&'Mergulho',
  location = U&'Litoral sudeste',
  access_type = U&'Parque Nacional',
  best_time = U&'Mar\00E9 baixa'
WHERE id = 5;

UPDATE public.tourist_points SET
  name = U&'Praia da Concei\00E7\00E3o',
  description = U&'Uma das praias mais acess\00EDveis e queridas da ilha, \00F3tima para banho, p\00F4r do sol e contempla\00E7\00E3o do Morro do Pico.',
  category = U&'Praia',
  location = U&'Vila do Trinta',
  access_type = U&'Livre acesso',
  best_time = U&'Fim da tarde'
WHERE id = 6;

UPDATE public.tourist_points SET
  name = U&'Cacimba do Padre',
  description = U&'Praia de ondas fortes e visual dram\00E1tico, muito procurada por surfistas e por quem quer ver o Morro Dois Irm\00E3os.',
  category = U&'Surf',
  location = U&'Litoral oeste',
  access_type = U&'Livre acesso',
  best_time = U&'Manh\00E3 e mar agitado'
WHERE id = 7;

UPDATE public.tourist_points SET
  name = U&'Porto de Santo Ant\00F4nio',
  description = U&'\00C1rea hist\00F3rica de chegada e pesca, hoje tamb\00E9m usada para banho, mergulho e passeios de barco.',
  category = U&'Hist\00F3rico',
  location = U&'Ba\00EDa do Porto',
  access_type = U&'Livre acesso',
  best_time = U&'Manh\00E3'
WHERE id = 8;

UPDATE public.tourist_points SET
  name = U&'Vila dos Rem\00E9dios',
  description = U&'Centro hist\00F3rico da ilha, com casario, vida local, pontos de encontro e acesso f\00E1cil a marcos culturais.',
  category = U&'Hist\00F3rico',
  location = U&'Centro da ilha',
  access_type = U&'Centro hist\00F3rico',
  best_time = U&'Fim da tarde e noite'
WHERE id = 9;

UPDATE public.tourist_points SET
  name = U&'Fortaleza dos Rem\00E9dios',
  description = U&'Conjunto militar colonial com vista estrat\00E9gica para o mar e leitura hist\00F3rica do per\00EDodo de defesa da ilha.',
  category = U&'Hist\00F3rico',
  location = U&'Vila dos Rem\00E9dios',
  access_type = U&'Patrim\00F4nio hist\00F3rico',
  best_time = U&'Fim da tarde'
WHERE id = 10;

UPDATE public.tourist_points SET
  name = U&'Capela de S\00E3o Pedro dos Pescadores',
  description = U&'Pequena capela ligada \00E0 tradi\00E7\00E3o dos pescadores, muito associada \00E0 identidade afetiva da ilha.',
  category = U&'Cultura',
  location = U&'Regi\00E3o do Porto',
  access_type = U&'Livre acesso',
  best_time = U&'Entardecer'
WHERE id = 11;

UPDATE public.tourist_points SET
  name = U&'Mirante do Boldr\00F3',
  description = U&'Um dos mirantes mais populares de Noronha, famoso pelo p\00F4r do sol e pela leitura panor\00E2mica da costa oeste.',
  category = U&'Mirante',
  location = U&'Boldr\00F3',
  access_type = U&'Livre acesso',
  best_time = U&'P\00F4r do sol'
WHERE id = 12;

UPDATE public.tourist_points SET
  name = U&'Trilha do Capim-A\00E7u',
  description = U&'Trilha longa e imersiva, indicada para quem busca vegeta\00E7\00E3o, cost\00F5es e paisagens menos \00F3bvias da ilha.',
  category = U&'Trilha',
  location = U&'Parque Nacional Marinho',
  access_type = U&'Parque Nacional',
  best_time = U&'Manh\00E3'
WHERE id = 13;

UPDATE public.tourist_points SET
  name = U&'Piscina da Atalaia',
  description = U&'Piscina natural sens\00EDvel e controlada, com visita\00E7\00E3o agendada e contato direto com a vida marinha.',
  category = U&'Trilha',
  location = U&'Atalaia',
  access_type = U&'Parque Nacional',
  best_time = U&'Mar\00E9 baixa'
WHERE id = 14;

UPDATE public.tourist_points SET
  name = U&'Centro de Visitantes ICMBio',
  description = U&'Ponto de apoio para informa\00E7\00F5es, orienta\00E7\00F5es, ingressos e agendamentos do Parque Nacional Marinho.',
  category = U&'Educa\00E7\00E3o',
  location = U&'Boldr\00F3',
  access_type = U&'Servi\00E7o tur\00EDstico',
  best_time = U&'Qualquer hor\00E1rio'
WHERE id = 15;