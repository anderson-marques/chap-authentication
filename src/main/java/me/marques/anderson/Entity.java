package me.marques.anderson;

public interface Entity {
    String username();
    Entity username(String username);
    String passwordHashSalted();
    Entity passwordHashSalted(String password);
    String challenge();
    Entity challenge(String challenge);
}
