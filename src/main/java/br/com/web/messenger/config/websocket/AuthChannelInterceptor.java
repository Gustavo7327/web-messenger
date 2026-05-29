package br.com.web.messenger.config.websocket;

import java.util.Optional;

import br.com.web.messenger.service.UserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import br.com.web.messenger.entity.User;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    public AuthChannelInterceptor(JwtDecoder jwtDecoder, UserService userService) {
        this.jwtDecoder = jwtDecoder;
        this.userService = userService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String tokenValue = authHeader.substring(7);

                try {
                    Jwt jwt = jwtDecoder.decode(tokenValue);

                    String email = jwt.getSubject();

                    Optional<User> userOpt = userService.findByEmail(email);

                    if (userOpt.isEmpty()) {
                        throw new IllegalArgumentException("Usuário não encontrado.");
                    }

                    User user = userOpt.get();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(email, user.getId(), null);

                    accessor.setUser(authentication);

                } catch (Exception e) {
                    throw new IllegalArgumentException("Acesso negado: Token inválido ou expirado.");
                }
            } else {
                throw new IllegalArgumentException("Acesso negado: Header de Authorization ausente ou mal formatado.");
            }
        }
        return message;
    }
}