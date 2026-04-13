//package asm.poly.asm_java6.controler.user;
//
//import asm.poly.asm_java6.dto.MessageDTO;
//import asm.poly.asm_java6.enity.Conversation;
//import asm.poly.asm_java6.enity.Message;
//import asm.poly.asm_java6.repository.UsersRepository;
//import asm.poly.asm_java6.service.ConversationService;
//import asm.poly.asm_java6.service.MessageService;
//import org.apache.catalina.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.RestController;
//import asm.poly.asm_java6.enity.users;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/chat")
//public class ChatConTroller {
//    @Autowired
//    private UsersRepository UsersRepository;
//    @Autowired
//    private ConversationService conversationService;
//
//    @Autowired
//    private MessageService messageService;
//
//    // Gửi tin nhắn (frontend gọi API này)
//    @PostMapping("/send")
//    public ResponseEntity<?> sendMessage(
//            @RequestBody Map<String, Object> payload,
//            Principal principal // Lấy user hiện tại từ Spring Security
//    ) {
//        // Kiểm tra xác thực
//        if (principal == null) {
//            return ResponseEntity.status(401).body(Map.of(
//                    "status", "fail",
//                    "message", "Bạn chưa đăng nhập!"
//            ));
//        }
//
//        // Lấy email từ principal
//        String email = principal.getName();
//        users user = UsersRepository.findByEmail(email); // ĐÚNG
//        if (user == null) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "status", "fail",
//                    "message", "User không hợp lệ!"
//            ));
//        }
//        Long userId = user.getId();
//
//        // Lấy nội dung tin nhắn và conversationId
//        String content = (String) payload.get("message");
//        Long conversationId = payload.get("conversationId") != null
//                ? Long.valueOf(payload.get("conversationId").toString())
//                : null;
//
//        Conversation conversation = null;
//
//        if (conversationId == null) {
//            // Tìm hoặc tạo conversation giữa user và staffId = 16
//            Optional<Conversation> optionalConv = conversationService.findByUserIdAndStaffId(userId, 16L);
//            if (optionalConv.isPresent()) {
//                conversation = optionalConv.get();
//            } else {
//                conversation = conversationService.createConversation(userId, 16L);
//            }
//            conversationId = conversation.getId();
//        } else {
//            Optional<Conversation> optionalConv = conversationService.findById(conversationId);
//            if (optionalConv.isPresent()) {
//                conversation = optionalConv.get();
//            } else {
//                return ResponseEntity.badRequest().body(Map.of(
//                        "status", "fail",
//                        "message", "Không tìm thấy cuộc trò chuyện!"
//                ));
//            }
//        }
//
//        // Lưu tin nhắn
//        Message m = messageService.sendMessage(conversationId, userId, content);
//
//        return ResponseEntity.ok(Map.of(
//                "status", "success",
//                "message", "Đã gửi",
//                "conversationId", conversationId,
//                "data", m
//        ));
//    }
//
//    // Lấy lịch sử tin nhắn của một cuộc trò chuyện
//    @GetMapping("/messages/{conversationId}")
//    public ResponseEntity<?> getMessages(@PathVariable Long conversationId) {
//        List<Message> messages = messageService.getMessagesByConversationId(conversationId);
//
//        List<MessageDTO> dtos = messages.stream().map(m -> {
//            MessageDTO dto = new MessageDTO();
//            dto.setId(m.getId());
//            dto.setSenderId(m.getSenderId());
//            dto.setContent(m.getContent());
//            dto.setCreatedAt(m.getSentAt());
//            // Lấy vai_tro từ user gửi tin nhắn
//            users sender = UsersRepository.findById(m.getSenderId()).orElse(null);
//            dto.setVai_tro(sender != null && Boolean.TRUE.equals(sender.getVaiTro()) ? 1 : 0); // getVaiTro() trả về 1 hoặc 0
//            return dto;
//        }).toList();
//
//        return ResponseEntity.ok(dtos);
//    }
//
//    // Lấy danh sách cuộc trò chuyện của user
//    @GetMapping("/conversations/user/{userId}")
//    public ResponseEntity<?> getConversationsByUser(@PathVariable Long userId) {
//        List<Conversation> conversations = conversationService.getConversationsByUserId(userId);
//        return ResponseEntity.ok(conversations);
//    }
//
//    @GetMapping("/conversations/user/me")
//    public ResponseEntity<?> getMyConversations(Principal principal) {
//        if (principal == null) return ResponseEntity.status(401).build();
//        String email = principal.getName();
//        users user = UsersRepository.findByEmail(email);
//        if (user == null) return ResponseEntity.badRequest().build();
//        List<Conversation> conversations = conversationService.getConversationsByUserId(user.getId());
//        return ResponseEntity.ok(conversations);
//    }
//}