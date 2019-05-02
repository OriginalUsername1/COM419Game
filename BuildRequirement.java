public class BuildRequirement {

    private int wood;
    private int stone;
    private int peasants;
    private int silver;
    private int gold;

    public BuildRequirement (int wood, int stone, int peasants, int silver, int gold) {
        this.wood = wood;
        this.stone = stone;
        this.peasants = peasants;
        this.silver = silver;
        this.gold = gold;
    }

    public int getWood () {
        return wood;
    }
    public int getStone () {
        return stone;
    }
    public int getPeasants () {
        return peasants;
    }
    public int getSilver () {
        return silver;
    }
    public int getGold () {
        return gold;
    }

}
