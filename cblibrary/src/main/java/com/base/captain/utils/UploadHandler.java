package com.base.captain.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import android.widget.Toast;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Handle the file upload. This does not support selecting multiple files yet.
 */
public class UploadHandler {
    private final static String IMAGE_MIME_TYPE = "image/*";
    private final static String VIDEO_MIME_TYPE = "video/*";
    private final static String AUDIO_MIME_TYPE = "audio/*";

    private static String FILE_PROVIDER_AUTHORITY;
    private ValueCallback<Uri[]> mUploadMessage;
    private Activity mController;
    private FileChooserParams mParams;
    private Uri mCapturedMedia;

    public UploadHandler(Activity controller) {
        mController = controller;
        FILE_PROVIDER_AUTHORITY = mController.getPackageName() + ".provider";
    }

    public void onResult(int resultCode, Intent intent) {
        Uri[] uris;
        // As the media capture is always supported, we can't use
        // FileChooserParams.parseResult().
        uris = parseResult(resultCode, intent);
        mUploadMessage.onReceiveValue(uris);
        mUploadMessage = null;
    }

    public void openFileChooser(ValueCallback<Uri[]> callback, FileChooserParams fileChooserParams) {

        if (mUploadMessage != null) {
            // Already a file picker operation in progress.
            return;
        }

        mUploadMessage = callback;
        mParams = fileChooserParams;
        Intent[] captureIntents = createCaptureIntent();
        assert (captureIntents != null && captureIntents.length > 0);
        Intent intent;
        // Go to the media capture directly if capture is specified, this is the
        // preferred way.
        if (fileChooserParams.isCaptureEnabled() && captureIntents.length == 1) {
            intent = captureIntents[0];
        } else {
            intent = new Intent(Intent.ACTION_CHOOSER);
            intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, captureIntents);
            intent.putExtra(Intent.EXTRA_INTENT, fileChooserParams.createIntent());
        }
        startActivity(intent);
    }

    private Uri[] parseResult(int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return null;
        }
        Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                : intent.getData();

        // As we ask the camera to save the result of the user taking
        // a picture, the camera application does not return anything other
        // than RESULT_OK. So we need to check whether the file we expected
        // was written to disk in the in the case that we
        // did not get an intent returned but did get a RESULT_OK. If it was,
        // we assume that this result has came back from the camera.
        if (result == null && intent == null && resultCode == Activity.RESULT_OK
                && mCapturedMedia != null) {
            result = mCapturedMedia;
        }

        Uri[] uris = null;
        if (result != null) {
            uris = new Uri[1];
            uris[0] = result;
        }
        return uris;
    }

    private void startActivity(Intent intent) {
        try {
            mController.startActivityForResult(intent, 4);
        } catch (ActivityNotFoundException e) {
            // No installed app was able to handle the intent that
            // we sent, so file upload is effectively disabled.
            Toast.makeText(mController, "禁止上传",
                    Toast.LENGTH_LONG).show();
        }
    }

    private Intent[] createCaptureIntent() {
        String mimeType = "*/*";
        String[] acceptTypes = mParams.getAcceptTypes();
        if (acceptTypes != null && acceptTypes.length > 0) {
            mimeType = acceptTypes[0];
        }
        Intent[] intents;
        if (mimeType.equals(IMAGE_MIME_TYPE)) {
            intents = new Intent[1];
            intents[0] = createCameraIntent(createTempFileContentUri(".jpg"));
        } else if (mimeType.equals(VIDEO_MIME_TYPE)) {
            intents = new Intent[1];
            intents[0] = createCamcorderIntent();
        } else if (mimeType.equals(AUDIO_MIME_TYPE)) {
            intents = new Intent[1];
            intents[0] = createSoundRecorderIntent();
        } else {
            intents = new Intent[3];
            intents[0] = createCameraIntent(createTempFileContentUri(".jpg"));
            intents[1] = createCamcorderIntent();
            intents[2] = createSoundRecorderIntent();
        }
        return intents;
    }

    private Uri createTempFileContentUri(String suffix) {
        try {
            File mediaPath = new File(mController.getFilesDir(), "captured_media");
            if (!mediaPath.exists() && !mediaPath.mkdir()) {
                throw new RuntimeException("Folder cannot be created.");
            }
            File mediaFile = File.createTempFile(
                    String.valueOf(System.currentTimeMillis()), suffix, mediaPath);
            return FileProvider.getUriForFile(mController, FILE_PROVIDER_AUTHORITY, mediaFile);
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
    }

    private Intent createCameraIntent(Uri contentUri) {
        if (contentUri == null) throw new IllegalArgumentException();
        mCapturedMedia = contentUri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedMedia);
        intent.setClipData(ClipData.newUri(mController.getContentResolver(),
                FILE_PROVIDER_AUTHORITY, mCapturedMedia));
        return intent;
    }

    private Intent createCamcorderIntent() {
        return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    }

    private Intent createSoundRecorderIntent() {
        return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
    }
}