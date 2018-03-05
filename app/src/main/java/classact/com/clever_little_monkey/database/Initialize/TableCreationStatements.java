package classact.com.clever_little_monkey.database.Initialize;

/**
 * Created by Tseliso on 5/8/2016.
 */
public class TableCreationStatements {
        /*Look up tables*/
        public static final String CREATE_LANGUAGE_TABLE = "create table " +
                "tbl_language ("+
                "id integer primary key autoincrement, "+
                "name text not null);";
        public static final String DROP_LANGUAGE_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_language;";
        public static final String CREATE_COMPREHENSION_QUESTION_TYPE_TABLE = "create table " +
                "tbl_comprehension_question_type ("+
                "id integer primary key autoincrement, "+
                "name text not null);";
        public static final String DROP_COMPREHENSION_QUESTION_TYPE_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_comprehension_question_type;";
        public static final String CREATE_DRILL_TYPE_TABLE = "create table " +
                "tbl_drill_type ("+
                "id integer primary key autoincrement, "+
                "name text not null);";
        public static final String DROP_DRILL_TYPE_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_drill_type;";
        /*Data Tables*/
        public static final String CREATE_PROGRAMME_TABLE = "create table " +
                "tbl_programme ("+
                "id integer primary key autoincrement, "+
                "name text not null);";
        public static final String DROP_PROGRAMME_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_programme;";
        public static final String CREATE_UNIT_TABLE ="create  table " +
                "tbl_unit ("+
                "id integer primary key autoincrement, "+
                "programme_id integer not null, "+
                "name text not null," +
                "opening_story_id integer," +
                "sounds_drill_block_Id integer," +
                "words_drill_block_Id integer," +
                "read_story_Id integer," +
                "comprehension_id integer);";
        public static final String DROP_UNIT_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_unit;";
        public static final String CREATE_DRILL_BLOCK_TABLE ="create  table " +
                "tbl_drill_block ("+
                "id integer primary key autoincrement, "+
                "unitId integer not null," +
                "name text not null);";
        public static final String DROP_DRILL_BLOCK_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_drill_block;";
        public static final String CREATE_DRILL_BLOCK_DRILL_TABLE ="create  table " +
                "tbl_drill_block_drill ("+
                "id integer primary key autoincrement, "+
                "drill_block_id integer," +
                "drill_id integer," +
                "sequence_number integer);";
        public static final String DROP_DRILL_BLOCK_DRILL_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_drill_block;";
        public static final String CREATE_DRILL_TABLE ="create  table " +
                "tbl_drill ("+
                "id integer primary key autoincrement, "+
                "drill_type_id integer, "+
                "intro_sound_uri text, "+
                "drill_letter_id integer, "+
                "drill_word_id integer, "+
                "name text not null);";
        public static final String DROP_DRILL_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_drill;";
        public static final String CREATE_STORY_TABLE ="create  table " +
                "tbl_story ("+
                "id integer primary key autoincrement, "+
                "name text not null);";
        public static final String DROP_STORY_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_story;";
        public static final String CREATE_STORY_SEGMENT_TABLE ="create  table " +
                "tbl_story_segment ("+
                "id integer primary key autoincrement, "+
                "story_id integer , "+
                "sequence_number integer , "+
                "sound_uri text , "+
                "illustration_uri text);";
        public static final String DROP_STORY_SEGMENT_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_story_segment;";
        public static final String CREATE_COMPREHENSION_TABLE ="create  table " +
                "tbl_comprehension ("+
                "id integer primary key autoincrement, "+
                "name text not null);";
        public static final String DROP_COMPREHENSION_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_comprehension;";
        public static final String CREATE_COMPREHENSION_QUESTION_TABLE ="create  table " +
                "tbl_comprehension_question ("+
                "id integer primary key autoincrement, "+
                "name text not null);";
        public static final String DROP_COMPREHENSION_QUESTION_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_comprehension_question;";
        public static final String CREATE_COMPREHENSION_ANSWER_TABLE ="create  table " +
                "tbl_comprehension_answer ("+
                "id integer primary key autoincrement, "+
                "name text not null);";
        public static final String DROP_COMPREHENSION_ANSWER_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_comprehension_answer;";
        public static final String CREATE_WORD_TABLE ="create  table " +
                "tbl_word("+
                "id integer primary key autoincrement, "+
                "name text not null, " +
                "word_picture_uri text, " +
                "word_spell_sound_uri text, " +
                "word_sound_uri text, " +
                "word_level integer);";
        public static final String DROP_WORD_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_word;";
        public static final String CREATE_LETTER_TABLE ="create  table " +
                "tbl_letter("+
                "id integer primary key autoincrement, "+
                "name text not null, "+
                "letter_picture_all_case_uri text, " +
                "letter_picture_lower_case_uri text, " +
                "letter_picture_upper_case_uri text, " +
                "letter_lower_case_writing_uri text, " +
                "letter_upper_case_writing_uri text, " +
                "letter_sound_uri text not null);";
        public static final String DROP_LETTER_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_letter;";
        public static final String CREATE_WORD_LETTER_TABLE ="create  table " +
                "tbl_word_letter("+
                "id integer primary key autoincrement, "+
                "word_id integer, "+
                "letter_id integer, " +
                "sequence_number integer);";
        public static final String DROP_WORD_LETTER_TABLE = "DROP TABLE IF EXISTS  " +
                "tbl_word_letter;";
        public static final String CREATE_LESSON_CONTROL_TABLE = "create table" +
                "tbl_lesson_control("+
                "id integer primary key autoincrement, "+
                "word_id integer, "+
                "letter_id integer, " +
                "sequence_number integer);";
}
