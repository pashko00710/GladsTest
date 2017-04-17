package me.uptop.gladstest.data.network.error;

public class NetworkAvailableError extends Throwable {
    public NetworkAvailableError() {
        super("Интернет недоступен, попробуйте позже");
    }
}
