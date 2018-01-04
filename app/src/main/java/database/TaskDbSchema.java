package database;

public class TaskDbSchema {

    public static class TaskTable {
        public static final String NAME = "tasks";

        public static final class Cols {

            public static final String UUID = "uuid";
            public static final String SUBJECT = "subject";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String COMPLETED = "completed";
        }
    }
}
