package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.SuggestionParticipants;
import cm.skysoft.app.mapper.UserMapper;
import cm.skysoft.app.repository.SuggestionParticipantRepository;
import cm.skysoft.app.service.SuggestionParticipantService;
import cm.skysoft.app.service.UserService;
import cm.skysoft.app.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class SuggestionParticipantServiceImpl implements SuggestionParticipantService {

    private final SuggestionParticipantRepository suggestionParticipantRepository;
    private final UserMapper userMapper;

    private final EntityManager em;
    private final UserService userService;

    public SuggestionParticipantServiceImpl(SuggestionParticipantRepository suggestionParticipantRepository, UserMapper userMapper, EntityManager em, UserService userService) {
        this.suggestionParticipantRepository = suggestionParticipantRepository;
        this.userMapper = userMapper;
        this.em = em;
        this.userService = userService;
    }

    @Override
    public SuggestionParticipants save(SuggestionParticipants suggestionParticipants) {
        return suggestionParticipantRepository.save(suggestionParticipants);
    }

    @Override
    public void update(SuggestionParticipants suggestionParticipants) {
        suggestionParticipantRepository.save(suggestionParticipants);
    }

    @Override
    public Optional<SuggestionParticipants> getsuggestionParticipantsById(Long id) {
        return suggestionParticipantRepository.findById(id);
    }

    @Override
    public SuggestionParticipants findsuggestionParticipantsById(Long idSuggestionParticipants) {
        return suggestionParticipantRepository.getOne(idSuggestionParticipants);
    }

    @Override
    public Page<SuggestionParticipants> getsuggestionParticipants(Pageable pageable) {
        return suggestionParticipantRepository.findAll(pageable);
    }

    @Override
    public List<SuggestionParticipants> getAllsuggestionParticipants() {
        return suggestionParticipantRepository.findAll();
    }

    @Override
    public List<UserDTO> getAllUserParticipants(Long idSuggestion) {
        return userMapper.usersToUserDTOs(suggestionParticipantRepository.getAllUserParticipants(idSuggestion));
    }

    @Override
    public void delete(SuggestionParticipants suggestionParticipants) {
        suggestionParticipantRepository.delete(suggestionParticipants);
    }

    @Override
    public void deleteById(Long id) {
        suggestionParticipantRepository.deleteById(id);
    }
}
