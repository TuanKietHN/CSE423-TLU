package Lab2.Dekker;

public interface Lock {
    void requestCS(int i);
    void releaseCS(int i);
}
