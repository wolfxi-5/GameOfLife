import java.util.Random;

public class GameMap {
    private int maxLength;
    private int maxWidth;
    private int generation;

    //一个数据代表一个细胞,0代表死，1代表活
    private int[][] map;

    public GameMap(int length, int width) {
        this.maxLength = length;
        this.maxWidth = width;
        generation = 0;
        map = new int[length + 2][width + 2];
        for (int i = 0; i <= length + 1; i++) {
            for (int j = 0; j <= width + 1; j++) {
                map[i][j] = 0;
            }
        }
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public int[][] getMap() {
        return map;
    }

    public void setGeneration(int generation){
        this.generation = generation;
    }

    public int getGeneration(){
        return generation;
    }

    //随机初始化地图
    public void resetMap(){
//        for (int i = 1; i <= maxLength; i++)
//            for (int j = 1; j <= maxWidth; j++)
//                map[i][j] = Math.random()>0.7?1:0;

        //设置伪随机（种子）
        Random r = new Random(1);
        for (int i = 1; i <= maxLength; i++)
            for (int j = 1; j <= maxWidth; j++)
                if(r.nextInt(10)>7){
                    map[i][j] = 1;
                }
    }

    //地图清零
    public void clearMap(){
        for (int i = 1; i <= maxLength; i++)
            for (int j = 1; j <= maxWidth; j++)
                map[i][j] = 0;
    }

    //获取细胞的邻居数量
    public int get_neighbor_count(int m, int n) {
        int count = 0;
        for (int i = m - 1; i <= m + 1; i++) {
            for (int j = n - 1; j <= n + 1; j++) {
                if (i == m && j == n) continue;
                count += map[i][j]; //如果邻居还活着，count+1
            }
        }
        return count;
    }

    //繁衍
    public void repoduce() {
        int[][] newGrid = new int[maxLength][maxWidth];
        for (int i = 0; i < maxLength; i++)
            for (int j = 0; j < maxWidth; j++)
                switch (get_neighbor_count(i+1, j+1)) {
                    case 2:
                        newGrid[i][j] = map[i+1][j+1]; //细胞状态保持不变
                        break;
                    case 3:
                        newGrid[i][j] = 1; // Cell is alive.
                        break;
                    default:
                        newGrid[i][j] = 0; // Cell is dead.
                }
        for (int i = 0; i < maxLength; i++) {
            for (int j = 0; j < maxWidth; j++) {
                map[i+1][j+1] = newGrid[i][j];
            }
        }
        generation++;
    }


}
