import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SimpletronV2 {
    Scanner input = new Scanner(System.in);
    private int accumulator;
    private int instructionCounter;
    private int instructions;
    private int indexRegister;
    private int operationCode;
    private int operand;
    private int[] memory;

    SimpletronV2() {
        this.accumulator = 0;
        this.instructionCounter = 0;
        this.indexRegister = 0;
        this.operationCode = 0;
        this.operand = 0;
        this.memory = new int[10000];
    }

    void loadProgram(int instruction) {
        memory[instructionCounter] = instruction;
        instructions = ++instructionCounter;
    }

    void executeProgram() throws FileNotFoundException {
        instructionCounter = 0;
        while (instructionCounter < instructions) {
            operationCode = memory[instructionCounter] / 10000;
            operand = memory[instructionCounter] % 10000;
            executeOperation(operationCode, operand);
            instructionCounter++;
        }
    }

    public void executeOperation(int operationCode, int operand) throws FileNotFoundException {
        switch (operationCode) {
            case 10:
                read(operand);
                break;
            case 11:
                write(operand);
                break;
            case 20:
                load(operand);
                break;
            case 21:
                loadImmediate(operand);
                break;
            case 22:
                loadX(operand);
                break;
            case 25:
                store(operand);
                break;
            case 30:
                add(operand);
                break;
            case 31:
                addX();
                break;
            case 32:
                subtract(operand);
                break;
            case 33:
                subtractX();
                break;
            case 34:
                divide(operand);
                break;
            case 35:
                divideX();
                break;
            case 36:
                multiply(operand);
                break;
            case 37:
                multiplyX();
                break;
            case 40:
                instructionCounter = operand;
                break;
            case 41:
                if (accumulator < 0)
                    instructionCounter = operand;
                break;
            case 42:
                if (accumulator == 0)
                    instructionCounter = operand;
                break;
            case 43:
                swap();
                break;
            case 45:
                halt(operand);
                return;
            default:
                throw new UnsupportedOperationException("Operation not supported: " + operationCode);
        }
    }

    public void read(int address) {
        System.out.print("Enter an integer: ");
        Scanner scanner = new Scanner(System.in);
        memory[address] = scanner.nextInt();
    }

    public void write(int address) {
        System.out.printf("Memory[%d] = %d%n", address, memory[address]);
    }

    public void load(int address) {
        accumulator = memory[address];
    }

    public void loadImmediate(int value) {
        accumulator = value;
    }

    public void loadX(int address) {
        indexRegister = memory[address];
    }

    public void store(int address) {
        memory[address] = accumulator;
    }

    public void add(int address) {
        accumulator += memory[address];
    }

    public void addX() {
        accumulator += memory[indexRegister];
    }

    public void subtract(int address) {
        accumulator -= memory[address];
    }

    public void subtractX() {
        accumulator -= memory[indexRegister];
    }

    public void divide(int address) {
        if (memory[address] == 0) {
            System.out.println("Error: Division by zero");
            halt(0);
            return;
        }
        accumulator /= memory[address];
    }

    public void divideX() {
        if (memory[indexRegister] == 0) {
            System.out.println("Error: Division by zero");
            halt(0);
            return;
        }
        accumulator /= memory[indexRegister];
    }

    public void multiply(int address) {
        accumulator *= memory[address];
    }

    public void multiplyX() {
        accumulator *= memory[indexRegister];
    }

    public void swap() {
        int temp = accumulator;
        accumulator = indexRegister;
        indexRegister = temp;
    }

    public void halt(int pageRange) {
        System.out.println("*** Program halted ***");
        dumpCore(pageRange);
    }

    void dumpCore(int pages) {
        int pageOne = pages / 100;
        int pageTwo = pages % 100;

        for (int i = pageOne; i <= pageTwo; i++) {
            System.out.printf("Page # %02d \n", i);
            System.out.println("\nREGISTERS: \n");
            System.out.printf("Accumulator: %06d \n", accumulator);
            System.out.printf("InstructionCounter: %06d \n", instructionCounter);
            System.out.printf("IndexRegister: %06d \n", indexRegister);
            System.out.printf("OperationCode: %02d \n", operationCode);
            System.out.printf("Operand: %04d \n", operand);
            System.out.println();

            for (int j = 0; j < 10; j++) {
                int start = i * 100 + j * 10;
                for (int k = 0; k < 10; k++) {
                    System.out.printf("%06d ", memory[start + k]);
                }
                System.out.println();
            }
        }
    }
}

class Main {
    public static void main(String[] args) throws FileNotFoundException {
        SimpletronV2 init = new SimpletronV2();
        Scanner instructions = new Scanner(new File("GCD.sml"));

        while (instructions.hasNextInt()) {
            init.loadProgram(instructions.nextInt());
        }

        init.executeProgram();
        init.dumpCore(0);
    }
}
