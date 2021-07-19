package rs.prefabs.general.utils;

public class SecurityRunner<T> {
    private SecureRuuner<T> secureRuuner;
    private SecureRuuner<T> excpExecuter;
    private final T defaultValue;

    public SecurityRunner(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public SecurityRunner<T> setActions(SecureRuuner<T> secureRuuner) {
        this.secureRuuner = secureRuuner;
        return this;
    }

    public SecurityRunner<T> setExcpActions(SecureRuuner<T> excpExecuter) {
        this.excpExecuter = excpExecuter;
        return this;
    }

    public final T execute() {
        try {
            if (secureRuuner == null) return defaultValue;
            return secureRuuner.execute();
        } catch (Exception e) {
            Debugger.Log(SecurityRunner.class, "IF YOUR GAME CONTINUES, IGNORE THIS.");
            e.printStackTrace();
            if (excpExecuter == null) {
                Debugger.Log(SecurityRunner.class, "Exceptions caught without handlers, returning default value = " + defaultValue);
                return defaultValue;
            }
            return excpExecuter.execute();
        }
    }

    public interface SecureRuuner<T> {
        T run();

        default T execute() {
            return run();
        }
    }
}