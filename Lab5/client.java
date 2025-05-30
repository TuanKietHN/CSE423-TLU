import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.Random;

public class client {
    
    private NameService nameService;
    private Scanner scanner;

    public client() {
        scanner = new Scanner(System.in);
    }

    public boolean connectToServer(String host, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);

            nameService = (NameService) registry.lookup("NameService");

            String response = nameService.checkConnection();
            System.out.println("Ket noi thanh cong! " + response);
            return true;
            
        } catch (Exception e) {
            System.err.println("Loi ket noi server: " + e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public void showMenu() {
        System.out.println("\n=== CHUONG TRINH SAP XEP MANG RMI ===");
        System.out.println("1. Nhap mang tu ban phim");
        System.out.println("2. Tao mang ngau nhien");
        System.out.println("3. Thoat chuong trinh");
        System.out.print("Chon chuc nang (1-3): ");
    }

    public int[] inputArrayFromKeyboard() {
        System.out.print("Nhap so luong phan tu: ");
        int n = scanner.nextInt();
        
        int[] arr = new int[n];
        System.out.println("Nhap " + n + " so nguyen duong:");
        
        for (int i = 0; i < n; i++) {
            System.out.print("Phan tu thu " + (i + 1) + ": ");
            arr[i] = Math.abs(scanner.nextInt()); // Dam bao so duong
        }
        
        return arr;
    }

    public int[] generateRandomArray() {
        System.out.print("Nhap so luong phan tu: ");
        int n = scanner.nextInt();
        
        int[] arr = new int[n];
        Random random = new Random();
        
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(100) + 1; // So tu 1 den 100
        }
        
        return arr;
    }

    public void printArray(int[] arr, String title) {
        System.out.print("\n" + title + ": ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    public void callRemoteSortMethod(int[] arr) {
        try {
            System.out.println("\nDang gui yeu cau den server...");

            int[] sortedArray = nameService.SapXep(arr);

            printArray(arr, "Mang ban dau");
            printArray(sortedArray, "Mang sau khi sap xep (chan-le dan xen)");
            
        } catch (Exception e) {
            System.err.println("Loi khi goi phuong thuc tu xa: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void run() {
        System.out.println("=== RMI CLIENT - CHUONG TRINH SAP XEP MANG ===");

        if (!connectToServer("localhost", 8080)) {
            System.out.println("Khong the ket noi den server. Vui long kiem tra lai!");
            return;
        }

        int choice;
        do {
            showMenu();
            choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    int[] arr1 = inputArrayFromKeyboard();
                    callRemoteSortMethod(arr1);
                    break;
                    
                case 2:
                    int[] arr2 = generateRandomArray();
                    callRemoteSortMethod(arr2);
                    break;
                    
                case 3:
                    System.out.println("Cam on ban da su dung chuong trinh!");
                    break;
                    
                default:
                    System.out.println("Lua chon khong hop le. Vui long chon lai!");
                    break;
            }
            
        } while (choice != 3);
        
        scanner.close();
    }

    public static void main(String[] args) {
        client client = new client();
        client.run();
    }
}