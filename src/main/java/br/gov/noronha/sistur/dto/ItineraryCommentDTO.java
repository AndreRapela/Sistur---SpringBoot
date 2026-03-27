package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.model.ItineraryComment;
import java.time.LocalDateTime;

public record ItineraryCommentDTO(
    Long id,
    Long itineraryId,
    String userName,
    String userPhoto,
    String content,
    LocalDateTime createdAt
) {
    public static ItineraryCommentDTO fromEntity(ItineraryComment comment) {
        return new ItineraryCommentDTO(
            comment.getId(),
            comment.getItinerary().getId(),
            comment.getUser().getName(),
            comment.getUser().getPhotoUrl(),
            comment.getContent(),
            comment.getCreatedAt()
        );
    }
}
