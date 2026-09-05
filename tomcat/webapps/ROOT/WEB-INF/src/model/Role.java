package model;

// Rôle donnant droit ou non à différentes opérations (surtout de modification) sur l'API
// Ordre croissant de privilèges : UNKNOWN < USER < ADMIN
public enum Role {
    UNKNOWN,
    USER,
    ADMIN;

    public boolean atLeast(Role required) {
        return this.ordinal() >= required.ordinal();
    }
}
