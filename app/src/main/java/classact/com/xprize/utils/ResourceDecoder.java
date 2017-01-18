package classact.com.xprize.utils;

import android.content.Context;

/**
 * Created by Tseliso on 11/29/2016.
 */

public class ResourceDecoder {
    public static int getIdentifier(Context pContext, String pString,String strResourceLocation){
        return pContext.getResources().getIdentifier(pString, strResourceLocation, pContext.getPackageName());
    }
}
