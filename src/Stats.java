public class Stats {
    public int gamesPlayed;
    public int gamesWon;
    public double winPercentage;
    public int fastestTime;

    public Stats(){
        gamesPlayed = 0;
        gamesWon = 0;
        winPercentage = 0.0;
        fastestTime = 999;
    }

    public Stats(int gamesPlayed, int gamesWon, double winPercentage, int fastestTime){
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.winPercentage = winPercentage;
        this.fastestTime = fastestTime;
    }
}
