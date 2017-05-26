package classact.com.xprize.control;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import classact.com.xprize.common.Code;
import classact.com.xprize.database.model.SimpleStoryWord;
import classact.com.xprize.utils.ResourceDecoder;

/**
 * Created by Tseliso on 1/3/2017.
 */

public class SimpleStoryJsonBuilder {
    public static String getSimpleStoryJson(Context context,
                                            String storyLinkSound,
                                            String readEachSentenceAfterMotherSound,
                                            String listenFirstSound,
                                            String nowReadSound,
                                            String listenToTheWholeStorySound,
                                            String nowReadWholeStorySound,
                                            String wellDoneYouCanReadSound,
                                            String nowAnswerQuestionsSound,
                                            String fullStorySound,
                                            String touchTheArrow,
                                            String storyImage,
                                            ArrayList<SimpleStorySentence> sentences,
                                            String comprehensionQuestion,
                                            String comprehensionInstructions,
                                            ArrayList<ComprehensionQuestion> questions) {

        String drillData = "{\"read_each_sentence_after_mother_sound\":\"" + readEachSentenceAfterMotherSound + "\"," +
                "\"listen_first_sound\":\"" + listenFirstSound + "\"," +
                "\"now_read_sound\":\"" + nowReadSound + "\"," +
                "\"listen_to_the_whole_story\":\"" + listenToTheWholeStorySound + "\"," +
                "\"now_read_whole_story_sound\":\"" + nowReadWholeStorySound + "\"," +
                "\"well_done_you_can_read_sound\":\"" + wellDoneYouCanReadSound + "\"," +
                "\"now_answer_sound\":\"" + nowAnswerQuestionsSound + "\"," +
                "\"story_link_sound\":\"" + storyLinkSound + "\"," +
                "\"story_image\":\"" + storyImage + "\"," +
                "\"full_story_sound\":\"" + fullStorySound + "\"," +
                "\"touch_the_arrow\":\"" + touchTheArrow + "\"," +
                "\"comprehension_question_sound\":\"" + comprehensionQuestion + "\"," +
                "\"comprehension_instructions_sound\":\"" + comprehensionInstructions + "\"," +
                "\"sentences\":[" ;
        for (int i = 0; i < sentences.size(); i++) {
            SimpleStorySentence sentence = sentences.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "[";
            List<SimpleStoryWord> sentenceWords = sentence.getWords();
            int numOfSentenceWords = sentenceWords.size();
            for (int j = 0; j < numOfSentenceWords; j++) {
                SimpleStoryWord word = sentenceWords.get(j);
                if (j > 0) {
                    drillData += ",";
                }
                drillData += "{\"black_word\":\"" + word.getBlackWord() + "\"";
                if (word.getRedWord() != null) {
                    drillData += ",\"red_word\":\"" + word.getRedWord() + "\"";
                } else {
                    drillData += ",\"red_word\":\"" + Code.BLANK_IMAGE + "\"";
                }
                drillData += ",\"sound\":\"" + word.getSound() + "\"";
                drillData += ",\"set_no\":" + word.getSentenceSetNo() + "}";
            }
            drillData += "]";
        }
        drillData += "],\"questions\":[";
        for (int i = 0; i < questions.size(); i++) {
            ComprehensionQuestion q = questions.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{";
            drillData += "\"is_touch\":" + q.getIsATouchQuestion() + ",";
            drillData += "\"question_sound\":\"" + q.getQuestionSound() + "\",";
            drillData += "\"answer_sound\":\"" + q.getAnswerSound() + "\",";
            drillData += "\"images\":[";

            List<DraggableImage<String>> qImages = q.getImages();
            int numOfQImages = qImages.size();

            for (int j = 0; j < numOfQImages; j++) {
                DraggableImage<String> image = qImages.get(j);
                if (j > 0) {
                    drillData += ",";
                }
                drillData += "{\"image\":\"" + image.getcontent() + "\"";
                drillData += ",\"is_right\":" + image.isRight() + "}";
            }
            drillData += "]}";
        }
        drillData += "]}";
        return drillData;
    }
}
