package com.github.yafeiwang1240.obrien.validation.model;

import java.util.List;

/**
 * 校验结果
 */
public class ValidateResult {

    public enum Status {
        SUCCESS,
        FAILED,
    }

    private Status status;
    private List<String> messages;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\"status\": \"").append(status)
                .append("\", \"message\": [");
        if (messages != null && messages.size() > 0) {
            for (int i = 0; i < messages.size(); i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append("\"").append(messages.get(i)).append("\"");
            }
        }
        builder.append("] }");
        return builder.toString();
    }
}
