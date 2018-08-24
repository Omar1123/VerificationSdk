package com.company.jak.faceverification.utils;

/**
 * Created by jake on 19/08/18.
 */

import java.util.List;

import android.os.Environment;
import com.neurotec.util.concurrent.AggregateExecutionException;

public class Utils {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String NEUROTECHNOLOGY_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + FILE_SEPARATOR + "Neurotechnology";

    public static String getMessage(Throwable th) {
        if (th == null) throw new NullPointerException("exception");
        String msg = null;
        if (th instanceof AggregateExecutionException) {
            List<Throwable> causes = ((AggregateExecutionException) th).getCauses();
            if (causes.size() > 0) {
                Throwable cause = causes.get(0);
                msg = cause.getMessage() != null ? cause.getMessage() : cause.toString();
            }
        } else {
            msg = th.getMessage() != null ? th.getMessage() : th.toString();
        }
        return msg;
    }

    public static String combinePath(String... folders) {
        String path = "";
        for (String folder : folders) {
            path = path.concat(FILE_SEPARATOR).concat(folder);
        }
        return path;
    }
}
