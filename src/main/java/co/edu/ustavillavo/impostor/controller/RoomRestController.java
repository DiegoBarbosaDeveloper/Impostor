package co.edu.ustavillavo.impostor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ustavillavo.impostor.service.assignment.AssignmentService;
import co.edu.ustavillavo.impostor.service.player.PlayerService;
import co.edu.ustavillavo.impostor.service.room.RoomService;
import co.edu.ustavillavo.impostor.service.vote.VoteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomRestController {
    
    private final RoomService roomService;
    private final PlayerService playerService;
    private final VoteService voteService;
    private final AssignmentService assignmentService;


    @GetMapping
    public ResponseEntity<?> nose(@RequestParam String me){

        return ResponseEntity.ok("Hola " + me);


    }


}
