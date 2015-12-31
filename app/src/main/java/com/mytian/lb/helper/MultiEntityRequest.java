package com.mytian.lb.helper;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 */
public class MultiEntityRequest extends StringRequest {


    private static final String STR_CR_LF = "\r\n";
    private static final byte[] CR_LF = STR_CR_LF.getBytes();
    private static final byte[] TRANSFER_ENCODING_BINARY =
            ("Content-Transfer-Encoding: binary" + STR_CR_LF).getBytes();

    private final static char[] MULTIPART_CHARS =
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final String boundary;
    private final byte[] boundaryLine;
    private final byte[] boundaryEnd;
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    public final static String APPLICATION_OCTET_STREAM = "application/octet-stream";


    private final List<FilePart> fileParts = new ArrayList<>();
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    {
        final StringBuilder buf = new StringBuilder();
        final Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        boundary = buf.toString();
        boundaryLine = ("--" + boundary + STR_CR_LF).getBytes();
        boundaryEnd = ("--" + boundary + "--" + STR_CR_LF).getBytes();
    }

    public MultiEntityRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST,url, listener, errorListener);
    }

    private byte[] createContentDisposition(String key) {
        return (
                HEADER_CONTENT_DISPOSITION +
                        ": form-data; name=\"" + key + "\"" + STR_CR_LF).getBytes();
    }

    private byte[] createContentDisposition(String key, String fileName) {
        return (
                HEADER_CONTENT_DISPOSITION +
                        ": form-data; name=\"" + key + "\"" +
                        "; filename=\"" + fileName + "\"" + STR_CR_LF).getBytes();
    }

    private byte[] createContentType(String type) {
        String result = HEADER_CONTENT_TYPE + ": " + normalizeContentType(type) + STR_CR_LF;
        return result.getBytes();
    }


    private String normalizeContentType(String type) {
        return type == null ? APPLICATION_OCTET_STREAM : type;
    }


    public void writeTo(final OutputStream outstream) throws IOException {
        out.writeTo(outstream);
        for (FilePart filePart : fileParts) {
            filePart.writeTo(outstream);
        }
        outstream.write(boundaryEnd);
    }


    public void addPart(String key, String value, String contentType) {
        try {
            out.write(boundaryLine);
            out.write(createContentDisposition(key));
            out.write(createContentType(contentType));
            out.write(CR_LF);
            out.write(value.getBytes());
            out.write(CR_LF);
        } catch (final IOException e) {
        }
    }


    public void addPartWithCharset(String key, String value, String charset) {
        if (charset == null) charset = "UTF-8";
        addPart(key, value, "text/plain; charset=" + charset);
    }


    public void addPart(String key, String streamName, InputStream inputStream, String type)
            throws IOException {
        out.write(boundaryLine);
        out.write(createContentDisposition(key, streamName));
        out.write(createContentType(type));
        out.write(TRANSFER_ENCODING_BINARY);
        out.write(CR_LF);
        final byte[] tmp = new byte[4096];
        int l = 0;
        while ((l = inputStream.read(tmp)) != -1) {
            out.write(tmp, 0, l);
        }
        out.write(CR_LF);
        out.flush();

    }

    private long getContentLength() {
        long contentLen = out.size();
        for (FilePart filePart : fileParts) {
            long len = filePart.getTotalLength();
            if (len < 0) {
                return -1;
            }
            contentLen += len;
        }
        contentLen += boundaryEnd.length;
        return contentLen;
    }

    public void addPart(String key, String value) {
        addPartWithCharset(key, value, null);
    }

    public void addPart(String key, File file) {
        addPart(key, file, null);
    }

    public void addPart(String key, File file, String type) {
        fileParts.add(new FilePart(key, file, normalizeContentType(type)));
    }

    public void addPart(String key, File file, String type, String customFileName) {
        fileParts.add(new FilePart(key, file, normalizeContentType(type), customFileName));
    }

    private class FilePart {
        public final File file;
        public final byte[] header;

        public FilePart(String key, File file, String type, String customFileName) {
            header = createHeader(key, TextUtils.isEmpty(customFileName) ? file.getName() : customFileName, type);
            this.file = file;
        }

        public FilePart(String key, File file, String type) {
            header = createHeader(key, file.getName(), type);
            this.file = file;
        }

        private byte[] createHeader(String key, String filename, String type) {
            ByteArrayOutputStream headerStream = new ByteArrayOutputStream();
            try {
                headerStream.write(boundaryLine);
                headerStream.write(createContentDisposition(key, filename));
                headerStream.write(createContentType(type));
                headerStream.write(TRANSFER_ENCODING_BINARY);
                headerStream.write(CR_LF);
            } catch (IOException e) {

            }
            return headerStream.toByteArray();
        }

        public long getTotalLength() {
            long streamLength = file.length() + CR_LF.length;
            return header.length + streamLength;
        }

        public void writeTo(OutputStream out) throws IOException {
            out.write(header);
            FileInputStream inputStream = new FileInputStream(file);
            final byte[] tmp = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(tmp)) != -1) {
                out.write(tmp, 0, bytesRead);
            }
            out.write(CR_LF);
            out.flush();
        }
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Length", getContentLength() + "");
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + boundary;
    }
}
