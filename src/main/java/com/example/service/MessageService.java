package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(String messageText, Integer postedBy, Long timePostedEpoch) {
        if (messageText == null || messageText.isEmpty() || messageText.length() > 255) {
            return null;
        }
    
        if (!this.accountRepository.existsById(postedBy)) {
            return null;
        }
    
        Message newMessage = new Message();
        newMessage.setMessageText(messageText);
        newMessage.setPostedBy(postedBy);
        newMessage.setTimePostedEpoch(timePostedEpoch);

        return this.messageRepository.save(newMessage);
    }

    public List<Message> getAllMessages() {
        return this.messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        return this.messageRepository.findById(messageId).orElse(null);
    }

    public int deleteMessageById(Integer messageId) {
        if (this.messageRepository.findById(messageId).isPresent()) {
            this.messageRepository.deleteById(messageId);
            return 1;
        }

        return 0;
    }

    public int updateMessageText(Integer messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return 0;
        }
    
        Optional<Message> messageOptional = this.messageRepository.findById(messageId);

        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setMessageText(newMessageText);
            this.messageRepository.save(message);
            return 1;
        }
    
        return 0;
    }

    public List<Message> getMessagesByUserId(Integer accountId) {
        return this.messageRepository.findByPostedBy(accountId);
    }
}
