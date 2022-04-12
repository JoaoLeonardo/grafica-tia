package com.gdx.tia.enums;

public enum AgentSpeech {
    ROTT("Eat lead!"),
    MGS("You're pretty good..."),
    GTASA("Damn, here we go again..."),
    DD("How quickly the tide turns!"),
    DD2("Dazed. Reeling. About to break...");

    private String description;

    AgentSpeech(String desc) { description = desc; }

    public static String getSpeech(int score, int health, boolean hasSpoken, boolean randomBool) {
        if (score == 0) {
            return (!hasSpoken && randomBool) ? GTASA.description : null;
        } else if (score % 200 == 0) {
            if (health == 1) return  DD.description;
            return randomBool ? MGS.description : ROTT.description;
        } else if (score % 100 == 0) {
            if (health == 1) return DD2.description;
            return randomBool ? ROTT.description : MGS.description;
        }
        return null;
    }

}
