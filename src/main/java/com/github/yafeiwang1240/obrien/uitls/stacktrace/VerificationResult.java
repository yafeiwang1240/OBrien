package com.github.yafeiwang1240.obrien.uitls.stacktrace;

import java.util.List;

/**
 * 校验结果
 */
public class VerificationResult {
    private Status status;
    private List<String> messages;

    public enum Status {
        SUCCEED,
        FAILED,
    }

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
}
