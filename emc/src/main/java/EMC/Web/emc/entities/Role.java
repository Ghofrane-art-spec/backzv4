package EMC.Web.emc.entities;

public enum Role {
	Client,
	Agence,
	ServiceEncaissement,
	Administrateur;
	
	public static Role fromString(String value) {
        if (value != null) {
            for (Role role : Role.values()) {
                if (value.equalsIgnoreCase(role.name())) {
                    return role;
                }
            }
        }
        return null;
    }

}
