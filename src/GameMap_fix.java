import java.util.Random;

public class GameMap_fix {
    private int maxLength;
    private int maxWidth;
    private int generation;
    //一个数据代表一个细胞,false代表死，true代表活
    private boolean[][] map;

    public GameMap_fix(int length, int width) {
        this.maxLength = length;
        this.maxWidth = width;
        generation = 0;
        map = new boolean[length + 2][width + 2];
        for (int i = 0; i <= length + 1; i++) {
            for (int j = 0; j <= width + 1; j++) {
                map[i][j] = false;
            }
        }
    }

    public void setMap(boolean[][] map) {
        this.map = map;
    }

    public boolean[][] getMap() {
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
//                map[i][j] = Math.random()>0.7?true:false;

        //设置伪随机（种子）
        Random r = new Random(1);
        for (int i = 1; i <= maxLength; i++)
            for (int j = 1; j <= maxWidth; j++)
                if(r.nextInt(10)>7){
                    map[i][j] = true;
                }
    }

    //地图清零
    public void clearMap(){
        for (int i = 1; i <= maxLength; i++)
            for (int j = 1; j <= maxWidth; j++)
                map[i][j] = false;
    }

    //获取细胞的邻居数量
    public int get_neighbor_count(int m, int n) {
        int count = 0;
        for (int i = m - 1; i <= m + 1; i++) {
            for (int j = n - 1; j <= n + 1; j++) {
                if (map[i][j])  count += 1; //如果邻居还活着，count+1
            }
        }

        if (map[m][n])  count -= 1;
        return count;
    }



    //繁衍
    public void repoduce() {
        boolean[][] newGrid = new boolean[maxLength + 2][maxWidth + 2];
        for (int i = 1; i <= maxLength; i++)
            for (int j = 1; j <= maxWidth; j++)
                switch (get_neighbor_count(i, j)) {
                    case 2:
                        newGrid[i][j] = map[i][j]; //细胞状态保持不变
                        break;
                    case 3:
                        newGrid[i][j] = true; // Cell is alive.
                        break;
                    default:
                        newGrid[i][j] = false; // Cell is dead.
                }
        setMap(newGrid);
        generation++;
    }


}
