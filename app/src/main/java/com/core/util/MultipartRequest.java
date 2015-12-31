package com.core.util;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultipartRequest extends Request<String> {
    /**
     * Socket timeout in milliseconds for requests
     */
    private static final int MULTI_TIMEOUT_MS = 45000;

    /**
     * Default number of retries for requests
     */
    private static final int MULTI_MAX_RETRIES = 0;

    /**
     * Default backoff multiplier for requests
     */
    private static final float MULTI_BACKOFF_MULT = 1f;
    private final String LINEEND = "\r\n";
    private final String TWOHYPHENS = "--";
    private final String BOUNDARY = "acebdf13572468";
    private Response.Listener mListener;
    private Response.ErrorListener errorListener;
    private HashMap<String, String> mParams;
    private HashMap<String, String> mFileParams;//文件地址指引 目前是头像
    private ArrayList<String> mFileUrlParams;//文件地址指引
    private String mFileKey = "";


    public MultipartRequest(int method, String url,
                            HashMap<String, String> params, HashMap<String, String> fileParams, String fileKey,
                            Response.Listener listener,
                            Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mFileKey = fileKey;
        mParams = params;
        mFileParams = fileParams;
        mListener = listener;
        this.errorListener = errorListener;
        setRetryPolicy(new DefaultRetryPolicy(MULTI_TIMEOUT_MS, MULTI_MAX_RETRIES, MULTI_BACKOFF_MULT));
    }

    public MultipartRequest(int method, String url,
                            HashMap<String, String> params, ArrayList<String> fileUrlParams, String fileKey,
                            Response.Listener listener,
                            Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mFileKey = fileKey;
        mParams = params;
        mFileUrlParams = fileUrlParams;
        mListener = listener;
        this.errorListener = errorListener;
        setRetryPolicy(new DefaultRetryPolicy(MULTI_TIMEOUT_MS, MULTI_MAX_RETRIES, MULTI_BACKOFF_MULT));
    }


    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }

    private byte[] getBitmapBytes(String filepath) {
        if ((null == filepath) || (filepath.equalsIgnoreCase(""))) {
            return null;
        }
        File bitFile = new File(filepath);
        if (!bitFile.exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(bitFile);
            byte[] bytes = new byte[(int) bitFile.length()];
            fis.read(bytes);
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            makeBodyFromParams(bos);
            makeBodyFromFileParams(bos);
            bos.write((TWOHYPHENS + BOUNDARY + TWOHYPHENS + LINEEND).getBytes());// 数据结束标志
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    private void makeBodyFromParams(ByteArrayOutputStream bos) {
        if (null != mParams) {
            Iterator iter = mParams.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if (null != key && !key.equalsIgnoreCase("") &&
                        null != val && !val.equalsIgnoreCase("")) {
                    try {
                        buildParmsPart(bos, key, val);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void makeBodyFromFileParams(ByteArrayOutputStream bos) {
        if (null != mFileParams) {
            Iterator iter = mFileParams.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if (null != key && !key.equalsIgnoreCase("") &&
                        null != val && !val.equalsIgnoreCase("")) {
                    try {
                        buildFileParamsPart(bos, key, val);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (mFileUrlParams != null) {
            for (int i = 0; i < mFileUrlParams.size(); i++) {
                try {
                    if (StringUtil.isBlank(mFileUrlParams.get(i))) {
                        continue;
                    }
                    buildFileParamsPart(bos, i + "", mFileUrlParams.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //组合成一个part 参数
    private void buildParmsPart(ByteArrayOutputStream bos, String key, String val) throws IOException {
        bos.write((TWOHYPHENS + BOUNDARY + LINEEND).getBytes());
        bos.write(("Content-Disposition: form-data; name=" + "\"" + key + "\"" + ";").getBytes());
        bos.write(LINEEND.getBytes());
        bos.write(("Content-Type: application/x-www-form-urlencoded;").getBytes());
        bos.write(LINEEND.getBytes());
        bos.write(LINEEND.getBytes());
        bos.write(val.getBytes());
        bos.write(LINEEND.getBytes());
    }

    //组合成一个part 头像
    private void buildFileParamsPart(ByteArrayOutputStream bos, String key, String val) throws IOException {
        byte[] bitmapbs = getBitmapBytes(val);
        if (null != bitmapbs) {
            bos.write((TWOHYPHENS + BOUNDARY + LINEEND).getBytes());
            bos.write(("Content-Disposition: form-data; name=" + "\"" + mFileKey + "\"" + ";" + "filename=" + "\"" + key + "\"" + ";").getBytes());
            bos.write(LINEEND.getBytes());
            bos.write("Content-Type: image/jpeg".getBytes());
            bos.write(LINEEND.getBytes());
            bos.write(LINEEND.getBytes());
            bos.write(bitmapbs);
            bos.write(LINEEND.getBytes());
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String responseString;
        try {
            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
        return Response.success(responseString, getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        if (null != mListener) {
            mListener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (null != errorListener) {
            errorListener.onErrorResponse(error);
        }
    }
}