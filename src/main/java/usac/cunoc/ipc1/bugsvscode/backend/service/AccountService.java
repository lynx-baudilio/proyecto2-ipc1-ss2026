package usac.cunoc.ipc1.bugsvscode.backend.service;

import java.io.IOException;
import usac.cunoc.ipc1.bugsvscode.backend.models.Account;
import usac.cunoc.ipc1.bugsvscode.backend.persistence.AccountRepository;

public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(String nickname) throws IOException {
        validateNickname(nickname);

        if (repository.exists(nickname)) {
            throw new IllegalArgumentException("Ese nickname ya está registrado.");
        }

        Account account = new Account(nickname);
        repository.save(account);
        return account;
    }

    public String[] listAccounts() throws IOException {
        return repository.findNicknames();
    }

    public Account selectAccount(String nickname) throws IOException {
        if (!repository.exists(nickname)) {
            throw new IOException("La cuenta seleccionada ya no existe.");
        }
        return repository.load(nickname);
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.length() < 1 || nickname.length() > 12) {
            throw new IllegalArgumentException(
                    "El nickname debe tener entre 1 y 12 caracteres."
            );
        }

        for (int index = 0; index < nickname.length(); index++) {
            char character = nickname.charAt(index);
            boolean letter = character >= 'A' && character <= 'Z'
                    || character >= 'a' && character <= 'z';
            boolean digit = character >= '0' && character <= '9';

            if (!letter && !digit) {
                throw new IllegalArgumentException(
                        "Usá únicamente letras sin acentos y números (sin espacios ni símbolos)."
                );
            }
        }
    }
}