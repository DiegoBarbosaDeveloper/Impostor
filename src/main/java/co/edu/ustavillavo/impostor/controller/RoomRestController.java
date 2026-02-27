package co.edu.ustavillavo.impostor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ustavillavo.impostor.domain.request.PlayerBody;
import co.edu.ustavillavo.impostor.domain.request.RoomsBody;
import co.edu.ustavillavo.impostor.domain.request.VoteBody;
import co.edu.ustavillavo.impostor.service.room.RoomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomRestController {
    
    private final RoomService roomService;


    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomsBody body){
        return ResponseEntity.ok(
                roomService.createRoomWithHost(
                        body.getHostNickname(),
                        body.getCategory(),
                        body.getImpostorCount()
                )
        );
    }

    @PostMapping("/{code}/players")
    public ResponseEntity<?> addPlayer(@PathVariable String code, @RequestBody PlayerBody body){
        return ResponseEntity.ok(roomService.joinPlayer(code, body.getNickname()));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRoom(@PathVariable String code){
        return ResponseEntity.ok(roomService.getRoomState(code));
    }

    @PostMapping("/{code}/start")
    public ResponseEntity<?> startGame(@PathVariable String code, @RequestParam java.util.UUID hostPlayerId){
        return ResponseEntity.ok(roomService.startGame(code, hostPlayerId));
    }

    @GetMapping("/{code}/me")
    public ResponseEntity<?> getRole(@PathVariable String code, @RequestParam java.util.UUID playerId){
        return ResponseEntity.ok(roomService.getPersonalWord(code, playerId));
    }

    @PostMapping("/{code}/votes")
    public ResponseEntity<?> registerVote(
            @PathVariable String code,
            @RequestParam java.util.UUID voterId,
            @RequestBody VoteBody body
    ){
        return ResponseEntity.ok(roomService.registerVote(code, voterId, body.getVotedId()));
    }

    @PostMapping("/{code}/round/close")
    public ResponseEntity<?> closeRound(@PathVariable String code, @RequestParam java.util.UUID hostPlayerId){
        return ResponseEntity.ok(roomService.closeRound(code, hostPlayerId));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}
