package br.gov.noronha.sistur.service;

import br.gov.noronha.sistur.model.Itinerary;
import br.gov.noronha.sistur.model.ItineraryItem;
import br.gov.noronha.sistur.model.User;
import br.gov.noronha.sistur.repository.ItineraryRepository;
import br.gov.noronha.sistur.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final UserRepository userRepository;

    public List<Itinerary> getMyItineraries(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return itineraryRepository.findByUser(user);
    }

    public List<Itinerary> getPublicItineraries() {
        return itineraryRepository.findByIsPublicTrue();
    }

    @Transactional
    public Itinerary save(Itinerary itinerary, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        itinerary.setUser(user);
        
        // Garante o vínculo bidirecional para os itens
        if (itinerary.getItems() != null) {
            itinerary.getItems().forEach(item -> item.setItinerary(itinerary));
        }
        
        return itineraryRepository.save(itinerary);
    }

    @Transactional
    public void delete(Long id, String email) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Roteiro não encontrado"));
        
        if (!itinerary.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Acesso negado");
        }
        
        itineraryRepository.delete(itinerary);
    }
}
