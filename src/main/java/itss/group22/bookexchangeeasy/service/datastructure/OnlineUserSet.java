package itss.group22.bookexchangeeasy.service.datastructure;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class OnlineUserSet extends HashMap<Long, UserContext> {
    public void removeUserBySessionId(String sessionId) {
        var entry = this.entrySet().stream().filter(e -> e.getValue().getSessionId().equals(sessionId)).findFirst();
        entry.ifPresent(e -> this.remove(e.getKey()));
    }
}
