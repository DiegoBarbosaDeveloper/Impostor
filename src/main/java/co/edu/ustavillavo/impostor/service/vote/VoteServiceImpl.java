package co.edu.ustavillavo.impostor.service.vote;

import co.edu.ustavillavo.impostor.domain.dto.Vote;
import co.edu.ustavillavo.impostor.repo.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    @Override
    public List<Vote> getAllVotes() {
        return List.of();
    }

    @Override
    public Optional<Vote> getVote(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Vote> saveVote(Vote dto) {
        return Optional.empty();
    }

    @Override
    public void updateVote(Vote dto) {

    }

    @Override
    public void modifyVote(Vote dto) {

    }

    @Override
    public void deleteVote(UUID id) {

    }
}
