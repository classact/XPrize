package classact.com.xprize.control;

import android.content.Context;

import java.util.ArrayList;

import classact.com.xprize.database.model.SimpleStoryWords;
import classact.com.xprize.utils.ResourceDecoder;

/**
 * Created by Tseliso on 1/3/2017.
 */

public class SimpleStoryJsonBuilder {
    public static String getSimpleStoryJson(Context context,
                                            String storyLinkSound,
                                            String amazingSound,
                                            String listenFirstSound,
                                            String nowYourReadSound,
                                            String listenToWholeStorySound,
                                            String readWholeStorySound,
                                            String wellDoneYouCanReadSound,
                                            String nowAnswerQuestionsSound,
                                            String fullStorySound,
                                            String touchTheArrow,
                                            String storyImage,
                                            ArrayList<SimpleStorySentence> sentences,
                                            String answerQuestionsSound,
                                            String comprehensionInstructions,
                                            ArrayList<ComprehensionQuestion> questions) {

        System.out.println(":::: " + storyLinkSound);
        System.out.println(":::: " + amazingSound);
        System.out.println(":::: " + listenFirstSound);
        System.out.println(":::: " + nowYourReadSound);
        System.out.println(":::: " + listenToWholeStorySound);
        System.out.println(":::: " + readWholeStorySound);
        System.out.println(":::: " + wellDoneYouCanReadSound);
        System.out.println(":::: " + nowAnswerQuestionsSound);
        System.out.println(":::: " + fullStorySound);
        System.out.println(":::: " + touchTheArrow);
        System.out.println(":::: " + storyImage);
        System.out.println(":::: " + answerQuestionsSound);
        System.out.println(":::: " + comprehensionInstructions);

        String drillData = "{\"amazing_sound\":" + ResourceDecoder.getIdentifier(context,amazingSound,"raw") + "," +
                "\"listen_first_sound\":" + ResourceDecoder.getIdentifier(context,listenFirstSound,"raw")+ "," +
                "\"now_you_read_sound\":" + ResourceDecoder.getIdentifier(context,nowYourReadSound,"raw") + "," +
                "\"listen_to_whole_story\":" + ResourceDecoder.getIdentifier(context,listenToWholeStorySound,"raw") + "," +
                "\"read_whole_story_sound\":" + ResourceDecoder.getIdentifier(context,readWholeStorySound,"raw") + "," +
                "\"well_done_sound\":" + ResourceDecoder.getIdentifier(context,wellDoneYouCanReadSound,"raw") + "," +
                "\"now_answer_sound\":" + ResourceDecoder.getIdentifier(context,nowAnswerQuestionsSound,"raw") + "," +
                "\"story_link_sound\":" + ResourceDecoder.getIdentifier(context,storyLinkSound,"raw") + "," +
                "\"story_image\":" + ResourceDecoder.getIdentifier(context,storyImage,"drawable") + "," +
                "\"full_story_sound\":" + ResourceDecoder.getIdentifier(context,fullStorySound,"raw")+ "," +
                "\"touch_arrow\":" + ResourceDecoder.getIdentifier(context,touchTheArrow,"raw")+ "," +
                "\"answer_questions_sound\":" + ResourceDecoder.getIdentifier(context,answerQuestionsSound,"raw")+ "," +
                "\"comprehension_instructions_sound\":" + ResourceDecoder.getIdentifier(context,comprehensionInstructions,"raw")+ "," +
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
            for(SimpleStoryWords word: sentence.getWords()){
                if (j ==  0){
                    drillData +="{";
                }
                else{
                    drillData +=",{";
                }
                j++;
                drillData += "\"black_word\":" + ResourceDecoder.getIdentifier(context,word.getWordName(),"drawable");
                /*if (word.getRedImage() != null)
                    drillData += ",\"red_word\":" + ResourceDecoder.getIdentifier(context,word.getRedImage(),"drawable");
                else*/
                drillData += ",\"red_word\":0";
                drillData += ",\"sound\":" + ResourceDecoder.getIdentifier(context,word.getWordSound(),"raw") + "}";
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
            drillData += "\"question_sound\":" + ResourceDecoder.getIdentifier(context,q.getQuestionSound(),"raw") + ",";
            drillData += "\"answer_sound\":" + ResourceDecoder.getIdentifier(context,q.getAnswerSound(),"raw") + ",";
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
