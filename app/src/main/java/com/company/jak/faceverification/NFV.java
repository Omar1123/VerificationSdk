package com.company.jak.faceverification;

/**
 * Created by jake on 19/08/18.
 */

import com.neurotec.face.verification.NFaceVerification;
import com.company.jak.faceverification.utils.Utils;

public class NFV {
    private static NFaceVerification instance;

    protected NFV() {
    }

    public static synchronized NFaceVerification getInstance() {
        if (instance == null) {
            instance = new NFaceVerification(Utils.combinePath(Utils.NEUROTECHNOLOGY_DIRECTORY, "face_database.db"), "database_password");
        }
        return instance;
    }

    public static synchronized void dispose() {
        if (instance != null) {
            instance.dispose();
            instance = null;
        }
    }
}
