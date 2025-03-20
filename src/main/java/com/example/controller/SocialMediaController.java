package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) {
        // handles conflicting account case
        if (accountService.login(account.getUsername(), account.getPassword()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
        }

        Account registeredAccount = accountService.register(account.getUsername(), account.getPassword());

        // handles bad input case
        if (registeredAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(registeredAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account account) {
        Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());

        // handles unauthorized case
        if (loggedInAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(loggedInAccount);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message newMessage = messageService.createMessage(message.getMessageText(), message.getPostedBy(), message.getTimePostedEpoch());

        // handles bad message case
        if (newMessage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(newMessage); 
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();

        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);

        // handles non-existent message case
        if (message == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        int rowsDeleted = messageService.deleteMessageById(messageId);

        // message existed and then deleted case
        if (rowsDeleted == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(rowsDeleted);
        }

        // message did not exist
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageText(@PathVariable Integer messageId, @RequestBody Map<String, String> requestBody) {
        String newMessageText = requestBody.get("messageText");

        int rowsUpdated = messageService.updateMessageText(messageId, newMessageText);

        // Successfully updated message case
        if (rowsUpdated == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(rowsUpdated);
        }

        // Message didn't exist or the update failed
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUserId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUserId(accountId);

        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }






}
