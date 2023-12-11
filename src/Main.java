import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

abstract class QueueOperation {
    public abstract void execute();
}

class DisplayAndReceiveCommand extends QueueOperation {
    // được cài đặt bởi các lớp con của extends.
    private MessagingSystem messagingSystem;

    public DisplayAndReceiveCommand(MessagingSystem messagingSystem) {
        this.messagingSystem = messagingSystem;
    }

    @Override
    public void execute() {
        messagingSystem.displayAndReceive();
    }
}

class SendMessageCommand extends QueueOperation {
    private MessagingSystem messagingSystem;
    private String message;

    public SendMessageCommand(MessagingSystem messagingSystem, String message) {
        this.messagingSystem = messagingSystem;
        this.message = message;
    }

    @Override
    public void execute() {
        messagingSystem.enqueue(message);
    }
}

class CustomQueue<T> {
    // Lớp CustomQueue<T>:
    //Đại diện cho một cấu trúc hàng đợi chung sử dụng một danh sách
    //(ArrayList) để lưu trữ các phần tử.
    //Cung cấp các phương thức để thêm vào hàng đợi,
    // lấy ra khỏi hàng đợi, kiểm tra xem hàng đợi có trống không, và lấy danh sách các phần tử.
    private List<T> elements = new ArrayList<>();

    public void enqueue(T element) {
        elements.add(element);
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        return elements.remove(0);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public List<T> getElements() {
        return elements;
    }
}

class CustomStack<T> {
    //Đại diện cho một cấu trúc ngăn xếp chung sử dụng một danh sách
    // (ArrayList) để lưu trữ các phần tử.
    //Cung cấp các phương thức để đẩy vào ngăn xếp, lấy ra khỏi ngăn xếp,
    // kiểm tra xem ngăn xếp có trống không, và lấy danh sách các phần tử.
    private List<T> elements = new ArrayList<>();

    public void push(T element) {
        elements.add(element);
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }
        int lastIndex = elements.size() - 1;
        T poppedElement = elements.get(lastIndex);
        elements.remove(lastIndex);
        return poppedElement;
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public List<T> getElements() {
        return elements;
    }
}

class MessagingSystem {
    //Lớp MessagingSystem:
    //Bao gồm một hàng đợi tin nhắn và một ngăn xếp tin nhắn.
    private static final int MAX_QUEUE_SIZE = 5;
    private CustomQueue<String> messageQueue = new CustomQueue<>();
    private CustomStack<String> messageStack = new CustomStack<>();

    public void enqueue(String message) {
        //Phương thức enqueue(String message) thêm một tin nhắn vào hàng đợi,
        // kiểm tra độ dài tin nhắn và giới hạn kích thước ngăn xếp.
        if (message.length() > 250) {
            System.out.println("Error: Message limit exceeded.");
            return;
        }

        if (!messageStack.isEmpty() && messageStack.getElements().size() >= MAX_QUEUE_SIZE) {
            System.out.println("Error: Stack is full. Cannot enqueue message.");
            return;
        }

        messageQueue.enqueue(message);
    }

    public void displayAndReceive() {
        //Phương thức displayAndReceive() lấy ra một tin nhắn từ hàng đợi,
        // đẩy nó lên ngăn xếp và in ra nội dung của hàng đợi và ngăn xếp.
        if (messageQueue.isEmpty()) {
            System.out.println("Error: Queue is empty.");
            return;
        }

        String dequeuedMessage = messageQueue.dequeue();
        System.out.println("Popped message: " + dequeuedMessage);
        messageStack.push(dequeuedMessage);
        System.out.println("Queue: " + messageQueue.getElements());
        System.out.println("Stack: " + messageStack.getElements());
    }
}

public class Main {
    public static void main(String[] args) {
        MessagingSystem messagingSystem = new MessagingSystem();
        Scanner scanner = new Scanner(System.in);
        String userInput;

        try {
            while (true) {
                System.out.println("Choose an option: \n" +
                        "1. Send a message \n" +
                        "2. Display and Receive messages \n" +
                        "3. Exit");
                userInput = scanner.nextLine();

                switch (userInput) {
                    case "1":
                        System.out.println("Enter the message (type 'exit' to stop): ");
                        String message = scanner.nextLine();
                        while (!message.equals("exit")) {
                            new SendMessageCommand(messagingSystem, message).execute();
                            message = scanner.nextLine();
                        }
                        break;
                    case "2":
                        new DisplayAndReceiveCommand(messagingSystem).execute();
                        break;
                    case "3":
                        return; // exit the program
                    default:
                        System.out.println("Invalid input. Please try again.");
                }
            }
        } finally {
            scanner.close();
        }
    }
}