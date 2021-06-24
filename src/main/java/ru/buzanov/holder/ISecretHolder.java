package ru.buzanov.holder;

public interface ISecretHolder {
    String generateSecret();

    boolean checkSecret(String secret);

}
