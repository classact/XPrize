package classact.com.xprize.control;

import android.content.Context;

import java.util.ArrayList;

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
                                            String listenToWholeStorySound,
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

        System.out.println(":::: storyLinkSound: " + storyLinkSound);
        System.out.println(":::: readEachSentenceAfterMotherSound: " + readEachSentenceAfterMotherSound);
        System.out.println(":::: listenFirstSound: " + listenFirstSound);
        System.out.println(":::: nowReadSound: " + nowReadSound);
        System.out.println(":::: listenToWholeStorySound: " + listenToWholeStorySound);
        System.out.println(":::: nowReadWholeStorySound: " + nowReadWholeStorySound);
        System.out.println(":::: wellDoneYouCanReadSound: " + wellDoneYouCanReadSound);
        System.out.println(":::: nowAnswerQuestionsSound: " + nowAnswerQuestionsSound);
        System.out.println(":::: fullStorySound: " + fullStorySound);
        System.out.println(":::: touchTheArrow: " + touchTheArrow);
        System.out.println(":::: storyImage: " + storyImage);
        System.out.println(":::: comprehensionQuestion: " + comprehensionQuestion);
        System.out.println(":::: comprehensionInstructions: " + comprehensionInstructions);

        String drillData = "{\"reach_each_sentence_after_mother_sound\":\"" + readEachSentenceAfterMotherSound + "\"," +
                "\"listen_first_sound\":\"" + listenFirstSound + "\"," +
                "\"now_read_sound\":\"" + nowReadSound + "\"," +
                "\"listen_to_whole_story\":\"" + listenToWholeStorySound + "\"," +
                "\"now_read_whole_story_sound\":\"" + nowReadWholeStorySound + "\"," +
                "\"well_done_sound\":\"" + wellDoneYouCanReadSound + "\"," +
                "\"now_answer_sound\":\"" + nowAnswerQuestionsSound + "\"," +
                "\"story_link_sound\":\"" + storyLinkSound + "\"," +
                "\"story_image\":" + ResourceDecoder.getIdentifier(context,storyImage,"drawable") + "," +
                "\"full_story_sound\":\"" + fullStorySound + "\"," +
                "\"touch_arrow\":\"" + touchTheArrow + "\"," +
                "\"comprehension_question_sound\":\"" + comprehensionQuestion + "\"," +
                "\"comprehension_instructions_sound\":\"" + comprehensionInstructions + "\"," +
                "\"sentences\":[" ;
        int i = 0;
        for (SimpleStorySentence sentence : sentences){
            if (i == 0){
                drillData +="[";
            }
            else{
                drillData +=",[";
            }
            i++;
            int j = 0;
            for(SimpleStoryWord word: sentence.getWords()){
                if (j ==  0){
                    drillData +="{";
                }
                else{
                    drillData +=",{";
                }
                j++;
                drillData += "\"black_word\":" + ResourceDecoder.getIdentifier(context,word.getBlackWord(),"drawable");
                if (word.getRedWord() != null) {
                    drillData += ",\"red_word\":" + ResourceDecoder.getIdentifier(context, word.getRedWord(), "drawable");
                } else {
                    drillData += ",\"red_word\":0";
                }
                drillData += ",\"sound\":\"" + word.getSound() + "\"}";
            }
            drillData += "]";
        }
        drillData += "],\"questions\":[";
        i = 0;
        for(ComprehensionQuestion q : questions){
            if (i == 0){
                drillData += "{";
            }
            else{
                drillData += ",{";
            }
            drillData += "\"is_touch\":" + q.getIsATouchQuestion() + ",";
            drillData += "\"question_sound\":\"" + q.getQuestionSound() + "\",";
            drillData += "\"answer_sound\":\"" + q.getAnswerSound() + "\",";
            drillData += "\"images\":[";
            int j = 0;
            for (DraggableImage<String> image : q.getImages()){
                if (j == 0){
                    drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable");
                }
                else{
                    drillData += ",{\"image\":" + ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable");
                }
                drillData += ",\"is_right\":" + image.isRight() + "}";
                j++;
            }
            drillData += "]}";
            i++;
        }
        drillData += "]}";
        return drillData;
    }
}
