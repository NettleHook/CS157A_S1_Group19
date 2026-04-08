package app.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Ingredient(
    String name,
    Double amount,
    String unit
) {}