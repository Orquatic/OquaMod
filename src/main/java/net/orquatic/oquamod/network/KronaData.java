package net.orquatic.oquamod.network;

public class KronaData implements IKronaData {
    private int kronaCount;

    @Override
    public int getKronaCount() {
        return kronaCount;
    }

    @Override
    public void setKronaCount(int kronaCount) {
        this.kronaCount = kronaCount;
    }
}
