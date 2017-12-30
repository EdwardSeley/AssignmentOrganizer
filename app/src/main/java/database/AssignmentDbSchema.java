package database;

public class AssignmentDbSchema {

    public static class AssignmentTable {
        public static final String NAME = "assignments";

        public static final class Cols {

            public static final String UUID = "uuid";
            public static final String SUBJECT = "subject";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String COMPLETED = "completed";
        }
    }
}
