package unibo.appl1.common;
public interface IRoomModel {
    int getDimX();
    int getDimY();
    void put(int x, int y, IBox box);
}
