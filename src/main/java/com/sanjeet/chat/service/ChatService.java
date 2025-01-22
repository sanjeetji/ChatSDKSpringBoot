package com.sanjeet.chat.service;


import com.sanjeet.chat.model.dto.MessageDetailsResponse;
import com.sanjeet.chat.model.entity.MessageDetailsEntity;
import com.sanjeet.chat.repository.ChatRepository;
import com.sanjeet.chat.repository.ClientRepository;
import com.sanjeet.chat.utils.core.globalExceptionHandller.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {


    private final ChatRepository chatRepository;
    private final ClientRepository clientRepository;
    @Autowired
    public ChatService(ChatRepository chatRepository
            ,ClientRepository clientRepository){
        this.chatRepository = chatRepository;
        this.clientRepository = clientRepository;
    }

    public MessageDetailsEntity postChat(MessageDetailsEntity request, String token, String apiKey) throws Exception {
        try {
            String secretKey = clientRepository.getSecretKey(apiKey);
            System.out.println("Post Message Request " + request + " token : "+token + " Api Key : "+apiKey +
                    "  Secret key is : "+secretKey);
            if (secretKey == null){
                throw new AccessDeniedException("You are not authorized to access this resource");
            }
            request.setSecretKey(secretKey);
            System.out.println("Message saved success...."+request);
            return chatRepository.save(request);
        }catch (Exception e){
            String message = "Failed to post chat message" + e;
            throw new Exception(message);
        }
    }

    public List<MessageDetailsResponse> fetchChat(String token, String receiverPhone, String apiKey) throws Exception {
        try {
            String secretKey = clientRepository.getSecretKey(apiKey);
            System.out.println("Fetch Message request : " + " Token value is "+token  + "  Api Key is : "+apiKey + "  Secret Key : "+secretKey);
            if (secretKey == null){
                throw new AccessDeniedException("You are not authorized to access this resource");
            }
            System.out.println("Fetching all messages from the database...");
            List<MessageDetailsEntity> messageDetailsEntityList = chatRepository.findAllByPhoneNumber(receiverPhone);
            System.out.println("Fetching all messages from the database...1111" + messageDetailsEntityList);
            return messageDetailsEntityList
                    .stream()
                    .map(entity -> {
                        System.out.println("Processing entity: " + entity);
                        // Handle potential null values in the database row
                        String message = entity.getMessage() != null ? entity.getMessage() : "No Message";
                        return new MessageDetailsResponse(
                                entity.getId(),
                                message
                        );
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching messages: " + e.getMessage());
            throw new Exception("Failed to fetch messages", e);
        }
    }


}
