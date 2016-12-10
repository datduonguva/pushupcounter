package com.duongCompany.duong.pushupcounter.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by duong on 12/1/2016.
 */

public final class PushContract implements BaseColumns {
    public final static String AUTHORITY = "com.duongCompany.duong.pushupcounter";
    public final static Uri BASE_URI = Uri.parse("content://"+ AUTHORITY);
    public final static String PATH_PUSH = "pushes";
    public final static String PATH_PARAMETERS = "parameters";
    private PushContract(){};
    public static final class PushEntry{
        public static final String TABLE_NAME = "pushes";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_DATE ="date";
        public static final String COLUMN_COUNT = "count";
        public static final String COLUMN_CALORIES = "calories";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,PATH_PUSH);
    }

    public static final class Parameter{
        public static final String TABLE_NAME = "parameters";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_HEIGHT = "height";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,PATH_PARAMETERS);
    }
}
