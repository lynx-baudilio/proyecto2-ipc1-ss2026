package usac.cunoc.ipc1.bugsvscode.backend.models;

public class Account {

    private final String nickname;

    public Account(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}