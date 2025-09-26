package com.superComics.inventory.comicsOnSite;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.Set;

@Embeddable
public class grading {

    //GemMint (GM), NearMintMint (NMM), NearMint (NM)
    //VeryFine (VF), Fine (FN), VeryGood(VG),
    //Good(GD) y FairRegular(FR)
    private static final Set<String> VALID_GRADING_CODES = Set.of(
            "GM", "NMM", "NM", "VF", "FN", "VG", "GD", "FR");

    private String code;

    protected grading(){

    }

    public grading(String code) {

        // Validación básica de vacío
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de grading no puede ser nulo o vacío.");
        }

        String normalizedCode = code.toUpperCase().trim();


        // Verifica si el código proporcionado existe en la lista de códigos válidos.

        if (!VALID_GRADING_CODES.contains(normalizedCode)) {

            // Cumple con la validación de negocio específica del PRD
            throw new IllegalArgumentException(
                    "Formato de grading no válido. El código '" + code +
                            "' no es uno de los códigos de grading permitidos (GM, NMM, NM, VF, FN, VG, GD, FR)."
            );
        }

        this.code = normalizedCode;
    }

    // Getter
    public String getCode() {
        return code;
    }

    // **IMPORTANTE:** Sobreescribir equals y hashCode para un Value Object.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        grading grading = (grading) o;
        return Objects.equals(code, grading.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
