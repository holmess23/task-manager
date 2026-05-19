package tasks.manager.model.task;

public enum State{
    TODAS{
        public boolean apply(Task t) {
            return true;
        }
    },
    COMPLETAS{
        public boolean apply(Task t) {
            return t.isCompleted();
        }
    },
    INCOMPLETAS{
        public boolean apply(Task t) {
            return !t.isCompleted();
        }
    };

    public abstract boolean apply(Task t);
}
