package com.atlan.functional;

import com.atlan.Atlan;

public class AtlanLiveTest {

    public static final String ANNOUNCEMENT_TITLE = "Automated testing.";
    public static final String ANNOUNCEMENT_MESSAGE = "Automated testing of the Java client.";

    static {
        Atlan.apiKey = System.getenv("ATLAN_API_KEY");
        Atlan.setApiBase(System.getenv("ATLAN_BASE_URL"));
    }
}
