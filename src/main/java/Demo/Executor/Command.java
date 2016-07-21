package Demo.Executor;

/**
 * Created by antong on 16/7/12.
 */
public abstract class Command {
    private String type;

    public Command(String type) {
        this.type = type;
    }

    public Command() {
    }

    public abstract void run() throws InterruptedException;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}