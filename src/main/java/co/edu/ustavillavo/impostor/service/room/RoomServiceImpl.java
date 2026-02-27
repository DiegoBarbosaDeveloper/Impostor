package co.edu.ustavillavo.impostor.service.room;

import co.edu.ustavillavo.impostor.domain.dto.Room;
import co.edu.ustavillavo.impostor.domain.enums.Role;
import co.edu.ustavillavo.impostor.domain.enums.RoomStatus;
import co.edu.ustavillavo.impostor.domain.response.CodePlayerResponse;
import co.edu.ustavillavo.impostor.domain.response.CodeResponse;
import co.edu.ustavillavo.impostor.domain.response.CloseConResponse;
import co.edu.ustavillavo.impostor.domain.response.CloseEndResponse;
import co.edu.ustavillavo.impostor.domain.response.ExpelledResponse;
import co.edu.ustavillavo.impostor.domain.response.PlayerResponse;
import co.edu.ustavillavo.impostor.domain.response.RevealResponse;
import co.edu.ustavillavo.impostor.domain.response.RoleStatus;
import co.edu.ustavillavo.impostor.domain.response.RoomsResponse;
import co.edu.ustavillavo.impostor.domain.response.StartResponse;
import co.edu.ustavillavo.impostor.domain.response.VoteResponse;
import co.edu.ustavillavo.impostor.entity.AssignmentEntity;
import co.edu.ustavillavo.impostor.entity.PlayerEntity;
import co.edu.ustavillavo.impostor.entity.RoomEntity;
import co.edu.ustavillavo.impostor.entity.VoteEntity;
import co.edu.ustavillavo.impostor.repo.AssignmentRepository;
import co.edu.ustavillavo.impostor.repo.PlayerRepository;
import co.edu.ustavillavo.impostor.repo.RoomRepository;
import co.edu.ustavillavo.impostor.repo.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private static final int CODE_LENGTH = 6;
    private static final String CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MAX_CODE_ATTEMPTS = 25;
    private static final Map<String, List<String>> WORDS_BY_CATEGORY = createWordsByCategory();

    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final AssignmentRepository assignmentRepository;
    private final VoteRepository voteRepository;
    private final Random random = new Random();

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public Optional<Room> getRoom(@NonNull UUID id) {
        return Optional.of(toDto(roomRepository.getReferenceById(id)));
    }

    @Override
    public Room saveRoom(@NonNull Room dto) {
        if (dto.code().isBlank() || dto.status() == null || dto.hostPlayerId() == null
        || dto.category().isBlank()  || dto.currentRound() == null
        || dto.secretWord() == null || dto.winnerTeam() == null){
            throw new RuntimeException("Null Attributes Not Allowed");
        }

        if (dto.impostorCount() == null || dto.impostorCount() < 1){
            RoomEntity entity = toEntity(dto);
            entity.setImpostorCount(1);

            roomRepository.save(entity);

            return dto;
        }

        roomRepository.save(toEntity(dto));

        return dto;

    }

    @Override
    public RoomsResponse createRoomWithHost(String hostNickname, String category, Integer impostorCount) {
        if (hostNickname == null || hostNickname.isBlank()) {
            throw new IllegalArgumentException("hostNickname is required");
        }

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category is required");
        }

        int resolvedImpostorCount = impostorCount == null || impostorCount < 1 ? 1 : impostorCount;

        RoomEntity room = new RoomEntity();
        room.setCode(generateUniqueRoomCode());
        room.setStatus(RoomStatus.LOBBY);
        room.setCategory(category.trim().toUpperCase(Locale.ROOT));
        room.setImpostorCount(resolvedImpostorCount);
        room.setCurrentRound((byte) 0);
        room.setSecretWord("");
        room.setWinnerTeam(null);
        room.setHostPlayer(null);

        RoomEntity createdRoom = roomRepository.save(room);

        PlayerEntity hostPlayer = new PlayerEntity();
        hostPlayer.setRoom(createdRoom);
        hostPlayer.setNickname(hostNickname.trim());
        hostPlayer.setAlive(true);

        PlayerEntity createdHostPlayer = playerRepository.save(hostPlayer);

        createdRoom.setHostPlayer(createdHostPlayer);
        roomRepository.save(createdRoom);

        RoomsResponse response = new RoomsResponse();
        response.setRoomCode(createdRoom.getCode());
        response.setHostPlayerId(createdHostPlayer.getPlayerId());
        return response;
    }

    @Override
    public PlayerResponse joinPlayer(String code, String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("nickname is required");
        }

        RoomEntity room = roomRepository.findByCode(normalizeCode(code))
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        PlayerEntity player = new PlayerEntity();
        player.setRoom(room);
        player.setNickname(nickname.trim());
        player.setAlive(true);

        PlayerEntity createdPlayer = playerRepository.save(player);

        PlayerResponse response = new PlayerResponse();
        response.setPlayerId(createdPlayer.getPlayerId());
        response.setNickname(createdPlayer.getNickname());
        return response;
    }

    @Override
    public StartResponse startGame(String code, UUID hostPlayerId) {
        if (hostPlayerId == null) {
            throw new IllegalArgumentException("hostPlayerId is required");
        }

        RoomEntity room = roomRepository.findByCode(normalizeCode(code))
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (room.getHostPlayer() == null || !room.getHostPlayer().getPlayerId().equals(hostPlayerId)) {
            throw new IllegalArgumentException("Only host can start the game");
        }

        List<PlayerEntity> alivePlayers = playerRepository.findByRoom_CodeAndAliveTrue(room.getCode());
        if (alivePlayers.size() < 3) {
            throw new IllegalStateException("At least 3 alive players are required");
        }

        assignmentRepository.deleteByRoom(room);

        List<PlayerEntity> shuffledPlayers = new ArrayList<>(alivePlayers);
        Collections.shuffle(shuffledPlayers, random);

        int configuredImpostors = room.getImpostorCount() <= 0 ? 1 : room.getImpostorCount();
        int maxImpostors = Math.max(1, alivePlayers.size() - 1);
        int impostorCount = Math.min(configuredImpostors, maxImpostors);

        String secretWord = selectWordByCategory(room.getCategory());

        for (int index = 0; index < shuffledPlayers.size(); index++) {
            PlayerEntity player = shuffledPlayers.get(index);
            boolean isImpostor = index < impostorCount;

            AssignmentEntity assignment = new AssignmentEntity();
            assignment.setRoom(room);
            assignment.setPlayer(player);
            assignment.setRole(isImpostor ? Role.IMPOSTER : Role.CIVIL);
            assignment.setWord(isImpostor ? "" : secretWord);

            assignmentRepository.save(assignment);
        }

        room.setSecretWord(secretWord);
        room.setStatus(RoomStatus.IN_GAME);
        room.setCurrentRound((byte) 1);
        roomRepository.save(room);

        StartResponse response = new StartResponse();
        response.setStatus(room.getStatus());
        response.setCurrentRound(room.getCurrentRound());
        return response;
    }

    @Override
    public RoleStatus getPersonalWord(String code, UUID playerId) {
        if (playerId == null) {
            throw new IllegalArgumentException("playerId is required");
        }

        AssignmentEntity assignment = assignmentRepository
                .findByRoom_CodeAndPlayer_PlayerId(normalizeCode(code), playerId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found for player in room"));

        RoleStatus response = new RoleStatus();
        boolean isImpostor = assignment.getRole() == Role.IMPOSTER;
        response.setRole(isImpostor ? "IMPOSTOR" : "CIVIL");
        response.setWord(isImpostor || assignment.getWord().isBlank() ? null : assignment.getWord());
        return response;
    }

    @Override
    public VoteResponse registerVote(String code, UUID voterId, UUID votedId) {
        if (voterId == null || votedId == null) {
            throw new IllegalArgumentException("voterId and votedId are required");
        }

        RoomEntity room = roomRepository.findByCode(normalizeCode(code))
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (room.getStatus() != RoomStatus.IN_GAME) {
            throw new IllegalStateException("Room must be IN_GAME");
        }

        PlayerEntity voter = playerRepository.findByRoom_CodeAndPlayerId(room.getCode(), voterId)
                .orElseThrow(() -> new IllegalArgumentException("voterId does not belong to room"));

        PlayerEntity voted = playerRepository.findByRoom_CodeAndPlayerId(room.getCode(), votedId)
                .orElseThrow(() -> new IllegalArgumentException("votedId does not belong to room"));

        if (!voter.isAlive()) {
            throw new IllegalStateException("voterId must be alive");
        }

        if (!voted.isAlive()) {
            throw new IllegalStateException("votedId must be alive");
        }

        int round = room.getCurrentRound() == null ? 0 : room.getCurrentRound();
        if (round < 1) {
            throw new IllegalStateException("Invalid current round");
        }

        boolean alreadyVoted = voteRepository.existsByRoom_CodeAndRoundNumberAndVoterPlayer_PlayerId(
                room.getCode(),
                round,
                voterId
        );

        if (alreadyVoted) {
            throw new IllegalStateException("Duplicate vote is not allowed in current round");
        }

        VoteEntity vote = new VoteEntity();
        vote.setRoom(room);
        vote.setRoundNumber(round);
        vote.setVoterPlayer(voter);
        vote.setVotedPlayer(voted);
        voteRepository.save(vote);

        VoteResponse response = new VoteResponse();
        response.setMessage("Voto registrado");
        response.setRound((byte) round);
        return response;
    }

    @Override
    public Object closeRound(String code, UUID hostPlayerId) {
        if (hostPlayerId == null) {
            throw new IllegalArgumentException("hostPlayerId is required");
        }

        RoomEntity room = roomRepository.findByCode(normalizeCode(code))
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (room.getHostPlayer() == null || !room.getHostPlayer().getPlayerId().equals(hostPlayerId)) {
            throw new IllegalArgumentException("Only host can close the round");
        }

        if (room.getStatus() != RoomStatus.IN_GAME) {
            throw new IllegalStateException("Room must be IN_GAME");
        }

        int currentRound = room.getCurrentRound() == null ? 0 : room.getCurrentRound();
        if (currentRound < 1) {
            throw new IllegalStateException("Invalid current round");
        }

        List<PlayerEntity> alivePlayers = playerRepository.findByRoom_CodeAndAliveTrue(room.getCode());
        if (alivePlayers.isEmpty()) {
            throw new IllegalStateException("No alive players available");
        }

        List<VoteEntity> roundVotes = voteRepository.findByRoom_CodeAndRoundNumber(room.getCode(), currentRound);
        Map<UUID, Integer> voteCountByPlayer = new HashMap<>();

        for (VoteEntity vote : roundVotes) {
            UUID votedPlayerId = vote.getVotedPlayer().getPlayerId();
            voteCountByPlayer.put(votedPlayerId, voteCountByPlayer.getOrDefault(votedPlayerId, 0) + 1);
        }

        PlayerEntity expelledPlayer = alivePlayers.stream()
                .max(Comparator
                        .comparingInt((PlayerEntity player) -> voteCountByPlayer.getOrDefault(player.getPlayerId(), 0))
                        .thenComparing(player -> player.getPlayerId().toString()))
                .orElseThrow(() -> new IllegalStateException("Unable to resolve expelled player"));

        expelledPlayer.setAlive(false);
        playerRepository.save(expelledPlayer);

        AssignmentEntity expelledAssignment = assignmentRepository
                .findByRoom_CodeAndPlayer_PlayerId(room.getCode(), expelledPlayer.getPlayerId())
                .orElseThrow(() -> new IllegalStateException("Missing assignment for expelled player"));

        boolean expelledWasImpostor = expelledAssignment.getRole() == Role.IMPOSTER;

        int aliveCount = playerRepository.findByRoom_CodeAndAliveTrue(room.getCode()).size();
        boolean impostorAlive = assignmentRepository.findByRoom_Code(room.getCode())
                .stream()
                .filter(assignment -> assignment.getRole() == Role.IMPOSTER)
                .anyMatch(assignment -> assignment.getPlayer() != null && assignment.getPlayer().isAlive());

        if (expelledWasImpostor) {
            room.setStatus(RoomStatus.FINISHED);
            room.setWinnerTeam("CIVILES");
            roomRepository.save(room);

            CloseEndResponse response = new CloseEndResponse();
            response.setRoundClosed((byte) currentRound);
            response.setStatus(room.getStatus());
            response.setWinner("CIVILES");
            response.setSecretWord(room.getSecretWord());
            response.setReveal(buildReveal(room.getCode()));
            return response;
        }

        if (aliveCount == 2 && impostorAlive) {
            room.setStatus(RoomStatus.FINISHED);
            room.setWinnerTeam("IMPOSTORES");
            roomRepository.save(room);

            CloseEndResponse response = new CloseEndResponse();
            response.setRoundClosed((byte) currentRound);
            response.setStatus(room.getStatus());
            response.setWinner("IMPOSTORES");
            response.setSecretWord(room.getSecretWord());
            response.setReveal(buildReveal(room.getCode()));
            return response;
        }

        room.setCurrentRound((byte) (currentRound + 1));
        roomRepository.save(room);

        CloseConResponse response = new CloseConResponse();
        response.setRoundClosed((byte) currentRound);
        response.setStatus(RoomStatus.IN_GAME);
        response.setNextRound(room.getCurrentRound());
        response.setAliveCount(aliveCount);

        ExpelledResponse expelledResponse = new ExpelledResponse();
        expelledResponse.setId(expelledPlayer.getPlayerId());
        expelledResponse.setNickname(expelledPlayer.getNickname());
        expelledResponse.setWasImpostor(expelledWasImpostor);
        response.setExpelled(expelledResponse);

        return response;
    }

    @Override
    public void updateRoom(@NonNull Room dto) {

        if(dto.id() == null){
            throw new RuntimeException("No Id for Updating Room");
        }

        if(dto.winnerTeam().isBlank() || dto.impostorCount()  == null|| dto.currentRound() == null ||
        dto.category().isBlank() || dto.status() == null || dto.hostPlayerId() == null){
            throw new RuntimeException("No Null attributes for Updating Room");
        }

        RoomEntity entity = roomRepository.getReferenceById(dto.id());

        entity.setCategory(dto.category());
        entity.setCode(dto.code());
        entity.setStatus(dto.status());
        entity.setHostPlayer(playerRepository.getReferenceById(dto.hostPlayerId()));
        entity.setCurrentRound(dto.currentRound());
        entity.setImpostorCount(dto.impostorCount());
        entity.setSecretWord(dto.secretWord());
        entity.setWinnerTeam(dto.winnerTeam());

        roomRepository.save(entity);

    }

    @Override
    public void modifyRoom(@NonNull Room dto) {

        if (dto.id() == null){
            throw new RuntimeException("No id for Modifying Room");
        }

        RoomEntity entity = roomRepository.getReferenceById(dto.id());

        if (!dto.code().isBlank()){
            entity.setCode(dto.code());
        }

        if (dto.status() != null){
            entity.setStatus(dto.status());
        }

        if (dto.hostPlayerId() != null){
            entity.setHostPlayer(playerRepository.getReferenceById(dto.hostPlayerId()));
        }

        if (!dto.category().isBlank()){
            entity.setCategory(dto.category());
        }

        if (dto.impostorCount() != null && dto.impostorCount() > 0){
            entity.setImpostorCount(dto.impostorCount());
        }

        if (dto.currentRound() != null){
            entity.setCurrentRound(dto.currentRound());
        }

        if (dto.secretWord() != null && !dto.secretWord().isBlank()){
            entity.setSecretWord(dto.secretWord());
        }

        if (!dto.winnerTeam().isBlank()){
            entity.setWinnerTeam(dto.winnerTeam());
        }

        roomRepository.save(entity);
    }

    @Override
    public CodeResponse getRoomState(String code) {
        RoomEntity room = roomRepository.findByCode(normalizeCode(code))
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        List<CodePlayerResponse> players = playerRepository.findByRoom_Code(room.getCode())
                .stream()
                .map(player -> {
                    CodePlayerResponse response = new CodePlayerResponse();
                    response.setId(player.getPlayerId());
                    response.setNickname(player.getNickname());
                    response.setAlive(player.isAlive());
                    return response;
                })
                .toList();

        CodeResponse response = new CodeResponse();
        response.setStatus(room.getStatus());
        response.setCategory(room.getCategory());
        response.setCurrentRound(room.getCurrentRound());
        response.setPlayers(players);
        return response;
    }

    @Override
    public void deleteRoom(UUID id) {
        roomRepository.deleteById(id);
    }

    private String normalizeCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Room code is required");
        }

        return code.trim().toUpperCase(Locale.ROOT);
    }

    private List<RevealResponse> buildReveal(String code) {
        return assignmentRepository.findByRoom_Code(code)
                .stream()
                .map(assignment -> {
                    RevealResponse reveal = new RevealResponse();
                    reveal.setPlayerId(assignment.getPlayer().getPlayerId());
                    reveal.setNickname(assignment.getPlayer().getNickname());
                    reveal.setRole(assignment.getRole() == Role.IMPOSTER ? "IMPOSTOR" : "CIVIL");
                    return reveal;
                })
                .toList();
    }

    private String selectWordByCategory(String category) {
        String normalizedCategory = category == null ? "" : category.trim().toUpperCase(Locale.ROOT);
        List<String> words = WORDS_BY_CATEGORY.getOrDefault(normalizedCategory, WORDS_BY_CATEGORY.get("DEFAULT"));
        int randomIndex = random.nextInt(words.size());
        return words.get(randomIndex);
    }

    private static Map<String, List<String>> createWordsByCategory() {
        Map<String, List<String>> categories = new HashMap<>();
        categories.put("TECNOLOGIA", Arrays.asList("microservicios", "backend", "compilador", "algoritmo"));
        categories.put("ANIMALES", Arrays.asList("tigre", "delfin", "jirafa", "pinguino"));
        categories.put("COMIDA", Arrays.asList("lasagna", "arepa", "sushi", "empanada"));
        categories.put("DEPORTES", Arrays.asList("futbol", "tenis", "natacion", "ciclismo"));
        categories.put("PAISES", Arrays.asList("colombia", "mexico", "japon", "canada"));
        categories.put("DEFAULT", Arrays.asList("microservicios", "cafe", "montana", "pelicula"));
        return categories;
    }

    private String generateUniqueRoomCode() {
        for (int attempt = 0; attempt < MAX_CODE_ATTEMPTS; attempt++) {
            StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);

            for (int index = 0; index < CODE_LENGTH; index++) {
                int randomIndex = random.nextInt(CODE_CHARS.length());
                codeBuilder.append(CODE_CHARS.charAt(randomIndex));
            }

            String code = codeBuilder.toString();
            if (!roomRepository.existsByCode(code)) {
                return code;
            }
        }

        throw new IllegalStateException("Unable to generate room code");
    }

    private RoomEntity toEntity(Room dto){
        return new RoomEntity(
                dto.id(),
                dto.code(),
                dto.status(),
                playerRepository.getReferenceById(dto.hostPlayerId()),
                dto.category(),
                dto.impostorCount(),
                dto.currentRound(),
                dto.secretWord(),
                dto.winnerTeam()
        );
    }

    private Room toDto(RoomEntity entity){
        return new Room(
                entity.getRoomId(),
                entity.getCode(),
                entity.getStatus(),
                entity.getHostPlayer() != null ? entity.getHostPlayer().getPlayerId() : null,
                entity.getCategory(),
                entity.getImpostorCount(),
                entity.getCurrentRound(),
                entity.getSecretWord(),
                entity.getWinnerTeam()
        );
    }
}
