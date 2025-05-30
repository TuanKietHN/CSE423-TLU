import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.rmi.Remote;
import java.rmi.RemoteException;
interface NameService extends Remote {

    int[] SapXep(int[] arr) throws RemoteException;

    String checkConnection() throws RemoteException;
}
public class server extends UnicastRemoteObject implements NameService {
    
    private static final long serialVersionUID = 1L;

    public server() throws RemoteException {
        super();
    }

    @Override
    public int[] SapXep(int[] arr) throws RemoteException {
        System.out.println("Server: Nhan yeu cau sap xep mang tu client.");
        if (arr == null || arr.length == 0) {
            return arr;
        }

        ArrayList<Integer> evenNumbers = new ArrayList<>();
        ArrayList<Integer> oddNumbers = new ArrayList<>();
        
        for (int num : arr) {
            if (num % 2 == 0) {
                evenNumbers.add(num);
            } else {
                oddNumbers.add(num);
            }
        }

        Collections.sort(evenNumbers);
        Collections.sort(oddNumbers);

        int[] result = new int[arr.length];
        int evenIndex = 0, oddIndex = 0, resultIndex = 0;
        boolean useEven = true; 
        
        while (evenIndex < evenNumbers.size() || oddIndex < oddNumbers.size()) {
            if (useEven && evenIndex < evenNumbers.size()) {
                result[resultIndex++] = evenNumbers.get(evenIndex++);
                useEven = false;
            } else if (!useEven && oddIndex < oddNumbers.size()) {
                result[resultIndex++] = oddNumbers.get(oddIndex++);
                useEven = true;
            } else if (evenIndex < evenNumbers.size()) {
                result[resultIndex++] = evenNumbers.get(evenIndex++);
            } else if (oddIndex < oddNumbers.size()) {
                result[resultIndex++] = oddNumbers.get(oddIndex++);
            }
        }
        
        System.out.println("Server: Da xu ly mang co " + arr.length + " phan tu");
        return result;
    }

    @Override
    public String checkConnection() throws RemoteException {
        System.out.println("Server: Nhan yeu cau kiem tra ket noi tu client.");
        return "Server dang hoat dong binh thuong!";
    }

    public static void main(String[] args) {
        try {
            server server = new server();

            Registry registry = LocateRegistry.createRegistry(8080);

            registry.rebind("NameService", server);
            
            System.out.println("Server RMI da khoi dong thanh cong!");
            System.out.println("Dang lang nghe tren cong 8080...");

            Thread statusThread = new Thread(() -> {
                while (true) {
                    try {
                        System.out.println("Server: Van dang chay... (Thoi gian: " + 
                            java.time.LocalDateTime.now() + ")");
                        Thread.sleep(10000); 
                    } catch (InterruptedException e) {
                        System.err.println("Loi thread trang thai: " + e.getMessage());
                    }
                }
            });
            statusThread.setDaemon(true); 
            statusThread.start();
            
        } catch (Exception e) {
            System.err.println("Loi khoi dong server: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}