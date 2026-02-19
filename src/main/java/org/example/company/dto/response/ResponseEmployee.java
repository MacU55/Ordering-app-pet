package org.example.company.dto.response;

import java.util.UUID;

public record ResponseEmployee(
    UUID uuid,
    String name,
    String email
) {

}
