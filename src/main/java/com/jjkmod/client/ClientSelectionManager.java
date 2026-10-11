package com.jjkmod.client;

public final class ClientSelectionManager {
    private static String selectedSorcererId = "gojo";

    private ClientSelectionManager() {
    }

    public static String getSelectedSorcererId() {
        return selectedSorcererId;
    }

    public static void setSelectedSorcererId(String selectedSorcererId) {
        ClientSelectionManager.selectedSorcererId = selectedSorcererId;
    }
}
